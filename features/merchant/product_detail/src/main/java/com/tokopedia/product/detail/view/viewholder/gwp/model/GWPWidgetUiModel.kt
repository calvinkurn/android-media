package com.tokopedia.product.detail.view.viewholder.gwp.model

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
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
        val action: ActionUiModel
        val trackData: CardTrackData

        data class Product(
            val title: String,
            val productName: String,
            val subTitle: String,
            val images: List<Images> = emptyList(),
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
            override val action: ActionUiModel = ActionUiModel(),
            override val trackData: CardTrackData = CardTrackData()
        ) : Card {
            override val id: Int
                get() = GWPCardOfProductViewHolder.ID

            data class Images(val imageUrl: String = "", val counter: String = "")
        }

        data class LoadMore(
            val title: String,
            override val action: ActionUiModel = ActionUiModel(),
            override val trackData: CardTrackData = CardTrackData()
        ) : Card {
            override val id: Int
                get() = GWPCardOfShowMoreViewHolder.ID
        }
    }

    data class CardTrackData(
        val componentTitle: String = "",
        val offerId: String = "",
        val cardCount: Int = Int.ZERO,
        val allText: String = "",
        val productIds: String = ""
    ) {

        var parentTrackData: ComponentTrackDataModel? = null

        var position: Int = -Int.ONE
    }

    val showSeparatorBottom
        get() = separator == Separator.BOTTOM || separator == Separator.BOTH
}
