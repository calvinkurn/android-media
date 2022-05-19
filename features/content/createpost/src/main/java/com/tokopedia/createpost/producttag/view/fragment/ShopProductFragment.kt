package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentShopProductBinding
import com.tokopedia.createpost.producttag.util.extension.hideKeyboard
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.ShopProductUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ShopProductFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "ShopProductFragment"

    private var _binding: FragmentShopProductBinding? = null
    private val binding: FragmentShopProductBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ProductTagCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ProductTagCardAdapter(
            onSelected = { viewModel.submitAction(ProductTagAction.ProductSelected(it)) },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadShopProduct) }
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
        _binding = FragmentShopProductBinding.inflate(
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

        viewModel.submitAction(ProductTagAction.LoadShopProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvShopProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.rvShopProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_shop_product))
            errorAction.gone()
            errorSecondaryAction.gone()
        }

        binding.sbShopProduct.searchBarTextField.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.sbShopProduct.searchBarTextField.text.toString()
                viewModel.submitAction(ProductTagAction.SearchShopProduct(query))

                textView.apply {
                    clearFocus()
                    hideKeyboard()
                }
                true
            }
            else false
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderShopProducts(it.prevValue?.shopProduct, it.value.shopProduct)
            }
        }
    }

    private fun renderShopProducts(prev: ShopProductUiState?, curr: ShopProductUiState) {

        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map {
                ProductTagCardAdapter.Model.Product(product = it)
            } + if(hasNextPage) listOf(ProductTagCardAdapter.Model.Loading) else emptyList()

            if(binding.rvShopProduct.isComputingLayout.not())
                adapter.setItemsAndAnimateChanges(finalProducts)

            binding.rvShopProduct.show()
            binding.globalError.hide()
        }

        if(prev?.products == curr.products && prev.state == curr.state) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr.products, true)
            }
            is PagedState.Success -> {
                if(curr.products.isEmpty()) {
                    binding.rvShopProduct.hide()
                    showEmptyState(curr.hasFilter())
                }
                else updateAdapterData(curr.products, curr.state.hasNextPage)
            }
            is PagedState.Error -> {
                updateAdapterData(curr.products, false)

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

    private fun showEmptyState(hasFilter: Boolean) {
        binding.globalError.apply {
            errorTitle.text = getString(
                if(hasFilter) R.string.cc_no_shop_product_filter_title
                else R.string.cc_no_shop_product_title
            )
            errorDescription.text = getString(
                if(hasFilter) R.string.cc_no_shop_product_filter_desc
                else R.string.cc_no_shop_product_desc
            )
            show()
        }
    }

    companion object {
        const val TAG = "ShopProductFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ) : Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ShopProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ShopProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ShopProductFragment::class.java.name
            ) as ShopProductFragment
        }
    }
}