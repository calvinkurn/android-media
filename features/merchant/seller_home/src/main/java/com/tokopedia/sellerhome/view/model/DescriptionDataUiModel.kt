package com.tokopedia.sellerhome.view.model

data class DescriptionDataUiModel(
        val state: DescriptionState,
        val description: String)

enum class DescriptionState {
    LOADING,
    ERROR,
    IDEAL
}