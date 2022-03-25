package com.tokopedia.search.result.presentation.model

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.violation.ViolationDataView
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

class ProductDataView {
    var productList = listOf<ProductItemDataView>()
    var additionalParams = ""
    var autocompleteApplink: String? = null
    var responseCode: String? = null
    var keywordProcess: String? = null
    var errorMessage: String? = null
    var tickerModel: TickerDataView? = null
    var suggestionModel: SuggestionDataView? = null
    var totalData = 0
    var isQuerySafe = false
    var adsModel: TopAdsModel = TopAdsModel()
    var cpmModel: CpmModel? = null
    var globalNavDataView: GlobalNavDataView? = null
    var inspirationCarouselDataView = listOf<InspirationCarouselDataView>()
    var inspirationWidgetDataView = listOf<InspirationWidgetVisitable>()
    var defaultView = 0
    var relatedDataView: RelatedDataView? = null
    var totalDataText = ""
    var bannerDataView = BannerDataView()
    var categoryIdL2 = ""
    var lastFilterDataView = LastFilterDataView()
    var pageComponentId = ""
    var violation: ViolationDataView? = null

    fun isAdvancedNegativeKeywordSearch(): Boolean {
        if (keywordProcess.isNullOrEmpty()) return false
        return keywordProcess.toIntOrZero() in 16..31
    }
}