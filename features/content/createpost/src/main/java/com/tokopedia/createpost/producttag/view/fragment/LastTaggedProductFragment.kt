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
import com.tokopedia.createpost.createpost.databinding.FragmentLastTaggedProductBinding
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.view.adapter.LastTaggedProductAdapter
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.LastTaggedProductUiState
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class LastTaggedProductFragment @Inject constructor(
    private val userSession: UserSessionInterface,
): TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "LastTaggedProductFragment"

    private var _binding: FragmentLastTaggedProductBinding? = null
    private val binding: FragmentLastTaggedProductBinding
        get() = _binding!!

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var viewModel: ProductTagViewModel
    private val adapter: LastTaggedProductAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        LastTaggedProductAdapter(
            onSelected = { viewModel.submitAction(ProductTagAction.ProductSelected(it)) },
            onLoading = { viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct) }
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
        _binding = FragmentLastTaggedProductBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct)
        setupView()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvLastTaggedProduct.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL,)
        binding.rvLastTaggedProduct.adapter = adapter
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderLastTaggedProducts(it.prevValue?.lastTaggedProduct, it.value.lastTaggedProduct)
            }
        }
    }

    private fun renderLastTaggedProducts(prev: LastTaggedProductUiState?, curr: LastTaggedProductUiState) {
        if(prev == curr) return

        when(curr.state) {
            is PagedState.Success -> {
                if(curr.products.isEmpty()) {
                    /** TODO: show empty state */
                }
                else {
                    val finalProducts = curr.products.map {
                        LastTaggedProductAdapter.Model.Product(product = it)
                    }.apply {
                        if(curr.state.hasNextPage) plus(LastTaggedProductAdapter.Model.Loading)
                    }

                    adapter.setItemsAndAnimateChanges(finalProducts)
                }
            }
            is PagedState.Error -> {
                /** TODO: show error state */
            }
            else -> { }
        }
    }

    fun setViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }

    companion object {
        const val TAG = "FeedAccountTypeBottomSheet"

        fun getFragment(
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