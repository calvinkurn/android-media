package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.nest.principles.utils.ImpressionHolder

data class AutoCompleteUnifyDataView(
    val domainModel: SuggestionUnify = SuggestionUnify(),
    val searchTerm: String = "",
    val dimension90: String = "",
    val appLogIndex: Int = 0
) : SearchComponentTracking by searchComponentTracking(
    trackingOption = domainModel.tracking.trackingOption,
    keyword = searchTerm,
    valueId = "0",
    valueName = "${domainModel.title}${if (domainModel.subtitle.text.isNotEmpty()) "|${domainModel.subtitle}" else ""}",
    componentId = domainModel.tracking.componentId,
    applink = domainModel.applink,
    dimension90 = dimension90
) {
    val impressionHolder = ImpressionHolder()
}
