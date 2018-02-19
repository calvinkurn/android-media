package com.tokopedia.tkpd.campaign.source;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.RequestBody;
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
    public Observable<CampaignResponseEntity> getCampaignFromShake(HashMap<String, RequestBody> params) {
        return campaignDataFactory.createCloudCampaignDataStore().getCampaignForShake(params);
    }
}
