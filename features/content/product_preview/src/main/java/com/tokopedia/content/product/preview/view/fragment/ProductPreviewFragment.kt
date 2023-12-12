package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.databinding.FragmentProductPreviewBinding
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter.Companion.TAB_PRODUCT_POS
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter.Companion.TAB_REVIEW_POS
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.content.product.preview.view.components.MediaBottomNav
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.EntrySource
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModelFactory
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ProductPreviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator,
    private val router: Router,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentProductPreviewBinding? = null
    private val binding: FragmentProductPreviewBinding
        get() = _binding!!

    private val pagerListener: ViewPager2.OnPageChangeCallback by lazyThreadSafetyNone {
        pageListenerObject()
    }

    private val pagerAdapter: ProductPreviewPagerAdapter by lazyThreadSafetyNone {
        ProductPreviewPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
        )
    }

    private val productId : String get() = "4937529690" //TODO: get from args

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        viewModelFactory.create(
            EntrySource(productId = productId) //TODO: Testing purpose, change from arguments
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

        onClickHandler()
        observeData()
    }

    override fun onResume() {
        super.onResume()

        viewModel.addAction(ProductPreviewAction.FetchMiniInfo)
    }

    private fun initViews() = with(binding) {
        vpProductPreview.apply {
            registerOnPageChangeCallback(pagerListener)
            adapter = pagerAdapter
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

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.miniInfo.withCache().collectLatest { (prev, curr) ->
                renderBottomNav(prev, curr)
            }
        }
    }

    private fun renderBottomNav(prev: BottomNavUiModel?, model: BottomNavUiModel) {
        if (prev == null || prev == model) return

        binding.viewFooter.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MediaBottomNav(product = model, onAtcClicked = {
                    handleAtc(model)
                })
            }
        }
    }

    private fun handleAtc(model: BottomNavUiModel) {
        if (model.hasVariant) {
            AtcVariantHelper.goToAtcVariant(
                context = requireContext(),
                pageSource = VariantPageSource.PRODUCT_PREVIEW_PAGESOURCE,
                shopId = model.shop.id,
                productId = productId,
                startActivitResult = { data, _ ->
                    startActivity(data) //TODO: change with result
                }
            )
        } else {
            viewModel.addAction(ProductPreviewAction.ProductAction(model))
        }
    }

    companion object {
        const val TAG = "ProductPreviewFragment"

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
