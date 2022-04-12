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
        var slug: String = "",
        var isSender: Boolean = false,
        var isButtonLoading: Boolean = false,
        var campaignCode: String = "",
        var warehouseId: String = "0",
        var isDisplayed: Boolean = false,
        var userStatus: String = ""
    ): HomeSharingWidgetUiModel(
        id,
        state
    ) {
        fun display() {
            isDisplayed = true
        }
    }

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