package com.tokopedia.seller.search.feature.initialsearch.view.model.compose

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller

data class InitialSearchUiState(
    val initialStateList: List<BaseInitialSearchSeller> = emptyList(),
    val titleList: List<String> = emptyList(),
    val isDismissKeyboard: Boolean = false,
    val throwable: Throwable? = null
)
