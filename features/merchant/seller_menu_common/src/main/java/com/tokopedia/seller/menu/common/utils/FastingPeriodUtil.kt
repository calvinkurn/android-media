package com.tokopedia.seller.menu.common.utils

import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import java.util.*

object FastingPeriodUtil {

    private const val SUHOOR_START_TIME = 2
    private const val FASTING_START_TIME = 5
    private const val IFTAR_START_TIME = 18
    private const val MINUTE_HALF_HOUR = 30
    private const val TWO_AM = 2

    /**
     * Get Ramadhan thematic illustration url according to its shop type and current hour of day
     * @param   shopType    type of shop (RM, PM, or OS)
     * @return  illustration image Url
     */
    fun getThematicIllustrationUrl(shopType: ShopType): String {
        return getFastingPeriod().run {
            when(shopType) {
                is RegularMerchant -> regularMerchantIllustrationUrl
                is PowerMerchantStatus -> powerMerchantIllustrationUrl
                is ShopType.OfficialStore -> officialStoreIllustrationUrl
            }
        }
    }

    /**
     * Suhoor : 2.30AM - 5PM
     * Fasting : 5AM - 6PM
     * Iftar : 6PM - 2.30AM
     */
    private fun getFastingPeriod(): FastingPeriod {
        Calendar.getInstance().run {
            return when(val hour = get(Calendar.HOUR_OF_DAY)) {
                in SUHOOR_START_TIME until FASTING_START_TIME -> {
                    if (hour == TWO_AM) {
                        get(Calendar.MINUTE).let { minute ->
                            if (minute < MINUTE_HALF_HOUR) {
                                FastingPeriod.IFTAR
                            } else {
                                FastingPeriod.SUHOOR
                            }
                        }
                    } else {
                        FastingPeriod.SUHOOR
                    }
                }
                in FASTING_START_TIME until IFTAR_START_TIME -> FastingPeriod.FASTING
                else -> FastingPeriod.IFTAR
            }
        }
    }

}