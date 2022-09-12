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
import com.tokopedia.content.common.databinding.FragmentLastPurchasedProductBinding
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.util.extension.isNetworkError
import com.tokopedia.content.common.producttag.util.extension.isProductFound
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class LastPurchasedProductFragment @Inject constructor(): BaseProductTagChildFragment() {

    override fun getScreenName(): String = "LastPurchasedProductFragment"

    private var _binding: FragmentLastPurchasedProductBinding? = null
    private val binding: FragmentLastPurchasedProductBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ProductTagCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        /** do nothing */
        ProductTagCardAdapter(
            onSelected = { product, position ->
                mAnalytic?.clickProductCard(
                    viewModel.selectedTagSource,
                    product,
                    position,
                    isEntryPoint = true,
                )
                viewModel.submitAction(ProductTagAction.ProductSelected(product))
             },
            onLoading = { /** do nothing */ }
        )
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
        _binding = FragmentLastPurchasedProductBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.lastPurchasedProductStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadLastPurchasedProduct)
        setupView()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvLastPurchasedProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL,)
        binding.rvLastPurchasedProduct.itemAnimator = null
        binding.rvLastPurchasedProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_last_purchase_product))
            errorTitle.text = getString(R.string.cc_no_last_purchased_product_title)
            errorDescription.text = getString(R.string.cc_no_last_purchased_product_desc)
            errorAction.gone()
            errorSecondaryAction.gone()
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderLastPurchasedProducts(it.prevValue, it.value)
            }
        }
    }

    private fun renderLastPurchasedProducts(prev: ProductTagUiState?, curr: ProductTagUiState) {

        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map { product ->
                if(viewModel.isMultipleSelectionProduct) {
                    ProductTagCardAdapter.Model.ProductWithCheckbox(
                        product = product,
                        isSelected = curr.selectedProduct.isProductFound(product),
                    )
                }
                else ProductTagCardAdapter.Model.Product(product = product)
            } + if(hasNextPage) listOf(ProductTagCardAdapter.Model.Loading) else emptyList()

            if(binding.rvLastPurchasedProduct.isComputingLayout.not())
                adapter.setItemsAndAnimateChanges(finalProducts)

            binding.rvLastPurchasedProduct.show()
            binding.globalError.hide()
        }

        if(prev?.lastPurchasedProduct?.products == curr.lastPurchasedProduct.products &&
            prev.lastPurchasedProduct.state == curr.lastPurchasedProduct.state &&
            prev.selectedProduct == curr.selectedProduct
        ) return

        val currProducts = curr.lastPurchasedProduct.products
        val currState = curr.lastPurchasedProduct.state

        when(currState) {
            is PagedState.Loading -> {
                updateAdapterData(currProducts, true)
            }
            is PagedState.Success -> {
                if(currProducts.isEmpty()) {
                    binding.rvLastPurchasedProduct.hide()
                    binding.globalError.show()
                }
                else updateAdapterData(currProducts, false)

                binding.tickerInfo.showWithCondition(curr.lastPurchasedProduct.isCoachmarkShown)
                binding.tickerInfo.setTextDescription(curr.lastPurchasedProduct.coachmark)
            }
            is PagedState.Error -> {
                binding.tickerInfo.hide()
                binding.rvLastPurchasedProduct.hide()
                binding.globalError.apply {
                    setType(
                        if(currState.error.isNetworkError) GlobalError.NO_CONNECTION
                        else GlobalError.SERVER_ERROR
                    )

                    setActionClickListener {
                        viewModel.submitAction(ProductTagAction.LoadLastPurchasedProduct)
                    }
                    show()
                }
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "LastPurchasedProductFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ) : Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): LastPurchasedProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? LastPurchasedProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                LastPurchasedProductFragment::class.java.name
            ) as LastPurchasedProductFragment
        }
    }
}