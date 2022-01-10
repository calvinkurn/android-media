package com.tokopedia.vouchercreation.shop.create.domain.model.validation

import androidx.annotation.IntDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherTargetType.PRIVATE, VoucherTargetType.PUBLIC)
annotation class VoucherTargetType {
    companion object {
        const val PRIVATE = 0
        const val PUBLIC = 1
    }
}