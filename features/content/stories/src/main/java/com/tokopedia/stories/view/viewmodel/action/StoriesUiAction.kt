package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle

sealed interface StoriesUiAction {

    data class SetArgumentsData(val bundle: Bundle?): StoriesUiAction
    data class SaveInstanceStateData(val bundle: Bundle?): StoriesUiAction
    data class GetSavedInstanceStateData(val bundle: Bundle?): StoriesUiAction
    data class SetGroupMainData(val selectedGroup: Int): StoriesUiAction
    data class SetGroup(val selectedGroup: Int, val showAnimation: Boolean): StoriesUiAction

    object NextDetail: StoriesUiAction
    object PreviousDetail: StoriesUiAction
    object PauseStories: StoriesUiAction
    object ResumeStories: StoriesUiAction
    object ContentIsLoaded: StoriesUiAction
}
