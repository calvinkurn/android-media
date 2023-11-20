package com.tokopedia.product.detail.view.viewholder.bmgm.model

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

sealed interface BMGMWidgetUiState {

    object Loading : BMGMWidgetUiState

    object Hide : BMGMWidgetUiState

    data class Show(val uiModel: BMGMWidgetUiModel) : BMGMWidgetUiState
}
