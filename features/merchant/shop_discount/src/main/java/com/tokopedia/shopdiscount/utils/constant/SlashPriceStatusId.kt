package com.tokopedia.shopdiscount.utils.constant

import androidx.annotation.IntDef
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId.Companion.CREATE
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId.Companion.ONGOING
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId.Companion.PAUSED
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId.Companion.SCHEDULED

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(CREATE, SCHEDULED, ONGOING, PAUSED)
annotation class SlashPriceStatusId {
    companion object {
        const val CREATE = 0
        const val SCHEDULED = 1
        const val ONGOING = 2
        const val PAUSED = 4
    }
}
