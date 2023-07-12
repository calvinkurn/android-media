package com.tokopedia.content.common.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.tokopedia.content.common.databinding.FragmentGlobalSearchBinding
import com.tokopedia.content.common.producttag.view.adapter.GlobalSearchResultPagerAdapter
import com.tokopedia.content.common.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.util.withCache
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyR

/**
* Created By : Jonathan Darwin on May 10, 2022
*/
class GlobalSearchFragment @Inject constructor() : BaseProductTagChildFragment() {

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
            childFragment.setAnalytic(mAnalytic)
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
            mAnalytic?.clickSearchBar(viewModel.selectedTagSource)
            viewModel.submitAction(ProductTagAction.OpenAutoCompletePage)
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { }

            override fun onPageSelected(position: Int) {
                if(position == 0) mAnalytic?.clickGlobalSearchTab(TYPE_PRODUCT)
                else mAnalytic?.clickGlobalSearchTab(TYPE_SHOP)
            }

            override fun onPageScrollStateChanged(state: Int) { }
        })
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSearchBar(it.value.globalSearchProduct.param.query)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is ProductTagUiEvent.HitGlobalSearchProductTracker -> {
                        mAnalytic?.trackGlobalSearchProduct(it.header, it.param)
                    }
                    is ProductTagUiEvent.HitGlobalSearchShopTracker -> {
                        mAnalytic?.trackGlobalSearchShop(it.header, it.param)
                    }
                    else -> {}
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

        private const val TYPE_PRODUCT = "barang"
        private const val TYPE_SHOP = "toko"

        fun getFragmentPair(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ) : Pair<BaseProductTagChildFragment, String> {
            return Pair(getFragment(fragmentManager, classLoader), TAG)
        }

        private fun getFragment(
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
