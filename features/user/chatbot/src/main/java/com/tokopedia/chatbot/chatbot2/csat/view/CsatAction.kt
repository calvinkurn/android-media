package com.tokopedia.chatbot.chatbot2.csat.view

import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel

sealed interface CsatAction {
    data class SelectScore(val pointModel: PointModel) : CsatAction
    data class SelectReason(val reason: String) : CsatAction
    data class UnselectReason(val reason: String) : CsatAction
    data class SetOtherReason(val reason: String) : CsatAction
    object SendCsat : CsatAction
}
