package com.tokopedia.tkpd.flashsale.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_MONTH_ONLY
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
import com.tokopedia.campaign.utils.constant.DateConstant.TIME_MINUTE_PRECISION_WITH_TIMEZONE
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.tkpd.flashsale.common.extension.enablePaging
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.common.extension.toCalendar
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.enums.DetailBottomSheetType
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.entity.enums.UpcomingCampaignStatus
import com.tokopedia.tkpd.flashsale.domain.entity.enums.isFlashSaleAvailable
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.ChooseProductActivity
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.common.provider.ProductProvider
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.OngoingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.OngoingRejectedDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.FinishedProcessSelectionDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.OnSelectionProcessDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.WaitingForSelectionDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet.CampaignDetailBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem
import com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleContainerFragment
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.mapper.BulkApplyMapper
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        private const val UPCOMING_TAB = "upcoming"
        private const val REGISTERED_TAB = "registered"
        private const val ONGOING_TAB = "ongoing"
        private const val FINISHED_TAB = "finished"
        private const val PAGE_SIZE = 10
        private const val IMAGE_PRODUCT_ELIGIBLE_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/seller_toped.png"
        private const val EMPTY_SUBMITTED_PRODUCT_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/empty_cdp_product_list_Illustration.png"
        private const val FINISHED_HEADER_IMAGE_BANNER_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/finished_campaign_banner.png"

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

    //main binding
    private var binding by autoClearedNullable<StfsFragmentCampaignDetailBinding>()

    //reusable binding
    private var cdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var cdpBodyBinding by autoClearedNullable<StfsCdpBodyBinding>()

    //upcoming
    private var upcomingCdpMidBinding by autoClearedNullable<StfsCdpUpcomingMidBinding>()
    private var upcomingCdpBodyBinding by autoClearedNullable<StfsCdpUpcomingBodyBinding>()

    //registered
    private var registeredCdpMidBinding by autoClearedNullable<StfsCdpRegisteredMidBinding>()

    //ongoing
    private var ongoingCdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var ongoingCdpMidBinding by autoClearedNullable<StfsCdpOngoingMidBinding>()

    //finished
    private var finishedCdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var finishedCdpMidBinding by autoClearedNullable<StfsCdpOngoingMidBinding>()

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
            .add(OnSelectionProcessDelegateAdapter(
                onProductItemClicked = { onProductClicked(it) }
            ))
            .add(FinishedProcessSelectionDelegateAdapter(
                onProductItemClicked = { onProductClicked(it) }
            ))
            .add(OngoingDelegateAdapter(
                onProductItemClicked = { onProductClicked(it) }
            ))
            .add(OngoingRejectedDelegateAdapter(
                onProductItemClicked = { onProductClicked(it) }
            ))
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
        setupChooseProductRedirection()
    }

    private fun observeCampaignDetail() {
        viewModel.campaign.observe(viewLifecycleOwner) { flashSale ->
            hideLoading()
            when (flashSale) {
                is Success -> {
                    setupView(flashSale.data)
                }
                is Fail -> {
                    showGlobalError()
                }
            }
        }
    }

    private fun observeSubmittedProductData() {
        viewModel.submittedProduct.observe(viewLifecycleOwner) { submittedProduct ->
            hideLoading()
            when (submittedProduct) {
                is Success -> {
                    productAdapter.addItems(submittedProduct.data)
                    setupSubmittedProductListData()
                }
                is Fail -> {
                    showGlobalError()
                }
            }
        }
    }

    private fun observeSelectedProductData() {
        viewModel.selectedItemsId.observe(viewLifecycleOwner) { ids ->
            cdpBodyBinding?.tpgSelectedProductCount?.text =
                getString(
                    R.string.stfs_choosen_product_count_placeholder,
                    ids.count()
                )
        }
    }

    private fun setupHeader(flashSale: FlashSale) {
        val activity = activity ?: return
        val infoIcon = IconUnify(activity, IconUnify.INFORMATION)
        binding?.header?.run {
            setNavigationOnClickListener { activity.finish() }
            addCustomRightContent(infoIcon)
            setOnClickListener {
                showBottomSheet(flashSale, DetailBottomSheetType.GENERAL)
            }
        }
    }

    private fun setupView(flashSale: FlashSale) {
        setupHeader(flashSale)
        when (tabName) {
            UPCOMING_TAB -> setupUpcoming(flashSale)
            REGISTERED_TAB -> {
                setupPaging()
                loadSubmittedProductListData(Int.ZERO)
                setupRegistered(flashSale)
            }
            ONGOING_TAB -> {
                setupPaging()
                loadSubmittedProductListData(Int.ZERO)
                setupOngoing(flashSale)
            }
            FINISHED_TAB -> {
                setupPaging()
                loadSubmittedProductListData(Int.ZERO)
                setupFinished(flashSale)
            }
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
                cdpHeaderBinding = StfsCdpHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                upcomingCdpMidBinding = StfsCdpUpcomingMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                upcomingCdpBodyBinding = StfsCdpUpcomingBodyBinding.bind(view)
            }
            cardBottomButtonGroup.isVisible = campaignStatus.isFlashSaleAvailable()
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
            iconRegisterPeriodInfo.setOnClickListener {
                showBottomSheet(flashSale, DetailBottomSheetType.TIMELINE)
            }
            btnSeeCriteria.setOnClickListener {
                showBottomSheet(flashSale, DetailBottomSheetType.PRODUCT_CRITERIA)
            }
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
        cdpHeaderBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> {
                    setNoProductEligibleHeader()
                }
                UpcomingCampaignStatus.FULL_QUOTA -> {
                    setFullQuotaHeader()
                }
                UpcomingCampaignStatus.CLOSED -> {
                    setClosedHeader()
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

    private fun setNoProductEligibleHeader() {
        cdpHeaderBinding?.run {
            tickerHeader.visible()
            tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_product_not_eligible_state))
            tgCampaignStatus.text = getString(R.string.registration_over_in_label)
        }
    }

    private fun setFullQuotaHeader() {
        cdpHeaderBinding?.run {
            tickerHeader.visible()
            tickerHeader.tickerTitle =
                getString(R.string.stfs_title_ticker_upcoming_cdp_full_quota_state)
            tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_full_quota_state))
            tgCampaignStatus.text = getString(R.string.registration_over_in_label)
        }
    }

    private fun setClosedHeader() {
        cdpHeaderBinding?.run {
            tickerHeader.visible()
            tickerHeader.tickerTitle =
                getString(R.string.stfs_title_ticker_upcoming_cdp_registration_close_state)
            tickerHeader.setTextDescription(getString(R.string.stfs_description_ticker_upcoming_cdp_registration_close_state))
            tgCampaignStatus.text = getString(R.string.registration_closed_in_label)
        }
    }

    private fun setupUpcomingMidData(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        upcomingCdpMidBinding?.run {
            when (campaignStatus) {
                UpcomingCampaignStatus.NO_PRODUCT_ELIGIBLE -> setNoProductEligibleMidSection(
                    flashSale
                )
                UpcomingCampaignStatus.FULL_QUOTA -> setFullQuotaMidSection()
                UpcomingCampaignStatus.CLOSED -> setClosedMidSection(flashSale)
                else -> setDefaultUpcomingMidSection(flashSale)
            }
        }
    }

    private fun setNoProductEligibleMidSection(flashSale: FlashSale) {
        upcomingCdpMidBinding?.run {
            tgCampaignQuota.text = getString(
                R.string.campaign_quota_value_placeholder,
                flashSale.remainingQuota
            )
            btnCheckReason.visible()
        }
    }

    private fun setFullQuotaMidSection() {
        upcomingCdpMidBinding?.run {
            tgCampaignQuota.apply {
                text = getString(R.string.full_quota_label)
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R500
                    )
                )
            }
        }
    }

    private fun setClosedMidSection(flashSale: FlashSale) {
        upcomingCdpMidBinding?.run {
            tgCampaignQuota.text = getString(
                R.string.campaign_quota_value_placeholder_2,
                flashSale.remainingQuota
            )
        }
    }

    private fun setDefaultUpcomingMidSection(flashSale: FlashSale) {
        upcomingCdpMidBinding?.run {
            tgCampaignQuota.text = getString(
                R.string.campaign_quota_value_placeholder,
                flashSale.remainingQuota
            )
        }
    }

    private fun setupUpcomingTimer(binding: StfsCdpHeaderBinding, flashSale: FlashSale) {
        val targetDate = flashSale.submissionEndDateUnix
        val onTimerFinished = { binding.timer.gone() }
        binding.timer.timerFormat = TimerUnifySingle.FORMAT_AUTO
        binding.timer.targetDate = targetDate.removeTimeZone().toCalendar()
        binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
        binding.timer.onFinish = onTimerFinished
    }

    private fun setupUpcomingButton() {
        binding?.run {
            btnRegister.text = getString(R.string.label_register)
            imageProductEligible.loadImage(IMAGE_PRODUCT_ELIGIBLE_URL)
        }
    }

    /**
     * Region Registered CDP
     */
    private fun setupRegistered(flashSale: FlashSale) {
        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                cdpHeaderBinding = StfsCdpHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                registeredCdpMidBinding = StfsCdpRegisteredMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                cdpBodyBinding = StfsCdpBodyBinding.bind(view)
            }
        }

        setupRegisteredHeader(flashSale)
        setupRegisteredMid(flashSale)
        setupRegisteredBody()
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
        inflatedView.layoutResource = R.layout.stfs_cdp_body
        inflatedView.inflate()
    }

    private fun setupRegisteredHeaderData(flashSale: FlashSale) {
        cdpHeaderBinding?.run {
            when (flashSale.status) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                    tgCampaignStatus.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                }
                FlashSaleStatus.WAITING_FOR_SELECTION -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_ORANGE)
                    tgCampaignStatus.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_YN400)
                }
                FlashSaleStatus.ON_SELECTION_PROCESS -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_BLUE)
                    tgCampaignStatus.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_BN500)
                }
                FlashSaleStatus.SELECTION_FINISHED -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREEN)
                    tgCampaignStatus.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                }
                else -> {
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                    tgCampaignStatus.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
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
                FlashSaleStatus.WAITING_FOR_SELECTION -> setWaitingForSelectionMidSection(flashSale)
                FlashSaleStatus.ON_SELECTION_PROCESS -> setOnSelectionProcessMidSection(flashSale)
                FlashSaleStatus.SELECTION_FINISHED -> setFinishedSelectionProcessMidSection(
                    flashSale
                )
                else -> setDefaultRegisteredMidSection(flashSale)
            }
        }
    }

    private fun setupChooseProductRedirection() {
        binding?.btnRegister?.setOnClickListener {
            ChooseProductActivity.start(context?: return@setOnClickListener, flashSaleId)
        }
        upcomingCdpMidBinding?.btnCheckReason?.setOnClickListener {
            ChooseProductActivity.start(context?: return@setOnClickListener, flashSaleId)
        }
    }

    private fun setWaitingForSelectionMidSection(flashSale: FlashSale) {
        registeredCdpMidBinding?.run {
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
    }

    private fun setOnSelectionProcessMidSection(flashSale: FlashSale) {
        registeredCdpMidBinding?.run {
            cardProductEligibleForSelection.visible()
            llSelectionProcessView.visible()
            llBeforeSelectionView.hide()
            imageProductEligibleForSelection.loadImage(IMAGE_PRODUCT_ELIGIBLE_URL)
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
    }

    private fun setFinishedSelectionProcessMidSection(flashSale: FlashSale) {
        registeredCdpMidBinding?.run {
            cardProductEligibleForSelection.visible()
            llSelectionProcessView.visible()
            llBeforeSelectionView.hide()
            imageProductEligibleForSelection.loadImage(IMAGE_PRODUCT_ELIGIBLE_URL)
            tpgCardMidTitle.text = MethodChecker.fromHtml(
                getString(
                    R.string.product_eligible_card_title_placeholder,
                    flashSale.productMeta.acceptedProduct
                )
            )
            tpgCardMidDesctiption.text = MethodChecker.fromHtml(
                getString(
                    R.string.product_eligible_start_date_placeholder,
                    flashSale.startDateUnix.formatTo(
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

    private fun setDefaultRegisteredMidSection(flashSale: FlashSale) {
        registeredCdpMidBinding?.run {
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
    }

    private fun setupRegisteredTimer(binding: StfsCdpRegisteredMidBinding, flashSale: FlashSale) {
        val targetDate = flashSale.submissionEndDateUnix
        binding.timerRegistered.timerFormat = TimerUnifySingle.FORMAT_AUTO
        binding.timerRegistered.targetDate = targetDate.removeTimeZone().toCalendar()
        binding.timerRegistered.timerVariant = TimerUnifySingle.VARIANT_GENERAL
    }

    private fun onShowOrHideItemCheckBox() {
        val oldItems = productAdapter.getItems().filterIsInstance<WaitingForSelectionItem>()
        val newItems = oldItems.map { it.copy(isCheckBoxShown = !it.isCheckBoxShown) }
        val isShown = newItems[Int.ZERO].isCheckBoxShown
        productAdapter.submit(newItems)
        viewModel.setCheckBoxStateStatus(isShown)

        cdpBodyBinding?.run {
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
            cardBottomButtonGroup.isVisible = viewModel.getAddProductButtonVisibility()
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

    private fun onCheckBoxClicked(itemPosition: Int, isCheckBoxChecked: Boolean) {
        val selectedProduct = productAdapter.getItems()[itemPosition]
        val selectedProductId = selectedProduct.id() as Long

        if (isCheckBoxChecked) {
            viewModel.setSelectedItem(selectedProductId)
        } else {
            viewModel.removeSelectedItem(selectedProductId)
        }
    }

    /**
     * Region Ongoing CDP
     */
    private fun setupOngoing(flashSale: FlashSale) {
        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                ongoingCdpHeaderBinding = StfsCdpHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                ongoingCdpMidBinding = StfsCdpOngoingMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                cdpBodyBinding = StfsCdpBodyBinding.bind(view)
            }
            cardBottomButtonGroup.gone()
        }
        setupOngoingHeader(flashSale)
        setupOngoingMid(flashSale)
        setupRegisteredBody()
        observeSubmittedProductData()
    }

    private fun setupOngoingHeader(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        inflatedView.inflate()
        setupOngoingHeaderData(flashSale)
    }

    private fun setupOngoingMid(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_ongoing_mid
        inflatedView.inflate()
        setupOngoingMidData(flashSale)
    }

    private fun setupOngoingHeaderData(flashSale: FlashSale) {
        ongoingCdpHeaderBinding?.run {
            when (flashSale.status) {
                FlashSaleStatus.ONGOING -> setOngoingHeader()
                FlashSaleStatus.REJECTED -> setRejectedHeader(flashSale)
                else -> setDefaultOngoingHeader(flashSale)
            }
            tickerHeader.gone()
            timer.gone()
            imageCampaign.setImageUrl(flashSale.coverImage)
            tgCampaignName.text = flashSale.name
            setHeaderCampaignPeriod(this, flashSale)
        }
    }

    private fun setOngoingHeader() {
        ongoingCdpHeaderBinding?.run {
            imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREEN)
            tgCampaignStatus.apply {
                setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                text = context.getString(R.string.stfs_status_ongoing)
            }
        }
    }

    private fun setRejectedHeader(flashSale: FlashSale) {
        ongoingCdpHeaderBinding?.run {
            imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_RED)
            tgCampaignStatus.apply {
                setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                text = flashSale.statusText
            }
        }
    }

    private fun setDefaultOngoingHeader(flashSale: FlashSale) {
        ongoingCdpHeaderBinding?.run {
            imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
            tgCampaignStatus.apply {
                setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                text = flashSale.statusText
            }
        }
    }

    private fun setupOngoingMidData(flashSale: FlashSale) {
        ongoingCdpMidBinding?.run {
            tpgProposedProductValue.text = MethodChecker.fromHtml(
                getString(
                    R.string.stfs_mid_section_product_count_placeholder,
                    flashSale.productMeta.totalProduct
                )
            )
            when (flashSale.status) {
                FlashSaleStatus.ONGOING -> setOngoingMidSection(flashSale)
                else -> setRejectedMidSection()
            }
        }
    }

    private fun setOngoingMidSection(flashSale: FlashSale) {
        ongoingCdpMidBinding?.run {
            cardFlashSalePerformance.setCardUnifyBackgroundColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_BN50
                )
            )
            imageCardFlashSalePerformance.loadImage(IMAGE_PRODUCT_ELIGIBLE_URL)
            tpgCardMidTitle.text =
                getString(R.string.stft_flash_sale_performace_card_title_label)
            tpgCardMidDesctiption.text =
                getString(R.string.stfs_flash_sale_performance_card_desc_label)
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
            tpgSoldValue.text = MethodChecker.fromHtml(
                getString(
                    R.string.stfs_mid_section_product_count_placeholder,
                    flashSale.productMeta.totalStockSold
                )
            )
            tpgSellingValue.text =
                flashSale.productMeta.totalSoldValue.getCurrencyFormatted()
        }
    }

    private fun setRejectedMidSection() {
        ongoingCdpMidBinding?.run {
            cardFlashSalePerformance.setCardUnifyBackgroundColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN50
                )
            )
            imageCardFlashSalePerformance.loadImage(IMAGE_PRODUCT_ELIGIBLE_URL)
            tpgCardMidTitle.text =
                getString(R.string.stft_flash_sale_performace_card_title_label_rejected)
            tpgCardMidDesctiption.text =
                getString(R.string.stft_flash_sale_performace_card_desc_label_rejected)
            tpgAcceptedProductValue.text = getString(R.string.stfs_dash_label)
            tpgRejectedProductValue.text = getString(R.string.stfs_dash_label)
            tpgSoldValue.text = getString(R.string.stfs_dash_label)
            tpgSellingValue.text = getString(R.string.stfs_dash_label)
        }
    }

    /**
     * Region Finished CDP
     */
    private fun setupFinished(flashSale: FlashSale) {
        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                finishedCdpHeaderBinding = StfsCdpHeaderBinding.bind(view)
            }
            layoutMid.setOnInflateListener { _, view ->
                finishedCdpMidBinding = StfsCdpOngoingMidBinding.bind(view)
            }
            layoutBody.setOnInflateListener { _, view ->
                cdpBodyBinding = StfsCdpBodyBinding.bind(view)
            }
            cardBottomButtonGroup.gone()
        }
        setupFinishedHeader(flashSale)
        setupFinishedMid(flashSale)
        setupFinishedBody()
        observeSubmittedProductData()
    }

    private fun setupFinishedHeader(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        inflatedView.inflate()
        setupFinishedHeaderData(flashSale)
    }

    private fun setupFinishedMid(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_ongoing_mid
        inflatedView.inflate()
        setupFinishedMidData(flashSale)
    }

    private fun setupFinishedBody() {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_body
        inflatedView.inflate()
    }

    private fun setupFinishedHeaderData(flashSale: FlashSale) {
        finishedCdpHeaderBinding?.run {
            when (flashSale.status) {
                FlashSaleStatus.FINISHED -> setFinishedHeader()
                else -> setFinishedDefaultHeader()
            }
            tgCampaignStatus.apply {
                text = flashSale.statusText
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R500
                    )
                )
            }
            imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_RED)
            tickerHeader.gone()
            timer.gone()
            imageCampaign.setImageUrl(flashSale.coverImage)
            tgCampaignName.text = flashSale.name
            setHeaderCampaignPeriod(this, flashSale)
        }
    }

    private fun setFinishedHeader() {
        finishedCdpHeaderBinding?.run {
            imageFinishedCampaingBanner.run {
                loadImage(FINISHED_HEADER_IMAGE_BANNER_URL)
                visible()
            }
            groupCampaignStatus.gone()
        }
    }

    private fun setFinishedDefaultHeader() {
        finishedCdpHeaderBinding?.run {
            imageFinishedCampaingBanner.gone()
            groupCampaignStatus.visible()
        }
    }

    private fun setupFinishedMidData(flashSale: FlashSale) {
        finishedCdpMidBinding?.run {
            tpgProposedProductValue.text = MethodChecker.fromHtml(
                getString(
                    R.string.stfs_mid_section_product_count_placeholder,
                    flashSale.productMeta.totalProduct
                )
            )
            when (flashSale.status) {
                FlashSaleStatus.FINISHED -> setFinishedMidSection(flashSale)
                FlashSaleStatus.CANCELLED -> setCanceledMidSection(flashSale)
                FlashSaleStatus.MISSED -> setMissedMidSection()
                else -> setDefaultFinishedMidSection()
            }
            setupFinishedMidCardTickerAppearance()
        }
    }

    private fun setFinishedMidSection(flashSale: FlashSale) {
        finishedCdpMidBinding?.run {
            cardFlashSalePerformance.gone()
            groupCampaignStatistics.visible()
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
            tpgSoldValue.text = MethodChecker.fromHtml(
                getString(
                    R.string.stfs_mid_section_product_count_placeholder,
                    flashSale.productMeta.totalStockSold
                )
            )
            tpgSellingValue.text =
                flashSale.productMeta.totalSoldValue.getCurrencyFormatted()
        }
    }

    private fun setCanceledMidSection(flashSale: FlashSale) {
        finishedCdpMidBinding?.run {
            cardFlashSalePerformance.visible()
            tpgCardMidTitle.text = getString(R.string.stfs_canceled_card_title_label)
            tpgCardMidDesctiption.text = getString(
                R.string.stfst_canceled_card_description_placeholder,
                flashSale.cancellationReason
            )
            groupCampaignStatistics.gone()
        }
    }

    private fun setMissedMidSection() {
        finishedCdpMidBinding?.run {
            cardFlashSalePerformance.visible()
            tpgCardMidTitle.text = getString(R.string.stfs_missed_card_title_label)
            tpgCardMidDesctiption.text = getString(R.string.stfs_missed_card_desc_label)
            groupCampaignStatistics.gone()
        }
    }

    private fun setDefaultFinishedMidSection() {
        finishedCdpMidBinding?.run {
            cardFlashSalePerformance.visible()
            groupCampaignStatistics.gone()
            tpgCardMidTitle.text =
                getString(R.string.stft_flash_sale_performace_card_title_label_rejected)
            tpgCardMidDesctiption.text =
                getString(R.string.stft_flash_sale_performace_card_desc_label_rejected)
            tpgAcceptedProductValue.text = getString(R.string.stfs_dash_label)
            tpgRejectedProductValue.text = getString(R.string.stfs_dash_label)
            tpgSoldValue.text = getString(R.string.stfs_dash_label)
            tpgSellingValue.text = getString(R.string.stfs_dash_label)
        }
    }

    private fun setupFinishedMidCardTickerAppearance() {
        finishedCdpMidBinding?.run {
            cardFlashSalePerformance.setCardUnifyBackgroundColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN50
                )
            )
            imageCardFlashSalePerformance.loadImage(IMAGE_PRODUCT_ELIGIBLE_URL)
        }
    }

    /**
     * Reusable Method
     */
    private fun loadCampaignDetailData() {
        showLoading()
        viewModel.getCampaignDetail(flashSaleId)
    }

    private fun loadSubmittedProductListData(offset: Int) {
        viewModel.getSubmittedProduct(flashSaleId, offset)
    }

    private fun setHeaderCampaignPeriod(
        binding: StfsCdpHeaderBinding,
        flashSale: FlashSale
    ) {
        binding.run {
            val startDate =
                flashSale.startDateUnix.formatTo(
                    DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
                )
            tgCampaignPeriod.text = if (viewModel.isFlashSalePeriodOnTheSameDate(flashSale)) {
                val endDate =
                    flashSale.endDateUnix.formatTo(TIME_MINUTE_PRECISION_WITH_TIMEZONE)
                "$startDate - $endDate"
            } else {
                val endDate = flashSale.endDateUnix.formatTo(
                    DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
                )
                "$startDate - $endDate"
            }
        }
    }

    private fun setupBody() {
        binding?.run {
            when (viewModel.getCampaignStatus()) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> {
                    setupNoRegisteredProductEmptyState()
                }
                FlashSaleStatus.MISSED -> {
                    setupFinishedEmptyProductState()
                }
                FlashSaleStatus.CANCELLED -> {
                    setupFinishedEmptyProductState()
                }
                else -> {
                    setupSubmittedProductList()
                }
            }
        }
    }

    private fun setupSubmittedProductListData() {
        cdpBodyBinding?.run {
            val isShowButtonToggle =
                viewModel.getCampaignStatus() == FlashSaleStatus.WAITING_FOR_SELECTION
            val isShowProductListHeader = when (viewModel.getCampaignStatus()) {
                FlashSaleStatus.NO_REGISTERED_PRODUCT -> false
                FlashSaleStatus.CANCELLED -> false
                FlashSaleStatus.MISSED -> false
                else -> true
            }

            btnSelectAllProduct.isVisible = isShowButtonToggle
            tpgProductCount.isVisible = isShowProductListHeader
            tpgRegisterBodyTitle.isVisible = isShowProductListHeader
            tpgProductCount.text = getString(
                R.string.stfs_product_count_place_holder,
                productAdapter.itemCount
            )

            btnSelectAllProduct.setOnClickListener {
                onShowOrHideItemCheckBox()
            }
        }
        setupBody()
        observeSelectedProductData()
        setupRegisteredButton()
    }

    private fun setupNoRegisteredProductEmptyState() {
        binding?.run {
            emptyStateProductList.visible()
            rvSubmittedProductList.gone()
            emptyStateProductList.setImageUrl(EMPTY_SUBMITTED_PRODUCT_URL)
            tpgEmptyBodyDescLabel.gone()
            btnCheckOtherCampaign.gone()
        }
    }

    private fun setupSubmittedProductList() {
        binding?.run {
            rvSubmittedProductList.visible()
            emptyStateProductList.gone()
            emptyStateProductList.gone()
            tpgEmptyBodyDescLabel.gone()
            btnCheckOtherCampaign.gone()
        }
    }

    private fun setupFinishedEmptyProductState() {
        binding?.run {
            tpgEmptyBodyDescLabel.visible()
            btnCheckOtherCampaign.run {
                visible()
                setOnClickListener {
                    activity?.finish()
                }
            }
            emptyStateProductList.gone()
            rvSubmittedProductList.gone()
            emptyStateProductList.gone()
        }
    }

    private fun setupPaging() {
        binding?.rvSubmittedProductList?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            adapter = productAdapter
        }

        binding?.nsvContent?.enablePaging() {
            val isInCheckBoxState = viewModel.isOnCheckBoxState()
            val hasNextPage = productAdapter.itemCount >= PAGE_SIZE
            if (hasNextPage && !isInCheckBoxState) {
                loadSubmittedProductListData(productAdapter.itemCount)
            }
        }
    }

    private fun showLoading() {
        binding?.run {
            loader.show()
            llContent.gone()
            cardBottomButtonGroup.gone()
            globalError.gone()
        }
    }

    private fun hideLoading() {
        binding?.run {
            loader.gone()
            llContent.show()
            globalError.gone()
        }
    }

    private fun showGlobalError() {
        binding?.run {
            loader.gone()
            llContent.gone()
            cardBottomButtonGroup.gone()
            globalError.apply {
                show()
                setActionClickListener {
                    loadCampaignDetailData()
                }
            }
        }
    }

    private fun showBottomSheet(flashSale: FlashSale, type: DetailBottomSheetType) {
        val activity = activity ?: return
        CampaignDetailBottomSheet.newInstance(
            viewModel.getBottomSheetData(
                type,
                flashSale
            )
        ).show(activity.supportFragmentManager, "")
    }

    private fun onProductClicked(itemPosition: Int) {
        val selectedProduct = productAdapter.getItems()[itemPosition]
        val selectedProductId = selectedProduct.id()
        //TODO: Open detail product bottom sheet
        val uiModel = BulkApplyMapper.mapProductToBulkParam(ProductProvider.generateProduct())
        val bottomSheet = ProductBulkApplyBottomSheet.newInstance(uiModel)
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
        bottomSheet.setOnApplyClickListener { Timber.d(it.toString()) }
    }
}