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
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.GlobalSearchProductUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchProductTabFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "GlobalSearchProductTabFragment"

    private var _binding: FragmentGlobalSearchProductTabBinding? = null
    private val binding: FragmentGlobalSearchProductTabBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private val adapter: ProductTagCardAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        ProductTagCardAdapter(
            onSelected = { viewModel.submitAction(ProductTagAction.ProductSelected(it)) },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadGlobalSearchProduct) }
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

        if(viewModel.globalStateProductStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadGlobalSearchProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvGlobalSearchProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL,)
        binding.rvGlobalSearchProduct.adapter = adapter
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderGlobalSearchProduct(it.prevValue?.globalSearchProduct, it.value.globalSearchProduct)
            }
        }
    }

    private fun renderGlobalSearchProduct(prev: GlobalSearchProductUiState?, curr: GlobalSearchProductUiState) {

        fun updateAdapterData(products: List<ProductUiModel>, hasNextPage: Boolean) {
            val finalProducts = products.map {
                ProductTagCardAdapter.Model.Product(product = it)
            } + if(hasNextPage) listOf(ProductTagCardAdapter.Model.Loading) else emptyList()

            if(binding.rvGlobalSearchProduct.isComputingLayout.not())
                adapter.setItemsAndAnimateChanges(finalProducts)

            binding.rvGlobalSearchProduct.show()
            binding.globalError.hide()
        }

        if(prev?.products == curr.products && prev.state == curr.state) return

        when(curr.state) {
            is PagedState.Loading -> {
                updateAdapterData(curr.products, true)
            }
            is PagedState.Success -> {
                if(curr.products.isEmpty()) {
                    binding.rvGlobalSearchProduct.hide()
                    binding.globalError.show()
                }
                else updateAdapterData(curr.products, curr.state.hasNextPage)
            }
            is PagedState.Error -> {
                updateAdapterData(curr.products, false)

                /** TODO: gonna handle this */
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