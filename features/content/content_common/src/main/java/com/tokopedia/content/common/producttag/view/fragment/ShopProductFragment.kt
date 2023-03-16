package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.FragmentShopProductBinding
import com.tokopedia.content.common.producttag.analytic.coordinator.ProductImpressionCoordinator
import com.tokopedia.content.common.producttag.util.extension.getVisibleItems
import com.tokopedia.content.common.producttag.util.extension.isProductFound
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.util.hideKeyboard
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ShopProductFragment @Inject constructor(
    private val impressionCoordinator: ProductImpressionCoordinator
) : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "ShopProductFragment"

    private var _binding: FragmentShopProductBinding? = null
    private val binding: FragmentShopProductBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ProductTagCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ProductTagCardAdapter(
            onSelected = { product, position ->
                mAnalytic?.clickProductCardOnShop(product, position)
                viewModel.submitAction(ProductTagAction.ProductSelected(product))
            },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadShopProduct) }
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
        _binding = FragmentShopProductBinding.inflate(
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

        viewModel.submitAction(ProductTagAction.LoadShopProduct)
    }

    override fun onPause() {
        super.onPause()
        impressionCoordinator.sendProductImpress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvShopProduct.removeOnScrollListener(scrollListener)
        _binding = null
    }

    private fun setupAnalytic() {
        impressionCoordinator.setInitialData(
            mAnalytic,
            viewModel.selectedTagSource,
            isEntryPoint = false
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

        binding.rvShopProduct.addOnScrollListener(scrollListener)
        binding.rvShopProduct.layoutManager = layoutManager
        binding.rvShopProduct.itemAnimator = null
        binding.rvShopProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_shop_product))
            errorAction.gone()
            errorSecondaryAction.gone()
        }

        binding.sbShopProduct.searchBarPlaceholder = requireContext().getString(
            R.string.cc_product_tag_search_hint_template,
            viewModel.selectedShop.shopName
        )
        binding.sbShopProduct.searchBarContainer.fitsSystemWindows = false
        binding.sbShopProduct.searchBarTextField.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mAnalytic?.clickSearchBarOnShop()
            }
            false
        }

        binding.sbShopProduct.searchBarTextField.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.sbShopProduct.searchBarTextField.text.toString()
                submitQuery(query)

                true
            } else {
                false
            }
        }

        binding.sbShopProduct.clearListener = {
            submitQuery("")
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderShopProducts(it.prevValue, it.value)
            }
        }
    }

    private fun renderShopProducts(prev: ProductTagUiState?, curr: ProductTagUiState) {
        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map { product ->
                if (viewModel.isMultipleSelectionProduct) {
                    ProductTagCardAdapter.Model.ProductWithCheckbox(
                        product = product,
                        isSelected = curr.selectedProduct.isProductFound(product)
                    )
                } else {
                    ProductTagCardAdapter.Model.Product(product = product)
                }
            } + if (hasNextPage) listOf(ProductTagCardAdapter.Model.Loading) else emptyList()

            if (binding.rvShopProduct.isComputingLayout.not()) {
                adapter.setItemsAndAnimateChanges(finalProducts)
            }

            binding.rvShopProduct.show()
            binding.globalError.hide()

            impressProduct()
        }

        if (prev?.shopProduct?.products == curr.shopProduct.products &&
            prev.shopProduct.state == curr.shopProduct.state &&
            prev.selectedProduct == curr.selectedProduct
        ) {
            return
        }

        val currProducts = curr.shopProduct.products
        val currState = curr.shopProduct.state

        when (currState) {
            is PagedState.Loading -> {
                updateAdapterData(currProducts, true)
            }
            is PagedState.Success -> {
                if (currProducts.isEmpty()) {
                    binding.rvShopProduct.hide()
                    showEmptyState(curr.shopProduct.hasFilter())
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
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadShopProduct) }
                ).show()
            }
            else -> {}
        }
    }

    private fun submitQuery(query: String) {
        viewModel.submitAction(ProductTagAction.SearchShopProduct(query))

        binding.sbShopProduct.searchBarTextField.apply {
            clearFocus()
            hideKeyboard()
        }
    }

    private fun showEmptyState(hasFilter: Boolean) {
        binding.globalError.apply {
            errorTitle.text = getString(
                if (hasFilter) {
                    R.string.cc_no_shop_product_filter_title
                } else {
                    R.string.cc_no_shop_product_title
                }
            )
            errorDescription.text = getString(
                if (hasFilter) {
                    R.string.cc_no_shop_product_filter_desc
                } else {
                    R.string.cc_no_shop_product_desc
                }
            )
            show()
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
        const val TAG = "ShopProductFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ShopProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ShopProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ShopProductFragment::class.java.name
            ) as ShopProductFragment
        }
    }
}
