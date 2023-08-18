package com.tokopedia.stories.view.viewmodel.event

import com.tokopedia.stories.view.utils.StoriesSharingComponent

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    data class TapSharing (val metadata: StoriesSharingComponent.StoriesSharingMetadata) : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object OpenProduct : StoriesUiEvent
}
