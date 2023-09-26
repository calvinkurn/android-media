package com.tokopedia.product.detail.data.model.social_proof

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/

data class SocialProofUiModel(
    val type: Type = Type.Text,
    val identifier: Identifier = Identifier.Empty,
    val title: String = "",
    val subtitle: String = "",
    val icon: String = "",
    val appLink: String = "",
    val impressHolder: ImpressHolder = ImpressHolder()
) {

    companion object {

        fun getType(type: String): Type = when (type) {
            SocialProofData.CHIP -> Type.Chip
            SocialProofData.ORANGE_CHIP -> Type.OrangeChip
            SocialProofData.TEXT -> Type.Text
            else -> Type.Loading
        }

        fun getID(id: String) = when (id) {
            SocialProofData.TALK_ID -> Identifier.Talk
            SocialProofData.RATING_ID -> Identifier.Rating
            SocialProofData.MEDIA_ID -> Identifier.Media
            SocialProofData.SHOP_RATING_ID -> Identifier.ShopRating
            SocialProofData.NEW_PRODUCT_ID -> Identifier.NewProduct
            else -> Identifier.Empty
        }

        fun createLoader() = SocialProofUiModel(type = Type.Loading)
    }

    sealed class Type {
        object Chip : Type()
        object Text : Type()
        object OrangeChip : Type()
        object Loading : Type()
    }

    sealed class Identifier {
        object Talk : Identifier()
        object Rating : Identifier()
        object Media : Identifier()
        object ShopRating : Identifier()
        object NewProduct : Identifier()
        object Empty : Identifier()
    }
}
