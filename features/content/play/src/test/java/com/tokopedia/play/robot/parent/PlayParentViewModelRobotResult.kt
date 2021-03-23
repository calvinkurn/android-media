package com.tokopedia.play.robot.parent

import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.robot.RobotResult
import com.tokopedia.play.robot.parent.result.PlayParentChannelDataResult
import com.tokopedia.play.robot.parent.result.PlayParentChannelIdResult
import com.tokopedia.play.robot.parent.result.PlayParentUserIdResult
import com.tokopedia.play.view.viewmodel.PlayParentViewModel

/**
 * Created by jegul on 10/02/21
 */
class PlayParentViewModelRobotResult(
        private val viewModel: PlayParentViewModel
) : RobotResult {

    val channelIdResult: PlayParentChannelIdResult
        get() = PlayParentChannelIdResult(
                viewModel.observableChannelIdsResult.getOrAwaitValue()
        )

    val userIdResult: PlayParentUserIdResult
        get() = PlayParentUserIdResult(
                viewModel.userId
        )

    fun channelDataResult(channelId: String): PlayParentChannelDataResult {
        return try {
            PlayParentChannelDataResult(viewModel.getLatestChannelStorageData(channelId))
        } catch (e: Throwable) {
            PlayParentChannelDataResult(e)
        }
    }
}