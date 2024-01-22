package com.tokopedia.purchase_platform.common.revamp

import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import timber.log.Timber
import javax.inject.Inject

class PromoEntryPointImprovementRollenceManager @Inject constructor(
    private val abTestPlatform: AbTestPlatform
) {

    private var currentValue: String? = null

    fun enableNewInterface(): Boolean {
        try {
            if (currentValue == null) {
                currentValue = abTestPlatform.getString(RollenceKey.PROMO_ENTRY_POINT_IMPROVEMENT)
            }
            return (currentValue ?: "") == RollenceKey.PROMO_ENTRY_POINT_NEW
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            return getDefaultValue()
        }
    }

    private fun getDefaultValue(): Boolean {
        return false
    }
}
