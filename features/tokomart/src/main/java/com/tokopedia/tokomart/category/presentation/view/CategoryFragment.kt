package com.tokopedia.tokomart.category.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.tokomart.category.analytics.CategoryTracking
import com.tokopedia.tokomart.category.di.CategoryComponent
import com.tokopedia.tokomart.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokomart.category.presentation.typefactory.CategoryTypeFactoryImpl
import com.tokopedia.tokomart.category.presentation.viewmodel.CategoryViewModel
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_DIRECTORY
import javax.inject.Inject

class CategoryFragment: BaseSearchCategoryFragment(), CategoryAisleListener {

    companion object {

        @JvmStatic
        fun create(): CategoryFragment {
            return CategoryFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var categoryViewModel: CategoryViewModel

    override val toolbarPageName = "TokoNow Category"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        activity?.let {
            categoryViewModel = ViewModelProvider(it, viewModelFactory).get(CategoryViewModel::class.java)
        }
    }

    override fun onSearchBarClick(hint: String) {
        CategoryTracking.sendSearchBarClickEvent(getViewModel().categoryL1)

        super.onSearchBarClick(hint)
    }

    override fun getBaseAutoCompleteApplink() =
            super.getBaseAutoCompleteApplink() + "?" +
                    "${SearchApiConst.NAVSOURCE}=$TOKONOW_DIRECTORY"

    override val disableDefaultCartTracker: Boolean
        get() = true

    override fun onNavToolbarCartClicked() {
        CategoryTracking.sendCartClickEvent(getViewModel().categoryL1)
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CategoryComponent::class.java).inject(this)
    }

    override fun createTypeFactory() = CategoryTypeFactoryImpl(
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
            emptyProductListener = this,
            categoryAisleListener = this,
    )

    override fun getViewModel() = categoryViewModel

    override val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.CATEGORY_PAGE

    override fun onAisleClick(categoryAisleItemDataView: CategoryAisleItemDataView) {
        CategoryTracking.sendAisleClickEvent(getViewModel().categoryL1, categoryAisleItemDataView.id)

        RouteManager.route(context, categoryAisleItemDataView.applink)
    }

    override fun onProductImpressed(productItemDataView: ProductItemDataView) {
        val trackingQueue = trackingQueue ?: return

        CategoryTracking.sendProductImpressionEvent(
                trackingQueue,
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
        )
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        CategoryTracking.sendProductClickEvent(
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
        )

        super.onProductClick(productItemDataView)
    }

    override fun onBannerImpressed(channelModel: ChannelModel, position: Int) {
        CategoryTracking.sendBannerImpressionEvent(channelModel, getViewModel().categoryL1, getUserId())

        super.onBannerImpressed(channelModel, position)
    }

    override fun onBannerClick(channelModel: ChannelModel, applink: String) {
        CategoryTracking.sendBannerClickEvent(channelModel, getViewModel().categoryL1, getUserId())

        super.onBannerClick(channelModel, applink)
    }

    override fun onSeeAllCategoryClicked() {
        CategoryTracking.sendAllCategoryClickEvent(getViewModel().categoryL1)

        super.onSeeAllCategoryClicked()
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        CategoryTracking.sendApplyCategoryL2FilterEvent(getViewModel().categoryL1, option.value)

        super.onCategoryFilterChipClick(option, isSelected)
    }

    override fun openFilterPage() {
        CategoryTracking.sendFilterClickEvent(getViewModel().categoryL1)

        super.openFilterPage()
    }

    override fun sendTrackingQuickFilter(quickFilterTracking: Pair<Option, Boolean>) {
        CategoryTracking.sendQuickFilterClickEvent(getViewModel().categoryL1)
    }
}