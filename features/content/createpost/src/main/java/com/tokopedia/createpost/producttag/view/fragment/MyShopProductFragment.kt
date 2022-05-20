package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.createpost.databinding.FragmentMyShopProductBinding
import com.tokopedia.createpost.producttag.util.extension.hideKeyboard
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.bottomsheet.SortBottomSheet
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.SortUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.MyShopProductUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class MyShopProductFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "MyShopProductFragment"

    private var _binding: FragmentMyShopProductBinding? = null
    private val binding: FragmentMyShopProductBinding
        get() = _binding!!

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
        setupView()
        setupObserver()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when(childFragment) {
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
        if(viewModel.myShopStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadMyShopProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvMyShopProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.rvMyShopProduct.adapter = adapter

        binding.globalError.apply {
            errorIllustration.loadImage(getString(R.string.img_no_shop_product))
            errorAction.gone()
            errorSecondaryAction.gone()
        }

        binding.sbShopProduct.searchBarTextField.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.sbShopProduct.searchBarTextField.text.toString()
                submitQuery(query)

                true
            }
            else false
        }

        binding.sbShopProduct.clearListener = {
            submitQuery("")
        }

        binding.chipSort.apply {
            setChevronClickListener { viewModel.submitAction(ProductTagAction.OpenMyShopSortBottomSheet) }
            setOnClickListener { viewModel.submitAction(ProductTagAction.OpenMyShopSortBottomSheet) }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderMyShopProducts(it.prevValue?.myShopProduct, it.value.myShopProduct)
                renderChip(it.prevValue?.myShopProduct, it.value.myShopProduct)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
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
                    clickListener = { viewModel.submitAction(ProductTagAction.LoadMyShopProduct) }
                ).show()
            }
            else -> {}
        }
    }

    private fun renderChip(prev: MyShopProductUiState?, curr: MyShopProductUiState) {
        if(prev?.param == curr.param) return

        val selectedSort = curr.sorts.firstOrNull {
            curr.param.isParamFound(it.key, it.value)
        }

        binding.chipSort.chipText = selectedSort?.text ?: getString(R.string.cc_product_tag_sort_label)
        binding.chipSort.chipType = if(selectedSort != null) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }

    private fun submitQuery(query: String) {
        viewModel.submitAction(ProductTagAction.SearchMyShopProduct(query))

        binding.sbShopProduct.searchBarTextField.apply {
            clearFocus()
            hideKeyboard()
        }
    }

    private fun showEmptyState(hasFilter: Boolean) {
        binding.globalError.apply {
            errorTitle.text = getString(
                if(hasFilter) R.string.cc_no_my_shop_product_filter_title
                else R.string.cc_no_my_shop_product_title
            )
            errorDescription.text = getString(
                if(hasFilter) R.string.cc_no_my_shop_product_filter_desc
                else R.string.cc_no_my_shop_product_desc
            )
            show()
        }
    }

    companion object {
        const val TAG = "MyShopProductFragment"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ) : Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
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