package com.tokopedia.createpost.producttag.view.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.createpost.createpost.databinding.FragmentProductTagParentBinding
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.imagepicker_insta.common.ui.bottomsheet.FeedAccountTypeBottomSheet
import javax.inject.Inject

/**
* Created By : Jonathan Darwin on April 25, 2022
*/
class ProductTagParentFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "ProductTagParentFragment"

    private lateinit var viewModel: ProductTagViewModel

    private var _binding: FragmentProductTagParentBinding? = null
    private val binding: FragmentProductTagParentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductTagParentBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false,
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.icCcProductTagBack.setOnClickListener {

        }
    }

    companion object {
        private const val TAG = "ProductTagParentFragment"
        private const val EXTRA_PRODUCT_TAG_LIST = "EXTRA_PRODUCT_TAG_LIST"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
        ): ProductTagParentFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductTagParentFragment
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource)
        }

        private fun createFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
        ): ProductTagParentFragment {
            return (
                fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ProductTagParentFragment::class.java.name
                ) as ProductTagParentFragment
            ).apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PRODUCT_TAG_LIST, productTagSource)
                }
            }
        }
    }
}