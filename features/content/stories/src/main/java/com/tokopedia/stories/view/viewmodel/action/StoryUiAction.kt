package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle

sealed interface StoryUiAction {

    data class SetArgumentsData(val data: Bundle?): StoryUiAction
    data class SetGroupMainData(val selectedGroup: Int): StoryUiAction

    object NextDetail: StoryUiAction

    object PreviousDetail: StoryUiAction

    object PauseStory: StoryUiAction

    object ResumeStory: StoryUiAction
}
