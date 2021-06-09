package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartAccordionUiModel(
        var isCollapsed: Boolean = false,
        var showMoreWording: String = "",
        var showLessWording: String = ""
) : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun deepCopy(): MiniCartAccordionUiModel {
        return MiniCartAccordionUiModel(
                isCollapsed = this.isCollapsed,
                showMoreWording = this.showMoreWording,
                showLessWording = this.showLessWording
        )
    }

}