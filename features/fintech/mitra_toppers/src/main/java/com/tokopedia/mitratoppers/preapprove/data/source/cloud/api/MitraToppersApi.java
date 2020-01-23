package com.tokopedia.mitratoppers.preapprove.data.source.cloud.api;

import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.network.data.model.response.DataResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface MitraToppersApi {

    @GET(MitraToppersBaseURL.PATH_PREAPPROVE_BALANCE + "{id}" )
    Observable<Response<DataResponse<ResponsePreApprove>>> preApproveBalance(@Path("id") String merchantId);
}
