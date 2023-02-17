package com.tokopedia.autocompletecomponent.initialstate.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.convertToSearchBarEducationDataView

data class MpsDataView(
    var header: String = "",
    var labelAction: String = "",
    val list: List<BaseItemInitialStateSearch> = mutableListOf(),
): Visitable<InitialStateTypeFactory> {
    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(
            data: InitialStateData,
            dimension90: String,
            keyword: String,
        ) : MpsDataView? {
            val items = data.items.map { item ->
                BaseItemInitialStateSearch(
                    itemId = item.itemId,
                    template = item.template,
                    imageUrl = item.imageUrl,
                    applink =  item.applink,
                    url = item.url,
                    title = item.title,
                    subtitle = item.subtitle,
                    iconTitle = item.iconTitle,
                    iconSubtitle = item.iconSubtitle,
                    label = item.label,
                    labelType = item.labelType,
                    shortcutImage = item.shortcutImage,
                    productId = item.itemId,
                    dimension90 = dimension90,
                    type = item.type,
                    position = 1,
                    componentId = item.componentId,
                    trackingOption = data.trackingOption,
                    keyword = keyword,
                )
            }

            return MpsDataView(data.header, data.labelAction, items)
//            return if (items.isNotEmpty()) MpsDataView(data.header, data.labelAction, items)
//            else null
        }
    }
}
