package com.tokopedia.search.result.data.mapper.favoriteshoplist;

import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.constant.ResponseStatus;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import retrofit2.Response;
import rx.functions.Func1;

final class FavoriteShopListMapper implements Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> {

    @Override
    public FavoriteShopListModel call(Response<FavoriteShopListModel> favoriteShopListModelResponse) {
        if(favoriteShopListModelResponse.isSuccessful()) {
            FavoriteShopListModel favoriteShopListModel = favoriteShopListModelResponse.body();

            if(favoriteShopListModel != null) {
                return favoriteShopListModel;
            }
            else {
                throw new RuntimeException(favoriteShopListModelResponse.errorBody().toString());
            }
        }
        else {
            throw new RuntimeException(getRuntimeErrorExceptionMessage(favoriteShopListModelResponse.code()));
        }
    }

    private String getRuntimeErrorExceptionMessage(int errorCode) {
        String messageError;

        switch (errorCode) {
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                messageError = ErrorNetMessage.MESSAGE_ERROR_SERVER;
                break;
            case ResponseStatus.SC_FORBIDDEN:
                messageError = ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN;
                break;
            case ResponseStatus.SC_REQUEST_TIMEOUT:
            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                messageError = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                break;
            default:
                messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                break;
        }

        return messageError;
    }
}
