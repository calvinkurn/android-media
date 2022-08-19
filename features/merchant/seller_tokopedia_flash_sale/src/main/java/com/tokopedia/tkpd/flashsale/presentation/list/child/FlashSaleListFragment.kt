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
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.campaign.entity.SingleSelectionItem
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.OngoingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.UpcomingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant.BUNDLE_KEY_TARGET_TAB_POSITION
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleListFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_TAB_NAME = "tab_name"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "campaign_count"
        private const val PAGE_SIZE = 10

        private const val IMAGE_URL_EMPTY_UPCOMING_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_upcoming_campaign.png"
        private const val IMAGE_URL_EMPTY_REGISTERED_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_registered_campaign.png"
        private const val IMAGE_URL_EMPTY_ONGOING_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_ongoing_campaign.png"
        private const val IMAGE_URL_EMPTY_FINISHED_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_finished_campaign.png"

        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            tabId: Int,
            tabName: String,
            totalCampaign: Int
        ): FlashSaleListFragment {
            val fragment = FlashSaleListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TARGET_TAB_POSITION, tabPosition)
                putInt(BundleConstant.BUNDLE_KEY_TAB_ID, tabId)
                putString(BUNDLE_KEY_TAB_NAME, tabName)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, totalCampaign)
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

    private val totalCampaign by lazy {
        arguments?.getInt(BUNDLE_KEY_CAMPAIGN_COUNT).orZero()
    }

    private val sortChips by lazy {  SortFilterItem(getString(R.string.stfs_sort)) }
    private val categoryChips by lazy { SortFilterItem(getString(R.string.stfs_all_category)) }

    private val flashSaleAdapter by lazy {
        CompositeAdapter.Builder()
            .add(OngoingFlashSaleDelegateAdapter())
            .add(UpcomingFlashSaleDelegateAdapter())
            .add(LoadingDelegateAdapter())
            .build()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private var binding by autoClearedNullable<StfsFragmentFlashSaleListBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleListViewModel::class.java) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

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
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiEffect()
        observeUiState()
        viewModel.processEvent(FlashSaleListViewModel.UiEvent.Init(tabName, tabId, totalCampaign))
    }


    private fun setupView() {
        setupSortFilter()
        setupClickListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.run {
            layoutManager =
                LinearLayoutManager(activity ?: return, LinearLayoutManager.VERTICAL, false)
            adapter = flashSaleAdapter

            endlessRecyclerViewScrollListener =
                object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        viewModel.processEvent(FlashSaleListViewModel.UiEvent.LoadNextPage(page * PAGE_SIZE))
                    }
                }
            addOnScrollListener(endlessRecyclerViewScrollListener ?: return)
        }
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
            is FlashSaleListViewModel.UiEffect.FetchFlashSaleError -> {
                binding?.root.showToasterError(effect.throwable)
            }
            is FlashSaleListViewModel.UiEffect.ShowSortBottomSheet -> {
                showSortBottomSheet(effect.selectedSortId)
            }
            is FlashSaleListViewModel.UiEffect.ShowCategoryBottomSheet -> {
                showCategoryFilterBottomSheet(effect.selectedCategoryIds, effect.categories)
            }

        }
    }


    private fun handleUiState(uiState: FlashSaleListViewModel.UiState) {
        renderLoadingState(uiState.isLoading)
        renderLoadNextPage(uiState.isLoadingNextPage)
        renderList(uiState.flashSales)
        renderSortChips(uiState.selectedSort)
        renderCategoryFilterChips(uiState.selectedCategoryIds)
    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
        binding?.recyclerView?.isVisible = !isLoading
    }

    private fun renderLoadNextPage(isLoading: Boolean) {
        if (isLoading) {
            flashSaleAdapter.addItem(LoadingItem)
        } else {
            flashSaleAdapter.removeItem(LoadingItem)
        }
    }


    private fun renderList(flashSales: List<DelegateAdapterItem>) {
        if (flashSales.isNotEmpty()) {
            val allItems = flashSaleAdapter.getItems() + flashSales
            flashSaleAdapter.submit(allItems)

            val hasNextPage = flashSales.size >= PAGE_SIZE

            endlessRecyclerViewScrollListener?.updateStateAfterGetData()
            endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)

            if (flashSaleAdapter.itemCount.orZero() < PAGE_SIZE && hasNextPage) {
                endlessRecyclerViewScrollListener?.loadMoreNextPage()
            }
        }
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
            categoryChips.selectedItem = arrayListOf(getString(R.string.stfs_placeholder_selected_category_count, selectedCategoryIds.size))
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


        val items = arrayListOf(sortChips, categoryChips)

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

}