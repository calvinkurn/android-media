package com.tokopedia.tokopedianow.category.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.category.di.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.category.presentation.typefactory.CategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CATEGORY_ATC_TRACKER_CD_LIST_NAME
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import javax.inject.Inject

class TokoNowCategoryFragment: BaseSearchCategoryFragment(), CategoryAisleListener {

    companion object {

        @JvmStatic
        fun create(): TokoNowCategoryFragment {
            return TokoNowCategoryFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var tokoNowCategoryViewModel: TokoNowCategoryViewModel

    override val toolbarPageName = "TokoNow Category"
    override fun cdListName(): String {
        return String.format(TOKONOW_CATEGORY_ATC_TRACKER_CD_LIST_NAME, tokoNowCategoryViewModel.categoryIdTracking)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        activity?.let {
            tokoNowCategoryViewModel = ViewModelProvider(it, viewModelFactory).get(TokoNowCategoryViewModel::class.java)
        }
    }

    override val isDisableSearchBarDefaultGtmTracker: Boolean
        get() = true

    override fun getBaseAutoCompleteApplink() =
            super.getBaseAutoCompleteApplink() + "?" +
                    "${SearchApiConst.NAVSOURCE}=$TOKONOW_DIRECTORY"

    override fun onSearchBarClick(hint: String) {
        CategoryTracking.sendSearchBarClickEvent(getViewModel().categoryL1)
        super.onSearchBarClick(hint)
    }

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
            outOfCoverageListener = this,
    )

    override fun getViewModel() = tokoNowCategoryViewModel

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
                getViewModel().categoryIdTracking,
        )
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        CategoryTracking.sendProductClickEvent(
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
                getViewModel().categoryIdTracking,
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

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        CategoryTracking.sendApplySortFilterEvent(getViewModel().categoryL1)

        super.onApplySortFilter(applySortFilterModel)
    }

    override fun openCategoryChooserFilterPage(filter: Filter) {
        CategoryTracking.sendOpenCategoryL3FilterEvent(getViewModel().categoryL1)

        super.openCategoryChooserFilterPage(filter)
    }

    override fun onApplyCategory(selectedOption: Option) {
        CategoryTracking.sendApplyCategoryL3FilterEvent(getViewModel().categoryL1, selectedOption.value)

        super.onApplyCategory(selectedOption)
    }

    override fun sendAddToCartTrackingEvent(atcData: Triple<Int, String, ProductItemDataView>) {
        val (quantity, cartId, productItemDataView) = atcData

        CategoryTracking.sendAddToCartEvent(
                productItemDataView,
                getViewModel().categoryL1,
                getUserId(),
                quantity,
                cartId,
        )
    }

    override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
        CategoryTracking.sendChooseVariantClickEvent(getViewModel().categoryL1)

        super.onProductChooseVariantClicked(productItemDataView)
    }

    override fun sendIncreaseQtyTrackingEvent(productId: String) {
        CategoryTracking.sendIncreaseQtyEvent(getViewModel().categoryL1)
    }

    override fun sendDecreaseQtyTrackingEvent(productId: String) {
        CategoryTracking.sendDecreaseQtyEvent(getViewModel().categoryL1)
    }
}