package com.tokopedia.feedplus.presentation.callback

import com.tokopedia.feedplus.presentation.model.FeedContentUiModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel

/**
 * Created by meyta.taliti on 11/10/23.
 */
interface FeedUiListener {

    fun onContentLoading()

    fun onContentLoaded(
        content: FeedContentUiModel,
        trackerModel: FeedTrackerDataModel?,
        uiActionListener: FeedUiActionListener,
        contentPosition: Int
    )

    fun onContentFailed()
}
