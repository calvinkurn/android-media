package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.FragmentMyShopProductBinding
import com.tokopedia.content.common.producttag.analytic.coordinator.ProductImpressionCoordinator
import com.tokopedia.content.common.producttag.util.extension.getVisibleItems
import com.tokopedia.content.common.producttag.util.extension.isProductFound
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.content.common.producttag.view.bottomsheet.SortBottomSheet
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SortUiModel
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.MyShopProductUiState
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class MyShopProductFragment @Inject constructor(
    private val userSession: UserSessionInterface,
    private val impressionCoordinator: ProductImpressionCoordinator
) : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "MyShopProductFragment"

    private var _binding: FragmentMyShopProductBinding? = null
    private val binding: FragmentMyShopProductBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: MyShopProductAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        MyShopProductAdapter(
            onSelected = { product, position ->
                mAnalytic?.clickProductCard(
                    viewModel.selectedTagSource,
                    product,
                    position,
                    isEntryPoint = viewModel.myShopQuery.isEmpty()
                )
                viewModel.submitAction(ProductTagAction.ProductSelected(product))
            },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadMyShopProduct) }
        )
    }
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) impressProduct()
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
        _binding = FragmentMyShopProductBinding.inflate(
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

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is SortBottomSheet -> {
                childFragment.setListener(object : SortBottomSheet.Listener {
                    override fun onSortSelected(sort: SortUiModel) {
                        viewModel.submitAction(ProductTagAction.ApplyMyShopSort(sort))
                    }
                })

                childFragment.setData(viewModel.myShopSortList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.myShopStateUnknown) {
            viewModel.submitAction(ProductTagAction.LoadMyShopProduct)
        }
    }

    override fun onPause() {
        super.onPause()
        impressionCoordinator.sendProductImpress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvMyShopProduct.removeOnScrollListener(scrollListener)
        _binding = null
    }

    private fun setupAnalytic() {
        impressionCoordinator.setInitialData(
            mAnalytic,
            viewModel.selectedTagSource,
            isEntryPoint = true
        )
    }

    @Suppress("ClickableViewAccessibility")
    private fun setupView() {
        layoutManager = object : StaggeredGridLayoutManager(2, RecyclerView.VERTICAL) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                impressProduct()
            }
        }

        binding.rvMyShopProduct.addOnScrollListener(scrollListener)
        binding.rvMyShopProduct.layoutManager = layoutManager
        binding.rvMyShopProduct.itemAnimator = null
        binding.rvMyShopProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_shop_product))
            errorAction.gone()
            errorSecondaryAction.gone()
        }

        binding.sbShopProduct.searchBarPlaceholder = requireContext().getString(
            R.string.cc_product_tag_search_hint_template,
            userSession.shopName
        )
        binding.sbShopProduct.searchBarContainer.fitsSystemWindows = false
        binding.sbShopProduct.searchBarTextField.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mAnalytic?.clickSearchBar(viewModel.selectedTagSource)
            }
            false
        }

        binding.sbShopProduct.searchBarTextField.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.sbShopProduct.searchBarTextField.text.toString()

                impressionCoordinator.sendProductImpress()
                impressionCoordinator.setInitialData(
                    mAnalytic,
                    viewModel.selectedTagSource,
                    isEntryPoint = query.isEmpty()
                )

                submitQuery(query)

                true
            } else {
                false
            }
        }

        binding.sbShopProduct.clearListener = {
            submitQuery("")
        }

        binding.chipSort.apply {
            setChevronClickListener { submitActionOpenSortBottomSheet() }
            setOnClickListener { submitActionOpenSortBottomSheet() }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderMyShopProducts(it.prevValue, it.value)
                renderChip(it.prevValue?.myShopProduct, it.value.myShopProduct)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    ProductTagUiEvent.OpenMyShopSortBottomSheet -> {
                        SortBottomSheet.getFragment(
                            childFragmentManager,
                            requireActivity().classLoader
                        ).showNow(childFragmentManager)
                    }
                }
            }
        }
    }

    private fun renderMyShopProducts(prev: ProductTagUiState?, curr: ProductTagUiState) {
        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map { product ->
                if (viewModel.isMultipleSelectionProduct) {
                    MyShopProductAdapter.Model.ProductWithCheckbox(
                        product = product,
                        isSelected = curr.selectedProduct.isProductFound(product)
                    )
                } else {
                    MyShopProductAdapter.Model.Product(product = product)
                }
            } + if (hasNextPage) listOf(MyShopProductAdapter.Model.Loading) else emptyList()

            if (binding.rvMyShopProduct.isComputingLayout.not()) {
                adapter.setItemsAndAnimateChanges(finalProducts)
            }

            binding.rvMyShopProduct.show()
            binding.globalError.hide()
            impressProduct()
        }

        if (prev?.myShopProduct?.products == curr.myShopProduct.products &&
            prev.myShopProduct.state == curr.myShopProduct.state &&
            prev.selectedProduct == curr.selectedProduct
        ) {
            return
        }

        val currState = curr.myShopProduct.state
        val currProducts = curr.myShopProduct.products

        when (currState) {
            is PagedState.Loading -> {
                updateAdapterData(currProducts, true)
            }
            is PagedState.Success -> {
                if (currProducts.isEmpty()) {
                    binding.rvMyShopProduct.hide()
                    showEmptyState(curr.myShopProduct.hasFilter())
                } else {
                    updateAdapterData(currProducts, currState.hasNextPage)
                }
            }
            is PagedState.Error -> {
                updateAdapterData(currProducts, false)

                Toaster.build(
                    binding.root,
                    text = getString(R.string.cc_failed_load_product),
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_LONG,
                    actionText = getString(R.string.feed_content_coba_lagi_text),
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadMyShopProduct) }
                ).show()
            }
            else -> {}
        }
    }

    private fun renderChip(prev: MyShopProductUiState?, curr: MyShopProductUiState) {
        if (prev?.param == curr.param) return

        val selectedSort = viewModel.myShopSortList.firstOrNull { it.isSelected }

        binding.chipSort.chipText = selectedSort?.text ?: getString(R.string.cc_product_tag_sort_label)
        binding.chipSort.chipType = if (selectedSort != null) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }

    private fun submitQuery(query: String) {
        clearSearchFocus()
        viewModel.submitAction(ProductTagAction.SearchMyShopProduct(query))
    }

    private fun submitActionOpenSortBottomSheet() {
        clearSearchFocus()
        viewModel.submitAction(ProductTagAction.OpenMyShopSortBottomSheet)
    }

    private fun showEmptyState(hasFilter: Boolean) {
        binding.globalError.apply {
            errorTitle.text = getString(
                if (hasFilter) {
                    R.string.cc_no_my_shop_product_filter_title
                } else {
                    R.string.cc_no_my_shop_product_title
                }
            )
            errorDescription.text = getString(
                if (hasFilter) {
                    R.string.cc_no_my_shop_product_filter_desc
                } else {
                    R.string.cc_no_my_shop_product_desc
                }
            )
            show()
        }
    }

    private fun clearSearchFocus() {
        binding.sbShopProduct.searchBarTextField.apply {
            clearFocus()
            hideKeyboard()
        }
    }

    private fun impressProduct() {
        if (this::layoutManager.isInitialized) {
            val visibleProducts = layoutManager.getVisibleItems(adapter, viewModel.isMultipleSelectionProduct)
            if (visibleProducts.isNotEmpty()) {
                impressionCoordinator.saveProductImpress(visibleProducts)
            }
        }
    }

    companion object {
        const val TAG = "MyShopProductFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): MyShopProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? MyShopProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                MyShopProductFragment::class.java.name
            ) as MyShopProductFragment
        }
    }
}
