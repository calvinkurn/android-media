package com.tokopedia.autocomplete.initialstate.productline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateData
import java.util.ArrayList

fun InitialStateData.convertToListInitialStateProductListDataView(dimension90: String): MutableList<Visitable<*>> {
    val childList = ArrayList<BaseItemInitialStateSearch>()
    var position = 1
    for (item in this.items) {
        val model = BaseItemInitialStateSearch(
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
                featureId = this.featureId,
                header = this.header,
                type = item.type,
                discountPercentage = item.discountPercentage,
                originalPrice = item.originalPrice,
                dimension90 = dimension90,
                position = position
        )
        childList.add(model)
        position++
    }
    return arrayListOf(InitialStateProductListDataView(childList))
}