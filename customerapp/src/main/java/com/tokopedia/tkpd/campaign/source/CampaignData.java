package com.tokopedia.tkpd.campaign.source;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sandeepgoyal on 18/12/17.
 */

public class CampaignData implements CampaignDataRepository {

    CampaignDataFactory campaignDataFactory;

    @Inject
    public CampaignData(CampaignDataFactory campaignDataFactory) {
        this.campaignDataFactory = campaignDataFactory;
    }

    @Override
    public Observable<CampaignResponseEntity> getCompaignData(HashMap<String, Object> params) {
        return campaignDataFactory.createCloudCampaignDataStore().getCampaign(params);
    }
    @Override
    public Observable<CampaignResponseEntity> getCampaignFromAudio(TKPDMapParam<String, Object> params) {
        return campaignDataFactory.createCloudCampaignDataStore().getCampaignFromAudio(params);
    }
}
