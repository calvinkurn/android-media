package com.tokopedia.stories.view.viewmodel.event

sealed interface StoriesUiEvent {
    data class NextPage(val position: Int) : StoriesUiEvent
}
