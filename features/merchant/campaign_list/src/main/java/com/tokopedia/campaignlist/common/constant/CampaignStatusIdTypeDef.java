package com.tokopedia.campaignlist.common.constant;

import androidx.annotation.IntDef;

@IntDef({ CampaignStatusIdTypeDef.TERSEDIA, CampaignStatusIdTypeDef.SEGERA_HADIR, CampaignStatusIdTypeDef.BERLANGSUNG })
public @interface CampaignStatusIdTypeDef {
    int TERSEDIA = 5;
    int SEGERA_HADIR = 6;
    int BERLANGSUNG = 7;
}
