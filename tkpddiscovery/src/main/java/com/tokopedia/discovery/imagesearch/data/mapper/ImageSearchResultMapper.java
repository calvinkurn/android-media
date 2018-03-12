package com.tokopedia.discovery.imagesearch.data.mapper;

import com.aliyuncs.imagesearch.model.v20180120.SearchItemResponse;
import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchResultModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchResultMapper implements Func1<Response<String>, ImageSearchResultModel> {

    private final Gson gson;

    public ImageSearchResultMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public ImageSearchResultModel call(Response<String> response) {
        if (response.isSuccessful()) {
            SearchItemResponse imageSearchResponse = gson.fromJson(response.body(), SearchItemResponse.class);
            if (imageSearchResponse != null) {
                return mappingPojoIntoDomain(imageSearchResponse);
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private ImageSearchResultModel mappingPojoIntoDomain(SearchItemResponse imageSearchResponse) {
        ImageSearchResultModel imageSearchResultModel = new ImageSearchResultModel();
        imageSearchResultModel.setAuctionArrayList(imageSearchResponse.getAuctions());
        return imageSearchResultModel;
    }
}
