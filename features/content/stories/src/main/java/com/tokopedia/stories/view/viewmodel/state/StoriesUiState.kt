package com.tokopedia.stories.view.viewmodel.state

import com.tokopedia.stories.view.model.StoriesUiModel

data class StoriesUiState(
    val storiesMainData: StoriesUiModel,
    val product: Any,
)
