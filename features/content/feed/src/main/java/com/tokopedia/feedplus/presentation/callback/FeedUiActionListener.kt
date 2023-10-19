package com.tokopedia.feedplus.presentation.callback

import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel

/**
 * Created by meyta.taliti on 11/10/23.
 */
interface FeedUiActionListener {

    fun onCommentClick(
        trackerModel: FeedTrackerDataModel?,
        contentId: String,
        isPlayContent: Boolean,
        rowNumber: Int
    )

    fun onSharePostClicked(data: FeedShareModel, trackerModel: FeedTrackerDataModel)

}
