package com.tokopedia.content.product.preview.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.doOnApplyWindowInsets
import com.tokopedia.content.common.util.requestApplyInsetsWhenAttached
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.databinding.FragmentProductPreviewBinding
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_FRAGMENT_TAG
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_SOURCE
import com.tokopedia.content.product.preview.view.components.MediaBottomNav
import com.tokopedia.content.product.preview.view.pager.ProductPreviewPagerAdapter
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel.ButtonState.OOS
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_PRODUCT_POS
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_REVIEW_POS
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewEvent
import com.tokopedia.content.product.preview.viewmodel.factory.ProductPreviewViewModelFactory
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductPreviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator,
    private val router: Router,
    private val analyticsFactory: ProductPreviewAnalytics.Factory
) : TkpdBaseV4Fragment() {

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        val productPreviewSource: ProductPreviewSourceModel by lazyThreadSafetyNone {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    PRODUCT_PREVIEW_SOURCE,
                    ProductPreviewSourceModel::class.java
                )
            } else {
                arguments?.getParcelable(PRODUCT_PREVIEW_SOURCE)
            } ?: ProductPreviewSourceModel.Empty
        }
        viewModelFactory.create(productPreviewSource)
    }

    private var _binding: FragmentProductPreviewBinding? = null
    private val binding: FragmentProductPreviewBinding
        get() = _binding!!

    private val analytics: ProductPreviewAnalytics by lazyThreadSafetyNone {
        analyticsFactory.create(viewModel.productPreviewSource.productId)
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

    private val pageSource: String by lazyThreadSafetyNone {
        pagerAdapter.getCurrentTabKey(binding.vpProductPreview.currentItem)
    }

    override fun getScreenName() = PRODUCT_PREVIEW_FRAGMENT_TAG

    private val productAtcResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        viewModel.onAction(ProductPreviewAction.ProductActionFromResult)
    }

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
        initSource()

        onClickHandler()
        observeData()
        observeEvent()
    }

    override fun onStart() {
        super.onStart()
        view?.requestApplyInsetsWhenAttached()
    }

    override fun onResume() {
        super.onResume()
        view?.doOnApplyWindowInsets { _, insets, padding, _ ->
            val isNavVisible = insets.isVisible(WindowInsetsCompat.Type.systemGestures())
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures())
            binding.root.updatePadding(
                bottom = padding.bottom + if (isNavVisible) navInsets.bottom else 0
            )
        }
    }

    private fun initViews() = with(binding) {
        vpProductPreview.apply {
            registerOnPageChangeCallback(pagerListener)
            adapter = pagerAdapter
        }
    }

    private fun initSource() {
        viewModel.onAction(ProductPreviewAction.CheckInitialSource)
        viewModel.onAction(ProductPreviewAction.FetchMiniInfo)
    }

    private fun isShowAllTab(isShow: Boolean) = with(binding) {
        layoutProductPreviewTab.tvProductTabTitle.showWithCondition(isShow)
        layoutProductPreviewTab.tvReviewTabTitle.showWithCondition(isShow)
        layoutProductPreviewTab.viewTabIndicator.showWithCondition(isShow)
    }

    private fun onClickHandler() = with(binding) {
        layoutProductPreviewTab.icBack.setOnClickListener {
            analytics.onClickBackButton(pageSource)
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

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state != SCROLL_STATE_IDLE) return

            val position = binding.vpProductPreview.currentItem
            viewModel.onAction(ProductPreviewAction.TabSelected(position))
        }
    }

    private fun updateSelectedTabView(position: Int) = with(binding.layoutProductPreviewTab) {
        when (position) {
            TAB_PRODUCT_POS -> root.transitionToStart()
            TAB_REVIEW_POS -> root.transitionToEnd()
            else -> return
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .withCache().collectLatest { (prev, curr) ->
                    renderTabs(prev?.tabsUiModel, curr.tabsUiModel)
                    renderBottomNav(prev?.bottomNavUiModel, curr.bottomNavUiModel)
                }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).collect {
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
                    // TODO: need to check all toaster in PDP unified media
                    is ProductPreviewEvent.ShowSuccessToaster -> {
                        Toaster.build(
                            requireView().rootView,
                            text = getString(event.message.orZero()),
                            actionText = if (event.type == ProductPreviewEvent.ShowSuccessToaster.Type.ATC) {
                                getString(
                                    contentproductpreviewR.string.bottom_atc_success_click_toaster
                                )
                            } else {
                                ""
                            },
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                viewModel.onAction(ProductPreviewAction.Navigate(ApplinkConst.CART))
                            }
                        ).show()
                    }

                    is ProductPreviewEvent.ShowErrorToaster -> {
                        Toaster.build(
                            requireView().rootView,
                            text = event.message.message.ifNull { getString(event.type.textRes) },
                            actionText = getString(contentproductpreviewR.string.bottom_atc_failed_click_toaster),
                            duration = Toaster.LENGTH_LONG,
                            clickListener = {
                                run { event.onClick() }
                            },
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }
                    is ProductPreviewEvent.FailFetchMiniInfo -> {
                        binding.viewFooter.gone()
                    }
                    is ProductPreviewEvent.UnknownSourceData -> activity?.finish()
                    is ProductPreviewEvent.TrackAllHorizontalScroll -> {
                        analytics.onSwipeContentAndTab()
                    }
                    else -> return@collect
                }
            }
        }
    }

    private fun renderTabs(
        prev: ProductPreviewTabUiModel?,
        curr: ProductPreviewTabUiModel
    ) {
        if (prev == curr) return

        isShowAllTab(curr.tabs.size > 1)
        pagerAdapter.insertFragment(curr.tabs)
    }

    private fun renderBottomNav(prev: BottomNavUiModel?, model: BottomNavUiModel) {
        if (prev == model) return

        analytics.onImpressATC(pageSource)
        if (model.buttonState == OOS) analytics.onImpressRemindMe(pageSource)

        binding.viewFooter.apply {
            show()
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MediaBottomNav(product = model, onAtcClicked = {
                    handleAtc(model)
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) return
        when (resultCode) {
            AtcVariantHelper.ATC_VARIANT_RESULT_CODE -> {
                AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
                    if (this.mapOfSelectedVariantOption.isNullOrEmpty()) return@onActivityResultAtcVariant
                    analytics.onClickVariantGBVS(pageSource)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleAtc(model: BottomNavUiModel) {
        analytics.onClickATC(pageSource, model)
        if (model.buttonState == OOS) analytics.onClickRemindMe(pageSource)
        if (model.hasVariant) {
            AtcVariantHelper.goToAtcVariant(
                context = requireContext(),
                pageSource = VariantPageSource.PRODUCT_PREVIEW_PAGESOURCE,
                shopId = model.shop.id,
                productId = viewModel.productPreviewSource.productId,
                startActivitResult = { intent, resultCode ->
                    startActivityForResult(intent, resultCode)
                }
            )
        } else {
            viewModel.onAction(ProductPreviewAction.ProductAction(model))
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
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle?
        ): ProductPreviewFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(PRODUCT_PREVIEW_FRAGMENT_TAG) as? ProductPreviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductPreviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ProductPreviewFragment
        }
    }
}
