package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentGlobalSearchShopTabBinding
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.createpost.producttag.view.decoration.ShopCardItemDecoration
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.NetworkResult
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.GlobalSearchProductUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.GlobalSearchShopUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchShopTabFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "GlobalSearchShopTabFragment"

    private var _binding: FragmentGlobalSearchShopTabBinding? = null
    private val binding: FragmentGlobalSearchShopTabBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ShopCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ShopCardAdapter(
            onSelected = { viewModel.submitAction(ProductTagAction.ShopSelected(it)) },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadGlobalSearchShop) }
        )
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
        setupView()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.globalStateShopStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadGlobalSearchShop)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvGlobalSearchShop.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGlobalSearchShop.addItemDecoration(ShopCardItemDecoration(requireContext()))
        binding.rvGlobalSearchShop.adapter = adapter
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
        if(prev?.shops == curr.shops && prev.state == curr.state) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr, true)
            }
            is PagedState.Success -> {
                updateAdapterData(curr, curr.state.hasNextPage)
            }
            is PagedState.Error -> {
                updateAdapterData(curr, false)

                /** TODO: gonna handle this */
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
    private fun updateAdapterData(state: GlobalSearchShopUiState, hasNextPage: Boolean) {
        val finalShops = buildList {
            if(state.shops.isEmpty() && !hasNextPage) {
                if(state.param.hasFilterApplied()) {
                    add(ShopCardAdapter.Model.EmptyState(true) {
                        viewModel.submitAction(ProductTagAction.ResetShopFilter)
                    })
                    binding.sortFilter.show()
                }
                else {
                    add(ShopCardAdapter.Model.EmptyState(false) {
                        viewModel.submitAction(ProductTagAction.OpenAutoCompletePage)
                    })
                    add(ShopCardAdapter.Model.Divider)
                    add(ShopCardAdapter.Model.RecommendationTitle(getString(R.string.cc_shop_recommendation_title)))
                    binding.sortFilter.hide()
                }
            }
            else {
                addAll(state.shops.map { ShopCardAdapter.Model.Shop(shop = it) })
                if(hasNextPage) add(ShopCardAdapter.Model.Loading)

                binding.sortFilter.show()
            }
        }

        if(binding.rvGlobalSearchShop.isComputingLayout.not())
            adapter.setItemsAndAnimateChanges(finalShops)
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