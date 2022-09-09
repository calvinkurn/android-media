package com.tokopedia.tkpd.flashsale.presentation.list.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.slideDown
import com.tokopedia.campaign.utils.extension.slideUp
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleStatusFilter
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailActivity
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.FinishedFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.OngoingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.RegisteredFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.UpcomingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.EmptyStateConfig
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEvent
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiState
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.TabConfig
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.tkpd.flashsale.util.constant.RemoteImageUrlConstant
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import java.util.Date
import javax.inject.Inject

class FlashSaleListFragment : BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        private const val BUNDLE_KEY_TAB_ID = "tab_id"
        private const val BUNDLE_KEY_TAB_NAME = "tab_name"
        private const val PAGE_SIZE = 10
        private const val SELLER_EDU_URL =
            "https://seller.tokopedia.com/edu/cara-daftar-produk-flash-sale/"


        @JvmStatic
        fun newInstance(tabId: Int, tabName: String): FlashSaleListFragment {
            val fragment = FlashSaleListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TAB_ID, tabId)
                putString(BUNDLE_KEY_TAB_NAME, tabName)
            }
            return fragment
        }

    }

    private val tabId by lazy { arguments?.getInt(BUNDLE_KEY_TAB_ID).orZero() }
    private val tabName by lazy { arguments?.getString(BUNDLE_KEY_TAB_NAME).orEmpty() }

    private val flashSaleAdapter by lazy {
        CompositeAdapter.Builder()
            .add(OngoingFlashSaleDelegateAdapter(onFlashSaleClicked))
            .add(RegisteredFlashSaleDelegateAdapter(onFlashSaleClicked, onAddProductClicked))
            .add(UpcomingFlashSaleDelegateAdapter(onFlashSaleClicked))
            .add(FinishedFlashSaleDelegateAdapter(onFlashSaleClicked))
            .add(LoadingDelegateAdapter())
            .build()
    }

    private val sortChips by lazy { SortFilterItem(getString(R.string.stfs_sort)) }
    private val categoryChips by lazy { SortFilterItem(getString(R.string.stfs_all_category)) }
    private val statusChips by lazy { SortFilterItem(getString(R.string.stfs_all_status)) }

    private val tabConfig by lazy {
        mapOf(
            TabConstant.TAB_ID_UPCOMING to TabConfig(
                filters = arrayListOf(sortChips, categoryChips),
                statusFilters = listOf(),
                emptyStateConfig = EmptyStateConfig(
                    RemoteImageUrlConstant.IMAGE_URL_EMPTY_UPCOMING_CAMPAIGN,
                    getString(R.string.stfs_empty_state_upcoming_title),
                    getString(R.string.stfs_empty_state_upcoming_description),
                    getString(R.string.stfs_read_article),
                    primaryCtaAction = { routeToUrl(SELLER_EDU_URL) }
                )),
            TabConstant.TAB_ID_REGISTERED to TabConfig(
                filters = arrayListOf(sortChips, categoryChips, statusChips),
                statusFilters = listOf(
                    FlashSaleStatusFilter(
                        FlashSaleStatus.NO_REGISTERED_PRODUCT.id,
                        getString(R.string.stfs_status_product_not_registered)
                    ),
                    FlashSaleStatusFilter(
                        FlashSaleStatus.WAITING_FOR_SELECTION.id,
                        getString(R.string.stfs_status_waiting_for_selection)
                    ),
                    FlashSaleStatusFilter(
                        FlashSaleStatus.ON_SELECTION_PROCESS.id,
                        getString(R.string.stfs_status_on_selection)
                    ),
                    FlashSaleStatusFilter(
                        FlashSaleStatus.SELECTION_FINISHED.id,
                        getString(R.string.stfs_status_selection_finished)
                    )
                ),
                emptyStateConfig = EmptyStateConfig(
                    RemoteImageUrlConstant.IMAGE_URL_EMPTY_REGISTERED_CAMPAIGN,
                    getString(R.string.stfs_empty_state_registered_title),
                    getString(R.string.stfs_empty_state_registered_description)
                )
            ),
            TabConstant.TAB_ID_ONGOING to TabConfig(
                filters = arrayListOf(sortChips, categoryChips),
                statusFilters = listOf(),
                emptyStateConfig = EmptyStateConfig(
                    RemoteImageUrlConstant.IMAGE_URL_EMPTY_ONGOING_CAMPAIGN,
                    getString(R.string.stfs_empty_state_ongoing_title),
                    getString(R.string.stfs_empty_state_ongoing_description)
                )
            ),
            TabConstant.TAB_ID_FINISHED to TabConfig(
                filters = arrayListOf(sortChips, categoryChips, statusChips),
                statusFilters = listOf(
                    FlashSaleStatusFilter(
                        FlashSaleStatus.FINISHED.id,
                        getString(R.string.stfs_status_finished)
                    ),
                    FlashSaleStatusFilter(
                        FlashSaleStatus.CANCELLED.id,
                        getString(R.string.stfs_status_cancelled)
                    ),
                    FlashSaleStatusFilter(
                        FlashSaleStatus.REJECTED.id,
                        getString(R.string.stfs_status_rejected)
                    ),
                    FlashSaleStatusFilter(
                        FlashSaleStatus.MISSED.id,
                        getString(R.string.stfs_status_skipped)
                    )
                ),
                emptyStateConfig = EmptyStateConfig(
                    RemoteImageUrlConstant.IMAGE_URL_EMPTY_FINISHED_CAMPAIGN,
                    getString(R.string.stfs_empty_state_done_title),
                    getString(R.string.stfs_empty_state_done_description)
                )
            )
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<StfsFragmentFlashSaleListBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleListViewModel::class.java) }
    private val currentDate = Date()

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
        viewModel.processEvent(FlashSaleListUiEvent.GetFlashSaleCategory(tabName))
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupView()
        observeUiEffect()
        observeUiState()
        viewModel.processEvent(FlashSaleListUiEvent.LoadPage(tabId, tabName, Int.ZERO, currentDate))
    }


    private fun setupView() {
        setupSortFilter()
        setupPaging()
        binding?.imgScrollUp?.setOnClickListener {
            binding?.recyclerView?.smoothSnapToPosition(Int.ZERO)
        }
    }

    private fun setupPaging() {
        val pagingConfig = HasPaginatedList.Config(
            pageSize = PAGE_SIZE,
            onLoadNextPage = {
                flashSaleAdapter.addItem(LoadingItem)
            }, onLoadNextPageFinished = {
                flashSaleAdapter.removeItem(LoadingItem)
            })

        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            attachOnScrollListener(onScrollDown = {
                binding?.imgScrollUp?.slideUp()
                binding?.sortFilter?.slideDown()
            }, onScrollUp = {
                binding?.imgScrollUp?.slideDown()
                binding?.sortFilter?.slideUp()
            })
            adapter = flashSaleAdapter

            attachPaging(this, pagingConfig) { _, offset ->
                viewModel.processEvent(FlashSaleListUiEvent.LoadPage(tabId, tabName, offset, currentDate))
            }
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

    private fun handleEffect(effect: FlashSaleListUiEffect) {
        when (effect) {
            is FlashSaleListUiEffect.ShowSortBottomSheet -> {
                showSortBottomSheet(effect.selectedSortId)
            }
            is FlashSaleListUiEffect.ShowCategoryBottomSheet -> {
                showCategoryFilterBottomSheet(effect.selectedCategoryIds, effect.categories)
            }
            is FlashSaleListUiEffect.ShowStatusBottomSheet -> {
                showStatusFilterBottomSheet(effect.selectedStatusIds)
            }
            is FlashSaleListUiEffect.FetchFlashSaleError -> {
                flashSaleAdapter.removeItem(LoadingItem)
                binding?.recyclerView.showToasterError(effect.throwable)
            }
            is FlashSaleListUiEffect.FetchCategoryError -> {
                flashSaleAdapter.removeItem(LoadingItem)
                binding?.recyclerView.showToasterError(effect.throwable)
            }
            is FlashSaleListUiEffect.LoadNextPageSuccess -> {
                notifyLoadResult(effect.currentPageItems.size == PAGE_SIZE)
                flashSaleAdapter.submit(effect.allItems)
            }

        }
    }


    private fun handleUiState(uiState: FlashSaleListUiState) {
        renderLoadingState(uiState.isLoading)
        renderSortFilter(uiState)
        renderEmptyState(uiState.isLoading, uiState.isFilterActive, uiState.searchResultCount)
        refreshScrollState(uiState.allItems)
        renderScrollUpButton(uiState.allItems.size)
    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
        binding?.recyclerView?.isVisible = !isLoading
    }


    private fun renderScrollUpButton(totalFlashSaleCount: Int) {
        binding?.imgScrollUp?.isVisible = totalFlashSaleCount.isMoreThanZero()
    }

    private fun renderSortFilter(uiState: FlashSaleListUiState) {
        if (!uiState.isFilterActive && uiState.allItems.isEmpty()) {
            binding?.sortFilter?.gone()
        } else {
            binding?.sortFilter?.visible()
        }
        renderSortChips(uiState.selectedSort)
        renderCategoryFilterChips(uiState.selectedCategoryIds)
        renderStatusChips(uiState.selectedStatusIds)
    }

    private fun renderEmptyState(isLoading: Boolean, isUsingFilter: Boolean, searchResultCount: Int) {
        if (isLoading) {
            binding?.emptyState?.gone()
        } else {
            handleEmptyState(isUsingFilter, searchResultCount)
        }
    }

    private fun handleEmptyState(isUsingFilter: Boolean, searchResultCount: Int) {
        when {
            searchResultCount.isZero() -> displayEmptySearchResult()
            searchResultCount.isMoreThanZero() -> binding?.emptyState?.gone()
            isUsingFilter && searchResultCount == 0 -> displayEmptySearchResult()
            !isUsingFilter && searchResultCount == 0 -> displayNoFlashSaleAvailable()
        }
    }

    private fun displayEmptySearchResult() {
        flashSaleAdapter.removeItem(LoadingItem)

        binding?.emptyState?.visible()
        binding?.emptyState?.setImageUrl(RemoteImageUrlConstant.IMAGE_URL_NO_SEARCH_RESULT)
        binding?.emptyState?.setTitle(getString(R.string.stfs_empty_search_result_title))
        binding?.emptyState?.setDescription(getString(R.string.stfs_empty_search_result_description))
    }

    private fun displayNoFlashSaleAvailable() {
        flashSaleAdapter.removeItem(LoadingItem)

        binding?.emptyState?.visible()

        val emptyStateConfig = tabConfig[tabId]?.emptyStateConfig ?: return
        binding?.emptyState?.setImageUrl(emptyStateConfig.imageUrl)
        binding?.emptyState?.setTitle(emptyStateConfig.title)
        binding?.emptyState?.setDescription(emptyStateConfig.description)
        handleEmptyStatePrimaryAction(emptyStateConfig)
    }

    private fun renderSortChips(selectedSort: SingleSelectionItem) {
        if (selectedSort.id == "DEFAULT_VALUE_PLACEHOLDER") {
            sortChips.type = ChipsUnify.TYPE_NORMAL
            sortChips.selectedItem = arrayListOf(getString(R.string.stfs_sort))
        } else {
            sortChips.type = ChipsUnify.TYPE_SELECTED
            sortChips.selectedItem = arrayListOf(selectedSort.name)
        }
    }


    private fun renderCategoryFilterChips(selectedCategoryIds: List<Long>) {
        if (selectedCategoryIds.isEmpty()) {
            categoryChips.type = ChipsUnify.TYPE_NORMAL
            categoryChips.selectedItem = arrayListOf(getString(R.string.stfs_all_category))
        } else {
            categoryChips.type = ChipsUnify.TYPE_SELECTED
            categoryChips.selectedItem = arrayListOf(
                getString(
                    R.string.stfs_placeholder_selected_category_count,
                    selectedCategoryIds.size
                )
            )
        }
    }

    private fun renderStatusChips(selectedStatusIds: List<String>) {
        if (selectedStatusIds.isEmpty()) {
            statusChips.type = ChipsUnify.TYPE_NORMAL
            statusChips.selectedItem = arrayListOf(getString(R.string.stfs_all_status))
        } else {
            statusChips.type = ChipsUnify.TYPE_SELECTED
            statusChips.selectedItem = arrayListOf(
                getString(
                    R.string.stfs_placeholder_selected_status_count,
                    selectedStatusIds.size
                )
            )
        }
    }

    private fun refreshScrollState(allItems: List<DelegateAdapterItem>) {
        if (allItems.isEmpty()) {
            resetPaging()
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
            viewModel.processEvent(FlashSaleListUiEvent.ChangeSort)
        }

        sortChips.listener = { onSortClicked() }
        sortChips.chevronListener = { onSortClicked() }

        val onCategoryClicked = {
            viewModel.processEvent(FlashSaleListUiEvent.ChangeCategory)
        }
        categoryChips.listener = { onCategoryClicked() }
        categoryChips.chevronListener = { onCategoryClicked() }


        val onStatusClicked = {
            viewModel.processEvent(FlashSaleListUiEvent.ChangeStatus)
        }
        statusChips.listener = { onStatusClicked() }
        statusChips.chevronListener = { onStatusClicked() }

        val items = tabConfig[tabId]?.filters ?: return

        binding?.sortFilter?.addItem(items)
        binding?.sortFilter?.parentListener = {}
        binding?.sortFilter?.dismissListener = {
            viewModel.processEvent(FlashSaleListUiEvent.ClearFilter)
        }
    }

    private fun showSortBottomSheet(selectedSortId: String) {
        if (!isAdded) return

        val sortOptions = arrayListOf(
            SingleSelectionItem(
                id = "CAMPAIGN_START_DATE",
                getString(R.string.stfs_nearest),
                direction = "ASC"
            ),
            SingleSelectionItem(
                id = "CAMPAIGN_ID",
                getString(R.string.stfs_newest),
                direction = "DESC"
            )
        )
        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedSortId, sortOptions)
        bottomSheet.setBottomSheetTitle(getString(R.string.stfs_sort_title))
        bottomSheet.setOnApplyButtonClick { sort ->
            viewModel.processEvent(FlashSaleListUiEvent.ApplySort(sort))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showCategoryFilterBottomSheet(
        selectedCategoryIds: List<Long>,
        categories: List<FlashSaleCategory>
    ) {
        if (!isAdded) return

        val mappedSelectedCategoryIds = selectedCategoryIds.map { id -> id.toString() }
        val mappedCategories = categories.map { category ->
            MultipleSelectionItem(
                category.categoryId.toString(),
                category.categoryName
            )
        }

        val bottomSheet = MultipleSelectionBottomSheet.newInstance(
            ArrayList(mappedSelectedCategoryIds),
            ArrayList(mappedCategories)
        )

        bottomSheet.setBottomSheetTitle(getString(R.string.stfs_category_title))
        bottomSheet.setOnApplyButtonClick { filter ->
            viewModel.processEvent(FlashSaleListUiEvent.ApplyCategoryFilter(filter))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showStatusFilterBottomSheet(selectedStatusIds: List<String>) {
        if (!isAdded) return

        val statuses = tabConfig[tabId]?.statusFilters ?: return
        val mappedStatuses =
            statuses.map { status -> MultipleSelectionItem(status.statusId, status.statusName) }

        val bottomSheet = MultipleSelectionBottomSheet.newInstance(
            ArrayList(selectedStatusIds),
            ArrayList(mappedStatuses)
        )

        bottomSheet.setBottomSheetTitle(getString(R.string.stfs_status_title))
        bottomSheet.setOnApplyButtonClick { filter ->
            viewModel.processEvent(FlashSaleListUiEvent.ApplyStatusFilter(filter))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private val onFlashSaleClicked: (Int) -> Unit = { selectedItemPosition ->
        val selectedFlashSale = flashSaleAdapter.getItems()[selectedItemPosition]
        val selectedFlashSaleId = selectedFlashSale.id()
        context?.let {
            CampaignDetailActivity.start(
                it,
                selectedFlashSaleId as Long,
                tabName
            )
        }
    }

    private val onAddProductClicked: (Int) -> Unit = { selectedItemPosition ->
        val selectedFlashSale = flashSaleAdapter.getItems()[selectedItemPosition]
        val selectedFlashSaleId = selectedFlashSale.id()
        //TODO: Navigate to add product page
    }

}