package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.product.preview.databinding.FragmentProductPreviewBinding
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter.Companion.TAB_PRODUCT_POS
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter.Companion.TAB_REVIEW_POS
import javax.inject.Inject

class ProductPreviewFragment @Inject constructor(): TkpdBaseV4Fragment() {

    private var _binding: FragmentProductPreviewBinding? = null
    private val binding: FragmentProductPreviewBinding
        get() = _binding!!

    private val pagerListener: ViewPager2.OnPageChangeCallback by lazy(LazyThreadSafetyMode.NONE) {
        pageListenerObject()
    }

    private val pagerAdapter: ProductPreviewPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ProductPreviewPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
        )
    }

    override fun getScreenName() = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        fun onClickHandler() {
            layoutProductPreviewTab.icBack.setOnClickListener {
                activity?.finish()
            }
            layoutProductPreviewTab.tvProductTabTitle.setOnClickListener {
                vpProductPreview.currentItem = TAB_PRODUCT_POS
            }
            layoutProductPreviewTab.tvReviewTabTitle.setOnClickListener {
                vpProductPreview.currentItem = TAB_REVIEW_POS
            }
        }

        onClickHandler()

        binding.vpProductPreview.apply {
            registerOnPageChangeCallback(pagerListener)
            adapter = pagerAdapter
        }
    }

    private fun pageListenerObject() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateSelectedTabView(position)
        }
    }

    private fun updateSelectedTabView(position: Int) = with(binding.layoutProductPreviewTab) {
        fun updateIndicatorTab(position: Int) {
            when (position) {
                TAB_PRODUCT_POS -> root.transitionToStart()
                TAB_REVIEW_POS -> root.transitionToEnd()
                else -> return
            }
        }

        updateIndicatorTab(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vpProductPreview.unregisterOnPageChangeCallback(
            pagerListener
        )
        _binding = null
    }

    companion object {
        const val TAG = "ProductPreviewFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ProductPreviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductPreviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductPreviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ProductPreviewFragment
        }
    }

}
