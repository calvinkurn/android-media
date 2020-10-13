package com.tokopedia.buyerorder.others;

import com.google.gson.JsonObject;
import com.tokopedia.buyerorder.others.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import rx.Observable;

public interface ICreditCardRepository {

    Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject object);
}
