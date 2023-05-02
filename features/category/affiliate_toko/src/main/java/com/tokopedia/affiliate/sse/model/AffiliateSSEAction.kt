package com.tokopedia.affiliate.sse.model

sealed class AffiliateSSEAction {
    data class Message(val message: AffiliateSSEResponse) : AffiliateSSEAction()
    data class Close(val reason: AffiliateSSECloseReason) : AffiliateSSEAction()
}
