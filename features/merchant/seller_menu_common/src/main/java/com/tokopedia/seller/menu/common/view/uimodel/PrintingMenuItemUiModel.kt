package com.tokopedia.seller.menu.common.view.uimodel

class PrintingMenuItemUiModel(
        override val title: String = "",
        override val iconUnify: Int? = null,
        override val clickAction: () -> Unit = {})
    : MenuItemUiModel(title, null, null, "", "",
        null, iconUnify, 0, true, clickAction)