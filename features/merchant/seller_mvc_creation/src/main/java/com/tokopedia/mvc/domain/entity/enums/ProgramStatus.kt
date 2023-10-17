package com.tokopedia.mvc.domain.entity.enums

enum class ProgramStatus(val id: Int)  {
    DRAFT(0),
    CREATED(1),
    IN_SUBMISSION(2),
    IN_REVIEW_LOCK(3),
    IN_REVIEW_START(4),
    IN_REVIEW_END(5),
    ONGOING(6),
    FINISHED(-1),
    CREATED_CANCEL(7),
    IN_SUBMISSION_CANCEL(8),
    REVIEW_LOCK_CANCEL(9),
    IN_REVIEW_CANCEL(10),
    ONGOING_CANCEL(11),
}
