package com.tokopedia.stories.view.viewmodel.event

sealed interface StoryUiEvent {
    data class SelectGroup(val position: Int) : StoryUiEvent
}
