package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentLastPurchasedProductBinding
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.LastPurchasedProductAdapter
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.LastPurchasedProductUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class LastPurchasedProductFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "LastPurchasedProductFragment"

    private var _binding: FragmentLastPurchasedProductBinding? = null
    private val binding: FragmentLastPurchasedProductBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: LastPurchasedProductAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        LastPurchasedProductAdapter(
            onSelected = { viewModel.submitAction(ProductTagAction.ProductSelected(it)) },
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
                renderLastPurchasedProducts(it.prevValue?.lastPurchasedProduct, it.value.lastPurchasedProduct)
            }
        }
    }

    private fun renderLastPurchasedProducts(prev: LastPurchasedProductUiState?, curr: LastPurchasedProductUiState) {

        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map {
                LastPurchasedProductAdapter.Model.Product(product = it)
            } + if(hasNextPage) listOf(LastPurchasedProductAdapter.Model.Loading) else emptyList()

            if(binding.rvLastPurchasedProduct.isComputingLayout.not())
                adapter.setItemsAndAnimateChanges(finalProducts)

            binding.rvLastPurchasedProduct.show()
            binding.globalError.hide()
        }

        if(prev?.products == curr.products && prev.state == curr.state) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr.products, true)
            }
            is PagedState.Success -> {
                if(curr.products.isEmpty()) {
                    binding.rvLastPurchasedProduct.hide()
                    binding.globalError.show()
                }
                else updateAdapterData(curr.products, false)

                binding.tickerInfo.showWithCondition(curr.isCoachmarkShown)
                binding.tickerInfo.setTextDescription(curr.coachmark)
            }
            is PagedState.Error -> {
                binding.tickerInfo.hide()
                updateAdapterData(curr.products, false)

                Toaster.build(
                    binding.root,
                    text = getString(R.string.cc_failed_load_product),
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_LONG,
                    actionText = getString(R.string.feed_content_coba_lagi_text),
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadLastPurchasedProduct) }
                ).show()
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "LastPurchasedProductFragment"

        fun getFragment(
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