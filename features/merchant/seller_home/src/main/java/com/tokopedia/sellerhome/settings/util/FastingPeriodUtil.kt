package com.tokopedia.sellerhome.settings.util

import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import java.util.*

object FastingPeriodUtil {

    private const val SUHOOR_START_TIME = 0
    private const val FASTING_START_TIME = 5
    private const val IFTAR_START_TIME = 17

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

    private fun getFastingPeriod(): FastingPeriod {
        return when(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in SUHOOR_START_TIME until FASTING_START_TIME -> FastingPeriod.SUHOOR
            in FASTING_START_TIME until IFTAR_START_TIME -> FastingPeriod.FASTING
            else -> FastingPeriod.IFTAR
        }
    }

}