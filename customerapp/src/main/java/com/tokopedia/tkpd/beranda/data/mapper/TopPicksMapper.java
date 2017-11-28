package com.tokopedia.tkpd.beranda.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksMapper implements Func1<Response<String>, TopPicksResponseModel>{

    private final Gson gson;

    public TopPicksMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public TopPicksResponseModel call(Response<String> stringResponse) {
        TopPicksResponseModel responseModel = new TopPicksResponseModel();
        if(stringResponse.isSuccessful()){
            responseModel = gson.fromJson(stringResponse.body(), TopPicksResponseModel.class);
            responseModel.setSuccess(true);
        } else {
            responseModel.setSuccess(false);
        }
        return responseModel;
    }
}
