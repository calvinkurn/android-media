package com.tokopedia.sellerhomecommon.domain.model

import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel

/**
 * Created by @ilhamsuaib on 18/07/22.
 */

data class UnificationDataFetchModel(
    val unificationDataKey: String = "",
    val shopId: String = "",
    val tabs: List<UnificationTabUiModel> = emptyList()
)