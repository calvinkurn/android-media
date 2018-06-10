package com.tokopedia.tkpd.campaign.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface CampaignAPI {
    @GET(CampaignURL.BARCODE_CAMPAIGN)
    Observable<Response<DataResponse<CampaignResponseEntity>>> getCampaign(@QueryMap Map<String, Object> param);

    //TODO Audio_Campagin multipart handling
    @Multipart
    @POST(CampaignURL.SHAKE_CAMPAIGN)
    Observable<Response<DataResponse<CampaignResponseEntity>>> getCampaignAudio(@PartMap Map<String, RequestBody> param,@Part MultipartBody.Part  audioFile);

    @GET(CampaignURL.SHAKE_CAMPAIGN)
    Observable<Response<DataResponse<CampaignResponseEntity>>> getCampaignForShake(@QueryMap Map<String, Object> param);

}
