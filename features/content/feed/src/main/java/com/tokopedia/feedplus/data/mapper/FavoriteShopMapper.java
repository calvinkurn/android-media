package com.tokopedia.feedplus.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopMapper implements Func1<Response<TkpdResponse>, Boolean> {

    @Override
    public Boolean call(Response<TkpdResponse> tkpdResponseResponse) {
        if (tkpdResponseResponse.isSuccessful())
            if (!tkpdResponseResponse.body().isError()) {
                return true;
            } else {
                String errorMessage = tkpdResponseResponse.body().getErrorMessages().toString();
                return false;
            }
        else {
            return false;
        }
    }
}
