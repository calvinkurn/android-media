package com.tokopedia.favorite.data;

import com.tokopedia.favorite.domain.model.FavoritShopResponseData;
import com.tokopedia.network.data.model.response.GraphqlResponse;

import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by naveengoyal on 5/11/18.
 */

public class FavoriteShopResponseValidator {

    private static final int HTTP_ERROR_500 = 500;
    private static final int HTTP_ERROR_600 = 600;
    private static final int HTTP_ERROR_400 = 400;

    public static Action1<Response<GraphqlResponse<FavoritShopResponseData>>> validate(final HttpValidationListener listener) {

        return new Action1<Response<GraphqlResponse<FavoritShopResponseData>>>() {
            @Override
            public void call(Response<GraphqlResponse<FavoritShopResponseData>> stringResponse) {
                if (stringResponse.code() >= HTTP_ERROR_500
                        && stringResponse.code() < HTTP_ERROR_600) {
                    throw new RuntimeException("Server Error!");
                } else if (stringResponse.code() >= HTTP_ERROR_400
                        && stringResponse.code() < HTTP_ERROR_500) {

                    throw new RuntimeException("Client Error!");
                } else {
                    listener.OnPassValidation(stringResponse);
                }
            }
        };
    }

    public interface HttpValidationListener {
        void OnPassValidation(Response<GraphqlResponse<FavoritShopResponseData>> response);
    }
}
