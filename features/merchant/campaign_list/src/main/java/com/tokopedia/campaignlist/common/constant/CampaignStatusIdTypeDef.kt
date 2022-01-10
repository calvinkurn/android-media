package com.tokopedia.campaignlist.common.constant

import androidx.annotation.IntDef
import com.tokopedia.campaignlist.common.constant.CampaignStatusIdTypeDef.Companion.BERLANGSUNG
import com.tokopedia.campaignlist.common.constant.CampaignStatusIdTypeDef.Companion.SEGERA_HADIR
import com.tokopedia.campaignlist.common.constant.CampaignStatusIdTypeDef.Companion.TERSEDIA

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(TERSEDIA, SEGERA_HADIR, BERLANGSUNG)
annotation class CampaignStatusIdTypeDef {
    companion object {
        const val TERSEDIA = 5;
        const val SEGERA_HADIR = 6;
        const val BERLANGSUNG = 7;
    }
}
