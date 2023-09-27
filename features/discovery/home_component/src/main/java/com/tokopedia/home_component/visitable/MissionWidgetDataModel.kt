package com.tokopedia.home_component.visitable

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
    val animateOnPress: Int = CardUnify2.ANIMATE_OVERLAY,
)
