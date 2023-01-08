package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.updateScrollingChild
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.FragmentGlobalSearchShopTabBinding
import com.tokopedia.content.common.producttag.analytic.coordinator.ShopImpressionCoordinator
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.util.extension.getVisibleItems
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.NetworkResult
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.GlobalSearchShopUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.util.getParentFragmentByInstance
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchShopTabFragment @Inject constructor(
    private val impressionCoordinator: ShopImpressionCoordinator,
) : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "GlobalSearchShopTabFragment"

    private var _binding: FragmentGlobalSearchShopTabBinding? = null
    private val binding: FragmentGlobalSearchShopTabBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ShopCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ShopCardAdapter(
            onSelected = { shop, position ->
                mAnalytic?.clickShopCard(shop, position + 1)
                viewModel.submitAction(ProductTagAction.ShopSelected(shop))
            },
            onLoading = {
                viewModel.submitAction(ProductTagAction.LoadGlobalSearchShop)
            }
        )
    }
    private lateinit var layoutManager: LinearLayoutManager
    private val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) impressShop()
        }
    }

    private val sortFilterBottomSheet: SortFilterBottomSheet = SortFilterBottomSheet()
    private val sortFilterCallback = object : SortFilterBottomSheet.Callback {
        override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
            applySortFilterModel.apply {
                viewModel.submitAction(
                    ProductTagAction.ApplyShopSortFilter(
                        selectedFilterMapParameter + selectedSortMapParameter
                    )
                )
            }
        }

        override fun getResultCount(mapParameter: Map<String, String>) {
            viewModel.submitAction(ProductTagAction.RequestShopFilterProductCount(mapParameter))
        }
    }

    private val bottomSheetContainer by lazy(LazyThreadSafetyMode.NONE) {
        getParentFragmentByInstance<BottomSheetUnify>()
    }

    private val onRecyclerViewTouchCallback = object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            bottomSheetContainer?.bottomSheet?.updateScrollingChild(rv)

            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGlobalSearchShopTabBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnalytic()
        setupView()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.globalStateShopStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadGlobalSearchShop)
    }

    override fun onPause() {
        super.onPause()
        impressionCoordinator.sendShopImpress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvGlobalSearchShop.removeOnItemTouchListener(onRecyclerViewTouchCallback)
        binding.rvGlobalSearchShop.removeOnScrollListener(scrollListener)
        _binding = null
    }

    private fun setupAnalytic() {
        impressionCoordinator.setInitialData(
            mAnalytic,
            viewModel.selectedTagSource
        )
    }

    private fun setupView() {
        layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                impressShop()
            }
        }

        binding.rvGlobalSearchShop.addOnItemTouchListener(onRecyclerViewTouchCallback)
        binding.rvGlobalSearchShop.addOnScrollListener(scrollListener)
        binding.rvGlobalSearchShop.layoutManager = layoutManager
        binding.rvGlobalSearchShop.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewModel.submitAction(ProductTagAction.SwipeRefreshGlobalSearchShop)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderGlobalSearchShop(it.prevValue?.globalSearchShop, it.value.globalSearchShop)
                renderQuickFilter(it.prevValue?.globalSearchShop, it.value.globalSearchShop)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is ProductTagUiEvent.OpenShopSortFilterBottomSheet -> {
                        sortFilterBottomSheet.show(
                            childFragmentManager,
                            event.param.value as Map<String, String>,
                            event.data,
                            sortFilterCallback,
                        )
                    }
                    is ProductTagUiEvent.SetShopFilterProductCount -> {
                        val text = when(event.result) {
                            is NetworkResult.Success -> {
                                getString(R.string.cc_filter_shop_count_template, event.result.data)
                            }
                            else -> getString(R.string.cc_filter_shop_count_label)
                        }

                        sortFilterBottomSheet.setResultCountText(text)
                    }
                }
            }
        }
    }

    private fun renderGlobalSearchShop(prev: GlobalSearchShopUiState?, curr: GlobalSearchShopUiState) {
        if(prev?.shops == curr.shops &&
            prev.state == curr.state
        ) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr, !binding.swipeRefresh.isRefreshing)
            }
            is PagedState.Success -> {
                binding.swipeRefresh.isRefreshing = false
                binding.sortFilter.showWithCondition(curr.shops.isNotEmpty() || (curr.shops.isEmpty() && curr.param.hasFilterApplied()))
                updateAdapterData(curr, curr.state.hasNextPage)
            }
            is PagedState.Error -> {
                binding.swipeRefresh.isRefreshing = false
                updateAdapterData(curr, false)

                Toaster.build(
                    binding.root,
                    text = getString(R.string.cc_failed_load_shop),
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_LONG,
                    actionText = getString(R.string.feed_content_coba_lagi_text),
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadGlobalSearchShop) }
                ).show()
            }
            else -> {}
        }
    }

    private fun renderQuickFilter(prev: GlobalSearchShopUiState?, curr: GlobalSearchShopUiState) {
        if(prev?.quickFilters == curr.quickFilters) return

        binding.sortFilter.apply {
            resetAllFilters()
            sortFilterItems.removeAllViews()
            addItem(
                curr.quickFilters.map {
                    it.toSortFilterItem(curr.param.isParamFound(it.key, it.value)) {
                        viewModel.submitAction(ProductTagAction.SelectShopQuickFilter(it))
                    }
                } as ArrayList<SortFilterItem>
            )
            textView?.text = getString(R.string.cc_product_tag_filter_label)
            parentListener = {
                viewModel.submitAction(ProductTagAction.OpenShopSortFilterBottomSheet)
            }
            indicatorCounter = curr.param.getFilterCount()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun updateAdapterData(currState: GlobalSearchShopUiState, showLoading: Boolean) {
        val finalShops = buildList {
            if(currState.shops.isEmpty() && currState.state is PagedState.Success)
                add(ShopCardAdapter.Model.EmptyState(currState.param.hasFilterApplied()))
            else
                addAll(currState.shops.map { ShopCardAdapter.Model.Shop(shop = it) })

            if(showLoading) add(ShopCardAdapter.Model.Loading)
        }

        if(binding.rvGlobalSearchShop.isComputingLayout.not())
            adapter.setItemsAndAnimateChanges(finalShops)

        impressShop()
    }

    private fun impressShop() {
        if(this::layoutManager.isInitialized) {
            val visibleProducts = layoutManager.getVisibleItems(adapter)
            if(visibleProducts.isNotEmpty())
                impressionCoordinator.saveShopImpress(visibleProducts)
        }
    }

    companion object {
        const val TAG = "GlobalSearchShopTabFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): GlobalSearchShopTabFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? GlobalSearchShopTabFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                GlobalSearchShopTabFragment::class.java.name
            ) as GlobalSearchShopTabFragment
        }
    }
}