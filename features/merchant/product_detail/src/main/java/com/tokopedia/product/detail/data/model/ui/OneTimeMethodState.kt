package com.tokopedia.product.detail.data.model.ui

import com.tokopedia.product.detail.common.data.model.re.RestrictionData

data class OneTimeMethodState(
    val event: OneTimeMethodEvent = OneTimeMethodEvent.Empty,
    val impressRestriction: Boolean = false,
    val impressGeneralEduBS: Boolean = false
)

sealed class OneTimeMethodEvent {
    data class ImpressRestriction(
        val reData: RestrictionData
    ) : OneTimeMethodEvent()
    data class ImpressGeneralEduBs(
        val appLink: String
    ) : OneTimeMethodEvent()
    object Empty : OneTimeMethodEvent()
}
