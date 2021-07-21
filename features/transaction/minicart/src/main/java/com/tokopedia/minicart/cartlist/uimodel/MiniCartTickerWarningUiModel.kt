package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartTickerWarningUiModel(
        var warningMessage: String = ""
) : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun deepCopy(): MiniCartTickerWarningUiModel {
        return MiniCartTickerWarningUiModel(
                warningMessage = this.warningMessage
        )
    }

}