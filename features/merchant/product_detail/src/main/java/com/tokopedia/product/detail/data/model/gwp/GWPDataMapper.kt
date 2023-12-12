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
    val cardUiModel = cards.asUiModel(
        loadMoreText = loadMoreText,
        parentAction = actionUiModel,
        componentTitle = title,
        offerId = offerId,
        productIDs = productIDs.joinToString(",")
    )

    return GWPWidgetUiModel(
        title = title,
        iconUrl = iconUrl,
        cards = cardUiModel,
        backgroundColor = backgroundColor,
        action = actionUiModel,
        titleColor = titleColor,
        separator = separator,
        offerId = offerId
    )
}

/**
 * mapping list of GWPDataOfCard to list of GWPWidgetUiModel.Card
 * @param loadMoreText as text for load more card
 * @param parentAction as if load more text is not empty, so add new card with load more type and set action to this
 * @param componentTitle is text from gwp of component title for tracker data support
 * @param offerId as product offer id, in here for tracker data support
 * @param productIDs one offerid can have multi products, in here for tracker data support
 */
private fun List<GWPData.Data.Card>.asUiModel(
    loadMoreText: String,
    parentAction: ActionUiModel,
    componentTitle: String,
    offerId: String,
    productIDs: String
): List<GWPWidgetUiModel.Card> {
    val isMultipleTier = loadMoreText.isNotBlank()
    val products = createProductCard(isMultipleTier, componentTitle, offerId, productIDs)

    return if (isMultipleTier) {
        val loadMore =
            createLoadMoreCard(loadMoreText, parentAction, componentTitle, offerId, productIDs)
        products + listOf(loadMore)
    } else {
        products
    }
}

private fun List<GWPData.Data.Card>.createLoadMoreCard(
    loadMoreText: String,
    parentAction: ActionUiModel,
    componentTitle: String,
    offerId: String,
    productIDs: String
) = GWPWidgetUiModel.Card.LoadMore(
    title = loadMoreText,
    action = parentAction,
    trackData = GWPWidgetUiModel.CardTrackData(
        componentTitle = componentTitle,
        cardCount = size,
        offerId = offerId,
        allText = loadMoreText,
        productIds = productIDs
    )
)

private fun List<GWPData.Data.Card>.createProductCard(
    isMultipleTier: Boolean,
    componentTitle: String,
    offerId: String,
    productIDs: String
) = map {
    it.asUiModel(
        multipleTier = isMultipleTier,
        cardCount = size,
        componentTitle = componentTitle,
        offerId = offerId,
        productIDs = productIDs
    )
}

/**
 * mapping GWPDataOfCard to GWPWidgetUiModel.Card
 * @param multipleTier as a flag to determine the width of the card
 * @param cardCount as number of card for tracker data support
 * @param componentTitle is text from gwp of component title for tracker data support
 * @param offerId as product offer id, in here for tracker data support
 * @param productIDs one offerid can have multi products, in here for tracker data support
 */
private val multipleTierWidth by lazy { 308.toPx() }
private val singleTierWidth by lazy { getScreenWidth() - 32.toPx() }
private fun GWPData.Data.Card.asUiModel(
    multipleTier: Boolean,
    cardCount: Int,
    componentTitle: String,
    offerId: String,
    productIDs: String
): GWPWidgetUiModel.Card.Product {
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
        width = width,
        trackData = GWPWidgetUiModel.CardTrackData(
            componentTitle = componentTitle,
            cardCount = cardCount,
            offerId = offerId,
            allText = "$title, $productName, $subtitle",
            productIds = productIDs
        )
    )
}

// mapping list of GWPDataOfCardContent to list of GWPWidgetUiModel.CardContent
private fun List<GWPData.Data.Card.Content>.asUiModel() = map {
    GWPWidgetUiModel.Card.Product.Images(
        imageUrl = it.imageUrl,
        counter = it.loadMoreText
    )
}
