package com.tokopedia.vouchercreation.common.consts

import androidx.annotation.IntDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherRecommendationStatus.WITH_RECOMMENDATION,
        VoucherRecommendationStatus.EDITED_RECOMMENDATION,
        VoucherRecommendationStatus.NO_RECOMMENDATION)
annotation class VoucherRecommendationStatus {

    companion object {
        const val WITH_RECOMMENDATION = 0
        const val EDITED_RECOMMENDATION = 1
        const val NO_RECOMMENDATION = 2
    }
}