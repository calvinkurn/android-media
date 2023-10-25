package com.tokopedia.product.detail.data.model.social_proof

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.SocialProofData

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val new = (other as? SocialProofUiModel) ?: return false
        return type == new.type &&
            identifier == new.identifier &&
            title == new.title &&
            subtitle == new.subtitle &&
            icon == new.icon &&
            appLink == new.appLink
    }

    companion object {

        fun getType(type: String): Type = when (type) {
            SocialProofData.CHIP -> Type.Chip
            SocialProofData.ORANGE_CHIP -> Type.OrangeChip
            else -> Type.Text
        }

        fun getID(id: String) = when (id) {
            SocialProofData.TALK_ID -> Identifier.Talk
            SocialProofData.RATING_ID -> Identifier.Rating
            SocialProofData.MEDIA_ID -> Identifier.Media
            SocialProofData.SHOP_RATING_ID -> Identifier.ShopRating
            SocialProofData.NEW_PRODUCT_ID -> Identifier.NewProduct
            else -> Identifier.Empty
        }
    }

    sealed class Type {
        object Chip : Type()
        object Text : Type()
        object OrangeChip : Type()
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

fun SocialProofData.asUiModel() = SocialProofUiModel(
    type = SocialProofUiModel.getType(socialProofType),
    identifier = SocialProofUiModel.getID(socialProofId),
    title = title,
    subtitle = subtitle,
    icon = icon,
    appLink = appLink.appLink
)

fun List<SocialProofData>.asUiModel() = map { it.asUiModel() }
