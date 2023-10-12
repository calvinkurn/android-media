package com.tokopedia.mvc.domain.entity.enums

enum class ProgramStatus(val id: Int)  {
    DRAFT(0),
    CREATED(1),
    IN_SUBMISSION(2),
    IN_REVIEW_LOCK(3),
    IN_REVIEW_START(4),
    IN_REVIEW_END(5),
    ONGOING(6),
    FINISHED(7),
    CREATED_CANCEL(8),
    IN_SUBMISSION_CANCEL(9),
    REVIEW_LOCK_CANCEL(10),
    IN_REVIEW_CANCEL(11),
    IN_PREPARATION_CANCEL(12),
    ONGOING_CANCEL(13),
    DELETED(14)
}
