package com.tokopedia.tkpd.campaign.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignAPI {
    @FormUrlEncoded
    @POST(CampaignURL.BARCODE_CAMPAIGN)
    Observable<Response<DataResponse<CampaignResponseEntity>>> getCampaign(@FieldMap Map<String, Object> param);

    //TODO Audio_Campagin multipart handling
    @FormUrlEncoded
    @POST(CampaignURL.SHAKE_CAMPAIGN)
    Observable<CampaignResponseEntity> getCampaignAudio(@FieldMap Map<String, Object> param);

    @Multipart
    @POST(CampaignURL.SHAKE_CAMPAIGN)
    Observable<Response<DataResponse<CampaignResponseEntity>>> getCampaignForShake(@PartMap Map<String, RequestBody> param);

}
