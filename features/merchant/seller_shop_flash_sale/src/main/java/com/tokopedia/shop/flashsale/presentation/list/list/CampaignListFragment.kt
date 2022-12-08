package com.tokopedia.shop.flashsale.presentation.list.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListBinding
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.constant.Constant.FIRST_PAGE
import com.tokopedia.shop.flashsale.common.constant.Constant.ZERO
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.showLoading
import com.tokopedia.shop.flashsale.common.extension.showToaster
import com.tokopedia.shop.flashsale.common.extension.slideDown
import com.tokopedia.shop.flashsale.common.extension.slideUp
import com.tokopedia.shop.flashsale.common.extension.stopLoading
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignCreationEligibility
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.shop.flashsale.presentation.cancelation.CancelCampaignBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity.Companion.REQUEST_CODE_CREATE_CAMPAIGN_INFO
import com.tokopedia.shop.flashsale.presentation.detail.CampaignDetailActivity
import com.tokopedia.shop.flashsale.presentation.draft.bottomsheet.DraftListBottomSheet
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerFragment
import com.tokopedia.shop.flashsale.presentation.list.list.adapter.CampaignAdapter
import com.tokopedia.shop.flashsale.presentation.list.list.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.shop.flashsale.presentation.list.list.dialog.ShopDecorationDialog
import com.tokopedia.shop.flashsale.presentation.list.list.listener.RecyclerViewScrollListener
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.QuotaMonitoringActivity
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignListFragment : BaseSimpleListFragment<CampaignAdapter, CampaignUiModel>(),
    CampaignListContainerFragment.ActiveCampaignListListener {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_ID = "status_id"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "product_count"
        private const val PAGE_SIZE = 10
        private const val MAX_DRAFT_COUNT = 3
        private const val TAB_POSITION_FIRST = 0
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 100
        private const val REFRESH_CAMPAIGN_DELAY_DURATION_IN_MILLIS: Long = 3_000
        private const val SHOP_DECORATION_ARTICLE_URL = "https://seller.tokopedia.com/dekorasi-toko"
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_no_active_campaign.png"
        private const val DRAFT_SERVER_SAVING_DURATION = 1000L
        private const val VPS_PACKAGE_ID_NOT_SELECTED: Long = 0

        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            campaignStatusIds: IntArray,
            totalCampaign: Int
        ): CampaignListFragment {
            val fragment = CampaignListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BundleConstant.BUNDLE_KEY_TARGET_TAB_POSITION, tabPosition)
                putIntArray(BUNDLE_KEY_CAMPAIGN_STATUS_ID, campaignStatusIds)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, totalCampaign)
            }
            return fragment
        }

    }

    private val tabPosition by lazy {
        arguments?.getInt(BundleConstant.BUNDLE_KEY_TARGET_TAB_POSITION).orZero()
    }

    private val campaignStatusIds by lazy {
        arguments?.getIntArray(BUNDLE_KEY_CAMPAIGN_STATUS_ID)
    }

    private val totalCampaign by lazy {
        arguments?.getInt(BUNDLE_KEY_CAMPAIGN_COUNT).orZero()
    }

    private val loaderDialog by lazy { LoaderDialog(requireActivity()) }

    private val campaignAdapter by lazy {
        CampaignAdapter(
            onCampaignClicked,
            onOverflowMenuClicked
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    @Inject
    lateinit var shareComponentInstanceBuilder: ShareComponentInstanceBuilder

    private var binding by autoClearedNullable<SsfsFragmentCampaignListBinding>()
    private var isFirstLoad = true
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListViewModel::class.java) }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}
    private var onCancelCampaignSuccess: () -> Unit = {}
    private var onNavigateToActiveCampaignTab: () -> Unit = {}
    private var shareComponentBottomSheet: UniversalShareBottomSheet? = null

    override fun getScreenName(): String = CampaignListFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (parentFragment as? CampaignListContainerFragment)?.setActiveCampaignListListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setFragmentToUnifyBgColor()
        observeVpsPackages()
        observeCampaigns()
        observeCampaignPrerequisiteData()
        observeShareComponentMetadata()
        observeShareComponentThumbnailImage()
        observeCampaignCreationEligibility()
        observeShopDecorStatus()
        observerTimeToFlip()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCampaignPrerequisiteData()
        viewModel.getVpsPackages()
    }


    private fun setupView() {
        setupClickListener()
        setupSearchBar()
        setupScrollListener()
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearAllData()
                    binding?.groupNoSearchResult?.gone()
                    binding?.loader?.visible()
                    getCampaigns(FIRST_PAGE)
                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }
            searchBar.clearListener = { refreshCampaigns() }
        }
    }

    private fun setupClickListener() {
        binding?.run {

            btnCreateCampaign.setOnClickListener {
                viewModel.getShopDecorStatus()
                binding?.btnCreateCampaign.showLoading()
            }

            btnDraft.setOnClickListener {
                showDraftListBottomSheet(viewModel.getCampaignDrafts())
            }

            btnCreateCampaignEmptyState.setOnClickListener {
                viewModel.getShopDecorStatus()
                binding?.btnCreateCampaignEmptyState.showLoading()
            }

            btnNavigateToFirstActiveCampaign.setOnClickListener {
                onNavigateToActiveCampaignTab()
            }
        }
    }

    private fun setupScrollListener() {
        binding?.run {
            recyclerView.addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = {
                        doOnDelayFinished(SCROLL_DISTANCE_DELAY_IN_MILLIS) {
                            onScrollDown()
                            handleScrollDownEvent()
                        }
                    },
                    onScrollUp = {
                        doOnDelayFinished(SCROLL_DISTANCE_DELAY_IN_MILLIS) {
                            onScrollUp()
                            handleScrollUpEvent()
                        }
                    }
                )
            )
        }
    }

    private fun refreshCampaigns() {
        clearAllData()
        binding?.loader?.visible()
        viewModel.getCampaigns(
            PAGE_SIZE,
            FIRST_PAGE,
            campaignStatusIds?.toList().orEmpty(),
            ""
        )
    }

    private fun observeCampaignPrerequisiteData() {
        viewModel.campaignPrerequisiteData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.loader?.gone()
                    viewModel.setCampaignDrafts(result.data.drafts)
                    handleCampaignPrerequisiteData(result.data.drafts.size)
                }
                is Fail -> {
                    binding?.loader?.gone()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeVpsPackages() {
        viewModel.vpsPackages.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    displayVpsPackages(result.data)
                }
                is Fail -> {
                    binding?.run {
                        cardQuotaMonitoring.gone()
                    }
                }
            }
        }
    }

    private fun observeCampaigns() {
        viewModel.campaigns.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    isFirstLoad = false
                    displayCampaigns(result.data)
                    binding?.loader?.gone()
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                    binding?.searchBar?.gone()
                    binding?.loader?.gone()
                }
            }
        }
    }


    private fun observeShareComponentThumbnailImage() {
        viewModel.shareComponentThumbnailImageUrl.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    viewModel.setThumbnailImageUrl(result.data)
                    viewModel.getShareComponentMetadata(viewModel.getSelectedCampaignId())
                }
                is Fail -> {
                    dismissLoaderDialog()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeShareComponentMetadata() {
        viewModel.shareComponentMetadata.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    dismissLoaderDialog()
                    val metadata = result.data
                    displayShareBottomSheet(viewModel.getThumbnailImageUrl(), metadata)
                }
                is Fail -> {
                    dismissLoaderDialog()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeCampaignCreationEligibility() {
        viewModel.creationEligibility.observe(viewLifecycleOwner) { result ->
            binding?.btnCreateCampaignEmptyState.stopLoading()
            binding?.btnCreateCampaign.stopLoading()

            when (result) {
                is Success -> {
                    handleEligibilityResult(result.data)
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeShopDecorStatus() {
        viewModel.shopDecorStatus.observe(viewLifecycleOwner) { result ->
            binding?.btnCreateCampaignEmptyState.stopLoading()
            binding?.btnCreateCampaign.stopLoading()

            when (result) {
                is Success -> {
                    val decorStatus = result.data
                    handleShopDecorStatusResult(decorStatus)
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observerTimeToFlip(){
        viewModel.timeToFlip.observe(viewLifecycleOwner){ result ->
            if(result%2 == 0){
                binding?.warningOfQuota?.transitionToEnd()
            }else {
                binding?.warningOfQuota?.transitionToStart()
            }
        }
    }

    private fun handleShopDecorStatusResult(decorStatus: String) {
        val hasDraft = viewModel.getCampaignDrafts().isNotEmpty()
        val hasCampaign = adapter?.itemCount.isMoreThanZero()
        val hasCampaignOrDraft = hasDraft || hasCampaign

        if (decorStatus == "native" && hasCampaignOrDraft) {
            binding?.btnCreateCampaignEmptyState.showLoading()
            binding?.btnCreateCampaign.showLoading()
            viewModel.validateCampaignCreationEligibility(VPS_PACKAGE_ID_NOT_SELECTED)
        } else {
            showShopDecorationDialog()
        }
    }


    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
    }

    fun setOnNavigateToActiveCampaignListener(onNavigateToActiveCampaignTab: () -> Unit) {
        this.onNavigateToActiveCampaignTab = onNavigateToActiveCampaignTab
    }

    private val onCampaignClicked: (CampaignUiModel, Int) -> Unit = { campaign, _ ->
        viewModel.setSelectedCampaignId(campaign.campaignId)
        handleViewCampaignDetail(campaign)
    }

    private val onOverflowMenuClicked: (CampaignUiModel) -> Unit = { campaign ->
        viewModel.setSelectedCampaignId(campaign.campaignId)
        displayMoreMenuBottomSheet(campaign)
    }

    override fun createAdapter(): CampaignAdapter {
        return campaignAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.recyclerView
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<CampaignUiModel>) {
        adapter?.submit(list)
    }

    override fun loadData(page: Int) {
        if (totalCampaign.isMoreThanZero()) {
            getCampaigns(page)
        }
    }

    private fun getCampaigns(page: Int) {
        val offset = if (isFirstLoad || page == FIRST_PAGE) {
            FIRST_PAGE
        } else {
            page * PAGE_SIZE
        }

        val searchKeyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()

        viewModel.getCampaigns(
            PAGE_SIZE,
            offset,
            campaignStatusIds?.toList().orEmpty(),
            searchKeyword
        )
    }

    override fun clearAdapterData() {
        adapter?.clearAll()
    }

    override fun onShowLoading() {
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
        adapter?.hideLoading()
    }

    override fun onGetListError(message: String) {
        adapter?.hideLoading()
    }

    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {
        // no-op
    }

    private fun handleScrollDownEvent() {
        binding?.searchBar.slideDown()
        binding?.cardView.slideDown()
    }

    private fun handleScrollUpEvent() {
        binding?.searchBar.slideUp()
        binding?.cardView.slideUp()
    }

    private fun displayVpsPackages(packages: List<VpsPackage>) {
        val packageAvailability = viewModel.getPackageAvailability(packages)
        val totalQuota = packageAvailability.totalQuota
        val totalRemainingQuota = packageAvailability.remainingQuota
        val totalQuotaSource = packages.count()
        val isNearExpirePackageAvailable = packageAvailability.isNearExpirePackageAvailable
        val packageNearExpireCount = packageAvailability.packageNearExpire

        binding?.run {
            if (isNearExpirePackageAvailable) {
                tgExpireValue.visible()
                startAnimationOfWarningQuota()
            } else {
                tgExpireValue.gone()
                stopAnimationOfWarningQuota()
            }
            tgQuotaValue.run {
                text = if (totalRemainingQuota.isMoreThanZero()) {
                    MethodChecker.fromHtml(
                        getString(
                            R.string.sfs_quota_monitoring_value_placeholder_normal_state,
                            totalRemainingQuota,
                            totalQuota
                        )
                    )
                } else {
                    MethodChecker.fromHtml(
                        getString(
                            R.string.sfs_quota_monitoring_value_placeholder_empty_state,
                            totalRemainingQuota,
                            totalQuota
                        )
                    )
                }
            }
            tgExpireValue.run {
                text = MethodChecker.fromHtml(
                    getString(
                        R.string.ssfs_expire_soon_value_placeholder,
                        packageNearExpireCount
                    )
                )
            }
            tgQuotaSourceValue.text = MethodChecker.fromHtml(
                getString(
                    R.string.sfs_quota_source_monitoring_value_placeholder,
                    totalQuotaSource
                )
            )
            cardQuotaMonitoring.apply {
                visible()
                setOnClickListener {
                    routeToYourShopFlashSaleQuotaPage()
                }
            }
        }
    }

    private fun startAnimationOfWarningQuota(){
        val isTimerForFlipRunning = viewModel.isTimerForFlipRunning()
        if (!isTimerForFlipRunning) {
            viewModel.startAnimationOfWarningQuota()
        }
    }

    private fun stopAnimationOfWarningQuota(){
        val isTimerForFlipRunning = viewModel.isTimerForFlipRunning()
        if (isTimerForFlipRunning){
            viewModel.stopAnimationOfWarningQuota()
        }
    }

    private fun displayCampaigns(data: CampaignMeta) {
        if (data.campaigns.size.isMoreThanZero()) {
            binding?.groupNoSearchResult?.gone()
            renderList(data.campaigns, data.campaigns.size == getPerPage())
        } else if (data.campaigns.isEmpty() && adapter?.itemCount == ZERO) {
            binding?.groupNoSearchResult?.visible()
        }

        adapter?.hideLoading()
    }

    private fun handleCampaignPrerequisiteData(draftCount: Int) {
        val hasDraft = draftCount.isMoreThanZero()
        val hasCampaign = totalCampaign.isMoreThanZero()
        val draftWording = String.format(getString(R.string.sfs_placeholder_draft), draftCount)

        binding?.btnDraft?.text = draftWording
        binding?.btnDraft?.isVisible = hasDraft
        binding?.cardView?.isVisible = hasDraft || hasCampaign

        if (tabPosition == TAB_POSITION_FIRST) {
            handleFirstTabEmptyState(hasCampaign, hasDraft)
        } else {
            handleSecondTabEmptyState(hasCampaign)
        }

    }

    private fun handleFirstTabEmptyState(hasCampaign: Boolean, hasDraft: Boolean) {
        binding?.btnNavigateToFirstActiveCampaign?.gone()

        if (hasCampaign || hasDraft) {
            binding?.btnCreateCampaignEmptyState?.gone()
        } else {
            binding?.btnCreateCampaignEmptyState?.visible()
        }

        binding?.searchBar?.isVisible = hasCampaign
        binding?.recyclerView?.isVisible = hasCampaign

        binding?.emptyState?.isVisible = !hasCampaign
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(getString(R.string.sfs_no_active_campaign_title))
        binding?.emptyState?.setDescription(getString(R.string.sfs_no_active_campaign_description))

        binding?.groupNoSearchResult?.gone()
    }

    private fun handleSecondTabEmptyState(hasCampaign: Boolean) {
        binding?.btnCreateCampaignEmptyState?.gone()

        if (hasCampaign) {
            binding?.btnNavigateToFirstActiveCampaign?.gone()
        } else {
            binding?.btnNavigateToFirstActiveCampaign?.visible()
        }

        binding?.searchBar?.isVisible = hasCampaign
        binding?.recyclerView?.isVisible = hasCampaign

        binding?.emptyState?.isVisible = !hasCampaign
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(getString(R.string.sfs_no_campaign_history_title))
        binding?.emptyState?.setDescription(getString(R.string.sfs_no_campaign_history_description))

        binding?.groupNoSearchResult?.gone()
    }

    private fun handleEligibilityResult(data: CampaignCreationEligibility) {
        val drafts = viewModel.getCampaignDrafts()

        if (!data.isEligible) {
            showCreateCampaignNotAllowedError(activity ?: return)
            return
        }

        if (drafts.size >= MAX_DRAFT_COUNT) {
            showDraftListBottomSheet(drafts)
            return
        }

        launchCampaignInformationPage()
    }

    private fun displayMoreMenuBottomSheet(campaign: CampaignUiModel) {
        val bottomSheet = MoreMenuBottomSheet.newInstance(campaign.campaignName, campaign.status)
        bottomSheet.setOnViewCampaignMenuSelected {
            viewModel.onMoreMenuViewCampaignDetailClicked()
            handleViewCampaignDetail(campaign)
        }
        bottomSheet.setOnCancelCampaignMenuSelected { handleCancelCampaign(campaign) }
        bottomSheet.setOnStopCampaignMenuSelected { handleStopCampaign(campaign) }
        bottomSheet.setOnShareCampaignMenuSelected { handleShareCampaign(campaign) }
        bottomSheet.setOnEditCampaignMenuSelected { handleEditCampaign(campaign) }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun handleViewCampaignDetail(campaign: CampaignUiModel) {
        val intent = CampaignDetailActivity.buildIntent(
            activity ?: return,
            campaign.campaignId,
            campaign.campaignName
        )
        startActivityForResult(intent, CampaignDetailActivity.REQUEST_CODE_CAMPAIGN_DETAIL)
    }

    private fun displayShareBottomSheet(
        thumbnailImageUrl: String,
        metadata: ShareComponentMetadata,
    ) {
        val param = ShareComponentInstanceBuilder.Param(
            metadata.banner.shop.name,
            metadata.banner.shop.logo,
            metadata.shop.isPowerMerchant,
            metadata.shop.isOfficial,
            metadata.banner.shop.domain,
            metadata.banner.campaignStatusId,
            metadata.banner.campaignId,
            metadata.banner.startDate,
            metadata.banner.endDate,
            metadata.banner.products.size,
            metadata.banner.products,
            metadata.banner.maxDiscountPercentage
        )

        shareComponentBottomSheet = shareComponentInstanceBuilder.build(
            thumbnailImageUrl,
            param,
            onShareOptionClick = ::handleShareOptionClick,
            onCloseOptionClicked = {}
        )
        shareComponentBottomSheet?.show(childFragmentManager, shareComponentBottomSheet?.tag)
    }

    private fun handleShareOptionClick(
        shareModel: ShareModel,
        linkerShareResult: LinkerShareResult,
        outgoingText: String
    ) {
        SharingUtil.executeShareIntent(
            shareModel,
            linkerShareResult,
            activity ?: return,
            view ?: return,
            outgoingText
        )
        shareComponentBottomSheet?.dismiss()
    }

    private fun onDeleteDraftSuccess() {
        binding?.cardView showToaster getString(R.string.sfs_draft_deleted)

        //Add some spare time caused by Backend write operation delay
        doOnDelayFinished(DRAFT_SERVER_SAVING_DURATION) {
            viewModel.getCampaignPrerequisiteData()
        }
    }

    private fun onDraftClicked(draft: DraftItemModel) {
        val activeVpsPackageCount = viewModel.findActiveVpsPackagesCount()
        if (activeVpsPackageCount == 0) {
            showUpdateDraftNotAllowedError(activity ?: return)
            return
        }

        launchCampaignInformationPageWitheEditDraftMode(draft.id)
    }

    private fun showLoaderDialog() {
        loaderDialog.setLoadingText(getString(R.string.sfs_please_wait))
        loaderDialog.show()
    }

    private fun dismissLoaderDialog() {
        loaderDialog.dialog.dismiss()
    }

    private fun handleCancelCampaign(campaign: CampaignUiModel) {
        viewModel.onMoreMenuCancelClicked(campaign)
        cancelCampaign(campaign)
    }

    private fun cancelCampaign(campaign: CampaignUiModel) {
        if (!campaign.isCancellable) {
            val errorWording = findCancelCampaignErrorWording(campaign.status)
            binding?.cardView showError errorWording
            return
        }

        showCancelCampaignBottomSheet(campaign)
    }

    private fun handleStopCampaign(campaign: CampaignUiModel) {
        viewModel.onMoreMenuStopClicked(campaign)
        cancelCampaign(campaign)
    }

    private fun handleShareCampaign(campaign: CampaignUiModel) {
        showLoaderDialog()
        viewModel.onMoreMenuShareClicked(campaign)
    }

    private fun handleEditCampaign(campaign: CampaignUiModel) {
        viewModel.onMoreMenuEditClicked(campaign)
        if (campaign.thematicParticipation) {
            binding?.cardView showError getString(R.string.sfs_cannot_edit_campaign)
            return
        }

        launchCampaignInformationPageWithEditMode(campaign.campaignId)
    }

    private fun showCancelCampaignBottomSheet(campaign: CampaignUiModel) {
        val bottomSheet = CancelCampaignBottomSheet(
            campaign.campaignId,
            campaign.campaignName,
            campaign.status
        ) {
            showCancelCampaignSuccess(campaign)
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun showCreateCampaignNotAllowedError(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(context.getString(R.string.sfs_cannot_create_campaign_title))
        dialog.setDescription(context.getString(R.string.sfs_cannot_create_campaign_description))

        dialog.setPrimaryCTAText(context.getString(R.string.sfs_learn_pm_pro))
        dialog.setSecondaryCTAText(context.getString(R.string.sfs_understand))

        dialog.setPrimaryCTAClickListener { routeToPmSubscribePage() }
        dialog.setSecondaryCTAClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showUpdateDraftNotAllowedError(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(context.getString(R.string.sfs_cannot_update_campaign_title))
        dialog.setDescription(context.getString(R.string.sfs_cannot_create_campaign_description))

        dialog.setPrimaryCTAText(context.getString(R.string.sfs_learn_pm_pro))
        dialog.setSecondaryCTAText(context.getString(R.string.sfs_understand))

        dialog.setPrimaryCTAClickListener { routeToPmSubscribePage() }
        dialog.setSecondaryCTAClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun showDraftListBottomSheet(drafts: List<CampaignUiModel>) {
        DraftListBottomSheet.showUsingCampaignUiModel(
            childFragmentManager,
            drafts,
            ::onDeleteDraftSuccess,
            ::onDraftClicked
        )
    }

    private fun showCancelCampaignSuccess(campaign: CampaignUiModel) {
        val toasterMessage =
            String.format(findCancelCampaignSuccessWording(campaign.status), campaign.campaignName)
        showCancellationMessageThenUpdateData(toasterMessage)
    }

    private fun findCancelCampaignErrorWording(campaignStatus: CampaignStatus): String {
        return if (campaignStatus.isOngoing()) {
            getString(R.string.sfs_cannot_stop_campaign)
        } else {
            getString(R.string.sfs_cannot_cancel_campaign)
        }
    }

    private fun findCancelCampaignSuccessWording(campaignStatus: CampaignStatus): String {
        return if (campaignStatus.isOngoing()) {
            getString(R.string.sfs_campaign_stopped)
        } else {
            getString(R.string.sfs_campaign_cancelled)
        }
    }

    fun setOnCancelCampaignSuccess(onCancelCampaignSuccess: () -> Unit) {
        this.onCancelCampaignSuccess = onCancelCampaignSuccess
    }


    override fun onSaveDraftSuccess() {
        showSaveDraftSuccessMessage()
    }

    override fun onEditCampaignSuccess() {
        binding?.cardView showToaster getString(R.string.sfs_edit_campaign_success)
        refreshCampaigns()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CREATE_CAMPAIGN_INFO && resultCode == Activity.RESULT_OK) {
            showSaveDraftSuccessMessage()
        } else if (requestCode == CampaignDetailActivity.REQUEST_CODE_CAMPAIGN_DETAIL
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            handleCancellationResultFromCampaignDetail(data)
        }
    }

    private fun handleCancellationResultFromCampaignDetail(data: Intent) {
        if (data.hasExtra(CampaignDetailActivity.BUNDLE_KEY_CAMPAIGN_CANCELLATION_MESSAGE)) {
            val toasterMessage =
                data.getStringExtra(CampaignDetailActivity.BUNDLE_KEY_CAMPAIGN_CANCELLATION_MESSAGE)
                    ?: return
            showCancellationMessageThenUpdateData(toasterMessage)
        }
    }

    private fun showSaveDraftSuccessMessage() {
        binding?.cardView showToaster getString(R.string.sfs_saved_as_draft)
    }

    private fun showCancellationMessageThenUpdateData(toasterMessage: String) {
        binding?.cardView showToaster toasterMessage

        //Add some spare time caused by Backend write operation delay
        doOnDelayFinished(REFRESH_CAMPAIGN_DELAY_DURATION_IN_MILLIS) {
            binding?.loader?.visible()
            getCampaigns(FIRST_PAGE)
            onCancelCampaignSuccess()
        }
    }


    private fun showShopDecorationDialog() {
        if (!isAdded) return

        val dialog = ShopDecorationDialog()
        dialog.setOnPrimaryActionClick {
            viewModel.validateCampaignCreationEligibility(VPS_PACKAGE_ID_NOT_SELECTED)
        }
        dialog.setOnHyperlinkClick { routeToShopDecorationArticle() }
        dialog.show(activity ?: return)
    }

    private fun launchCampaignInformationPage() {
        val starter = Intent(activity, CampaignInformationActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.CREATE)
        starter.putExtras(bundle)

        startActivityForResult(starter, REQUEST_CODE_CREATE_CAMPAIGN_INFO)
    }

    private fun launchCampaignInformationPageWithEditMode(campaignId: Long) {
        val starter = Intent(activity, CampaignInformationActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.UPDATE)
        bundle.putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
        starter.putExtras(bundle)

        startActivityForResult(starter, REQUEST_CODE_CREATE_CAMPAIGN_INFO)
    }

    private fun launchCampaignInformationPageWitheEditDraftMode(campaignId: Long) {
        val starter = Intent(activity, CampaignInformationActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, PageMode.DRAFT)
        bundle.putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
        starter.putExtras(bundle)

        startActivityForResult(starter, REQUEST_CODE_CREATE_CAMPAIGN_INFO)
    }

    private fun routeToShopDecorationArticle() {
        if (!isAdded) return
        val encodedUrl = SHOP_DECORATION_ARTICLE_URL.encodeToUtf8()
        val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
        RouteManager.route(activity ?: return, route)
    }

    private fun routeToYourShopFlashSaleQuotaPage() {
        QuotaMonitoringActivity.start(activity ?: return)
    }

    private fun routeToPmSubscribePage() {
        val intent = RouteManager.getIntent(context,
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE
        )
        startActivity(intent)
    }

}

