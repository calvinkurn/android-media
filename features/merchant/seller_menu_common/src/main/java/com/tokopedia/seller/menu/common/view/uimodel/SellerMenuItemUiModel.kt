package com.tokopedia.seller.menu.common.view.uimodel

/**
 * Created By @ilhamsuaib on 07/09/20
 */

data class SellerMenuItemUiModel(
        override val title: String = "",
        val clickApplink: String? = null,
        val eventActionSuffix: String = "",
        val settingTypeInfix: String = "",
        override val trackingAlias: String? = null,
        val type: String,
        override val iconUnify: Int? = null,
        override var notificationCount: Int = 0,
        override val clickAction: () -> Unit = {}
) : MenuItemUiModel(title, clickApplink, eventActionSuffix, settingTypeInfix, trackingAlias, null, notificationCount, "", clickAction)