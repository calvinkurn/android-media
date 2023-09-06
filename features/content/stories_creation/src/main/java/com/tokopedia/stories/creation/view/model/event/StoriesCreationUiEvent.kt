package com.tokopedia.stories.creation.view.model.event

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
sealed interface StoriesCreationUiEvent {

    object ShowTooManyStoriesReminder : StoriesCreationUiEvent
}
