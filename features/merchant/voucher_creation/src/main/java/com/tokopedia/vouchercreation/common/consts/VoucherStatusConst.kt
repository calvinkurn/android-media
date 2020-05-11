package com.tokopedia.vouchercreation.common.consts

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 11/05/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherStatusConst.DELETED, VoucherStatusConst.PROCESSING,
        VoucherStatusConst.NOT_STARTED, VoucherStatusConst.ON_GOING,
        VoucherStatusConst.ENDED, VoucherStatusConst.STOPPED)
annotation class VoucherStatusConst {

    companion object {
        const val DELETED = -1
        const val PROCESSING = 0
        const val NOT_STARTED = 1
        const val ON_GOING = 2
        const val ENDED = 3
        const val STOPPED = 4
    }
}