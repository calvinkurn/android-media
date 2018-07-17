package com.tokopedia.feedplus.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.feedplus.domain.model.wishlist.RemoveWishlistDomain;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/30/17.
 */

public class RemoveWishlistMapper implements Func1<Response<TkpdResponse>, RemoveWishlistDomain> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";

    @Inject
    public RemoveWishlistMapper() {
    }

    @Override
    public RemoveWishlistDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private RemoveWishlistDomain mappingResponse(Response<TkpdResponse> response) {
        RemoveWishlistDomain model = new RemoveWishlistDomain();
        if (response.isSuccessful()) {
            if (response.code() == ResponseStatus.SC_NO_CONTENT) {
                model.setSuccess(true);
            } else {
                try {
                    String msgError = "";
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        msgError += jsonArray.get(i).toString() + " ";
                    }
                    throw new ErrorMessageException(msgError);
                } catch (Exception e) {
                    throw new ErrorMessageException(DEFAULT_ERROR);
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
