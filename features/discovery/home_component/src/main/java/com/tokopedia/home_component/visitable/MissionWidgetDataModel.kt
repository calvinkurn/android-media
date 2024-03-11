package com.tokopedia.home_component.visitable

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by dhaba
 */
data class MissionWidgetDataModel(
    val id: Long = 0L,
    val title: String = "",
    val subTitle: String = "",
    val appLink: String = "",
    val imageURL: String = "",
    val pageName: String = "",
    val categoryID: String = "",
    val productID: String = "",
    val productName: String = "",
    val recommendationType: String = "",
    val buType: String = "",
    val isTopads: Boolean = false,
    val isCarousel: Boolean = false,
    val shopId: String = "",
    val campaignCode: String = "",
    val isCache: Boolean,
    val animateOnPress: Int = CardUnify2.ANIMATE_OVERLAY, // Not included in equalsWith
    val appLog: RecommendationAppLog = RecommendationAppLog(), // Not included in equalsWith
) {
    fun equalsWith(b: MissionWidgetDataModel): Boolean = this.id == b.id
        && this.title == b.title
        && this.subTitle == b.subTitle
        && this.appLink == b.appLink
        && this.imageURL == b.imageURL
        && this.pageName == b.pageName
        && this.categoryID == b.categoryID
        && this.productID == b.productID
        && this.productName == b.productName
        && this.recommendationType == b.recommendationType
        && this.buType == b.buType
        && this.isTopads == b.isTopads
        && this.isCarousel == b.isCarousel
        && this.shopId == b.shopId
        && this.campaignCode == b.campaignCode
        && this.isCache == b.isCache
}
