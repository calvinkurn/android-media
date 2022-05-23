package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentGlobalSearchProductTabBinding
import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.decoration.ProductTagItemDecoration
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.NetworkResult
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.GlobalSearchProductUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import com.tokopedia.filter.common.helper.getSortFilterCount
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchProductTabFragment @Inject constructor(
    private val analytic: ProductTagAnalytic,
): BaseProductTagChildFragment() {

    override fun getScreenName(): String = "GlobalSearchProductTabFragment"

    private var _binding: FragmentGlobalSearchProductTabBinding? = null
    private val binding: FragmentGlobalSearchProductTabBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ProductTagCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ProductTagCardAdapter(
            onSelected = { product, position ->
                analytic.clickProductCard(
                    viewModel.selectedTagSource,
                    product,
                    position,
                    true
                )
                viewModel.submitAction(ProductTagAction.ProductSelected(product))
            },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadGlobalSearchProduct) },
        )
    }
    private val sortFilterBottomSheet: SortFilterBottomSheet = SortFilterBottomSheet()
    private val sortFilterCallback = object : SortFilterBottomSheet.Callback {
        override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
            applySortFilterModel.apply {
                viewModel.submitAction(
                    ProductTagAction.ApplyProductSortFilter(
                        selectedFilterMapParameter + selectedSortMapParameter
                    )
                )
            }
        }

        override fun getResultCount(mapParameter: Map<String, String>) {
            viewModel.submitAction(ProductTagAction.RequestProductFilterProductCount(mapParameter))
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
        _binding = FragmentGlobalSearchProductTabBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.globalStateProductStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadGlobalSearchProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvGlobalSearchProduct.addItemDecoration(ProductTagItemDecoration(requireContext()))
        binding.rvGlobalSearchProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL,)
        binding.rvGlobalSearchProduct.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewModel.submitAction(ProductTagAction.SwipeRefreshGlobalSearchProduct)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderGlobalSearchProduct(it.prevValue?.globalSearchProduct, it.value.globalSearchProduct)
                renderQuickFilter(it.prevValue?.globalSearchProduct, it.value.globalSearchProduct)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is ProductTagUiEvent.OpenProductSortFilterBottomSheet -> {
                        sortFilterBottomSheet.show(
                            childFragmentManager,
                            event.param.value as Map<String, String>,
                            event.data,
                            sortFilterCallback,
                        )
                    }
                    is ProductTagUiEvent.SetProductFilterProductCount -> {
                        val text = when(event.result) {
                            is NetworkResult.Success -> {
                                getString(R.string.cc_filter_product_count_template, event.result.data)
                            }
                            else -> getString(R.string.cc_filter_product_count_label)
                        }

                        sortFilterBottomSheet.setResultCountText(text)
                    }
                }
            }
        }
    }

    private fun renderGlobalSearchProduct(prev: GlobalSearchProductUiState?, curr: GlobalSearchProductUiState) {
        if(prev?.products == curr.products &&
            prev.state == curr.state &&
            prev.ticker == curr.ticker &&
            prev.suggestion == curr.suggestion
        ) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr, !binding.swipeRefresh.isRefreshing)
            }
            is PagedState.Success -> {
                binding.swipeRefresh.isRefreshing = false
                binding.sortFilter.showWithCondition(curr.products.isNotEmpty() || (curr.products.isEmpty() && curr.param.hasFilterApplied()))
                updateAdapterData(curr, curr.state.hasNextPage)
            }
            is PagedState.Error -> {
                binding.swipeRefresh.isRefreshing = false
                updateAdapterData(curr,false)

                Toaster.build(
                    binding.root,
                    text = getString(R.string.cc_failed_load_product),
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_LONG,
                    actionText = getString(R.string.feed_content_coba_lagi_text),
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadGlobalSearchProduct) }
                ).show()
            }
            else -> {}
        }
    }

    private fun renderQuickFilter(prev: GlobalSearchProductUiState?, curr: GlobalSearchProductUiState) {
        if(prev?.quickFilters == curr.quickFilters) return

        binding.sortFilter.apply {
            resetAllFilters()
            sortFilterItems.removeAllViews()
            addItem(
                curr.quickFilters.map {
                    it.toSortFilterItem(curr.param.isParamFound(it.key, it.value)) {
                        viewModel.submitAction(ProductTagAction.SelectProductQuickFilter(it))
                    }
                } as ArrayList<SortFilterItem>
            )
            textView?.text = getString(R.string.cc_product_tag_filter_label)
            parentListener = {
                viewModel.submitAction(ProductTagAction.OpenProductSortFilterBottomSheet)
            }
            indicatorCounter = curr.param.getFilterCount()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun updateAdapterData(currState: GlobalSearchProductUiState, showLoading: Boolean) {
        val finalProducts = buildList {
            if(currState.suggestion.isNotEmpty()) add(ProductTagCardAdapter.Model.Suggestion(text = currState.suggestion))
            if(currState.ticker.text.isNotEmpty())
                add(
                    ProductTagCardAdapter.Model.Ticker(
                        text = currState.ticker.text,
                        onTickerClicked = { viewModel.submitAction(ProductTagAction.TickerClicked) },
                        onTickerClosed = { viewModel.submitAction(ProductTagAction.CloseTicker) },
                    )
                )

            if(currState.products.isEmpty() && currState.state is PagedState.Success)
                add(ProductTagCardAdapter.Model.EmptyState(currState.param.hasFilterApplied()))
            else
                addAll(currState.products.map { ProductTagCardAdapter.Model.Product(product = it) })


            if(showLoading) add(ProductTagCardAdapter.Model.Loading)
        }

        if(binding.rvGlobalSearchProduct.isComputingLayout.not())
            adapter.setItemsAndAnimateChanges(finalProducts)
    }

    companion object {
        const val TAG = "GlobalSearchProductTabFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): GlobalSearchProductTabFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? GlobalSearchProductTabFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                GlobalSearchProductTabFragment::class.java.name
            ) as GlobalSearchProductTabFragment
        }
    }
}