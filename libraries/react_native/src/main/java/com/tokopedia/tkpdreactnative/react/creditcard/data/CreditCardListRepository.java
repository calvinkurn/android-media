package com.tokopedia.buyerorder.others;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.buyerorder.others.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class CreditCardListRepository implements ICreditCardRepository {


    private static final int SUCCESS_CODE = 200;

    private CreditCardAuthService authService;

    public CreditCardListRepository(
            CreditCardAuthService authService) {
        this.authService = authService;
    }

    @Override
    public Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject request) {
        return authService.getApi().updateWhiteList(request)
                .map(new Func1<Response<String>, AuthenticatorUpdateWhiteListResponse>() {
                    @Override
                    public AuthenticatorUpdateWhiteListResponse call(Response<String> stringResponse) {
                        AuthenticatorUpdateWhiteListResponse response = new Gson().fromJson(
                                stringResponse.body(),
                                AuthenticatorUpdateWhiteListResponse.class
                        );
                        handleDataError(response.getStatusCode() == SUCCESS_CODE, response.getMessage());
                        return response;
                    }
                });
    }

    private void handleDataError(boolean isSuccess, String message) {
        if (!isSuccess) {
            throw new ResponseRuntimeException(message);
        }
    }
}
