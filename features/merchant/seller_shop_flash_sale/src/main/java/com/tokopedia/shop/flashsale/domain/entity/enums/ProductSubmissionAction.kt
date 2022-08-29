package com.tokopedia.shop.flashsale.domain.entity.enums

private const val ACTION_ID_RESERVE = 0
private const val ACTION_ID_SUBMIT = 1
private const val ACTION_ID_DELETE = 2

enum class ProductionSubmissionAction(val id : Int) {
    RESERVE(ACTION_ID_RESERVE),
    SUBMIT(ACTION_ID_SUBMIT),
    DELETE(ACTION_ID_DELETE)
}