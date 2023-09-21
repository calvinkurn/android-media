package com.tokopedia.feedplus.browse.presentation.model

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
sealed interface FeedCategoryInspirationModel {

    val modelId: String

    data class Chips(
        val id: String,
        val chipList: List<FeedBrowseChipUiModel>,
    ) : FeedCategoryInspirationModel {
        override val modelId: String = "chips_$id"
    }

    data class Card(
        val id: String,
        val imageUrl: String,
        val partnerName: String,
        val avatarUrl: String,
        val badgeUrl: String,
        val title: String,
    ) : FeedCategoryInspirationModel {
        override val modelId: String = "card_$id"
    }
}
