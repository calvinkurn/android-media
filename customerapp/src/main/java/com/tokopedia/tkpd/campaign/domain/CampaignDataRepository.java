package com.tokopedia.tkpd.campaign.domain;

import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignDataRepository {
    Observable<CampaignResponseEntity> getCompaignData(HashMap<String, Object> params);
    Observable<CampaignResponseEntity> getCampaignFromAudio(HashMap<String, Object> params) ;
}
