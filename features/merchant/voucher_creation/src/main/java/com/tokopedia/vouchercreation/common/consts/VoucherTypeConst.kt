package com.tokopedia.vouchercreation.common.consts

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 11/05/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherTypeConst.FREE_ONGKIR, VoucherTypeConst.DISCOUNT, VoucherTypeConst.CASHBACK)
annotation class VoucherTypeConst {

    companion object {
        const val FREE_ONGKIR = 1
        const val DISCOUNT = 2
        const val CASHBACK = 3
    }
}