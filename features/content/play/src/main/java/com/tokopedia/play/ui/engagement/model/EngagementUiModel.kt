package com.tokopedia.play.ui.engagement.model

import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play_common.model.dto.interactive.GameUiModel

/**
 * @author by astidhiyaa on 24/08/22
 */

sealed class EngagementUiModel{
    data class Game(val game: GameUiModel): EngagementUiModel()
    data class Promo(val info: PlayVoucherUiModel.Merchant, val size: Int): EngagementUiModel()
}
