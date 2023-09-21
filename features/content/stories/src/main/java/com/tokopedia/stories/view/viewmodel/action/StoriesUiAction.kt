package com.tokopedia.stories.view.viewmodel.action

import com.tokopedia.stories.view.model.StoriesGroupHeader

sealed interface StoriesUiAction {
    data class SetMainData(val selectedGroup: Int) : StoriesUiAction
    data class SelectGroup(val selectedGroup: Int, val showAnimation: Boolean) : StoriesUiAction
    data class CollectImpressedGroup(val data: StoriesGroupHeader) : StoriesUiAction

    object SetInitialData : StoriesUiAction
    object NextDetail : StoriesUiAction
    object PreviousDetail : StoriesUiAction
    object PauseStories : StoriesUiAction
    object ResumeStories : StoriesUiAction
    object ContentIsLoaded : StoriesUiAction
    object SaveInstanceStateData : StoriesUiAction
}
