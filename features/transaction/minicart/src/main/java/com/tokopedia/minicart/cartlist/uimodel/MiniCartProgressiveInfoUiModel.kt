package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartProgressiveInfoUiModel(
    val offerId: Long,
    val message: String,
    val icon: String,
    val appLink: String,
    val state: State
) : Visitable<MiniCartListAdapterTypeFactory> {
    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int = typeFactory.type(this)

    enum class State {
        LOADING,
        LOADED,
        ERROR
    }
}
