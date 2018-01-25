package com.tokopedia.tkpd.campaign.domain;



import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.campaign.data.entity.CampaignRequestEntity;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignDataRepository {
    Observable<CampaignResponseEntity> getCompaignData(TKPDMapParam<String, Object> params);
    Observable<CampaignResponseEntity> getCampaignFromAudio(TKPDMapParam<String, Object> params) ;
}
