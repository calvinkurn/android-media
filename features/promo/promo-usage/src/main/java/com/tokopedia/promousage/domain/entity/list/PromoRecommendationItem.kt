package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.composite.DelegatePayload

data class PromoRecommendationItem(
    override val id: String = "",
    val title: String = "",
    val selectedCodes: List<String> = emptyList(),
    val codes: List<String> = emptyList(),
    val message: String = "",
    val messageSelected: String = "",
    val backgroundUrl: String = "",
    val animationUrl: String = "",
    val backgroundColor: String = "",
    val isCalculating: Boolean = false,
    val showAnimation: Boolean = false,

    // Property only for UI
    val promos: List<PromoItem> = emptyList(),
) : DelegateAdapterItem {

    override fun getChangePayload(other: Any): Any? {
        if (other is PromoRecommendationItem && id == other.id) {
            val isPromoStateUpdated = promos.any { oldPromo ->
                val newPromo = other.promos.firstOrNull { newPromo -> newPromo.id == oldPromo.id }
                oldPromo.state != newPromo?.state || oldPromo.isCalculating != newPromo.isCalculating
            }
            return DelegatePayload.UpdatePromoRecommendation(
                isReload = false,
                isPromoStateUpdated = isPromoStateUpdated
            )
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromoRecommendationItem

        if (id != other.id) return false
        if (title != other.title) return false
        if (selectedCodes != other.selectedCodes) return false
        if (codes != other.codes) return false
        if (message != other.message) return false
        if (messageSelected != other.messageSelected) return false
        if (backgroundUrl != other.backgroundUrl) return false
        if (animationUrl != other.animationUrl) return false
        if (backgroundColor != other.backgroundColor) return false
        if (isCalculating != other.isCalculating) return false
        if (showAnimation != other.showAnimation) return false
        if (promos != other.promos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + selectedCodes.hashCode()
        result = 31 * result + codes.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + messageSelected.hashCode()
        result = 31 * result + backgroundUrl.hashCode()
        result = 31 * result + animationUrl.hashCode()
        result = 31 * result + backgroundColor.hashCode()
        result = 31 * result + isCalculating.hashCode()
        result = 31 * result + showAnimation.hashCode()
        result = 31 * result + promos.hashCode()
        return result
    }
}
