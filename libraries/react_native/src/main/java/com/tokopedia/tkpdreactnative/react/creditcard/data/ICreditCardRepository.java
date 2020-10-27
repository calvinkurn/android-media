package com.tokopedia.tkpdreactnative.react.creditcard.data;

import com.google.gson.JsonObject;
import com.tokopedia.tkpdreactnative.react.creditcard.data.creditcardauthentication.authenticator.AuthenticatorUpdateWhiteListResponse;

import rx.Observable;

public interface ICreditCardRepository {

    Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject object);
}
