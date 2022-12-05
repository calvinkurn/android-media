package com.tokopedia.entertainment.pdp.adapter.factory

import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel

/**
 * Author firmanda on 17,Nov,2022
 */

interface EventRedeemRevampTypeFactory {
    fun type(uiModel: ParticipantUiModel): Int
    fun type(uiModel: ParticipantTitleUiModel): Int
}
