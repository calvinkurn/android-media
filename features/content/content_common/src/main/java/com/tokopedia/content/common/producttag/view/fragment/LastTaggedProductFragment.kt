package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.FragmentLastTaggedProductBinding
import com.tokopedia.content.common.producttag.analytic.coordinator.ProductImpressionCoordinator
import com.tokopedia.content.common.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.content.common.producttag.util.extension.getVisibleItems
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.state.LastTaggedProductUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class LastTaggedProductFragment @Inject constructor(
    private val analytic: ProductTagAnalytic,
    private val impressionCoordinator: ProductImpressionCoordinator,
) : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "LastTaggedProductFragment"

    private var _binding: FragmentLastTaggedProductBinding? = null
    private val binding: FragmentLastTaggedProductBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ProductTagCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ProductTagCardAdapter(
            onSelected = { product, position ->
                analytic.clickProductCard(
                    viewModel.selectedTagSource,
                    product,
                    position,
                    isEntryPoint = true,
                )
                viewModel.submitAction(ProductTagAction.ProductSelected(product))
            },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct) }
        )
    }
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private val scrollListener = object: RecyclerView.OnScrollListener(){
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
        _binding = FragmentLastTaggedProductBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.lastTaggedProductStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct)

        setupAnalytic()
        setupView()
        setupObserver()
    }

    override fun onPause() {
        super.onPause()
        impressionCoordinator.sendProductImpress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvLastTaggedProduct.removeOnScrollListener(scrollListener)
        _binding = null
    }

    private fun setupAnalytic() {
        impressionCoordinator.setInitialData(
            viewModel.selectedTagSource,
            isEntryPoint = true,
        )
    }

    private fun setupView() {
        layoutManager = object : StaggeredGridLayoutManager(2, RecyclerView.VERTICAL) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                impressProduct()
            }
        }

        binding.rvLastTaggedProduct.addOnScrollListener(scrollListener)
        binding.rvLastTaggedProduct.layoutManager = layoutManager
        binding.rvLastTaggedProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_last_tag_product))
            errorTitle.text = getString(R.string.cc_no_product_tag_title)
            errorDescription.text = getString(R.string.cc_no_product_tag_desc)
            errorAction.gone()
            errorSecondaryAction.gone()
        }

        binding.clSearch.setOnClickListener {
            analytic.clickSearchBar(viewModel.selectedTagSource)
            viewModel.submitAction(ProductTagAction.OpenAutoCompletePage)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderLastTaggedProducts(it.prevValue?.lastTaggedProduct, it.value.lastTaggedProduct)
            }
        }
    }

    private fun renderLastTaggedProducts(prev: LastTaggedProductUiState?, curr: LastTaggedProductUiState) {

        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map {
                ProductTagCardAdapter.Model.Product(product = it)
            } + if(hasNextPage) listOf(ProductTagCardAdapter.Model.Loading) else emptyList()

            if(binding.rvLastTaggedProduct.isComputingLayout.not())
                adapter.setItemsAndAnimateChanges(finalProducts)

            binding.rvLastTaggedProduct.show()
            binding.globalError.hide()

            impressProduct()
        }

        if(prev?.products == curr.products && prev.state == curr.state) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr.products, true)
            }
            is PagedState.Success -> {
                if(curr.products.isEmpty()) {
                    binding.rvLastTaggedProduct.hide()
                    binding.globalError.show()
                }
                else updateAdapterData(curr.products, curr.state.hasNextPage)
            }
            is PagedState.Error -> {
                updateAdapterData(curr.products, false)

                Toaster.build(
                    binding.root,
                    text = getString(R.string.cc_failed_load_product),
                    type = TYPE_ERROR,
                    duration = LENGTH_LONG,
                    actionText = getString(R.string.feed_content_coba_lagi_text),
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct) }
                ).show()
            }
        }
    }

    private fun impressProduct() {
        if(this::layoutManager.isInitialized) {
            val visibleProducts = layoutManager.getVisibleItems(adapter)
            if(visibleProducts.isNotEmpty())
                impressionCoordinator.saveProductImpress(visibleProducts)
        }
    }

    companion object {
        const val TAG = "LastTaggedProductFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ) : Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): LastTaggedProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? LastTaggedProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                LastTaggedProductFragment::class.java.name
            ) as LastTaggedProductFragment
        }
    }
}