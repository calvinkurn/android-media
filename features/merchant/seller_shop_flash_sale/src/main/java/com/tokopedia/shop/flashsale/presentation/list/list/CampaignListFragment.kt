package com.tokopedia.shop.flashsale.presentation.list.list

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
import com.tokopedia.applink.ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListBinding
import com.tokopedia.shop.flashsale.common.constant.Constant.EMPTY_STRING
import com.tokopedia.shop.flashsale.common.constant.Constant.FIRST_PAGE
import com.tokopedia.shop.flashsale.common.constant.Constant.ZERO
import com.tokopedia.shop.flashsale.common.constant.ShopInfoConstant.OFFICIAL_STORE_ID
import com.tokopedia.shop.flashsale.common.constant.ShopInfoConstant.POWER_MERCHANT_PRO_ID
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.ShopInfo
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerFragment
import com.tokopedia.shop.flashsale.presentation.list.dialog.showNoCampaignQuotaDialog
import com.tokopedia.shop.flashsale.presentation.list.list.adapter.CampaignAdapter
import com.tokopedia.shop.flashsale.presentation.list.list.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.shop.flashsale.presentation.list.list.listener.RecyclerViewScrollListener
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.cancelation.CancelCampaignBottomSheet
import com.tokopedia.shop.flashsale.presentation.draft.bottomsheet.DraftListBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CampaignListFragment : BaseSimpleListFragment<CampaignAdapter, CampaignUiModel>() {

    companion object {
        private const val BUNDLE_KEY_TAB_POSITION = "tab_position"
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_ID = "status_id"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "product_count"
        private const val PAGE_SIZE = 10
        private const val MAX_DRAFT_COUNT = 3
        private const val TAB_POSITION_FIRST = 0
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 300
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_no_active_campaign.png"
        private const val DRAFT_SERVER_SAVING_DURATION = 1000L

        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            campaignStatusIds: IntArray,
            totalCampaign: Int
        ): CampaignListFragment {
            val fragment = CampaignListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TAB_POSITION, tabPosition)
                putIntArray(BUNDLE_KEY_CAMPAIGN_STATUS_ID, campaignStatusIds)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, totalCampaign)
            }
            return fragment
        }

    }

    private val tabPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_TAB_POSITION).orZero()
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
    private var onNavigateToActiveCampaignTab: () -> Unit = {}
    private var shareComponentBottomSheet: UniversalShareBottomSheet? = null

    override fun getScreenName(): String = CampaignListFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
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
        observeCampaigns()
        observeCampaignPrerequisiteData()
        observeShareComponentMetadata()
        observeSellerEligibility()
        viewModel.getCampaignPrerequisiteData()
    }


    private fun setupView() {
        binding?.btnCreateCampaign?.setOnClickListener {
            binding?.btnCreateCampaign?.isLoading = true
            binding?.btnCreateCampaign?.loadingText = getString(R.string.sfs_please_wait)
            viewModel.getSellerEligibility()
        }

        binding?.btnDraft?.setOnClickListener {
            DraftListBottomSheet.showUsingCampaignUiModel(
                childFragmentManager,
                viewModel.getCampaignDrafts(), ::onDeleteDraftSuccess
            )
        }
        setupSearchBar()
        setupScrollListener()
        setupTabChangeListener()
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
            searchBar.clearListener = { clearSearchBar() }
        }
    }

    private fun setupScrollListener() {
        binding?.run {
            recyclerView.addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = {
                        doOnDelayFinished {
                            onScrollDown()
                            handleScrollDownEvent()
                        }
                    },
                    onScrollUp = {
                        doOnDelayFinished {
                            onScrollUp()
                            handleScrollUpEvent()
                        }
                    }
                )
            )
        }
    }

    private fun clearSearchBar() {
        clearAllData()
        binding?.loader?.visible()
        viewModel.getCampaigns(
            PAGE_SIZE,
            FIRST_PAGE,
            campaignStatusIds?.toList().orEmpty(),
            EMPTY_STRING
        )
    }

    private fun observeCampaignPrerequisiteData() {
        viewModel.campaignPrerequisiteData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    viewModel.setCampaignDrafts(result.data.drafts)
                    handleDraftCount(result.data.drafts.size)
                    displayRemainingQuota(result.data.remainingQuota)
                    checkCampaignEligibility(result.data.remainingQuota, result.data.shopInfo)
                }
                is Fail -> {
                    binding?.root showError result.throwable
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
                    binding?.root showError result.throwable
                    binding?.searchBar?.gone()
                    binding?.loader?.gone()
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
                    displayShareBottomSheet(metadata)
                }
                is Fail -> {
                    dismissLoaderDialog()
                    binding?.root showError result.throwable
                }
            }
        }
    }

    private fun observeSellerEligibility() {
        viewModel.sellerEligibility.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.btnCreateCampaign?.isLoading = false
                    handleSellerEligibility(result.data)
                }
                is Fail -> {
                    binding?.btnCreateCampaign?.isLoading = false
                    binding?.root showError result.throwable
                }
            }
        }
    }

    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
    }

    fun setOnNavigateToActiveCampaignListener(onNavigateToActiveCampaignTab : () -> Unit){
        this.onNavigateToActiveCampaignTab = onNavigateToActiveCampaignTab
    }

    private fun setupTabChangeListener() {
        val listener = object : CampaignListContainerFragment.TabChangeListener {
            override fun onTabChanged() {
                if (totalCampaign == ZERO) {
                    showEmptyState()
                    binding?.cardView?.gone()
                } else {
                    hideEmptyState()
                    binding?.cardView?.visible()
                }
            }
        }
        (parentFragment as? CampaignListContainerFragment)?.setTabChangeListener(listener)
    }

    private val onCampaignClicked: (CampaignUiModel, Int) -> Unit = { campaign, position ->

    }

    private val onOverflowMenuClicked: (CampaignUiModel) -> Unit = { campaign ->
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
    }

    override fun onGetListError(message: String) {

    }

    private fun handleScrollDownEvent() {
        binding?.searchBar.slideDown()
        binding?.cardView.slideDown()
    }

    private fun handleScrollUpEvent() {
        binding?.searchBar.slideUp()
        binding?.cardView.slideUp()
    }

    private fun handleCreateCampaign() {
        val campaignDrafts = viewModel.getCampaignDrafts()
        if (campaignDrafts.size >= MAX_DRAFT_COUNT) {
            DraftListBottomSheet.showUsingCampaignUiModel(
                childFragmentManager,
                campaignDrafts,
                ::onDeleteDraftSuccess
            )
        } else {
            CampaignInformationActivity.start(requireActivity(), PageMode.CREATE)
        }
    }

    private fun handleSellerEligibility(isEligible : Boolean) {
        if (isEligible) {
            handleCreateCampaign()
        } else {
            routeToPmSubscribePage()
        }
    }

    private fun displayCampaigns(data: CampaignMeta) {
        if (data.campaigns.size.isMoreThanZero()) {
            binding?.groupNoSearchResult?.gone()
            renderList(data.campaigns, data.campaigns.size == getPerPage())
        } else {
            binding?.groupNoSearchResult?.visible()
        }
    }

    private fun showEmptyState() {
        binding?.searchBar?.gone()
        binding?.recyclerView?.gone()

        val buttonWording = if (tabPosition == TAB_POSITION_FIRST) {
            getString(R.string.sfs_create_campaign)
        } else {
            getString(R.string.sfs_monitor_campaign)
        }

        val buttonOnClickAction =  if (tabPosition == TAB_POSITION_FIRST) {
            {  }
        } else {
            { onNavigateToActiveCampaignTab() }
        }

        binding?.btnCreateCampaignEmptyState?.visible()
        binding?.btnCreateCampaignEmptyState?.text = buttonWording
        binding?.btnCreateCampaignEmptyState?.setOnClickListener { buttonOnClickAction() }

        binding?.emptyState?.visible()
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)

        val title = if (tabPosition == TAB_POSITION_FIRST) {
            getString(R.string.sfs_no_active_campaign_title)
        } else {
            getString(R.string.sfs_no_campaign_history_title)
        }

        binding?.emptyState?.setTitle(title)

        val description = if (tabPosition == TAB_POSITION_FIRST) {
            getString(R.string.sfs_no_active_campaign_description)
        } else {
            getString(R.string.sfs_no_campaign_history_description)
        }

        binding?.emptyState?.setDescription(description)
    }

    private fun hideEmptyState() {
        binding?.emptyState?.gone()
        binding?.btnCreateCampaignEmptyState?.gone()
        binding?.tpgRemainingQuotaEmptyState?.gone()
    }

    private fun doOnDelayFinished(block: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(SCROLL_DISTANCE_DELAY_IN_MILLIS)
            block()
        }
    }

    private fun handleDraftCount(draftCount: Int) {
        val wording = String.format(getString(R.string.sfs_placeholder_draft), draftCount)
        binding?.btnDraft?.text = wording
        binding?.btnDraft?.isVisible = draftCount.isMoreThanZero()
    }

    private fun displayRemainingQuota(remainingQuota: Int) {
        val counter = if (remainingQuota.isMoreThanZero()) {
            remainingQuota
        } else {
            ZERO
        }
        val wording = String.format(
            getString(R.string.sfs_placeholder_remaining_quota),
            counter
        )

        binding?.tpgRemainingQuota?.text = wording
        binding?.tpgRemainingQuota?.visible()

        val shouldVisible = tabPosition == TAB_POSITION_FIRST && totalCampaign == ZERO
        binding?.tpgRemainingQuotaEmptyState?.isVisible = shouldVisible
        binding?.tpgRemainingQuotaEmptyState?.text = wording
    }

    private fun checkCampaignEligibility(remainingQuota: Int, shopInfo: ShopInfo) {
        val shopTierId = shopInfo.shopTierId
        if (remainingQuota == ZERO
            && shopTierId != POWER_MERCHANT_PRO_ID
            && shopTierId != OFFICIAL_STORE_ID) {
            showNoCampaignQuotaDialog(requireActivity()) {
                routeToPmSubscribePage()
            }
        }
    }

    private fun displayMoreMenuBottomSheet(campaign: CampaignUiModel) {
        val bottomSheet = MoreMenuBottomSheet.newInstance(
            campaign.campaignId,
            campaign.campaignName,
            campaign.status
        )
        bottomSheet.setOnViewCampaignMenuSelected {}
        bottomSheet.setOnCancelCampaignMenuSelected { id: Long, title: String, status: CampaignStatus? ->
            handleCancelCampaign(campaign.thematicParticipation, campaign.isCancellable, id, title, status)
        }
        bottomSheet.setOnShareCampaignMenuSelected {
            showLoaderDialog()
            viewModel.getShareComponentMetadata(campaign.campaignId)
        }
        bottomSheet.setOnEditCampaignMenuSelected {}
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayShareBottomSheet(metadata: ShareComponentMetadata) {
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
            requireActivity(),
            view ?: return,
            outgoingText
        )
        shareComponentBottomSheet?.dismiss()
    }

    private fun onDeleteDraftSuccess() {
        binding?.cardView showToaster getString(R.string.sfs_draft_deleted)

        // add delay to wait until server done to saving data,
        // with this delay we can get more actual draft data
        view?.postDelayed( {
            viewModel.getCampaignPrerequisiteData()
        }, DRAFT_SERVER_SAVING_DURATION)
    }

    private fun showLoaderDialog() {
        loaderDialog.setLoadingText(getString(R.string.sfs_please_wait))
        loaderDialog.show()
    }

    private fun dismissLoaderDialog() {
        loaderDialog.dialog.dismiss()
    }

    private fun handleCancelCampaign(
        isParticipatingThematicCampaign: Boolean,
        isCancellable: Boolean,
        id: Long,
        title: String,
        status: CampaignStatus?
    ) {
        if (isParticipatingThematicCampaign && isCancellable) {
            binding?.cardView showError getString(R.string.sfs_cannot_cancel_campaign)
            return
        }

        showCancelCampaignBottomSheet(id, title, status)
    }

    private fun routeToPmSubscribePage() {
        val intent = RouteManager.getIntent(context, POWER_MERCHANT_SUBSCRIBE)
        startActivity(intent)
    }

    private fun showCancelCampaignBottomSheet(id: Long, title: String, status: CampaignStatus?) {
        val toasterActionText = getString(R.string.action_oke)
        val toasterMessage = getString(R.string.cancelcampaign_message_success, title)
        val bottomSheet = CancelCampaignBottomSheet(id, title, status) {
            Toaster.build(view ?: return@CancelCampaignBottomSheet, toasterMessage,
                Toaster.LENGTH_SHORT, actionText = toasterActionText
            ).show()
            view?.post {
                clearAllData()
                getCampaigns(FIRST_PAGE)
            }
        }
        bottomSheet.show(childFragmentManager)
    }
}