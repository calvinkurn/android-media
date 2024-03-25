package com.tokopedia.chatbot.chatbot2.csat.view

import com.tokopedia.csat_rating.dynamiccsat.domain.model.CsatModel

sealed interface CsatEvent {
    data class NavigateToSubmitCsat(val csatData: CsatModel) : CsatEvent
    data class UpdateButton(val isEnabled: Boolean) : CsatEvent
    object FallbackDismissBottomSheet : CsatEvent
}
