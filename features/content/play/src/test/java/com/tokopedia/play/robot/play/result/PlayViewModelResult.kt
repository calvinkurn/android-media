package com.tokopedia.play.robot.play.result

import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.view.viewmodel.PlayViewModel

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelRobotResult(
        private val viewModel: PlayViewModel
) {

    val videoMetaResult: PlayVideoMetaResult
        get() = PlayVideoMetaResult(viewModel.observableVideoMeta.getOrAwaitValue())

    val likeStatusResult: PlayLikeStatusResult
        get() = PlayLikeStatusResult(viewModel.observableLikeStatusInfo.getOrAwaitValue())

    val totalViewResult: PlayTotalViewResult
        get() = PlayTotalViewResult(viewModel.observableTotalViews.getOrAwaitValue())

    val partnerInfoResult: PlayPartnerInfoResult
        get() = PlayPartnerInfoResult(viewModel.observablePartnerInfo.getOrAwaitValue())

    val cartInfoResult: PlayCartInfoResult
        get() = PlayCartInfoResult(viewModel.observableCartInfo.getOrAwaitValue())

    val quickReplyResult: PlayQuickReplyResult
        get() = PlayQuickReplyResult(viewModel.observableQuickReply.getOrAwaitValue())

    val shareInfoResult: PlayShareInfoResult
        get() = PlayShareInfoResult(viewModel.observableShareInfo.getOrAwaitValue())

    val channelInfoResult: PlayChannelInfoResult
        get() = PlayChannelInfoResult(viewModel.observableChannelInfo.getOrAwaitValue())

    val pinnedResult: PlayPinnedResult
        get() = PlayPinnedResult(viewModel.observablePinned.getOrAwaitValue())
}