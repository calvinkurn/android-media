package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.createpost.createpost.databinding.FragmentGlobalSearchBinding
import com.tokopedia.createpost.producttag.view.adapter.GlobalSearchResultPagerAdapter
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel

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
    }

    private fun setupView() {
        tabAdapter = GlobalSearchResultPagerAdapter(childFragmentManager, requireActivity().classLoader)

        binding.viewPager.adapter = tabAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if(childFragment is BaseProductTagChildFragment) {
            childFragment.createViewModelProvider(viewModelProvider)
        }
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