package com.tokopedia.shop.flash_sale.domain.entity.enums

private const val STATUS_ID_DRAFT = 2
private const val STATUS_ID_INSUBMISSION = 4
private const val STATUS_ID_INREVIEW = 5
private const val STATUS_ID_READY = 6
private const val STATUS_ID_ONGOING = 7
private const val STATUS_ID_FINISHED = 8
private const val STATUS_ID_PUBLISH_CANCELLED = 9
private const val STATUS_ID_SUBMISSION_CANCELLED = 10
private const val STATUS_ID_REVIEW_CANCELLED = 11
private const val STATUS_ID_READY_CANCELLED = 12
private const val STATUS_ID_ONGOING_CANCELLED = 13
private const val STATUS_ID_READYLOCKED = 14
private const val STATUS_ID_READYLOCKCANCELLED = 15

enum class StatusIdFilter(val statusList: List<Int>) {
    DRAFT(listOf(STATUS_ID_DRAFT)),
    ACTIVE(listOf(
        STATUS_ID_INSUBMISSION,
        STATUS_ID_INREVIEW,
        STATUS_ID_READY,
        STATUS_ID_ONGOING,
        STATUS_ID_READYLOCKED
    )),
    RIWAYAT(listOf(
        STATUS_ID_FINISHED,
        STATUS_ID_PUBLISH_CANCELLED,
        STATUS_ID_SUBMISSION_CANCELLED,
        STATUS_ID_REVIEW_CANCELLED,
        STATUS_ID_READY_CANCELLED,
        STATUS_ID_ONGOING_CANCELLED,
        STATUS_ID_READYLOCKCANCELLED
    ))
}