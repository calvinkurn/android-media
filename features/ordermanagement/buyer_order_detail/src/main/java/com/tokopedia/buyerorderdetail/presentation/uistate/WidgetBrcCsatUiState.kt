package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.WidgetBrcCsatUiModel

/**
 * The [WidgetBrcCsatUiState] represent every possible state of the BRC CSAT widget. Generally the
 * widget will have 2 state ([NoData] and [HasData]) depending on whether the widget already have
 * the data required to show or not. Each general state also have it's own states breakdown such as:
 *
 * * [NoData] have 3 states:
 * 1. [NoData.Hidden] represent the state where the widget doesn't have any data to show and must be
 * hidden from the UI (the widget will be on this state if the BE return an empty data or an error occurred)
 * 2. [NoData.Reloading] represent the state where the widget doesn't have any data to show and must
 * be hidden from the UI but it is reloading a new data right now (the widget will be on this state if
 * previously the widget is on the [NoData.Hidden] state and the new data is still reloading)
 * 3. [NoData.Loading] represent the state where the widget doesn't have any data to show and must be
 * hidden from the UI (this is the default state of the widget so the widget will only get into this
 * state on initial state)
 *
 * * [HasData] have 3 states:
 * 1. [HasData.Reloading] represent the state where the widget already have the data to show to the UI
 * and must be visible on the UI but it is reloading a new data right now (the widget will be in this
 * state if previously the widget is on the [HasData.Showing] state and the new data is still reloading),
 * the [HasData.Reloading.data] come from the [HasData.Showing.data].
 * 2. [HasData.Showing] represent the state where the widget already have the data to show on the UI
 * and must be visible on the UI
 */
sealed interface WidgetBrcCsatUiState {
    sealed interface NoData : WidgetBrcCsatUiState {
        object Hidden : NoData
        object Reloading : NoData
        object Loading : NoData
    }

    sealed interface HasData : WidgetBrcCsatUiState {

        val data: WidgetBrcCsatUiModel

        data class Reloading(override val data: WidgetBrcCsatUiModel) : HasData
        data class Showing(override val data: WidgetBrcCsatUiModel) : HasData
    }

    companion object {
        fun getDefaultState() = NoData.Loading
    }
}
