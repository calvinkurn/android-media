package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.createpost.createpost.databinding.FragmentGlobalSearchShopTabBinding
import com.tokopedia.createpost.producttag.view.fragment.base.BaseProductTagChildFragment
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchShopTabFragment : BaseProductTagChildFragment() {

    override fun getScreenName(): String = "GlobalSearchShopTabFragment"

    private var _binding: FragmentGlobalSearchShopTabBinding? = null
    private val binding: FragmentGlobalSearchShopTabBinding
        get() = _binding!!

    private lateinit var viewModel: ProductTagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGlobalSearchShopTabBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "GlobalSearchShopTabFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): GlobalSearchShopTabFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? GlobalSearchShopTabFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                GlobalSearchShopTabFragment::class.java.name
            ) as GlobalSearchShopTabFragment
        }
    }
}