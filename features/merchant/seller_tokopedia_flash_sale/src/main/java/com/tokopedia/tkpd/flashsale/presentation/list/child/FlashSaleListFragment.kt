package com.tokopedia.tkpd.flashsale.presentation.list.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.campaign.entity.SingleSelectionItem
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleStatusEnum
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.FinishedFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.OngoingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.RegisteredFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.UpcomingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.EmptyStateConfig
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant.BUNDLE_KEY_TARGET_TAB_POSITION
import com.tokopedia.tkpd.flashsale.util.constant.RemoteImageUrlConstant
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleListFragment : BaseSimpleListFragment<CompositeAdapter, DelegateAdapterItem>() {

    companion object {
        private const val BUNDLE_KEY_TAB_NAME = "tab_name"
        private const val BUNDLE_KEY_TOTAL_FLASH_SALE_COUNT = "campaign_count"
        private const val PAGE_SIZE = 10
        private const val ONE = 1
        private const val SELLER_EDU_URL = "https://seller.tokopedia.com/edu/cara-daftar-produk-flash-sale/"


        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            tabId: Int,
            tabName: String,
            totalFlashSaleCount: Int
        ): FlashSaleListFragment {
            val fragment = FlashSaleListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TARGET_TAB_POSITION, tabPosition)
                putInt(BundleConstant.BUNDLE_KEY_TAB_ID, tabId)
                putString(BUNDLE_KEY_TAB_NAME, tabName)
                putInt(BUNDLE_KEY_TOTAL_FLASH_SALE_COUNT, totalFlashSaleCount)
            }
            return fragment
        }

    }

    private val tabPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_TARGET_TAB_POSITION).orZero()
    }

    private val tabId by lazy {
        arguments?.getInt(BundleConstant.BUNDLE_KEY_TAB_ID).orZero()
    }

    private val tabName by lazy {
        arguments?.getString(BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    private val totalFlashSaleCount by lazy {
        arguments?.getInt(BUNDLE_KEY_TOTAL_FLASH_SALE_COUNT).orZero()
    }

    private val flashSaleAdapter by lazy {
        CompositeAdapter.Builder()
            .add(OngoingFlashSaleDelegateAdapter())
            .add(RegisteredFlashSaleDelegateAdapter())
            .add(UpcomingFlashSaleDelegateAdapter())
            .add(FinishedFlashSaleDelegateAdapter())
            .add(LoadingDelegateAdapter())
            .build()
    }

    private val emptyStateConfig by lazy {
        mapOf(
            TabConstant.TAB_ID_UPCOMING to EmptyStateConfig(
                RemoteImageUrlConstant.IMAGE_URL_EMPTY_UPCOMING_CAMPAIGN,
                getString(R.string.stfs_empty_state_upcoming_title),
                getString(R.string.stfs_empty_state_upcoming_description),
                getString(R.string.stfs_read_article),
                primaryCtaAction = { routeToUrl(SELLER_EDU_URL) }
            ),
            TabConstant.TAB_ID_REGISTERED to EmptyStateConfig(
                RemoteImageUrlConstant.IMAGE_URL_EMPTY_REGISTERED_CAMPAIGN,
                getString(R.string.stfs_empty_state_registered_title),
                getString(R.string.stfs_empty_state_registered_description)
            ),
            TabConstant.TAB_ID_ONGOING to EmptyStateConfig(
                RemoteImageUrlConstant.IMAGE_URL_EMPTY_ONGOING_CAMPAIGN,
                getString(R.string.stfs_empty_state_ongoing_title),
                getString(R.string.stfs_empty_state_ongoing_description)
            ),
            TabConstant.TAB_ID_FINISHED to EmptyStateConfig(
                RemoteImageUrlConstant.IMAGE_URL_EMPTY_FINISHED_CAMPAIGN,
                getString(R.string.stfs_empty_search_result_title),
                getString(R.string.stfs_empty_search_result_description)
            )
        )
    }

    private val sortChips by lazy {  SortFilterItem(getString(R.string.stfs_sort)) }
    private val categoryChips by lazy { SortFilterItem(getString(R.string.stfs_all_category)) }
    private val statusChips by lazy { SortFilterItem(getString(R.string.stfs_all_status)) }


    private val sortFilterItemsConfig by lazy {
        mapOf(
            TabConstant.TAB_ID_UPCOMING to arrayListOf(sortChips, categoryChips),
            TabConstant.TAB_ID_REGISTERED to arrayListOf(sortChips, categoryChips, statusChips),
            TabConstant.TAB_ID_ONGOING to arrayListOf(sortChips, categoryChips),
            TabConstant.TAB_ID_FINISHED to arrayListOf(sortChips, categoryChips, statusChips)
        )
    }

    private val statusConfig by lazy {
        mapOf(
            TabConstant.TAB_ID_UPCOMING to listOf(),
            TabConstant.TAB_ID_REGISTERED to listOf(
                FlashSaleStatus(FlashSaleStatusEnum.NO_REGISTERED_PRODUCT.value, getString(R.string.stfs_status_product_not_registered)),
                FlashSaleStatus(FlashSaleStatusEnum.WAITING_FOR_SELECTION.value, getString(R.string.stfs_status_waiting_for_selection)),
                FlashSaleStatus(FlashSaleStatusEnum.ON_SELECTION_PROCESS.value,  getString(R.string.stfs_status_on_selection)),
                FlashSaleStatus(FlashSaleStatusEnum.SELECTION_FINISHED.value, getString(R.string.stfs_status_selection_finished))
            ),
            TabConstant.TAB_ID_ONGOING to listOf(),
            TabConstant.TAB_ID_FINISHED to listOf(
                FlashSaleStatus(FlashSaleStatusEnum.FINISHED.value, getString(R.string.stfs_status_finished)),
                FlashSaleStatus(FlashSaleStatusEnum.CANCELLED.value, getString(R.string.stfs_status_cancelled)),
                FlashSaleStatus(FlashSaleStatusEnum.REJECTED.value, getString(R.string.stfs_status_rejected)),
                FlashSaleStatus(FlashSaleStatusEnum.MISSED.value, getString(R.string.stfs_status_skipped))
            )
        )
    }



    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private var binding by autoClearedNullable<StfsFragmentFlashSaleListBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleListViewModel::class.java) }

    override fun getScreenName(): String = FlashSaleListFragment::class.java.canonicalName.orEmpty()

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
        binding = StfsFragmentFlashSaleListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.processEvent(FlashSaleListViewModel.UiEvent.Init(tabName, tabId, totalFlashSaleCount))
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupView()
        observeUiEffect()
        observeUiState()
    }


    private fun setupView() {
        setupSortFilter()
        setupClickListener()
    }


    private fun setupClickListener() {
        binding?.run {


        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleEffect(effect: FlashSaleListViewModel.UiEffect) {
        when (effect) {
            is FlashSaleListViewModel.UiEffect.ShowSortBottomSheet -> {
                showSortBottomSheet(effect.selectedSortId)
            }
            is FlashSaleListViewModel.UiEffect.ShowCategoryBottomSheet -> {
                showCategoryFilterBottomSheet(effect.selectedCategoryIds, effect.categories)
            }
            is FlashSaleListViewModel.UiEffect.ShowStatusBottomSheet -> {
                showStatusFilterBottomSheet(effect.selectedStatusIds)
            }
            is FlashSaleListViewModel.UiEffect.FetchFlashSaleError -> {
                flashSaleAdapter.removeItem(LoadingItem)
                binding?.recyclerView.showToasterError(effect.throwable)
            }
            is FlashSaleListViewModel.UiEffect.FetchCategoryError -> {
                binding?.recyclerView.showToasterError(effect.throwable)
            }
            is FlashSaleListViewModel.UiEffect.LoadNextPageSuccess -> {
                renderList(effect.allItems, effect.currentPageItems.size == getPerPage())
            }

        }
    }


    private fun handleUiState(uiState: FlashSaleListViewModel.UiState) {
        renderSortChips(uiState.selectedSort, uiState.totalFlashSaleCount)
        renderCategoryFilterChips(uiState.selectedCategoryIds)
        renderStatusChips(uiState.selectedStatusIds)
        renderEmptyState(uiState.isFilterActive, totalFlashSaleCount)
        if (uiState.allItems.isEmpty()) {
            endlessRecyclerViewScrollListener?.resetState()
        }
        renderEmptySearchResult(uiState.isLoading, uiState.isFilterActive, uiState.allItems)
    }

    private fun renderEmptyState(isUsingFilter : Boolean, totalFlashSaleCount: Int) {
        if (!isUsingFilter && totalFlashSaleCount == Int.ZERO) {
            binding?.emptyState?.visible()
            binding?.emptyState?.setImageUrl(RemoteImageUrlConstant.IMAGE_URL_NO_SEARCH_RESULT)
            binding?.emptyState?.setTitle(getString(R.string.stfs_empty_search_result_title))
            binding?.emptyState?.setDescription(getString(R.string.stfs_empty_search_result_description))
        } else {
            binding?.emptyState?.gone()
        }
    }

    private fun renderEmptySearchResult(isLoading: Boolean, isUsingFilter : Boolean, allItems: List<DelegateAdapterItem>) {
        if (!isLoading && isUsingFilter && allItems.isEmpty()) {
            binding?.emptyState?.visible()

            val emptyStateConfig = emptyStateConfig[tabId] ?: return
            binding?.emptyState?.setImageUrl(emptyStateConfig.imageUrl)
            binding?.emptyState?.setTitle(emptyStateConfig.title)
            binding?.emptyState?.setDescription(emptyStateConfig.description)
            handleEmptyStatePrimaryAction(emptyStateConfig)
        }
    }

    private fun renderSortChips(selectedSort: SingleSelectionItem, totalFlashSaleItemCount: Int) {
        if (selectedSort.id == "DEFAULT_VALUE_PLACEHOLDER") {
            sortChips.type = ChipsUnify.TYPE_NORMAL
            sortChips.selectedItem = arrayListOf(getString(R.string.stfs_sort))
        } else {
            sortChips.type = ChipsUnify.TYPE_SELECTED
            sortChips.selectedItem = arrayListOf(selectedSort.name)
        }

        if (totalFlashSaleItemCount == Int.ZERO) {
            binding?.sortFilter?.gone()
        }
    }


    private fun renderCategoryFilterChips(selectedCategoryIds: List<Long>) {
        if (selectedCategoryIds.isEmpty()) {
            categoryChips.type = ChipsUnify.TYPE_NORMAL
            categoryChips.selectedItem = arrayListOf(getString(R.string.stfs_all_category))
        } else {
            categoryChips.type = ChipsUnify.TYPE_SELECTED
            categoryChips.selectedItem = arrayListOf(getString(R.string.stfs_placeholder_selected_category_count, selectedCategoryIds.size))
        }
    }

    private fun renderStatusChips(selectedStatusIds: List<String>) {
        if (selectedStatusIds.isEmpty()) {
            statusChips.type = ChipsUnify.TYPE_NORMAL
            statusChips.selectedItem = arrayListOf(getString(R.string.stfs_all_status))
        } else {
            statusChips.type = ChipsUnify.TYPE_SELECTED
            statusChips.selectedItem = arrayListOf(getString(R.string.stfs_placeholder_selected_status_count, selectedStatusIds.size))
        }
    }


    private fun handleEmptyStatePrimaryAction(emptyStateConfig: EmptyStateConfig) {
        if (emptyStateConfig.primaryCtaText.isNotEmpty()) {
            binding?.emptyState?.setPrimaryCTAText(emptyStateConfig.primaryCtaText)
            binding?.emptyState?.setPrimaryCTAClickListener { emptyStateConfig.primaryCtaAction() }
        }
    }


    private fun setupSortFilter() {
        val onSortClicked = {
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ChangeSort)
        }

        sortChips.listener = { onSortClicked() }
        sortChips.chevronListener = { onSortClicked() }

        val onCategoryClicked = {
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ChangeCategory)
        }
        categoryChips.listener = { onCategoryClicked() }
        categoryChips.chevronListener = { onCategoryClicked() }


        val onStatusClicked = {
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ChangeStatus)
        }
        statusChips.listener = { onStatusClicked() }
        statusChips.chevronListener = { onStatusClicked() }

        val items = sortFilterItemsConfig[tabId] ?: return

        binding?.sortFilter?.addItem(items)
        binding?.sortFilter?.parentListener = {}
        binding?.sortFilter?.dismissListener = {
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ClearFilter)
        }
    }

    private fun showSortBottomSheet(selectedSortId : String) {
        if (!isAdded) return

        val sortOptions = arrayListOf(
            SingleSelectionItem(id = "CAMPAIGN_START_DATE", getString(R.string.stfs_nearest), direction = "ASC"),
            SingleSelectionItem(id = "CAMPAIGN_ID", getString(R.string.stfs_newest), direction = "DESC")
        )
        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedSortId, sortOptions)
        bottomSheet.setBottomSheetTitle(getString(R.string.stfs_sort_title))
        bottomSheet.setOnApplyButtonClick { sort ->
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ApplySort(sort))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showCategoryFilterBottomSheet(selectedCategoryIds : List<Long>, categories : List<FlashSaleCategory>) {
        if (!isAdded) return

        val mappedSelectedCategoryIds = selectedCategoryIds.map { id -> id.toString() }
        val mappedCategories = categories.map { category -> MultipleSelectionItem(category.categoryId.toString(), category.categoryName) }

        val bottomSheet = MultipleSelectionBottomSheet.newInstance(
            ArrayList(mappedSelectedCategoryIds),
            ArrayList(mappedCategories)
        )

        bottomSheet.setBottomSheetTitle(getString(R.string.stfs_category_title))
        bottomSheet.setOnApplyButtonClick { filter ->
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ApplyCategoryFilter(filter))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showStatusFilterBottomSheet(selectedStatusIds : List<String>) {
        if (!isAdded) return

        val statuses : List<FlashSaleStatus> = statusConfig[tabId] ?: return
        val mappedStatuses = statuses.map { status -> MultipleSelectionItem(status.statusId, status.statusName) }

        val bottomSheet = MultipleSelectionBottomSheet.newInstance(
            ArrayList(selectedStatusIds),
            ArrayList(mappedStatuses)
        )

        bottomSheet.setBottomSheetTitle(getString(R.string.stfs_status_title))
        bottomSheet.setOnApplyButtonClick { filter ->
            viewModel.processEvent(FlashSaleListViewModel.UiEvent.ApplyStatusFilter(filter))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


    override fun createAdapter(): CompositeAdapter {
        return flashSaleAdapter
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

    override fun addElementToAdapter(list: List<DelegateAdapterItem>) {
        adapter?.submit(list)
    }

    override fun loadData(page: Int) {
        getFlashSales(page)
    }

    private fun getFlashSales(page: Int) {
        val offset = if (page == ONE) {
            Int.ZERO
        } else {
            (page - ONE) * PAGE_SIZE
        }
        viewModel.processEvent(FlashSaleListViewModel.UiEvent.LoadPage(offset))
    }

    override fun clearAdapterData() {
        adapter?.submit(listOf())
    }

    override fun onShowLoading() {
        flashSaleAdapter.addItem(LoadingItem)
    }

    override fun onHideLoading() {
        flashSaleAdapter.removeItem(LoadingItem)
    }

    override fun onDataEmpty() {
        flashSaleAdapter.removeItem(LoadingItem)
    }

    override fun onGetListError(message: String) {
        flashSaleAdapter.removeItem(LoadingItem)
    }


    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {

    }

}