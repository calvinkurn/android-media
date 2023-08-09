package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle

sealed interface StoriesUiAction {

    data class SelectPage(val selectedPage: Int): StoriesUiAction
    data class SetInitialData(val data: Bundle?): StoriesUiAction

    object NextIndicator: StoriesUiAction

    object NextPage: StoriesUiAction

    object PreviousPage: StoriesUiAction
}
