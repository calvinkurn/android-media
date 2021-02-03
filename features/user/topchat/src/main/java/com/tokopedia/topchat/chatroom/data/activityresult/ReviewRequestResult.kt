package com.tokopedia.topchat.chatroom.data.activityresult

import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel

class ReviewRequestResult(
        val review: ReviewUiModel,
        val lastKnownPosition: Int
)