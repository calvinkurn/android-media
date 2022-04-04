package com.tokopedia.shopdiscount.manage.domain.entity

class DiscountStatusMetaComparator : Comparator<DiscountStatusMeta> {
    override fun compare(o1: DiscountStatusMeta, o2: DiscountStatusMeta): Int {
        return when (o1.id) {
            "ACTIVE" -> 1
            "SCHEDULED" -> -1
            "PAUSED" -> -1
            else -> 0
        }
    }
}