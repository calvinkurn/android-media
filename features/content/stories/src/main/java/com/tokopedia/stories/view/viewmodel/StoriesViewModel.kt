package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tokopedia.stories.data.StoriesRepository
import com.tokopedia.stories.view.viewmodel.action.StoriesAction
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val repository: StoriesRepository,
) : ViewModel() {

    private var shopId: String = ""
    private var storiesId: String = ""
    var mCounter = mutableStateOf(1)

    var mTotalSteps = mutableStateOf(0)
    var mCurrentSteps = mutableStateOf(1)
    var mPercent = mutableStateOf(0F)
    var mIsPause = mutableStateOf(false)

    var mNextPage = MutableStateFlow(0)

    fun submitAction(event: StoriesAction) {
        when (event) {
           is StoriesAction.SetInitialData -> handleSetInitialData(event.data)
            StoriesAction.NextPage -> handleNextPage()
            StoriesAction.PreviousPage -> handlePreviousPage()
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()
        storiesId = data?.getString(STORIES_ID, "").orEmpty()
    }

    private fun handleNextPage() {
        mNextPage.value += 1
    }

    private fun handlePreviousPage() {
        mNextPage.value -= 1
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
    }

}
