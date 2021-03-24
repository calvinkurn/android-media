package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory

/**
 * Created By @ilhamsuaib on 15/02/21
 */

data class StatisticMenuItemUiModel(
        val clickApplink: String? = null,
        override val title: String = "",
        override val iconUnify: Int? = null,
        override val clickAction: () -> Unit = {}
) : MenuItemUiModel(title, null, clickApplink, "", "", null, iconUnify, 0, clickAction) {

    override fun type(typeFactory: OtherMenuTypeFactory): Int {
        return typeFactory.type(this)
    }
}