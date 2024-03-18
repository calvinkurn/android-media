package com.tokopedia.chatbot.chatbot2.csat.view

import com.tokopedia.csat_rating.dynamiccsat.domain.model.PointModel

sealed interface CsatUserAction {
    data class SelectScore(val pointModel: PointModel) : CsatUserAction
    data class SelectReason(val reason: String) : CsatUserAction
    data class UnselectReason(val reason: String) : CsatUserAction
    data class SetOtherReason(val reason: String) : CsatUserAction
    object SubmitCsat : CsatUserAction
}
