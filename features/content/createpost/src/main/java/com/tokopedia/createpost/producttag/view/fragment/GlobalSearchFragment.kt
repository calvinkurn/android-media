package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.createpost.createpost.databinding.FragmentGlobalSearchBinding
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
        if(viewModel.lastTaggedProductStateUnknown)
            viewModel.submitAction(ProductTagAction.LoadLastTaggedProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "LastTaggedProductFragment"

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