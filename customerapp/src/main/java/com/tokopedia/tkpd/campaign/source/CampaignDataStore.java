package com.tokopedia.tkpd.campaign.source;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.campaign.data.entity.CampaignRequestEntity;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.source.api.CampaignAPI;

import rx.Observable;


/**
 * Created by sandeepgoyal on 15/12/17.
 */

public class CampaignDataStore {
    private final CampaignAPI mCampaignAPI;

    public CampaignDataStore(CampaignAPI mCampaignAPI) {
        this.mCampaignAPI = mCampaignAPI;
    }

    public Observable<CampaignResponseEntity> getCampaign(TKPDMapParam<String, Object> param) {
        return this.mCampaignAPI.getCampaign(param);
    }
}
