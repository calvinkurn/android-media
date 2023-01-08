package com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemDataView
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking

class ListGridDataView(
    val data: BaseUniversalDataView = BaseUniversalDataView(),
    val related: List<RelatedItemDataView> = listOf(),
): Visitable<UniversalSearchTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
    trackingOption = data.trackingOption,
    keyword = data.keyword,
    valueName = data.title,
    componentId = data.componentId,
    applink = data.applink,
    dimension90 = data.dimension90,
) {

    override fun type(typeFactory: UniversalSearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}