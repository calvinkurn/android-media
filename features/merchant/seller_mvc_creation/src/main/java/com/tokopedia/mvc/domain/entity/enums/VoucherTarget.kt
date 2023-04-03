package com.tokopedia.mvc.domain.entity.enums

enum class VoucherTarget(val id: Int) {
    PUBLIC(1),
    PRIVATE(0);

    companion object {
        fun mapToIsPublic(isPublic: Boolean): Int {
            return if (isPublic) {
                PUBLIC.id
            } else {
                PRIVATE.id
            }
        }
    }
}
