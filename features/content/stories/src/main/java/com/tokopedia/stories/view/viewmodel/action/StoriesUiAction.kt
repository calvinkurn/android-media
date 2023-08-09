package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle

sealed interface StoriesUiAction {

    data class SelectCategories(val selectedCategories: Int): StoriesUiAction
    data class SetInitialData(val data: Bundle?): StoriesUiAction

    object PauseStories: StoriesUiAction

    object ResumeStories: StoriesUiAction

    object NextStories: StoriesUiAction

    object PreviousStories: StoriesUiAction

    object NextCategory: StoriesUiAction

    object PreviousCategory: StoriesUiAction
}
