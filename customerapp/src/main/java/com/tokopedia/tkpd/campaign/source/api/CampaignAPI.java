package com.tokopedia.tkpd.campaign.source.api;

import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignAPI {
    @FormUrlEncoded
    @POST(CampaignURL.BARCODE_CAMPAIGN)
    Observable<CampaignResponseEntity> getCampaign(@FieldMap Map<String, Object> param);

    //TODO Audio_Campagin multipart handling
    @FormUrlEncoded
    @POST(CampaignURL.BARCODE_CAMPAIGN)
    Observable<CampaignResponseEntity> getCampaignAudio(@FieldMap Map<String, Object> param);

}
