package com.tokopedia.tokopedianow.category.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.adapter.viewpager.CategoryL2TabViewPagerAdapter
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.view.CategoryL2MainView
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryL2Binding
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_CLP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.IMPRESSION_CLP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.RECOM_LIST_PAGE_NON_OOC
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendRecommendationSeeAllClickEvent
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_REF
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker
import com.tokopedia.tokopedianow.searchcategory.presentation.bottomsheet.TokoNowProductFeedbackBottomSheet
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2Fragment : BaseCategoryFragment(), CategoryL2MainView {

    companion object {
        fun newInstance(
            categoryL1: String,
            categoryL2: String,
            queryParamMap: HashMap<String, String>?
        ): TokoNowCategoryL2Fragment {
            return TokoNowCategoryL2Fragment().apply {
                this.categoryIdL1 = categoryL1
                this.categoryIdL2 = categoryL2
                this.currentCategoryId = categoryL2
                this.queryParamMap = queryParamMap
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryL2Binding>()

    private val viewPagerAdapter by lazy { CategoryL2TabViewPagerAdapter(this) }

    override val viewModel: TokoNowCategoryL2ViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[TokoNowCategoryL2ViewModel::class.java]
    }

    override val swipeRefreshLayout: SwipeToRefresh?
        get() = binding?.swipeToRefresh

    override val recyclerView: RecyclerView?
        get() = binding?.recyclerView

    override fun createAdapterTypeFactory(): CategoryL2AdapterTypeFactory {
        return CategoryL2AdapterTypeFactory(
            tokoNowView = createTokoNowViewCallback(),
            emptyStateNoResultListener = createEmptyStateNoResultListener(),
            productRecommendationListener = createProductRecommendationListener(),
            chooseAddressListener = createChooseAddressWidgetCallback(),
            productAdsCarouselListener = createProductAdsCarouselListener(),
            categoryMenuListener = createCategoryMenuListener(),
            feedbackWidgetListener = createFeedbackWidgetListener()
        )
    }

    override fun createAdapterDiffer() = CategoryL2Differ()

    override fun createMainView(): View? {
        binding = FragmentTokopedianowCategoryL2Binding.inflate(LayoutInflater.from(context))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setupAppBarLayout()
        setupViewPager()
        setupTabsUnify()
        onViewCreated()
    }

    override fun setupMainLayout(
        container: ConstraintLayout?,
        mainLayout: FrameLayout?
    ) {
        super.setupMainLayout(container, mainLayout)
        container?.let {
            val constraintSet = ConstraintSet().apply {
                clone(container)
            }
            constraintSet.connect(
                R.id.main_layout,
                ConstraintSet.TOP,
                R.id.nav_toolbar,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(container)
        }
    }

    override fun onShowEmptyState(
        model: CategoryEmptyStateModel,
        filterController: FilterController
    ) {
        viewModel.showEmptyState(model, filterController)
    }

    override fun onHideEmptyState() {
        viewModel.hideEmptyState()
    }

    override fun initInjector() {
        DaggerCategoryL2Component.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun observeLiveData() {
        observe(viewModel.categoryTab) { data ->
            clearAllCategoryTabs()
            addTabFragments(data)
            setupTabsUnifyMediator(data)
            setSelectedTabPosition(data)
        }

        observe(viewModel.showEmptyState) { showEmptyState ->
            if(showEmptyState) {
                hideCategoryTabs()
            } else {
                showCategoryTabs()
            }
        }
    }

    private fun showCategoryTabs() {
        binding?.apply {
            tabsUnify.show()
            viewPager.show()
            swipeToRefresh.isEnabled = true
        }
    }

    private fun hideCategoryTabs() {
        binding?.apply {
            tabsUnify.hide()
            viewPager.hide()
            swipeToRefresh.isEnabled = false
        }
    }

    private fun setupAppBarLayout() {
        binding?.apply {
            appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
                val isEmptyState = viewModel.isEmptyState()
                swipeToRefresh.isEnabled = (verticalOffset == 0) && !isEmptyState
            }
        }
    }

    private fun setupViewPager() {
        binding?.apply {
            val onPageChangeCallback = createOnPageChangeCallback()
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = viewPagerAdapter
        }
    }

    private fun setupTabsUnify() {
        binding?.apply {
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
            tabsUnify.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.position, false)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun addTabFragments(data: CategoryL2TabUiModel) {
        binding?.apply {
            val selectedTabPosition = data.selectedTabPosition
            data.categoryL2Ids.forEach {
                val fragment = TokoNowCategoryL2TabFragment
                    .newInstance(data.categoryIdL1, it, data.componentList)
                fragment.categoryL2MainView = this@TokoNowCategoryL2Fragment
                viewPagerAdapter.addFragment(fragment)
            }
            viewPager.currentItem = selectedTabPosition
        }
    }

    private fun setupTabsUnifyMediator(data: CategoryL2TabUiModel) {
        binding?.apply {
            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                val title = data.titleList.getOrNull(position).orEmpty()
                if (title.isNotBlank()) tab.setCustomText(title)
            }
        }
    }

    private fun setSelectedTabPosition(data: CategoryL2TabUiModel) {
        binding?.tabsUnify?.apply {
            tabLayout.post {
                val selectedTabPosition = data.selectedTabPosition
                viewPagerAdapter.setSelectedTabPosition(selectedTabPosition)
                tabLayout.getTabAt(selectedTabPosition)?.select()
            }
        }
    }

    private fun clearAllCategoryTabs() {
        binding?.apply {
            tabsUnify.tabLayout.removeAllTabs()
            viewPagerAdapter.clearFragments()
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated()
    }

    private fun goToTokopediaHomePage() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    private fun goToTokopediaNowHomePage() {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
    }

    private fun goToProductDetailPage(appLink: String) {
        val newAppLink = viewModel.createAffiliateLink(appLink)
        RouteManager.route(activity, newAppLink)
    }

    private fun goToAtcVariant(productId: String, shopId: String) {
        AtcVariantHelper.goToAtcVariant(
            context = requireContext(),
            productId = productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
            startActivitResult = ::startActivityForResult,
        )
    }

    private fun trackClickProductRecommendation(
        product: ProductCardCompactCarouselItemUiModel,
        position: Int,
        userId: String
    ) {
        val recommendationItem =
            ProductRecommendationMapper.mapProductItemToRecommendationItem(product)
        val eventLabel = viewModel.getCategoryIdForTracking()

        SearchResultTracker.trackClickProduct(
            position,
            eventLabel,
            CLICK_CLP_PRODUCT_TOKONOW,
            TOKONOW_CATEGORY_PAGE,
            RECOM_LIST_PAGE_NON_OOC,
            userId,
            recommendationItem
        )
    }

    private fun trackProductRecommendationImpression(
        product: ProductCardCompactCarouselItemUiModel,
        position: Int,
        userId: String
    ) {
        val recommendationItem =
            ProductRecommendationMapper.mapProductItemToRecommendationItem(product)
        val eventLabel = viewModel.getCategoryIdForTracking()
        SearchResultTracker.trackImpressionProduct(
            position,
            eventLabel,
            IMPRESSION_CLP_PRODUCT_TOKONOW,
            TOKONOW_CATEGORY_PAGE,
            RECOM_LIST_PAGE_NON_OOC,
            userId,
            recommendationItem
        )
    }

    private fun directToSeeMorePage(appLink: String) {
        val categoryIdTracking = viewModel.getCategoryIdForTracking()
        val newAppLink = modifySeeMoreAppLink(appLink)

        sendRecommendationSeeAllClickEvent(categoryIdTracking)
        RouteManager.route(activity, newAppLink)
    }

    private fun modifySeeMoreAppLink(
        originalAppLink: String
    ): String {
        val uri = Uri.parse(originalAppLink)
        val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: "")
        val ref = queryParamsMap[RECOM_QUERY_PARAM_REF] ?: ""

        return if (ref == RecomPageConstant.TOKONOW_CLP) {
            val recomCategoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] ?: ""

            if (recomCategoryId.isEmpty()) {
                queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = categoryIdL1
            }

            "${uri.scheme}://" +
                "${uri.host}/" +
                "${uri.path}?" +
                UrlParamUtils.generateUrlParamString(queryParamsMap)
        } else {
            originalAppLink
        }
    }

    private fun showShopClosedToaster() {
        showToaster(
            message = getString(R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop),
            toasterType = Toaster.TYPE_ERROR
        )
    }

    private fun createOnPageChangeCallback(): OnPageChangeCallback {
        return object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                val isEmptyState = viewModel.isEmptyState()
                binding?.swipeToRefresh?.isEnabled =
                    state == ViewPager2.SCROLL_STATE_IDLE && !isEmptyState
            }
        }
    }

    private fun createChooseAddressWidgetCallback(): TokoNowChooseAddressWidgetListener {
        return object : TokoNowChooseAddressWidgetListener {
            override fun onChooseAddressWidgetRemoved() {
                viewModel.removeChooseAddressWidget()
            }

            override fun onClickChooseAddressWidgetTracker() {

            }
        }
    }

    private fun createProductAdsCarouselListener() = object : ProductAdsCarouselListener {
        override fun onProductCardClicked(
            position: Int,
            title: String,
            product: ProductCardCompactCarouselItemUiModel
        ) {
            goToProductDetailPage(product.appLink)
        }

        override fun onProductCardImpressed(
            position: Int,
            title: String,
            product: ProductCardCompactCarouselItemUiModel
        ) {
        }

        override fun onProductCardQuantityChanged(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            quantity: Int
        ) {
            viewModel.onCartQuantityChanged(
                product = product.productCardModel,
                shopId = product.shopId,
                quantity = quantity,
                layoutType = TokoNowStaticLayoutType.PRODUCT_ADS_CAROUSEL
            )
        }

        override fun onProductCardAddVariantClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        ) {
            goToAtcVariant(product.getProductId(), product.shopId)
        }

        override fun onProductCardAddToCartBlocked() {
            showShopClosedToaster()
        }
    }

    private fun createEmptyStateNoResultListener(): TokoNowEmptyStateNoResultListener {
        return object : TokoNowEmptyStateNoResultListener {
            override fun onFindInTokopediaClick() {
                goToTokopediaHomePage()
            }

            override fun goToTokopediaNowHome() {
                goToTokopediaNowHomePage()
            }

            override fun onRemoveFilterClick(option: Option) {
                val tabFragment = viewPagerAdapter.getSelectedTabFragment()
                tabFragment.onRemoveFilter(option)
            }
        }
    }

    private fun createProductRecommendationListener(): TokoNowProductRecommendationListener {
        return object : TokoNowProductRecommendationListener {
            override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel {
                return productRecommendationViewModel
            }

            override fun hideProductRecommendationWidget() {
                viewModel.removeProductRecommendationWidget()
            }

            override fun openLoginPage() {
                this@TokoNowCategoryL2Fragment.openLoginPage()
            }

            override fun productCardAddVariantClicked(productId: String, shopId: String) {
                goToAtcVariant(productId, shopId)
            }

            override fun productCardClicked(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                isLogin: Boolean,
                userId: String
            ) {
                trackClickProductRecommendation(product, position, userId)
                goToProductDetailPage(product.appLink)
            }

            override fun productCardImpressed(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                isLogin: Boolean,
                userId: String
            ) {
                trackProductRecommendationImpression(product, position, userId)
            }

            override fun seeMoreClicked(seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel) {
                directToSeeMorePage(seeMoreUiModel.appLink)
            }

            override fun seeAllClicked(appLink: String) {
                directToSeeMorePage(appLink)
            }

            override fun productCardAddToCartBlocked() {
                showShopClosedToaster()
            }
        }
    }

    private fun createCategoryMenuListener(): TokoNowCategoryMenuListener {
        return object : TokoNowCategoryMenuListener {
            override fun onCategoryMenuWidgetRetried() {
                viewModel.getCategoryMenuData()
            }

            override fun onSeeAllCategoryClicked() {
            }

            override fun onCategoryMenuItemClicked(
                data: TokoNowCategoryMenuItemUiModel,
                itemPosition: Int
            ) {
            }

            override fun onCategoryMenuItemImpressed(
                data: TokoNowCategoryMenuItemUiModel,
                itemPosition: Int
            ) {
            }

            override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) {
            }
        }
    }

    private fun createFeedbackWidgetListener(): FeedbackWidgetListener {
        return object : FeedbackWidgetListener {
            override fun onFeedbackCtaClicked() {
                TokoNowProductFeedbackBottomSheet().also {
                    it.showBottomSheet(activity?.supportFragmentManager,view)
                }
            }
        }
    }
}
