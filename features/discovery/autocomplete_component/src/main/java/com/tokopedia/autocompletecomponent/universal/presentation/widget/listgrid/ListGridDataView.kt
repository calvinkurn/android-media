package com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemDataView
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking

class ListGridDataView(
    val id: String = "",
    val applink: String = "",
    val title: String = "",
    val subtitle: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val related: List<RelatedItemDataView> = listOf(),
    val keyword: String = "",
    val dimension90: String = "",
    val position: Int = 0,
): Visitable<UniversalSearchTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
    trackingOption = trackingOption,
    keyword = keyword,
    valueName = title,
    componentId = componentId,
    applink = applink,
    dimension90 = dimension90,
) {

    override fun type(typeFactory: UniversalSearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}