package com.tokopedia.autocompletecomponent.initialstate.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.discovery.common.constants.SearchApiConst

data class MpsDataView(
    var header: String = "",
    var labelAction: String = "",
    val list: List<BaseItemInitialStateSearch> = mutableListOf(),
    val disableAddButton: Boolean = false,
): Visitable<InitialStateTypeFactory> {
    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
    companion object {
        fun create(
            data: InitialStateData,
            dimension90: String,
            keyword: String,
            searchParameter: Map<String, String>,
        ) : MpsDataView? {
            val items = data.items.mapIndexed { index, item ->
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
                    position = index + 1,
                    componentId = item.componentId,
                    trackingOption = data.trackingOption,
                    keyword = keyword,
                )
            }

            return if (items.isNotEmpty()) MpsDataView(data.header, data.labelAction, items, searchParameter.hasMaxKeyword())
            else null
        }

        private fun Map<String, String>.hasMaxKeyword() : Boolean {
            return containsKey(SearchApiConst.Q1) && !get(SearchApiConst.Q1).isNullOrBlank()
                && containsKey(SearchApiConst.Q2) && !get(SearchApiConst.Q2).isNullOrBlank()
                && containsKey(SearchApiConst.Q3) && !get(SearchApiConst.Q3).isNullOrBlank()
        }
    }
}
