package com.tokopedia.product.detail.view.viewholder.gwp.model

import com.tokopedia.product.detail.data.util.Separator

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class GWPWidgetUiModel(
    val title: String = "",
    val iconUrl: String = "",
    val cards: List<Card> = emptyList(),
    val backgroundColor: String = "",
    val action: Action = Action(),
    val titleColor: String = "",
    val separator: String = "",
    val offerId: String = ""
) {

    sealed interface Card {
        data class Product(
            val title: String,
            val productName: String,
            val subTitle: String,
            val images: List<Images> = emptyList()
        ) : Card {
            data class Images(
                val imageUrl: String = "",
                val counter: String = ""
            )
        }

        data class LoadMore(
            val title: String
        ) : Card
    }

    data class Action(
        val type: String = "",
        val link: String = ""
    )

    val showSeparatorBottom
        get() = separator == Separator.BOTTOM || separator == Separator.BOTH
}
