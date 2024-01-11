package com.tokopedia.content.product.preview.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.FragmentProductPreviewBinding
import com.tokopedia.content.product.preview.view.components.MediaBottomNav
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter.Companion.TAB_PRODUCT_POS
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter.Companion.TAB_REVIEW_POS
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.factory.ProductPreviewViewModelFactory
import com.tokopedia.content.product.preview.viewmodel.utils.EntrySource
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductPreviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator,
    private val router: Router,
) : TkpdBaseV4Fragment() {
    val viewModelProvider get() = viewModelFactory.create(EntrySource("123"))

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
            lifecycle
        )
    }

    private val productId: String get() = "4937529690" //TODO: get from args

    private val viewModel by viewModels<ProductPreviewViewModel> {
        viewModelFactory.create(
            EntrySource(productId = productId) //TODO: Testing purpose, change from arguments
        )
    }

    private val productAtcResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) viewModel.onAction(ProductPreviewAction.ProductActionFromResult)
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
        observeEvent()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onAction(ProductPreviewAction.FetchMiniInfo)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.miniInfo
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .withCache().collectLatest { (prev, curr) ->
                    renderBottomNav(prev, curr)
                }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).collectLatest {
                when (val event = it) {
                    is ProductPreviewEvent.LoginEvent<*> -> {
                        val intent = router.getIntent(requireContext(), ApplinkConst.LOGIN)
                        when (event.data) {
                            is BottomNavUiModel -> productAtcResult.launch(intent)
                        }
                    }

                    is ProductPreviewEvent.NavigateEvent -> router.route(
                        requireContext(),
                        event.appLink
                    )
                    //TODO: need to check all toaster in PDP unified media
                    is ProductPreviewEvent.ShowSuccessToaster -> {
                        Toaster.build(
                            requireView().rootView,
                            text = getString(event.message.orZero()),
                            actionText = if (event.type == ProductPreviewEvent.ShowSuccessToaster.Type.ATC) getString(
                                R.string.bottom_atc_success_click_toaster
                            ) else "",
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                viewModel.onAction(ProductPreviewAction.Navigate(ApplinkConst.CART))
                            }
                        ).show()
                    }

                    is ProductPreviewEvent.ShowErrorToaster -> {
                        Toaster.build(
                            requireView().rootView,
                            text = getString(R.string.bottom_atc_failed_toaster),
                            actionText = getString(R.string.bottom_atc_failed_click_toaster),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                run { event.onClick() }
                            },
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun renderBottomNav(prev: BottomNavUiModel?, model: BottomNavUiModel) {
        if (prev == model) return

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
                startActivitResult = { intent, _ -> startActivity(intent) }
            )
        } else {
            viewModel.onAction(ProductPreviewAction.ProductAction(model))
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
