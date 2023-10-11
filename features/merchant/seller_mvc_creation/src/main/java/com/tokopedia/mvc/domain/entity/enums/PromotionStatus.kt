package com.tokopedia.mvc.domain.entity.enums

enum class PromotionStatus(val id: Int) {
    DELETED(-1),
    REGISTERED(0),
    APPROVED(1),
    REJECTED(2),
    REVISION(3)
}
