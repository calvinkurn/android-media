package com.tokopedia.vouchercreation.common.consts

import androidx.annotation.IntDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherSourceConst.PRIVATE, VoucherSourceConst.PUBLIC)
annotation class VoucherSourceConst {
    companion object {
        const val PRIVATE = 0
        const val PUBLIC = 1
    }
}
