package com.tokopedia.tkpd.flashsale.presentation.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_MONTH_ONLY
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE_ID_FORMAT
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_YEAR_PRECISION
import com.tokopedia.campaign.utils.constant.DateConstant.TIME_MINUTE_PRECISION_WITH_TIMEZONE
import com.tokopedia.campaign.utils.constant.ImageUrlConstant
import com.tokopedia.campaign.utils.extension.*
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.common.bottomsheet.sse_submission_error.FlashSaleProductListSseSubmissionErrorBottomSheet
import com.tokopedia.tkpd.flashsale.common.dialog.FlashSaleProductSseSubmissionDialog
import com.tokopedia.tkpd.flashsale.common.dialog.FlashSaleProductSseSubmissionProgressDialog
import com.tokopedia.tkpd.flashsale.common.extension.enablePaging
import com.tokopedia.tkpd.flashsale.common.extension.toCalendar
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionSseResult
import com.tokopedia.tkpd.flashsale.domain.entity.enums.*
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.ProductCheckBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.ChooseProductActivity
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.OngoingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.OngoingRejectedDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.FinishedProcessSelectionDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.OnSelectionProcessDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.WaitingForSelectionDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet.CampaignDetailBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.detail.mapper.ProductCheckingResultMapper
import com.tokopedia.tkpd.flashsale.presentation.ineligibleaccess.IneligibleAccessActivity
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.FlashSaleManageProductListActivity
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val APPLINK_SEGMENTS_SIZE = 2
        private const val DEFAULT_FOR_EMPTY_FLASH_SALE_ID = 0L
        private const val DELAY = 1500L
        private const val IMAGE_PRODUCT_ELIGIBLE_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/seller_toped_new.png"
        private const val IMAGE_PRODUCT_INELIGIBLE_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/surprised_toped.png"
        private const val EMPTY_SUBMITTED_PRODUCT_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/empty_cdp_product_list_Illustration_1.png"
        private const val FINISHED_HEADER_IMAGE_BANNER_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/finished_campaign_banner.png"

        @JvmStatic
        fun newInstance(
            flashSaleId: Long,
            totalSubmittedProduct: Long = 0
        ): CampaignDetailFragment {
            val fragment = CampaignDetailFragment()
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putLong(BundleConstant.BUNDLE_KEY_TOTAL_SUBMITTED_PRODUCT, totalSubmittedProduct)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }
    private val checkProductBottomSheet = ProductCheckBottomSheet()

    // main binding
    private var binding by autoClearedNullable<StfsFragmentCampaignDetailBinding>()

    // reusable binding
    private var cdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var cdpBodyBinding by autoClearedNullable<StfsCdpBodyBinding>()

    // upcoming
    private var upcomingCdpMidBinding by autoClearedNullable<StfsCdpUpcomingMidBinding>()
    private var upcomingCdpBodyBinding by autoClearedNullable<StfsCdpUpcomingBodyBinding>()

    // registered
    private var registeredCdpMidBinding by autoClearedNullable<StfsCdpRegisteredMidBinding>()

    // ongoing
    private var ongoingCdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var ongoingCdpMidBinding by autoClearedNullable<StfsCdpOngoingMidBinding>()

    // finished
    private var finishedCdpHeaderBinding by autoClearedNullable<StfsCdpHeaderBinding>()
    private var finishedCdpMidBinding by autoClearedNullable<StfsCdpOngoingMidBinding>()

    private var flashSaleName: String = ""
    private val flashSaleId by lazy {
        val appLinkData = RouteManager.getIntent(activity, activity?.intent?.data.toString()).data
        if (isOpenedFromApplink(appLinkData)) {
            getFLashSaleIdFromApplink(appLinkData)
        } else {
            getFlashSaleIdFromBundle()
        }
    }

    private var totalSubmittedProduct: Long = Int.ZERO.toLong()

    // coachmark
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
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
                    }
                )
            )
            .add(
                OnSelectionProcessDelegateAdapter(
                    onProductItemClicked = { onProductClicked(it) }
                )
            )
            .add(
                FinishedProcessSelectionDelegateAdapter(
                    onProductItemClicked = { onProductClicked(it) }
                )
            )
            .add(
                OngoingDelegateAdapter(
                    onProductItemClicked = { onProductClicked(it) }
                )
            )
            .add(
                OngoingRejectedDelegateAdapter(
                    onProductItemClicked = { onProductClicked(it) }
                )
            )
            .add(LoadingDelegateAdapter())
            .build()
    }
    private var sseProgressDialog: FlashSaleProductSseSubmissionProgressDialog? = null

    override fun getScreenName(): String =
        CampaignDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentCampaignDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupProductSubmissionCount()
        setupChooseProductRedirection()
        observeCampaignDetail()
        observeUiEffect()
        observeProductReserveResult()
        observeDeletedProductResult()
        observeCampaignRegistrationResult()
        observeSubmittedProductVariant()
        observeSubmittedProductData()
        loadCampaignDetailData()
    }

    private fun init() {
        context?.let {
            sseProgressDialog = FlashSaleProductSseSubmissionProgressDialog(it)
        }
    }

    private fun observeCampaignDetail() {
        viewModel.campaign.observe(viewLifecycleOwner) { flashSale ->
            hideLoading()
            doOnDelayFinished(DELAY) {
                when (flashSale) {
                    is Success -> {
                        setupView(flashSale.data)
                        getFlashSaleSubmissionProgress(flashSaleId)
                    }
                    is Fail -> {
                        binding?.run { header.setNavigationOnClickListener { activity?.finish() } }
                        showGlobalError()
                    }
                }
            }
        }
    }

    private fun observeSubmittedProductData() {
        viewModel.submittedProduct.observe(viewLifecycleOwner) { submittedProduct ->
            hideLoading()
            when (submittedProduct) {
                is Success -> {
                    if (viewModel.isTriggeredFromDelete()) {
                        productAdapter.submit(submittedProduct.data)
                    } else {
                        productAdapter.addItems(submittedProduct.data)
                    }
                    setupSubmittedProductListData()
                }
                is Fail -> {
                    showGlobalError()
                }
            }
            viewModel.removeAllSelectedItems()
        }
    }

    private fun observeSelectedProductData() {
        viewModel.selectedProducts.observe(viewLifecycleOwner) { products ->
            cdpBodyBinding?.tpgSelectedProductCount?.text =
                getString(
                    R.string.stfs_choosen_product_count_placeholder,
                    products.count()
                )
            binding?.apply {
                if (products.count() > Int.ZERO && viewModel.isOnCheckBoxState()) {
                    btnRegister.gone()
                    btnDelete.visible()
                    btnEdit.visible()
                } else {
                    btnDelete.gone()
                    btnEdit.gone()
                }
            }
        }
    }

    private fun observeProductReserveResult() {
        viewModel.productReserveResult.observe(viewLifecycleOwner) { result ->
            val campaignId = flashSaleId
            val reservationId = result.second
            binding?.btnEdit?.isLoading = false
            if (result.first.isSuccess) {
                redirectToFlashSaleManageProductListPage(reservationId, campaignId.toString())
            } else {
                showErrorToaster(result.first.errorMessage)
            }
        }
    }

    private fun redirectToFlashSaleManageProductListPage(
        reservationId: String,
        campaignId: String
    ) {
        activity?.let {
            it.finish()
            FlashSaleManageProductListActivity.start(
                it,
                reservationId,
                campaignId,
                viewModel.getTabName()
            )
        }
    }

    private fun observeDeletedProductResult() {
        viewModel.productDeleteResult.observe(viewLifecycleOwner) { result ->
            doOnDelayFinished(DELAY) {
                if (result.isSuccess) {
                    val selectedProductCount = viewModel.selectedProducts.value?.count()
                    binding?.run {
                        cardBottomButtonGroup.showToaster(
                            getString(
                                R.string.stfs_success_delete_product_message,
                                selectedProductCount
                            ),
                            getString(R.string.stfs_oke_label)
                        )
                    }
                } else {
                    showErrorToaster(result.errorMessage)
                }
                loadCampaignDetailData()
            }
        }
    }

    private fun observeCampaignRegistrationResult() {
        viewModel.flashSaleRegistrationResult.observe(viewLifecycleOwner) { result ->
            doOnDelayFinished(DELAY) {
                binding?.btnRegister?.isLoading = false
                if (result.isSuccess) {
                    navigateToChooseProductPage()
                } else {
                    showErrorToaster(result.errorMessage)
                }
            }
        }
    }

    private fun observeSubmittedProductVariant() {
        viewModel.submittedProductVariant.observe(viewLifecycleOwner) {
            checkProductBottomSheet.show(it, childFragmentManager, "")
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleEffect(effect: CampaignDetailViewModel.UiEffect) {
        when (effect) {
            CampaignDetailViewModel.UiEffect.ShowIneligibleAccessWarning -> {
                navigateToIneligibleAccessPage()
            }
            is CampaignDetailViewModel.UiEffect.OnSseOpen -> {
                listenToExistingSse()
            }
            is CampaignDetailViewModel.UiEffect.OnProductSseSubmissionProgress -> {
                checkProductSubmissionProgressStatus(effect.flashSaleProductSubmissionSseResult)
            }
            is CampaignDetailViewModel.UiEffect.OnSuccessAcknowledgeProductSubmissionSse -> {
                totalSubmittedProduct = effect.totalSubmittedProduct.toLong()
                showProductSubmissionResultToaster(flashSaleName)
            }
        }
    }

    private fun checkProductSubmissionProgressStatus(
        flashSaleProductSubmissionSseResult: FlashSaleProductSubmissionSseResult
    ) {
        val currentProcessedProduct = flashSaleProductSubmissionSseResult.countProcessedProduct
        val totalProduct = flashSaleProductSubmissionSseResult.countAllProduct
        when (flashSaleProductSubmissionSseResult.status) {
            FlashSaleProductSubmissionSseResult.Status.IN_PROGRESS -> {
                showProductSubmissionSseProgressDialog()
            }
            FlashSaleProductSubmissionSseResult.Status.PARTIAL_SUCCESS -> {
                hideProductSubmissionSseProgressDialog()
                showDialogProductSubmissionSsePartialSuccess()
            }
            FlashSaleProductSubmissionSseResult.Status.COMPLETE -> {
                hideProductSubmissionSseProgressDialog()
                acknowledgeProductSubmissionSse(
                    flashSaleProductSubmissionSseResult.campaignId,
                    flashSaleProductSubmissionSseResult.countProcessedProduct
                )
            }
            else -> {}
        }
        updateProductSubmissionProgressDialog(currentProcessedProduct, totalProduct)
    }

    private fun showProductSubmissionSseProgressDialog() {
        sseProgressDialog?.show()
    }

    private fun hideProductSubmissionSseProgressDialog() {
        sseProgressDialog?.hide()
    }

    private fun acknowledgeProductSubmissionSse(campaignId: String, totalSubmittedProduct: Int) {
        viewModel.acknowledgeProductSubmissionSse(campaignId, totalSubmittedProduct)
    }

    private fun updateProductSubmissionProgressDialog(
        currentProcessedProduct: Int,
        totalProduct: Int
    ) {
        sseProgressDialog?.updateData(currentProcessedProduct, totalProduct)
    }

    private fun showDialogProductSubmissionSsePartialSuccess() {
        context?.let {
            val productSseSubmissionErrorDialog = FlashSaleProductSseSubmissionDialog(it)
            productSseSubmissionErrorDialog.show(getString(R.string.stfs_dialog_error_product_submission_sse_title_partial_success)) {
                openFlashSaleProductListSseSubmissionErrorBottomSheet()
            }
        }
    }

    private fun openFlashSaleProductListSseSubmissionErrorBottomSheet() {
        val bottomSheet = FlashSaleProductListSseSubmissionErrorBottomSheet.createInstance(
            flashSaleId.toString()
        )
        bottomSheet.show(childFragmentManager)
    }

    private fun listenToExistingSse() {
        viewModel.listenToOpenedSse(flashSaleId.toString())
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
        flashSaleName = flashSale.name
        showProductSubmissionResultToaster(flashSaleName)
        setupHeader(flashSale)
        when (flashSale.tabName) {
            FlashSaleListPageTab.UPCOMING -> setupUpcoming(flashSale)
            FlashSaleListPageTab.REGISTERED -> {
                setupPaging()
                loadSubmittedProductListData(Int.ZERO)
                setupRegistered(flashSale)
            }
            FlashSaleListPageTab.ONGOING -> {
                setupPaging()
                loadSubmittedProductListData(Int.ZERO)
                setupOngoing(flashSale)
            }
            FlashSaleListPageTab.FINISHED -> {
                setupPaging()
                loadSubmittedProductListData(Int.ZERO)
                setupFinished(flashSale)
            }
        }
    }

    private fun setupProductSubmissionCount() {
        totalSubmittedProduct =
            arguments?.getLong(BundleConstant.BUNDLE_KEY_TOTAL_SUBMITTED_PRODUCT).orZero()
    }

    private fun showProductSubmissionResultToaster(flashSaleName: String) {
        if (totalSubmittedProduct.isMoreThanZero()) {
            doOnDelayFinished(DELAY) {
                binding?.run {
                    cardBottomButtonGroup.showToaster(
                        getString(
                            R.string.stfs_manage_product_list_success_product_submitted,
                            totalSubmittedProduct,
                            flashSaleName
                        ),
                        getString(R.string.stfs_oke_label)
                    )
                }
                totalSubmittedProduct = Int.ZERO.toLong()
            }
        }
    }

    private fun getFlashSaleSubmissionProgress(flashSaleId: Long) {
        viewModel.getFlashSaleSubmissionProgress(flashSaleId.toString())
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
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        setupUpcomingHeaderData(flashSale, campaignStatus)
    }

    private fun setupUpcomingMid(flashSale: FlashSale, campaignStatus: UpcomingCampaignStatus) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        upcomingCdpMidBinding?.run {
            setupUpcomingMidData(flashSale, campaignStatus)
            val startSubmissionDate = flashSale.submissionStartDateUnix.formatTo(
                DATE_MONTH_ONLY
            )
            val endSubmissionDate = flashSale.submissionEndDateUnix.formatTo(DATE_YEAR_PRECISION)
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
                viewModel.sendSeeCriteriaClickEvent(flashSaleId)
            }
            btnCheckReason.setOnClickListener {
                navigateToChooseProductPage()
                viewModel.sendCheckReasonClickEvent(flashSaleId)
            }
            if (!isCoachMarkShown()) {
                showCriteriaCoachMark(this)
            }
        }
    }

    private fun setupUpcomingBody(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_body
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
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
            timer.gone()
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
        binding.timer.targetDate = targetDate.toCalendar()
        binding.timer.timerVariant = TimerUnifySingle.VARIANT_GENERAL
        binding.timer.onFinish = onTimerFinished
    }

    private fun setupUpcomingButton() {
        binding?.run {
            cardProductEligible.visible()
            btnRegister.apply {
                visible()
                text = getString(R.string.label_register)
                setOnClickListener {
                    registerToCampaign()
                    viewModel.sendRegisterClickEvent(flashSaleId)
                }
            }
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
    }

    private fun setupRegisteredHeader(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        setupRegisteredHeaderData(flashSale)
    }

    private fun setupRegisteredMid(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_registered_mid
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        setupRegisteredMidData(flashSale)
    }

    private fun setupRegisteredBody() {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_body
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
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
            navigateToChooseProductPage()
            viewModel.sendAddProductClickEvent(flashSaleId)
        }
    }

    private fun navigateToChooseProductPage() {
        ChooseProductActivity.start(
            context ?: return,
            flashSaleId,
            viewModel.getTabName()
        )
    }

    private fun navigateToIneligibleAccessPage() {
        activity?.finish()
        IneligibleAccessActivity.start(context ?: return)
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
                        DATE_YEAR_PRECISION
                    ),
                    flashSale.reviewEndDateUnix.formatTo(
                        TIME_MINUTE_PRECISION_WITH_TIMEZONE
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
                        DATE_YEAR_PRECISION
                    ),
                    flashSale.startDateUnix.formatTo(
                        TIME_MINUTE_PRECISION_WITH_TIMEZONE
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
            tpgRejectedProductValue.text = if (flashSale.productMeta.rejectedProduct.isZero()) {
                MethodChecker.fromHtml(getString(R.string.stfs_dash_label))
            } else {
                MethodChecker.fromHtml(
                    getString(
                        R.string.stfs_mid_section_product_count_placeholder,
                        flashSale.productMeta.rejectedProduct
                    )
                )
            }
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
        binding.timerRegistered.targetDate = targetDate.toCalendar()
        binding.timerRegistered.timerVariant = TimerUnifySingle.VARIANT_GENERAL
    }

    private fun onShowOrHideItemCheckBox() {
        val oldItems = productAdapter.getItems().filterIsInstance<WaitingForSelectionItem>()
        val newItems =
            oldItems.map { it.copy(isCheckBoxShown = !it.isCheckBoxShown, isSelected = false) }
        val isShown = newItems[Int.ZERO].isCheckBoxShown
        productAdapter.submit(newItems)
        viewModel.setCheckBoxStateStatus(isShown)

        cdpBodyBinding?.run {
            if (isShown) {
                tpgSelectedProductCount.visible()
                tpgProductCount.invisible()
                btnSelectAllProduct.text = getString(R.string.fs_canceled_lable)
                viewModel.sendBulkChooseClickEvent(flashSaleId)
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
            btnDelete.apply {
                text = getString(R.string.stfs_label_delete)
                setOnClickListener {
                    showDeleteDialog()
                    viewModel.sendDeleteClickEvent(flashSaleId)
                }
            }
            btnEdit.apply {
                text = getString(R.string.stfs_label_edit)
                setOnClickListener {
                    reserveProduct()
                    viewModel.sendEditClickEvent(flashSaleId)
                }
            }
            if (!isShown) {
                btnRegister.visible()
                btnDelete.gone()
                btnEdit.gone()
            } else {
                btnRegister.gone()
            }
        }
    }

    private fun onCheckBoxClicked(itemPosition: Int, isCheckBoxChecked: Boolean) {
        val selectedProduct = productAdapter.getItems().filterIsInstance<WaitingForSelectionItem>()
        val selectedProductId = selectedProduct[itemPosition].productId
        val selectedProductCriteriaId = selectedProduct[itemPosition].productCriteria.criteriaId
        val param = Pair(selectedProductId, selectedProductCriteriaId)

        if (isCheckBoxChecked) {
            viewModel.setSelectedItem(param)
        } else {
            viewModel.removeSelectedItem(param)
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
    }

    private fun setupOngoingHeader(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        setupOngoingHeaderData(flashSale)
    }

    private fun setupOngoingMid(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_ongoing_mid
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
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
            tpgRejectedProductValue.text = if (flashSale.productMeta.rejectedProduct.isZero()) {
                MethodChecker.fromHtml(getString(R.string.stfs_dash_label))
            } else {
                MethodChecker.fromHtml(
                    getString(
                        R.string.stfs_mid_section_product_count_placeholder,
                        flashSale.productMeta.rejectedProduct
                    )
                )
            }
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
    }

    private fun setupFinishedHeader(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_header
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        setupFinishedHeaderData(flashSale)
    }

    private fun setupFinishedMid(flashSale: FlashSale) {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_ongoing_mid
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
        setupFinishedMidData(flashSale)
    }

    private fun setupFinishedBody() {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_body
        if (inflatedView.parent != null) {
            inflatedView.inflate()
        }
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
            tpgAcceptedProductValue.text =
                if (flashSale.productMeta.acceptedProduct.isMoreThanZero()) {
                    MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.acceptedProduct
                        )
                    )
                } else {
                    getString(R.string.stfs_dash_label)
                }
            tpgRejectedProductValue.text =
                if (flashSale.productMeta.rejectedProduct.isZero()) {
                    getString(R.string.stfs_dash_label)
                } else {
                    MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.rejectedProduct
                        )
                    )
                }
            tpgSoldValue.text =
                if (flashSale.productMeta.totalStockSold.isMoreThanZero()) {
                    MethodChecker.fromHtml(
                        getString(
                            R.string.stfs_mid_section_product_count_placeholder,
                            flashSale.productMeta.totalStockSold
                        )
                    )
                } else {
                    getString(R.string.stfs_dash_label)
                }
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
            imageCardFlashSalePerformance.loadImage(IMAGE_PRODUCT_INELIGIBLE_URL)
        }
    }

    /**
     * Reusable Method
     */

    private fun registerToCampaign() {
        binding?.btnRegister?.isLoading = true
        viewModel.register(flashSaleId)
    }

    private fun loadCampaignDetailData() {
        showLoading()
        viewModel.getCampaignDetail(flashSaleId)
    }

    private fun loadSubmittedProductListData(offset: Int) {
        viewModel.getSubmittedProduct(flashSaleId, offset)
    }

    private fun reserveProduct() {
        binding?.btnEdit?.isLoading = true
        viewModel.reserveProduct(flashSaleId)
    }

    private fun showDeleteDialog() {
        val context = context ?: return
        val selectedProductCount = viewModel.selectedProducts.value?.count()
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.run {
            setTitle(getString(R.string.stfs_delete_dialog_title, selectedProductCount))
            setDescription(getString(R.string.stfs_delete_dialog_description))
            setPrimaryCTAText(getString(R.string.stfs_delete_dialog_primary_cta_text))
            setSecondaryCTAText(getString(R.string.stfs_delete_dialog_secondary_cta_text))
            setPrimaryCTAClickListener {
                dismiss()
                showLoading()
                deleteProduct()
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
            show()
        }
    }

    private fun deleteProduct() {
        viewModel.setDeleteStateStatus(true)
        viewModel.deleteProduct(flashSaleId)
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

            tpgSelectedProductCount.invisible()
            tpgProductCount.visible()
            btnSelectAllProduct.text = getString(R.string.stfs_choose_all_product_label)
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

        binding?.nsvContent?.enablePaging {
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
        doOnDelayFinished(DELAY) {
            binding?.run {
                loader.gone()
                llContent.show()
                globalError.gone()
            }
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
                    activity?.finish()
                }
            }
        }
    }

    private fun showErrorToaster(msg: String = getString(R.string.stfs_global_error_message)) {
        binding?.run {
            cardBottomButtonGroup.showToasterError(msg)
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
        val selectedProductId = selectedProduct.id() as? Long
        val isVariantProduct = ProductCheckingResultMapper.isVariantProduct(selectedProduct)
        val isMultiloc = ProductCheckingResultMapper.isMultiloc(selectedProduct)
        val productName = ProductCheckingResultMapper.getProductName(selectedProduct)
        val displayProductSold =
            ProductCheckingResultMapper.getProductSold(selectedProduct).isNotEmpty()
        val imageUrl = ProductCheckingResultMapper.getImageUrl(selectedProduct)

        checkProductBottomSheet.setProductName(productName)

        if (isVariantProduct) {
            viewModel.getSubmittedProductVariant(
                flashSaleId,
                selectedProductId.orZero(),
                displayProductSold,
                imageUrl
            )
        } else if (isMultiloc) {
            val productCheckingResult = ProductCheckingResultMapper.mapFromAdapterItem(
                selectedProduct,
                displayProductSold,
                viewModel.getTabName()
            )
            checkProductBottomSheet.show(listOf(productCheckingResult), childFragmentManager, "")
        }
    }

    private fun showCriteriaCoachMark(binding: StfsCdpUpcomingMidBinding) {
        doOnDelayFinished(DELAY) {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMarkItem.add(
                CoachMark2Item(
                    binding.btnSeeCriteria,
                    getString(R.string.stfs_campaign_detail_upcoming_coachmark_title),
                    getString(R.string.stfs_campaign_detail_upcoming_coachmark_description)
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            coachMark?.onDismissListener = { setCoachMarkAlreadyShown() }
        }
    }

    private fun isCoachMarkShown(): Boolean {
        return viewModel.isCoachMarkShown()
    }

    private fun setCoachMarkAlreadyShown() {
        viewModel.setSharedPrefCoachMarkAlreadyShown()
    }

    private fun isOpenedFromApplink(appLinkData: Uri?): Boolean {
        return appLinkData?.lastPathSegment?.isNotEmpty() == true && appLinkData.pathSegments.size >= APPLINK_SEGMENTS_SIZE
    }

    private fun getFLashSaleIdFromApplink(appLinkData: Uri?): Long {
        return try {
            appLinkData?.lastPathSegment?.toLong().orZero()
        } catch (e: NumberFormatException) {
            DEFAULT_FOR_EMPTY_FLASH_SALE_ID
        }
    }

    private fun getFlashSaleIdFromBundle(): Long {
        return try {
            arguments?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
        } catch (e: NumberFormatException) {
            DEFAULT_FOR_EMPTY_FLASH_SALE_ID
        }
    }
}
