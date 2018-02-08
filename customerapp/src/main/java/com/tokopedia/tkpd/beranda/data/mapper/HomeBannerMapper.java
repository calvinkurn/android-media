package com.tokopedia.tkpd.beranda.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeBannerMapper implements Func1<Response<String>, HomeBannerResponseModel> {

    private final Gson gson;

    public HomeBannerMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public HomeBannerResponseModel call(Response<String> stringResponse) {
        HomeBannerResponseModel responseModel = new HomeBannerResponseModel();
        if(stringResponse.isSuccessful()){
            responseModel = gson.fromJson(stringResponse.body(), HomeBannerResponseModel.class);
            responseModel.setSuccess(true);
        } else {
            responseModel.setSuccess(false);
        }
        return responseModel;
    }
}
