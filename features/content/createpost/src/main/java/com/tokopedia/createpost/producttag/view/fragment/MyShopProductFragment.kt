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
import com.tokopedia.createpost.createpost.databinding.FragmentMyShopProductBinding
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.MyShopProductUiState
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
class MyShopProductFragment : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "MyShopProductFragment"

    private var _binding: FragmentMyShopProductBinding? = null
    private val binding: FragmentMyShopProductBinding
        get() = _binding!!

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var viewModel: ProductTagViewModel
    private val adapter: MyShopProductAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        MyShopProductAdapter(
            onSelected = { viewModel.submitAction(ProductTagAction.ProductSelected(it)) },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadMyShopProduct) }
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
        _binding = FragmentMyShopProductBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.myShopStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadMyShopProduct)
        setupView()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvMyShopProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL,)
        binding.rvMyShopProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_my_shop_product))
            errorTitle.text = getString(R.string.cc_no_my_shop_product_title)
            errorDescription.text = getString(R.string.cc_no_my_shop_product_desc)
            errorAction.gone()
            errorSecondaryAction.gone()
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderMyShopProducts(it.prevValue?.myShopProduct, it.value.myShopProduct)
            }
        }
    }

    private fun renderMyShopProducts(prev: MyShopProductUiState?, curr: MyShopProductUiState) {

        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map {
                MyShopProductAdapter.Model.Product(product = it)
            } + if(hasNextPage) listOf(MyShopProductAdapter.Model.Loading) else emptyList()

            if(binding.rvMyShopProduct.isComputingLayout.not())
                adapter.setItemsAndAnimateChanges(finalProducts)

            binding.rvMyShopProduct.show()
            binding.globalError.hide()
        }

        if(prev?.products == curr.products && prev.state == curr.state) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr.products, true)
            }
            is PagedState.Success -> {
                if(curr.products.isEmpty()) {
                    binding.rvMyShopProduct.hide()
                    binding.globalError.show()
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
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct) }
                ).show()
            }
            else -> {}
        }
    }

    fun setViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }

    companion object {
        const val TAG = "MyShopProductFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): MyShopProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? MyShopProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                MyShopProductFragment::class.java.name
            ) as MyShopProductFragment
        }
    }
}