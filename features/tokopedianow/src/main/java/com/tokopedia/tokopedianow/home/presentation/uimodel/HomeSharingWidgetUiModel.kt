package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

sealed class HomeSharingWidgetUiModel (
    open val id: String,
    open val state: HomeLayoutItemState
) : HomeLayoutUiModel(id)  {

    data class HomeSharingReferralWidgetUiModel(
        override val id: String,
        override val state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED,
        val slug: String,
        val isSender: Boolean
    ): HomeSharingWidgetUiModel(
        id,
        state
    )

    data class HomeSharingEducationWidgetUiModel (
        override val id: String,
        override val state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED,
        val btnTextRes: Int,
        val serviceType: String
    ): HomeSharingWidgetUiModel(
        id,
        state
    )

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}