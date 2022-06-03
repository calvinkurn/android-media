package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListBinding
import com.tokopedia.shop.flash_sale.common.constant.Constant.EMPTY_STRING
import com.tokopedia.shop.flash_sale.common.constant.Constant.FIRST_PAGE
import com.tokopedia.shop.flash_sale.common.constant.Constant.ZERO
import com.tokopedia.shop.flash_sale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flash_sale.common.extension.showError
import com.tokopedia.shop.flash_sale.common.extension.slideDown
import com.tokopedia.shop.flash_sale.common.extension.slideUp
import com.tokopedia.shop.flash_sale.common.share_component.ShareComponentInstanceBuilder
import com.tokopedia.shop.flash_sale.common.util.DateManager
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flash_sale.presentation.campaign_list.container.CampaignListContainerFragment
import com.tokopedia.shop.flash_sale.presentation.campaign_list.dialog.showNoCampaignQuotaDialog
import com.tokopedia.shop.flash_sale.presentation.draft.bottomsheet.DraftListBottomSheet
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
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_NAME = "status_name"
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_ID = "status_id"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "product_count"
        private const val PAGE_SIZE = 10
        private const val MAX_DRAFT_COUNT = 3
        private const val TAB_POSITION_FIRST = 0
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 300
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_no_active_campaign.png"

        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            campaignStatusName: String,
            campaignStatusIds: IntArray,
            totalCampaign: Int,
        ): CampaignListFragment {
            val fragment = CampaignListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TAB_POSITION, tabPosition)
                putString(BUNDLE_KEY_CAMPAIGN_STATUS_NAME, campaignStatusName)
                putIntArray(BUNDLE_KEY_CAMPAIGN_STATUS_ID, campaignStatusIds)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, totalCampaign)
            }
            return fragment
        }

    }

    private val tabPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_TAB_POSITION).orZero()
    }

    private val campaignStatusName by lazy {
        arguments?.getString(BUNDLE_KEY_CAMPAIGN_STATUS_NAME).orEmpty()
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
    lateinit var dateManager: DateManager

    @Inject
    lateinit var shareComponentInstanceBuilder: ShareComponentInstanceBuilder

    private var binding by autoClearedNullable<SsfsFragmentCampaignListBinding>()
    private var isFirstLoad = true
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListViewModel::class.java) }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}
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
        observeCampaigns()
        observeRemainingQuota()
        observeCampaignCreation()
        observeCampaignDrafts()
        observeShareComponentMetadata()
        viewModel.getRemainingQuota(dateManager.getCurrentMonth(), dateManager.getCurrentYear())
    }

    private fun setupView() {
        binding?.btnCreateCampaign?.setOnClickListener { handleCreateCampaign() }
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


    private fun observeRemainingQuota() {
        viewModel.campaignAttribute.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.cardView?.visible()
                    displayRemainingQuota(result.data.remainingCampaignQuota)

                    binding?.btnDraft?.isLoading = true
                    viewModel.getCampaignDrafts(
                        MAX_DRAFT_COUNT,
                        FIRST_PAGE
                    )

                }
                is Fail -> {
                    binding?.root showError result.throwable
                }
            }
        }
    }


    private fun observeCampaignDrafts() {
        viewModel.campaignDrafts.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.btnDraft?.isLoading = false

                    val draftCount = result.data.campaigns.size
                    viewModel.setCampaignDrafts(result.data.campaigns)
                    binding?.btnDraft?.isVisible = draftCount.isMoreThanZero()
                    handleDraftCount(draftCount)
                }
                is Fail -> {
                    binding?.btnDraft?.isLoading = false
                    binding?.root showError result.throwable
                    binding?.cardView?.gone()
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


    private fun observeCampaignCreation() {
        viewModel.campaignCreation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val creationResult = result.data
                }
                is Fail -> {

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

    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
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
        adapter?.addData(list)
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
        adapter?.clearData()
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
            //TODO: Navigate to info campaign page
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
        val title = String.format(
            getString(R.string.sfs_placeholder_no_campaign_title),
            campaignStatusName.lowercase()
        )
        val description = getString(R.string.sfs_no_campaign_description)

        binding?.searchBar?.gone()
        binding?.recyclerView?.gone()

        binding?.btnCreateCampaignEmptyState?.isVisible = tabPosition == TAB_POSITION_FIRST

        binding?.emptyState?.visible()
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(title)
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

        if (counter == ZERO) {
            showNoCampaignQuotaDialog(requireActivity()) {

            }
        }
    }

    private fun onDeleteDraftSuccess() {
        viewModel.getCampaignDrafts(
            MAX_DRAFT_COUNT,
            FIRST_PAGE
        )
    }

    private fun displayMoreMenuBottomSheet(campaign: CampaignUiModel) {
        val bottomSheet = MoreMenuBottomSheet.newInstance(campaign.campaignName, campaign.status)
        bottomSheet.setOnViewCampaignMenuSelected {}
        bottomSheet.setOnCancelCampaignMenuSelected {}
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

    private fun showLoaderDialog() {
        loaderDialog.setLoadingText(getString(R.string.sfs_please_wait))
        loaderDialog.show()
    }

    private fun dismissLoaderDialog() {
        loaderDialog.dialog.dismiss()
    }

}