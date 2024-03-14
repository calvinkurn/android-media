package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.nest.principles.utils.ImpressionHolder

data class AutoCompleteUnifyDataView(
    val domainModel: SuggestionUnify = SuggestionUnify(),
    val searchTerm: String = "",
    val dimension90: String = "",
    val appLogIndex: Int = 0,
    val shopAdsDataView: ShopAdsDataView? = null
) : SearchComponentTracking by searchComponentTracking(
    trackingOption = domainModel.tracking.trackingOption,
    keyword = searchTerm,
    valueName = domainModel.title.text,
    componentId = domainModel.tracking.componentId,
    applink = domainModel.applink,
    dimension90 = dimension90
) {
    val impressionHolder = ImpressionHolder()

    val groupId: String
        get() = domainModel.tracking.tts.groupId

    val sugType: String
        get() = domainModel.tracking.tts.sugType
    val uniqueIdentifier = System.currentTimeMillis()

    data class ShopAdsDataView(
        val clickUrl: String = "",
        val impressionUrl: String = "",
        val imageUrl: String = "",
    )
}
