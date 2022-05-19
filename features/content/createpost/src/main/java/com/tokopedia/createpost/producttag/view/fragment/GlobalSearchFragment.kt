package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.createpost.databinding.FragmentGlobalSearchBinding
import com.tokopedia.createpost.producttag.util.extension.withCache
import com.tokopedia.createpost.producttag.util.getAutocompleteApplink
import com.tokopedia.createpost.producttag.view.adapter.GlobalSearchResultPagerAdapter
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import kotlinx.coroutines.flow.collect
import com.tokopedia.unifyprinciples.R as unifyR

/**
* Created By : Jonathan Darwin on May 10, 2022
*/
class GlobalSearchFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "GlobalSearchFragment"

    private var _binding: FragmentGlobalSearchBinding? = null
    private val binding: FragmentGlobalSearchBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel
    private lateinit var tabAdapter: GlobalSearchResultPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGlobalSearchBinding.inflate(
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
        if(childFragment is BaseProductTagChildFragment) {
            childFragment.createViewModelProvider(viewModelProvider)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        tabAdapter = GlobalSearchResultPagerAdapter(childFragmentManager, requireActivity().classLoader)

        binding.viewPager.adapter = tabAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.clSearch.setOnClickListener {
            viewModel.submitAction(ProductTagAction.OpenAutoCompletePage)
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collect {
                renderSearchBar(it.value.globalSearchProduct.param.query)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is ProductTagUiEvent.OpenAutoCompletePage -> {
                        RouteManager.route(requireContext(), getAutocompleteApplink(it.query))
                    }
                }
            }
        }
    }

    private fun renderSearchBar(query: String) {
        binding.etSearch.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if(query.isEmpty()) unifyR.color.Unify_NN600 else unifyR.color.Unify_NN950
            )
        )
        binding.etSearch.text = query
    }

    companion object {
        const val TAG = "GlobalSearchFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): GlobalSearchFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? GlobalSearchFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                GlobalSearchFragment::class.java.name
            ) as GlobalSearchFragment
        }
    }
}