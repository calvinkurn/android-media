package com.tokopedia.search.result.domain.usecase.productwishlisturl;

import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.constant.ResponseStatus;
import com.tokopedia.search.result.network.service.TopAdsService;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;

import static com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.PRODUCT_WISHLIST_URL;

class ProductWishlistUrlUseCase extends UseCase<Boolean> {

    private final TopAdsService topAdsService;

    ProductWishlistUrlUseCase(TopAdsService topAdsService) {
        this.topAdsService = topAdsService;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsService.productWishlistUrl(requestParams.getString(PRODUCT_WISHLIST_URL, ""))
                .map(this::mapResponseToBoolean);
    }

    private boolean mapResponseToBoolean(Response<String> response) {
        if(response.isSuccessful()) {
            return getSuccessfulResponse(response);
        } else {
            throw new RuntimeException(getRuntimeErrorExceptionMessage(response.code()));
        }
    }

    private boolean getSuccessfulResponse(Response<String> response) {
        try {
            JSONObject object = new JSONObject(response.body());
            return object.getJSONObject("data").getBoolean("success");
        } catch (JSONException e) {
            return false;
        }
    }

    private String getRuntimeErrorExceptionMessage(int errorCode) {
        String errorMessage;

        switch (errorCode) {
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                errorMessage = ErrorNetMessage.MESSAGE_ERROR_SERVER;
                break;
            case ResponseStatus.SC_FORBIDDEN:
                errorMessage = ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN;
                break;
            case ResponseStatus.SC_REQUEST_TIMEOUT:
            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                errorMessage = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                break;
            default:
                errorMessage = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                break;
        }

        return errorMessage;
    }
}
