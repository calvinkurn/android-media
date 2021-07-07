package com.tokopedia.tokopedianow.search.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.tokopedianow.search.di.SearchComponent
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactoryImpl
import com.tokopedia.tokopedianow.search.presentation.viewmodel.TokoNowSearchViewModel
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_SEARCH_ATC_TRACKER_CD_LIST_NAME
import javax.inject.Inject

class TokoNowSearchFragment: BaseSearchCategoryFragment(), SuggestionListener {

    companion object {

        @JvmStatic
        fun create(): TokoNowSearchFragment {
            return TokoNowSearchFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var tokoNowSearchViewModel: TokoNowSearchViewModel

    override val toolbarPageName = "TokoNow Search"
    override fun cdListName(): String {
        return TOKONOW_SEARCH_ATC_TRACKER_CD_LIST_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        activity?.let {
            tokoNowSearchViewModel = ViewModelProvider(it, viewModelFactory).get(TokoNowSearchViewModel::class.java)
        }
    }

    override fun getNavToolbarHint() =
            listOf(HintData(tokoNowSearchViewModel.query, tokoNowSearchViewModel.query))

    override fun getBaseAutoCompleteApplink() =
            super.getBaseAutoCompleteApplink() + "?" +
                    "${SearchApiConst.Q}=${tokoNowSearchViewModel.query}" + "&" +
                    "${SearchApiConst.NAVSOURCE}=$TOKONOW"

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(SearchComponent::class.java).inject(this)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        getViewModel().generalSearchEventLiveData.observe(this::sendTrackingGeneralEvent)
    }

    private fun sendTrackingGeneralEvent(dataLayer: Map<String, Any>) {
        SearchTracking.sendGeneralEvent(dataLayer)
    }

    override fun sendAddToCartTrackingEvent(atcData: Triple<Int, String, ProductItemDataView>) {
        val (quantity, _, productItemDataView) = atcData

        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendAddToCartEvent(
                productItemDataView,
                getViewModel().query,
                getUserId(),
                sortFilterParams,
                quantity,
        )
    }

    override fun sendIncreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendIncreaseQtyEvent(tokoNowSearchViewModel.query, productId)
    }

    override fun sendDecreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendDecreaseQtyEvent(tokoNowSearchViewModel.query, productId)
    }

    override fun createTypeFactory() = SearchTypeFactoryImpl(
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
            emptyProductListener = this,
            suggestionListener = this,
            outOfCoverageListener = this,
    )

    override val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.SEARCH_PAGE

    override fun getViewModel() = tokoNowSearchViewModel

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView) {
        val context = context ?: return

        SearchTracking.sendSuggestionClickEvent(getViewModel().query, suggestionDataView.suggestion)

        val applink = ApplinkConstInternalTokopediaNow.SEARCH + "?" + suggestionDataView.query
        RouteManager.route(context, applink)
    }

    override fun onGoToGlobalSearch() {
        super.onGoToGlobalSearch()

        val queryParams = "${SearchApiConst.Q}=${tokoNowSearchViewModel.query}"
        val applinkToSearchResult = "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?$queryParams"

        RouteManager.route(context, applinkToSearchResult)
    }

    override fun onProductImpressed(productItemDataView: ProductItemDataView) {
        val trackingQueue = trackingQueue ?: return

        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendProductImpressionEvent(
                trackingQueue,
                productItemDataView,
                getViewModel().query,
                getUserId(),
                sortFilterParams,
        )
    }

    private fun getQueryParamWithoutExcludes(): Map<String, String> {
        return FilterHelper.createParamsWithoutExcludes(tokoNowSearchViewModel.queryParam)
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendProductClickEvent(
                productItemDataView,
                getViewModel().query,
                getUserId(),
                sortFilterParams,
        )

        super.onProductClick(productItemDataView)
    }

    override fun openFilterPage() {
        SearchTracking.sendOpenFilterPageEvent()

        super.openFilterPage()
    }

    override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        val filterParamMap = applySortFilterModel.selectedFilterMapParameter
        val paramMapWithoutExclude =
                FilterHelper.createParamsWithoutExcludes(filterParamMap) as Map<String?, String>
        val filterParams = UrlParamUtils.generateUrlParamString(paramMapWithoutExclude)
        SearchTracking.sendApplySortFilterEvent(filterParams)

        super.onApplySortFilter(applySortFilterModel)
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        SearchTracking.sendApplyCategoryL2FilterEvent(option.name)

        super.onCategoryFilterChipClick(option, isSelected)
    }

    override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
        SearchTracking.sendChooseVariantEvent(tokoNowSearchViewModel.query, productItemDataView.id)

        super.onProductChooseVariantClicked(productItemDataView)
    }

    override fun onBannerClick(channelModel: ChannelModel, applink: String) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendBannerClickEvent(
                channelModel,
                getViewModel().query,
                getUserId(),
                sortFilterParams,
        )

        super.onBannerClick(channelModel, applink)
    }

    override fun onBannerImpressed(channelModel: ChannelModel, position: Int) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendBannerImpressionEvent(
                channelModel,
                getViewModel().query,
                getUserId(),
                sortFilterParams,
        )

        super.onBannerImpressed(channelModel, position)
    }

    override fun sendTrackingQuickFilter(quickFilterTracking: Pair<Option, Boolean>) {
        SearchTracking.sendQuickFilterClickEvent(
                quickFilterTracking.first,
                quickFilterTracking.second
        )
    }

    override fun onApplyCategory(selectedOption: Option) {
        val filterParam = selectedOption.key.removePrefix(OptionHelper.EXCLUDE_PREFIX) +
                "=" +
                selectedOption.value
        SearchTracking.sendApplyCategoryL3FilterEvent(filterParam)

        super.onApplyCategory(selectedOption)
    }
}