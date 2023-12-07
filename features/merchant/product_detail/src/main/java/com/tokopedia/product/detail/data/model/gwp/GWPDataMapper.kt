package com.tokopedia.product.detail.data.model.gwp

import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.product.detail.data.model.asUiModel
import com.tokopedia.product.detail.view.viewholder.ActionUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.unifyprinciples.microinteraction.toPx

/**
 * Created by yovi.putra on 02/08/23"
 * Project name: android-tokopedia-core
 **/

// mapping GWPData to GWPWidgetUiModel
fun GWPData.Data.asUiModel(separator: String): GWPWidgetUiModel {
    val actionUiModel = action.asUiModel()
    return GWPWidgetUiModel(
        title = title,
        iconUrl = iconUrl,
        cards = cards.asUiModel(loadMoreText = loadMoreText, parentAction = actionUiModel),
        backgroundColor = backgroundColor,
        action = actionUiModel,
        titleColor = titleColor,
        separator = separator,
        offerId = offerId
    )
}

// mapping list of GWPDataOfCard to list of GWPWidgetUiModel.Card
fun List<GWPData.Data.Card>.asUiModel(
    loadMoreText: String,
    parentAction: ActionUiModel
): List<GWPWidgetUiModel.Card> {
    val isMultipleTier = loadMoreText.isNotBlank()
    val products = map { it.asUiModel(multipleTier = isMultipleTier) }

    return if (isMultipleTier) {
        val loadMore = GWPWidgetUiModel.Card.LoadMore(title = loadMoreText, action = parentAction)
        products + listOf(loadMore)
    } else {
        products
    }
}

// mapping GWPDataOfCard to GWPWidgetUiModel.Card
private val multipleTierWidth by lazy { 308.toPx() }
private val singleTierWidth by lazy { getScreenWidth() - 32.toPx() }
fun GWPData.Data.Card.asUiModel(multipleTier: Boolean): GWPWidgetUiModel.Card.Product {
    val width = if (multipleTier) {
        multipleTierWidth
    } else {
        singleTierWidth
    }
    return GWPWidgetUiModel.Card.Product(
        title = title,
        productName = productName,
        subTitle = subtitle,
        action = action.asUiModel(),
        images = contents.asUiModel(),
        width = width
    )
}

// mapping list of GWPDataOfCardContent to list of GWPWidgetUiModel.CardContent
fun List<GWPData.Data.Card.Content>.asUiModel() = map {
    GWPWidgetUiModel.Card.Product.Images(
        imageUrl = it.imageUrl,
        counter = it.loadMoreText
    )
}
