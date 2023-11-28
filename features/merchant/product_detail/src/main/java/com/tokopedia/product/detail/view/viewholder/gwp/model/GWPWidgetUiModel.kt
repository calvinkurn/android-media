package com.tokopedia.product.detail.view.viewholder.gwp.model

import com.tokopedia.product.detail.data.util.Separator
import com.tokopedia.product.detail.view.viewholder.ActionUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.adapter.GWPCardOfProductViewHolder
import com.tokopedia.product.detail.view.viewholder.gwp.adapter.GWPCardOfShowMoreViewHolder

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class GWPWidgetUiModel(
    val title: String = "",
    val iconUrl: String = "",
    val cards: List<Card> = emptyList(),
    val backgroundColor: String = "",
    val action: ActionUiModel = ActionUiModel(),
    val titleColor: String = "",
    val separator: String = "",
    val offerId: String = ""
) {

    sealed interface Card {
        val id: Int

        data class Product(
            val title: String,
            val productName: String,
            val subTitle: String,
            val images: List<Images> = emptyList()
        ) : Card {
            override val id: Int
                get() = GWPCardOfProductViewHolder.ID

            data class Images(val imageUrl: String = "", val counter: String = "")
        }

        data class LoadMore(val title: String) : Card {
            override val id: Int
                get() = GWPCardOfShowMoreViewHolder.ID
        }
    }

    val showSeparatorBottom
        get() = separator == Separator.BOTTOM || separator == Separator.BOTH
}
