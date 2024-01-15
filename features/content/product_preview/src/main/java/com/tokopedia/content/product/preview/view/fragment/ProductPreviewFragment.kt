package com.tokopedia.content.product.preview.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.product.preview.databinding.FragmentProductPreviewBinding
import com.tokopedia.content.product.preview.utils.TAB_PRODUCT_POS
import com.tokopedia.content.product.preview.utils.TAB_REVIEW_POS
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.emptyProduct
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.withProduct
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.factory.ProductPreviewViewModelFactory
import com.tokopedia.content.product.preview.viewmodel.utils.EntrySource
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import javax.inject.Inject

class ProductPreviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentProductPreviewBinding? = null
    private val binding: FragmentProductPreviewBinding
        get() = _binding!!

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        viewModelFactory.create(
            EntrySource(productId = "4937529690") // TODO: Testing purpose, change from arguments
        )
    }

    private val productPreviewData: ProductContentUiModel by lazyThreadSafetyNone {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(
                PRODUCT_DATA,
                ProductContentUiModel::class.java
            )
        } else {
            arguments?.getParcelable(PRODUCT_DATA)
        } ?: ProductContentUiModel()
    }

    private val productVideoLastDuration: Long by lazyThreadSafetyNone {
        arguments?.getLong(PRODUCT_LAST_VIDEO_DURATION, 0L) ?: 0L
    }

    private val pagerListener: ViewPager2.OnPageChangeCallback by lazyThreadSafetyNone {
        pageListenerObject()
    }

    private val pagerAdapter: ProductPreviewPagerAdapter by lazyThreadSafetyNone {
        ProductPreviewPagerAdapter(
            fragmentManager = childFragmentManager,
            fragmentActivity = requireActivity(),
            lifecycle = lifecycle
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
        initData()
        initViews()

        onClickHandler()
    }

    private fun initData() {
        viewModel.submitAction(InitializeProductMainData(productPreviewData))
        viewModel.submitAction(
            ProductPreviewUiAction.SetProductVideoLastDuration(productVideoLastDuration)
        )
    }

    private fun initViews() = with(binding) {
        vpProductPreview.apply {
            registerOnPageChangeCallback(pagerListener)
            adapter = pagerAdapter
        }

        if (productPreviewData == ProductContentUiModel()) {
            layoutProductPreviewTab.tvProductTabTitle.gone()
            layoutProductPreviewTab.tvReviewTabTitle.gone()
            layoutProductPreviewTab.viewTabIndicator.gone()
            pagerAdapter.insertFragment(emptyProduct)
        } else {
            layoutProductPreviewTab.tvProductTabTitle.visible()
            layoutProductPreviewTab.tvReviewTabTitle.visible()
            layoutProductPreviewTab.viewTabIndicator.visible()
            pagerAdapter.insertFragment(withProduct)
        }
    }

    private fun onClickHandler() = with(binding) {
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

    private fun pageListenerObject() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateSelectedTabView(position)
        }
    }

    private fun updateSelectedTabView(position: Int) = with(binding.layoutProductPreviewTab) {
        when (position) {
            TAB_PRODUCT_POS -> root.transitionToStart()
            TAB_REVIEW_POS -> root.transitionToEnd()
            else -> return
        }
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

        const val PRODUCT_DATA = "product_data"
        const val PRODUCT_LAST_VIDEO_DURATION = "product_video_last_duration"

        fun getOrCreate(
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
