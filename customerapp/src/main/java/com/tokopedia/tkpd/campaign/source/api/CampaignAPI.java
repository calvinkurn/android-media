package com.tokopedia.tkpd.campaign.source.api;

import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.barcode.CampaignDataRepository;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignAPI {
    @GET(CampaignURL.BARCODE_CAMPAIGN)
    Observable<CampaignResponseEntity> getCampaign(@QueryMap Map<String, Object> param);

}
