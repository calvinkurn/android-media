package com.tokopedia.tkpd.beranda.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BrandsOfficialStoreMapper implements Func1<Response<String>, BrandsOfficialStoreResponseModel> {

    private final Gson gson;

    public BrandsOfficialStoreMapper(Gson gson) {
        this.gson = gson;
    }


    @Override
    public BrandsOfficialStoreResponseModel call(Response<String> stringResponse) {
        BrandsOfficialStoreResponseModel responseModel = new BrandsOfficialStoreResponseModel();
        if(stringResponse.isSuccessful()){
            responseModel = gson.fromJson(stringResponse.body(), BrandsOfficialStoreResponseModel.class);
            responseModel.setExpiredTime(System.currentTimeMillis() + (5 * 1000));
            responseModel.setSuccess(true);
        } else {
            responseModel.setSuccess(false);
            throw new RuntimeException("brand os error");
        }
        return responseModel;
    }
}
