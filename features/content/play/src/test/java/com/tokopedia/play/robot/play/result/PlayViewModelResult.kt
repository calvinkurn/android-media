package com.tokopedia.play.robot.play.result

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.robot.RobotResult
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelRobotResult(
        val viewModel: PlayViewModel
) : RobotResult {

    /**
     * Observable
     */
    val videoMetaResult: PlayVideoMetaResult
        get() = PlayVideoMetaResult(viewModel.observableVideoMeta.getOrAwaitValue())

    val likeStatusResult: PlayLikeInfoResult.LikeStatusResult
        get() = PlayLikeInfoResult.LikeStatusResult { viewModel.observableLikeStatusInfo.getOrAwaitValue() }

    val totalViewResult: PlayTotalViewResult
        get() = PlayTotalViewResult(viewModel.observableTotalViews.getOrAwaitValue())

    val cartInfoResult: PlayCartInfoResult
        get() = PlayCartInfoResult(viewModel.observableCartInfo.getOrAwaitValue())

    val quickReplyResult: PlayQuickReplyResult
        get() = PlayQuickReplyResult(viewModel.observableQuickReply.getOrAwaitValue())

    val shareInfoResult: PlayShareInfoResult
        get() = PlayShareInfoResult(viewModel.observableShareInfo.getOrAwaitValue())

    val channelInfoResult: PlayChannelInfoResult
        get() = PlayChannelInfoResult(viewModel.observableChannelInfo.getOrAwaitValue())

    val pinnedMessageResult: PlayPinnedMessageResult
        get() = PlayPinnedMessageResult(viewModel.observablePinnedMessage.getOrAwaitValue())

    val pinnedProductResult: PlayPinnedProductResult
        get() = PlayPinnedProductResult(viewModel.observablePinnedProduct.getOrAwaitValue())

    val statusInfoResult: PlayStatusInfoResult
        get() = PlayStatusInfoResult(viewModel.observableStatusInfo.getOrAwaitValue())

    val bottomInsetsResult: PlayBottomInsetsResult
        get() = PlayBottomInsetsResult(viewModel.observableBottomInsetsState.getOrAwaitValue())

    val newChatResult: PlayNewChatResult
        get() = PlayNewChatResult { viewModel.observableNewChat.getOrAwaitValue() }

    val onboardingResult: PlayOnboardingResult
        get() = PlayOnboardingResult { viewModel.observableOnboarding.getOrAwaitValue() }

    /**
     * Field
     */
    val videoOrientationFieldResult: PlayVideoMetaResult.VideoOrientationResult
        get() = PlayVideoMetaResult.VideoOrientationResult(viewModel.videoOrientation)

    val videoPlayerFieldResult: PlayVideoMetaResult.VideoPlayerResult
        get() = PlayVideoMetaResult.VideoPlayerResult(viewModel.videoPlayer)

    val statusTypeFieldResult: PlayStatusInfoResult.StatusTypeResult
        get() = PlayStatusInfoResult.StatusTypeResult(viewModel.statusType)

    val channelTypeFieldResult: PlayChannelInfoResult.ChannelTypeResult
        get() = PlayChannelInfoResult.ChannelTypeResult(viewModel.channelType)

    val likeParamInfoFieldResult: PlayLikeInfoResult.LikeParamResult
        get() = PlayLikeInfoResult.LikeParamResult(viewModel.likeParamInfo)

    val lastCompleteChannelDataFieldResult: PlayChannelDataResult
        get() = PlayChannelDataResult(viewModel.latestCompleteChannelData)

    val pipStateFieldResult: PlayPiPStateResult
        get() = PlayPiPStateResult(viewModel.pipState)

    /**
     * State
     */
    fun withState(
            dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
            fn: suspend PlayViewerNewUiState.() -> Unit
    ) = runBlockingTest(dispatcher.coroutineDispatcher) {
        state().fn()
    }

    suspend fun state() = viewModel.uiState.first()
}