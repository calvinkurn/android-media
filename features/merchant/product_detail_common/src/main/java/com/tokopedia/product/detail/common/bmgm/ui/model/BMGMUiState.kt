package com.tokopedia.product.detail.common.bmgm.ui.model

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

sealed interface BMGMUiState {

    object Loading: BMGMUiState

    data class Loaded(val uiModel: BMGMUiModel): BMGMUiState
}
