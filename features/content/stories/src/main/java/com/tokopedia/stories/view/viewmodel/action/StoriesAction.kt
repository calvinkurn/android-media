package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle

sealed interface StoriesAction {
    data class SetInitialData(val data: Bundle?): StoriesAction
    object NextPage: StoriesAction
    object PreviousPage: StoriesAction
}
