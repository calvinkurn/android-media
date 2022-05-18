package com.tokopedia.tokopedianow.search.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_ATC_SRP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_SRP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_SRP_RECOM_OOC
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.IMPRESSION_SRP_PRODUCT_TOKONOW
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.IMPRESSION_SRP_RECOM_OOC
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_DASH_SEARCH_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_EMPTY_RESULT
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKOOW_SEARCH_RESULT_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.RECOM_LIST_PAGE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.RECOM_LIST_PAGE_EMPTY_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ATC_VARIANT
import com.tokopedia.tokopedianow.search.di.SearchComponent
import com.tokopedia.tokopedianow.search.presentation.listener.BroadMatchListener
import com.tokopedia.tokopedianow.search.presentation.listener.CTATokoNowHomeListener
import com.tokopedia.tokopedianow.search.presentation.listener.CategoryJumperListener
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchItemDataView
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactoryImpl
import com.tokopedia.tokopedianow.search.presentation.viewmodel.TokoNowSearchViewModel
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_LIST_OOC
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.VALUE_TOPADS
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import javax.inject.Inject

class TokoNowSearchFragment :
    BaseSearchCategoryFragment(),
    SuggestionListener,
    CategoryJumperListener,
    CTATokoNowHomeListener,
    BroadMatchListener,
    SwitcherWidgetListener{

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
        getViewModel().addToCartBroadMatchTrackingLiveData.observe(this::sendATCBroadMatchTrackingEvent)
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

    override fun sendDeleteCartTrackingEvent(productId: String) {
        SearchTracking.sendDeleteCartEvent(productId)
    }

    override fun sendIncreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendIncreaseQtyEvent(tokoNowSearchViewModel.query, productId)
    }

    override fun sendDecreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendDecreaseQtyEvent(tokoNowSearchViewModel.query, productId)
    }

    override fun createTypeFactory() = SearchTypeFactoryImpl(
            tokoNowEmptyStateOocListener = createTokoNowEmptyStateOocListener(TOKONOW_DASH_SEARCH_PAGE),
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
            switcherWidgetListener = this,
            tokoNowEmptyStateNoResultListener = this,
            suggestionListener = this,
            categoryJumperListener = this,
            ctaTokoNowHomeListener = this,
            recommendationCarouselListener = this,
            broadMatchListener = this,
            recomWidgetBindPageNameListener = this
    )

    override val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.SEARCH_PAGE

    override fun getViewModel() = tokoNowSearchViewModel

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView) {
        SearchTracking.sendSuggestionClickEvent(getViewModel().query, suggestionDataView.suggestion)

        performNewProductSearch(suggestionDataView.query)
    }

    private fun performNewProductSearch(queryParams: String) {
        val context = context ?: return

        val applinkToSearchResult = ApplinkConstInternalTokopediaNow.SEARCH + "?" + queryParams
        val modifiedApplinkToSearchResult = modifyApplinkToSearchResult(applinkToSearchResult)

        RouteManager.route(context, modifiedApplinkToSearchResult)
    }

    private fun modifyApplinkToSearchResult(applink: String): String {
        val urlParser = URLParser(applink)

        val params = urlParser.paramKeyValueMap
        params[SearchApiConst.PREVIOUS_KEYWORD] = getViewModel().query

        return ApplinkConstInternalTokopediaNow.SEARCH + "?" +
            UrlParamUtils.generateUrlParamString(params)
    }

    override fun onFindInTokopediaClick() {
        super.onFindInTokopediaClick()

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

    override fun getCDListName(): String {
        return TOKONOW_SEARCH_PRODUCT_ATC_VARIANT
    }

    override fun onBannerClick(channelModel: ChannelModel, applink: String, param: String) {
        val queryParam = getQueryParamWithoutExcludes()
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        SearchTracking.sendBannerClickEvent(
                channelModel,
                getViewModel().query,
                getUserId(),
                sortFilterParams,
        )

        super.onBannerClick(channelModel, applink, param)
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

    override fun onCategoryJumperItemClick(item: CategoryJumperDataView.Item) {
        val context = context ?: return

        SearchTracking.sendClickCategoryJumperEvent(item.title)
        RouteManager.route(context, item.applink)
    }

    override fun onCTAToTokopediaNowHomeClick() {
        SearchTracking.sendClickCTAToHome()
        goToTokopediaNowHome()
    }

    override fun getImpressionEventAction(isOOC: Boolean): String {
        return if (isOOC) {
            IMPRESSION_SRP_RECOM_OOC
        } else {
            IMPRESSION_SRP_PRODUCT_TOKONOW
        }
    }

    override fun getClickEventAction(isOOC: Boolean): String {
        return if (isOOC) {
            CLICK_SRP_RECOM_OOC
        } else {
            CLICK_SRP_PRODUCT_TOKONOW
        }
    }

    override fun getAtcEventAction(isOOC: Boolean): String {
        return CLICK_ATC_SRP_PRODUCT_TOKONOW
    }

    override fun getEventCategory(isOOC: Boolean): String {
        return if (isOOC) TOKOOW_SEARCH_RESULT_PAGE else TOKONOW_EMPTY_RESULT
    }

    override fun getListValue(isOOC: Boolean, recommendationItem: RecommendationItem): String {
        return if (isOOC) {
            String.format(
                VALUE_LIST_OOC,
                RECOM_LIST_PAGE,
                recommendationItem.recommendationType,
                if (recommendationItem.isTopAds) VALUE_TOPADS else ""
            )
        } else {
            String.format(
                RECOM_LIST_PAGE_EMPTY_SEARCH,
                recommendationItem.recommendationType
            )
        }
    }

    override fun getEventLabel(isOOC: Boolean): String {
        return getViewModel().query
    }

    override fun getRecyclerViewPool() = recycledViewPool

    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        val trackingQueue = trackingQueue ?: return

        SearchTracking.sendBroadMatchImpressionEvent(
            trackingQueue = trackingQueue,
            broadMatchItemDataView = broadMatchItemDataView,
            keyword = getViewModel().query,
            userId = getUserId(),
        )
    }

    override fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        SearchTracking.sendBroadMatchClickEvent(
            broadMatchItemDataView = broadMatchItemDataView,
            keyword = getViewModel().query,
            userId = getUserId(),
        )

        RouteManager.route(context, broadMatchItemDataView.applink)
    }

    override fun onBroadMatchItemATCNonVariant(
        broadMatchItemDataView: BroadMatchItemDataView,
        quantity: Int,
        broadMatchIndex: Int,
    ) {
        getViewModel().onViewATCBroadMatchItem(broadMatchItemDataView, quantity, broadMatchIndex)
    }

    private fun sendATCBroadMatchTrackingEvent(
        atcTrackingData: Triple<Int, String, BroadMatchItemDataView>
    ) {
        val (quantity, _, broadMatchItemDataView) = atcTrackingData

        SearchTracking.sendBroadMatchAddToCartEvent(
            broadMatchItemDataView,
            getViewModel().query,
            getUserId(),
            quantity,
        )
    }

    override fun onBroadMatchSeeAllClicked(broadMatchDataView: BroadMatchDataView) {
        SearchTracking.sendBroadMatchSeeAllClickEvent(broadMatchDataView, getViewModel().query)

        val applink = getBroadMatchSeeAllApplink(broadMatchDataView)

        RouteManager.route(context, applink)
    }

    private fun getBroadMatchSeeAllApplink(broadMatchDataView: BroadMatchDataView) =
        if (broadMatchDataView.applink.startsWith(ApplinkConst.TokopediaNow.SEARCH))
            modifyApplinkToSearchResult(broadMatchDataView.applink)
        else broadMatchDataView.applink

    override fun onSeeMoreClick(data: RecommendationCarouselData, applink: String) {
        SearchTracking.sendRecommendationSeeAllClickEvent(getViewModel().query)

        RouteManager.route(context, applink)
    }

    override fun sendOOCOpenScreenTracking(isTracked: Boolean) {
        SearchTracking.sendOOCOpenScreenTracking(userSession.isLoggedIn)
    }
}