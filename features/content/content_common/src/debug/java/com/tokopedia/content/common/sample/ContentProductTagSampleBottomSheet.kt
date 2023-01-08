package com.tokopedia.content.common.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.BottomSheetContentProductTagSampleBinding
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on September 14, 2022
 */
class ContentProductTagSampleBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetContentProductTagSampleBinding? = null
    private val binding: BottomSheetContentProductTagSampleBinding get() = _binding!!

    private var mDataSource: DataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetContentProductTagSampleBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        clearContentPadding = true
        showHeader = false


        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * SCREEN_HEIGHT_DIVIDER).toInt()
        }

        getFragment()?.let { productTagFragment ->
            childFragmentManager.beginTransaction()
                .replace(
                    binding.container.id,
                    productTagFragment,
                    ProductTagParentFragment.TAG,
                )
                .commit()
        }
    }

    private fun getFragment(): Fragment? {
        val argumentBuilder = mDataSource?.getProductTagArgumentBuilder() ?: return null

        return ProductTagParentFragment.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
            argumentBuilder,
        )
    }

    fun showNow(fragmentManager: FragmentManager) {
        showNow(fragmentManager, TAG)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    companion object {
        private const val TAG = "ContentProductTagSampleBottomSheet"
        private const val SCREEN_HEIGHT_DIVIDER = 0.85f

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentProductTagSampleBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ContentProductTagSampleBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    ContentProductTagSampleBottomSheet::class.java.name
                ) as ContentProductTagSampleBottomSheet
            }
        }
    }

    interface DataSource {
        fun getProductTagArgumentBuilder(): ContentProductTagArgument.Builder
    }
}