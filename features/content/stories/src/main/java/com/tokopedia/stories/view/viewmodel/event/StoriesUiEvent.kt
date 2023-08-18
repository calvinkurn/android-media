package com.tokopedia.stories.view.viewmodel.event

import com.tokopedia.stories.view.model.StoriesDetailUiModel

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    data class TapSharing (val metadata: StoriesDetailUiModel.Sharing) : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object OpenProduct : StoriesUiEvent
}
