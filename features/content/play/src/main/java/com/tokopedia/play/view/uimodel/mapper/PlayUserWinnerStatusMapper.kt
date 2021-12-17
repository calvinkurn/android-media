package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.UserWinnerStatus
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.PlayUserWinnerStatusUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 04, 2021
 */
@PlayScope
class PlayUserWinnerStatusMapper @Inject constructor() {

    fun mapUserWinnerStatus(userWinnerStatus: UserWinnerStatus) = PlayUserWinnerStatusUiModel (
        channelId = userWinnerStatus.channelId,
        interactiveId = userWinnerStatus.interactiveId,
        userId = userWinnerStatus.userId,
        name = userWinnerStatus.name,
        imageUrl = userWinnerStatus.imageUrl,
        winnerTitle = userWinnerStatus.winnerTitle,
        winnerText = userWinnerStatus.winnerText,
        loserTitle = userWinnerStatus.loserTitle,
        loserText = userWinnerStatus.loserText,
    )
}