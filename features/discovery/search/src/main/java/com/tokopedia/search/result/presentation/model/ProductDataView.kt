package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.ProductListType.FIXED_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.ProductListType.LIST_VIEW
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.product.banner.BannerDataView
import com.tokopedia.search.result.product.broadmatch.RelatedDataView
import com.tokopedia.search.result.product.changeview.ViewType
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConstant
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.product.violation.ViolationDataView
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

class ProductDataView {
    var productList = listOf<ProductItemDataView>()
    var additionalParams = ""
    var autocompleteApplink: String? = null
    var responseCode: String? = null
    var keywordProcess: String? = null
    var tickerModel: TickerDataView? = null
    var suggestionModel: SuggestionDataView? = null
    var totalData = 0L
    var isQuerySafe = false
    var adsModel: TopAdsModel = TopAdsModel()
    var cpmModel: CpmModel? = null
    var globalNavDataView: GlobalNavDataView? = null
    var inspirationCarouselDataView = listOf<InspirationCarouselDataView>()
    var inspirationWidgetDataView = listOf<InspirationWidgetVisitable>()
    var relatedDataView: RelatedDataView? = null
    var bannerDataView = BannerDataView()
    var categoryIdL2 = ""
    var lastFilterDataView = LastFilterDataView()
    var pageComponentId = ""
    var violation: ViolationDataView? = null
    var backendFilters: String = ""
    var keywordIntention: Int = SameSessionRecommendationConstant.DEFAULT_KEYWORD_INTENT
    var isPostProcessing: Boolean = false
    var redirectApplink: String = ""
    var productListType: String = ""
    var isShowButtonAtc: Boolean = false
    var isReimagineProductCard: Boolean = false
    var seamlessCarouselDataViewList =  listOf<InspirationCarouselDataView>()
    val defaultView: Int
        get() =
            if (productListType == LIST_VIEW) ViewType.LIST.value
            else ViewType.SMALL_GRID.value

    val gridType: String
        get() =
            if (isReimagineProductCard) FIXED_GRID
            else productListType.takeIf { it == FIXED_GRID } ?: ""

    val suggestion: String
        get() = suggestionModel?.suggestion.orEmpty()

    val relatedKeyword: String
        get() = relatedDataView?.relatedKeyword.orEmpty()

    fun isAdvancedNegativeKeywordSearch(): Boolean {
        if (keywordProcess.isNullOrEmpty()) return false
        return keywordProcess.toIntOrZero() in ADVANCED_NEGATIVE_KEYWORD_RANGE
    }

    companion object {
        private const val ADVANCED_NEGATIVE_KEYWORD_PROCESS_START = 16
        private const val ADVANCED_NEGATIVE_KEYWORD_PROCESS_END = 31
        private val ADVANCED_NEGATIVE_KEYWORD_RANGE =
            ADVANCED_NEGATIVE_KEYWORD_PROCESS_START..ADVANCED_NEGATIVE_KEYWORD_PROCESS_END
    }
}
