package com.tokopedia.tkpd.flashsale.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_MONTH_ONLY
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
import com.tokopedia.campaign.utils.constant.DateConstant.TIME_MINUTE_PRECISION_WITH_TIMEZONE
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.common.extension.toCalendar
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.entity.enums.UpcomingCampaignStatus
import com.tokopedia.tkpd.flashsale.domain.entity.enums.isFlashSaleAvailable
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.WaitingForSelectionDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.R.color
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseSimpleListFragment<CompositeAdapter, DelegateAdapterItem>() {

    companion object {
        private const val UPCOMING_TAB = "upcoming"
        private const val REGISTERED_TAB = "registered"
        private const val ONGOING_TAB = "ongoing"
        private const val FINISHED_TAB = "finished"
        private const val PAGE_SIZE = 10

        @JvmStatic
        fun newInstance(flashSaleId: Long, tabName: String): CampaignDetailFragment {
            val fragment = CampaignDetailFragment()
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putString(BundleConstant.BUNDLE_KEY_TAB_NAME, tabName)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    //View Binding
    private var binding by autoClearedNullable<StfsFragmentCampaignDetailBinding>()

    //upcoming
    private var upcomingCdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var upcomingCdpMidBinding by autoClearedNullable<StfsCdpUpcomingMidBinding>()
    private var upcomingCdpBodyBinding by autoClearedNullable<StfsCdpUpcomingBodyBinding>()

    //registered
    private var registeredCdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var registeredCdpMidBinding by autoClearedNullable<StfsCdpRegisteredMidBinding>()
    private var registeredCdpBodyBinding by autoClearedNullable<StfsCdpRegisteredBodyBinding>()

    private val flashSaleId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
    }

    private val tabName by lazy {
        arguments?.getString(BundleConstant.BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    private val productAdapter by lazy {
        CompositeAdapter.Builder()
            .add(
                WaitingForSelectionDelegateAdapter(
                    onProductItemClicked = { onProductClicked(it) },
                    onCheckBoxClicked = { itemPosition, isChecked ->
                        onCheckBoxClicked(
                            itemPosition,
                            isChecked
                        )
                    })
            )
            .add(LoadingDelegateAdapter())
            .build()
    }

    override fun getScreenName(): String =
        CampaignDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentCampaignDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCampaignDetail()
        loadCampaignDetailData()
    }

    private fun observeCampaignDetail() {
        viewModel.campaign.observe(viewLifecycleOwner) { flashSale ->
            hideLoading()
            when (flashSale) {
                is Success -> {
                    setupView(flashSale.data)
                }
                is Fail -> {
                    //TODO: Add Error Handling, not available on figma yet
                }
            }
        }
    }

    private fun observeSubmittedProductData() {
        viewModel.submittedProduct.observe(viewLifecycleOwner) { submittedProduct ->
            when (submittedProduct) {
                is Success -> {
                    renderList(submittedProduct.data, submittedProduct.data.size == getPerPage())
                    setupRegisteredBody()
                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeSelectedProductData() {
        viewModel.selectedItemsId.observe(viewLifecycleOwner) { ids ->
            registeredCdpBodyBinding?.tpgSelectedProductCount?.text =
                getString(
                    R.string.stfs_choosen_product_count_placeholder,
                    ids.count()
                )
        }
    }

    private fun loadCampaignDetailData() {
        showLoading()
        viewModel.getCampaignDetail(flashSaleId)
    }

    private fun loadSubmittedProductListData(offset: Int) {
        viewModel.getSubmittedProduct(flashSaleId, offset)
    }

    private fun setupView(flashSale: FlashSale) {
        when (tabName) {
            UPCOMING_TAB -> setupUpcoming(flashSale)
            REGISTERED_TAB -> setupRegistered(flashSale)
            ONGOING_TAB -> setupOngoing()
            FINISHED_TAB -> setupFinished()
        }
    }

    /**
     *Region Upcoming CDP
     */
    private fun setupUpcoming(flashSale: FlashSale) {
        val campaignStatus = when {
            viewModel.isCampaignRegisterClosed(flashSale) -> UpcomingCampaignStatus.CLOSED
            flashSale.remainingQuota.isZero() -> UpcomingCampaignStatus.FULL_QUOTA
            else -> {
                if (flashSale.hasEligibleProduct) {
                    UpcomingCampaignStatus.AVAILABLE
                } else {
                    UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE
                }
            }
        }

        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                upcomingCdpHeaderBinding = StfsCdpHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                upcomingCdpMidBinding = StfsCdpUpcomingMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                upcomingCdpBodyBinding = StfsCdpUpcomingBodyBinding.bind(view)
            }
            cardBottomButtonGroup.isVisible = campaignStatus.isFlashSaleAvailable()
            rvSubmittedProductList.gone()
        }

        setupUpcomingHeader(flashSale, campaignStatus)
        setupUpcomingMid(flashSale, campaignStatus)
        setupUpcomingBody(flashSale)
        setupUpcomingButton()
    }

    private fun setupUpcomingHeader(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        inflatedView.inflate()
        setupUpcomingHeaderData(flashSale, campaignStatus)
    }

    private fun setupUpcomingMid(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        inflatedView.inflate()
        upcomingCdpMidBinding?.run {
            setupUpcomingMidData(flashSale, campaignStatus)
            val startSubmissionDate = flashSale.submissionStartDateUnix.formatTo(
                DATE_MONTH_ONLY
            )
            val endSubmissionDate = flashSale.submissionEndDateUnix.formatTo(DATE_MONTH_ONLY)
            tgRegisterPeriod.text = getString(
                R.string.register_period_value_placeholder_2,
                startSubmissionDate,
                endSubmissionDate
            )
        }
    }

    private fun setupUpcomingBody(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_body
        inflatedView.inflate()
        upcomingCdpBodyBinding?.run {
            tgDescription.text = MethodChecker.fromHtml(
                flashSale.description
            )
        }
    }

    private fun setupUpcomingHeaderData(
        flashSale: FlashSale,
        campaignStatus: UpcomingCampaignStatus
    ) {
        upcomingCdpHeaderBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> {
                    tickerHeader.visible()
                    tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_product_not_eligible_state))
                    tgCampaignStatus.text = getString(R.string.registration_over_in_label)
                }
                UpcomingCampaignStatus.FULL_QUOTA -> {
                    tickerHeader.visible()
                    tickerHeader.tickerTitle =
                        getString(R.string.stfs_title_ticker_upcoming_cdp_full_quota_state)
                    tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_full_quota_state))
                    tgCampaignStatus.text = getString(R.string.registration_over_in_label)
                }
                UpcomingCampaignStatus.CLOSED -> {
                    tickerHeader.visible()
                    tickerHeader.tickerTitle =
                        getString(R.string.stfs_title_ticker_upcoming_cdp_registration_close_state)
                    tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_registration_close_state))
                    tgCampaignStatus.text = getString(R.string.registration_closed_in_label)
                }
                else -> {
                    tickerHeader.gone()
                    tgCampaignStatus.text = getString(R.string.registration_over_in_label)
                }
            }
            imageCampaign.setImageUrl(flashSale.coverImage)
            tgCampaignName.text = flashSale.name
            setupUpcomingTimer(this, flashSale)
            setHeaderCampaignPeriod(this, flashSale)
        }
    }

    private fun setupUpcomingMidData(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        upcomingCdpMidBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder,
                        flashSale.remainingQuota
                    )
                    btnCheckReason.visible()
                }
                UpcomingCampaignStatus.FULL_QUOTA -> {
                    tgCampaignQuota.apply {
                        text = getString(R.string.full_quota_label)
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                color.Unify_R500
                            )
                        )
                    }
                }
                UpcomingCampaignStatus.CLOSED -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder_2,
                        flashSale.remainingQuota
                    )
                }
                else -> {
                    tgCampaignQuota.text = getString(
                        R.string.campaign_quota_value_placeholder,
                        flashSale.remainingQuota
                    )
                }
            }
        }
    }

    private fun setupUpcomingTimer(binding: StfsCdpHeaderBinding, flashSale: FlashSale) {
        val targetDate = flashSale.submissionEndDateUnix
        val onTimerFinished = { binding.timer.gone() }
        when {
            viewModel.isFlashSaleClosedMoreThan24Hours(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_DAY
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
                binding.timer.onFinish = onTimerFinished
            }
            viewModel.isFlashSaleClosedLessThan24Hour(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                binding.timer.onFinish = onTimerFinished
            }
            viewModel.isFlashSaleClosedLessThan60Minutes(targetDate) -> {
                binding.timer.timerFormat = TimerUnifySingle.FORMAT_MINUTE
                binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
                binding.timer.onFinish = onTimerFinished
            }
            else -> onTimerFinished
        }
    }

    private fun setupUpcomingButton() {
        binding?.run {
            btnRegister.text = getString(R.string.label_register)
        }
    }

    /**
     * Region Registered CDP
     */
    private fun setupRegistered(flashSale: FlashSale) {
        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                registeredCdpHeaderBinding = StfsCdpHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                registeredCdpMidBinding = StfsCdpRegisteredMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                registeredCdpBodyBinding = StfsCdpRegisteredBodyBinding.bind(view)
            }
        }

        setupRegisteredHeader(flashSale)
        setupRegisteredMid(flashSale)
        observeSubmittedProductData()
    }

    private fun setupRegisteredHeader(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        inflatedView.inflate()
        setupRegisteredHeaderData(flashSale)
    }

    private fun setupRegisteredMid(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_registered_mid
        inflatedView.inflate()
        setupRegisteredMidData(flashSale)
    }

    private fun setupRegisteredBody() {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_registered_body
        inflatedView.inflate()
        setupRegisteredBodyData()
    }

    private fun setupRegisteredHeaderData(flashSale: FlashSale) {
        registeredCdpHeaderBinding?.run {
            when (flashSale.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                    tgCampaignStatus.setTextColorCompat(color.Unify_NN600)
                }
                FlashSaleStatus.WAITING_FOR_SELECTION -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_ORANGE)
                    tgCampaignStatus.setTextColorCompat(color.Unify_YN400)
                }
                FlashSaleStatus.ON_SELECTION_PROCESS -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_BLUE)
                    tgCampaignStatus.setTextColorCompat(color.Unify_BN500)
                }
                FlashSaleStatus.SELECTION_FINISHED -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREEN)
                    tgCampaignStatus.setTextColorCompat(color.Unify_GN500)
                }
                else -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                    tgCampaignStatus.setTextColorCompat(color.Unify_NN600)
                }
            }
            tickerHeader.gone()
            timer.gone()
            tgCampaignStatus.text = flashSale.statusText
            imageCampaign.setImageUrl(flashSale.coverImage)
            tgCampaignName.text = flashSale.name
            setHeaderCampaignPeriod(this, flashSale)
        }
    }

    private fun setupRegisteredMidData(flashSale: FlashSale) {
        registeredCdpMidBinding?.run {
            when (flashSale.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> {
                    cardProductEligibleForSelection.hide()
                    llSelectionProcessView.hide()
                    llBeforeSelectionView.visible()
                    tgRemainingQuotaValue.text = MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_remaining_quota_placeholder,
                            flashSale.remainingQuota,
                            flashSale.maxProductSubmission
                        )
                    )
                    setupRegisteredTimer(this, flashSale)
                }
                FlashSaleStatus.WAITING_FOR_SELECTION -> {
                    cardProductEligibleForSelection.hide()
                    llSelectionProcessView.hide()
                    llBeforeSelectionView.visible()
                    tgRemainingQuotaValue.text = MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_remaining_quota_placeholder,
                            flashSale.remainingQuota,
                            flashSale.maxProductSubmission
                        )
                    )
                    setupRegisteredTimer(this, flashSale)
                }
                FlashSaleStatus.ON_SELECTION_PROCESS -> {
                    cardProductEligibleForSelection.visible()
                    llSelectionProcessView.visible()
                    llBeforeSelectionView.hide()
                    tpgCardMidTitle.text = MethodChecker.fromHtml(
                        getString(
                            R.string.product_eligible_for_selection_card_title_placeholder,
                            flashSale.productMeta.totalProduct
                        )
                    )
                    tpgCardMidDesctiption.text = MethodChecker.fromHtml(
                        getString(
                            R.string.product_eligible_for_selection_end_date_placeholder,
                            flashSale.reviewEndDateUnix.formatTo(
                                DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
                            )
                        )
                    )
                    tpgProposedProductValue.text = MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.totalProduct
                        )
                    )
                }
                FlashSaleStatus.SELECTION_FINISHED -> {
                    cardProductEligibleForSelection.visible()
                    llSelectionProcessView.visible()
                    llBeforeSelectionView.hide()
                    tpgCardMidTitle.text = MethodChecker.fromHtml(
                        getString(
                            R.string.product_eligible_card_title_placeholder,
                            flashSale.productMeta.acceptedProduct
                        )
                    )
                    tpgCardMidDesctiption.text = MethodChecker.fromHtml(
                        getString(
                            R.string.product_eligible_start_date_placeholder,
                            flashSale.reviewEndDateUnix.formatTo(
                                DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
                            )
                        )
                    )
                    tpgProposedProductValue.text = MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.totalProduct
                        )
                    )
                    tpgAcceptedProductValue.text = MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.acceptedProduct
                        )
                    )
                    tpgRejectedProductValue.text = MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.rejectedProduct
                        )
                    )
                }
            }
        }
    }

    private fun setupRegisteredBodyData() {
        registeredCdpBodyBinding?.run {
            btnSelectAllProduct.isVisible =
                viewModel.getCampaignRegisteredStatus() == FlashSaleStatus.WAITING_FOR_SELECTION

            tpgProductCount.text = getString(
                R.string.stfs_product_count_place_holder,
                productAdapter.itemCount
            )

            btnSelectAllProduct.setOnClickListener {
                onShowOrHideItemCheckBox()
            }
        }
        observeSelectedProductData()
        setupRegisteredButton()
    }

    private fun setupRegisteredTimer(binding: StfsCdpRegisteredMidBinding, flashSale: FlashSale) {
        val targetDate = flashSale.submissionEndDateUnix
        when {
            viewModel.isFlashSaleClosedMoreThan24Hours(targetDate) -> {
                binding.timerRegistered.timerFormat = TimerUnifySingle.FORMAT_DAY
                binding.timerRegistered.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timerRegistered.timerVariant = TimerUnifySingle.VARIANT_GENERAL
            }
            viewModel.isFlashSaleClosedLessThan24Hour(targetDate) -> {
                binding.timerRegistered.timerFormat = TimerUnifySingle.FORMAT_HOUR
                binding.timerRegistered.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timerRegistered.timerVariant = TimerUnifySingle.VARIANT_GENERAL
            }
            viewModel.isFlashSaleClosedLessThan60Minutes(targetDate) -> {
                binding.timerRegistered.timerFormat = TimerUnifySingle.FORMAT_MINUTE
                binding.timerRegistered.targetDate = targetDate.removeTimeZone().toCalendar()
                binding.timerRegistered.timerVariant = TimerUnifySingle.VARIANT_GENERAL
            }
        }
    }

    private fun onShowOrHideItemCheckBox() {
        val oldItems = adapter?.getItems() as List<WaitingForSelectionItem>
        val newItems = oldItems.map { it.copy(isCheckBoxShown = !it.isCheckBoxShown) }
        val isShown = newItems[0].isCheckBoxShown
        adapter?.submit(listOf())
        adapter?.submit(newItems)

        registeredCdpBodyBinding?.run {
            if (isShown) {
                tpgSelectedProductCount.visible()
                tpgProductCount.invisible()
                btnSelectAllProduct.text = getString(R.string.fs_canceled_lable)
            } else {
                tpgSelectedProductCount.invisible()
                tpgProductCount.visible()
                btnSelectAllProduct.text = getString(R.string.stfs_choose_all_product_label)
            }
        }
        setupRegisteredButton(isShown)
    }

    private fun setupRegisteredButton(isShown: Boolean = false) {
        binding?.run {
            cardProductEligible.gone()
            cardBottomButtonGroup.visible()
            btnRegister.text = getString(R.string.stfs_add_product)
            btnDelete.text = getString(R.string.stfs_label_delete)
            btnEdit.text = getString(R.string.stfs_label_edit)
            if (isShown) {
                btnRegister.gone()
                btnDelete.visible()
                btnEdit.visible()
            } else {
                btnRegister.visible()
                btnDelete.gone()
                btnEdit.gone()
            }
        }
    }

    /**
     * Region Ongoing CDP
     */
    private fun setupOngoing() {
        //TODO: implement ongoing CDP
    }

    /**
     * Region Finished CDP
     */
    private fun setupFinished() {
        //TODO: implement finished CDP
    }

    private fun setHeaderCampaignPeriod(
        binding: StfsCdpHeaderBinding,
        flashSale: FlashSale
    ) {
        binding.run {
            val startDate =
                flashSale.startDateUnix.formatTo(DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT)
            tgCampaignPeriod.text = if (viewModel.isFlashSalePeriodOnTheSameDate(flashSale)) {
                val endDate = flashSale.endDateUnix.formatTo(TIME_MINUTE_PRECISION_WITH_TIMEZONE)
                "$startDate - $endDate"
            } else {
                val endDate = flashSale.endDateUnix.formatTo(
                    DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
                )
                "$startDate - $endDate"
            }
        }
    }

    private fun showLoading() {
        binding?.run {
            loader.show()
            nsvContent.gone()
            cardBottomButtonGroup.gone()
        }
    }

    private fun hideLoading() {
        binding?.run {
            loader.gone()
            nsvContent.show()
        }
    }

    private fun onProductClicked(itemPosition: Int) {
        val selectedProduct = productAdapter.getItems()[itemPosition]
        val selectedProductId = selectedProduct.id()
        //TODO: Open detail product bottom sheet
    }

    private fun onCheckBoxClicked(itemPosition: Int, isCheckBoxChecked: Boolean) {
        val selectedProduct = productAdapter.getItems()[itemPosition]
        val selectedProductId = selectedProduct.id() as Long

        if (isCheckBoxChecked) {
            viewModel.setSelectedItem(selectedProductId)
        } else {
            viewModel.removeSelectedItem(selectedProductId)
        }
    }

    override fun createAdapter(): CompositeAdapter {
        return productAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.rvSubmittedProductList
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<DelegateAdapterItem>) {
        adapter?.submit(list)
    }

    override fun loadData(page: Int, offset: Int) {
        loadSubmittedProductListData(offset)
    }

    override fun clearAdapterData() {
        adapter?.submit(listOf())
    }

    override fun onShowLoading() {
        adapter?.addItem(LoadingItem)
    }

    override fun onHideLoading() {
        adapter?.removeItem(LoadingItem)
    }

    override fun onDataEmpty() {
        adapter?.removeItem(LoadingItem)
    }

    override fun onGetListError(message: String) {
        adapter?.removeItem(LoadingItem)
    }
}