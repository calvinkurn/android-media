package com.tokopedia.mvc.presentation.list.constant

import com.tokopedia.mvc.domain.entity.enums.VoucherStatus

enum class ApplinkFilterStatus(val id: String, val statuses: List<VoucherStatus>) {
    ACTIVE("active",
        listOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING)),
    HISTORY("history",
        listOf(VoucherStatus.ENDED, VoucherStatus.STOPPED)),
    ONGOING("ongoing",
        listOf(VoucherStatus.ONGOING)),
    UPCOMING("upcoming",
        listOf(VoucherStatus.NOT_STARTED)),
    ALL("all",
        listOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING, VoucherStatus.ENDED, VoucherStatus.STOPPED));

    companion object {
        fun getById(id: String) = values().firstOrNull { it.id == id }?.statuses ?: ALL.statuses
    }
}
