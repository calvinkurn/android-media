package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle

sealed interface StoriesUiAction {

    data class SetInitialData(val data: Bundle?): StoriesUiAction
    data class SelectGroup(val selectedGroup: Int): StoriesUiAction

    object NextGroup: StoriesUiAction

    object PreviousGroup: StoriesUiAction

    object NextDetail: StoriesUiAction

    object PreviousDetail: StoriesUiAction

    object PauseStories: StoriesUiAction

    object ResumeStories: StoriesUiAction

    object OpenKebabMenu : StoriesUiAction

    object OpenProduct : StoriesUiAction
}
