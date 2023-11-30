package com.tokopedia.product.detail.data.model.gwp

import com.tokopedia.product.detail.data.model.asUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 02/08/23"
 * Project name: android-tokopedia-core
 **/

fun GWPData.Data.asUiModel(separator: String) =
    GWPWidgetUiModel(
        title = title,
        iconUrl = iconUrl,
        cards = cards.asUiModel(loadMoreText = loadMoreText),
        backgroundColor = backgroundColor,
        action = action.asUiModel(),
        titleColor = titleColor,
        separator = separator,
        offerId = offerId
    )

fun List<GWPData.Data.Card>.asUiModel(loadMoreText: String): List<GWPWidgetUiModel.Card> {
    val products = map { it.asUiModel() }

    return if (loadMoreText.isNotBlank()) {
        val loadMore = GWPWidgetUiModel.Card.LoadMore(title = loadMoreText)
        products + listOf(loadMore)
    } else {
        products
    }
}

fun GWPData.Data.Card.asUiModel() = GWPWidgetUiModel.Card.Product(
    title = title,
    productName = productName,
    subTitle = subtitle,
    images = contents.asUiModel()
)

fun List<GWPData.Data.Card.Content>.asUiModel() = map {
    GWPWidgetUiModel.Card.Product.Images(
        imageUrl = it.imageUrl,
        counter = it.loadMoreText
    )
}
