package com.tokopedia.tkpd.beranda.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.homeMenu.LayoutRow;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;

import retrofit2.Response;
import rx.functions.Func1;


/**
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeCategoryMapper implements Func1<Response<String>, HomeCategoryResponseModel> {

    private final Gson gson;

    public HomeCategoryMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public HomeCategoryResponseModel call(Response<String> stringResponse) {
        HomeCategoryResponseModel responseModel = new HomeCategoryResponseModel();
        if(stringResponse.isSuccessful()) {
            responseModel = gson.fromJson(stringResponse.body(), HomeCategoryResponseModel.class);
            responseModel.setSuccess(true);
        } else {
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

}
