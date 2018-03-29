package com.tokopedia.tkpd.campaign.source;


import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.source.api.CampaignAPI;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by sandeepgoyal on 15/12/17.
 */

public class CampaignDataStore {
    private final CampaignAPI mCampaignAPI;

    public CampaignDataStore(CampaignAPI mCampaignAPI) {
        this.mCampaignAPI = mCampaignAPI;
    }

    public Observable<CampaignResponseEntity> getCampaign(HashMap<String, Object> param) {
        return this.mCampaignAPI.getCampaign(param).map(new Func1<Response<DataResponse<CampaignResponseEntity>>, CampaignResponseEntity>() {
            @Override
            public CampaignResponseEntity call(Response<DataResponse<CampaignResponseEntity>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }

    public Observable<CampaignResponseEntity> getCampaignFromAudio(HashMap<String, RequestBody> param, MultipartBody.Part requestBody) {
        return this.mCampaignAPI.getCampaignAudio(param,requestBody).map(new Func1<Response<DataResponse<CampaignResponseEntity>>, CampaignResponseEntity>() {
            @Override
            public CampaignResponseEntity call(Response<DataResponse<CampaignResponseEntity>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }
    public Observable<CampaignResponseEntity> getCampaignForShake(HashMap<String, RequestBody> param) {
        return this.mCampaignAPI.getCampaignForShake(param).map(new Func1<Response<DataResponse<CampaignResponseEntity>>, CampaignResponseEntity>() {
            @Override
            public CampaignResponseEntity call(Response<DataResponse<CampaignResponseEntity>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }
}
