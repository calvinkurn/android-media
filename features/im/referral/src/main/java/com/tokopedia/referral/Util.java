package com.tokopedia.referral;

import com.google.gson.JsonObject;
import com.tokopedia.referral.domain.GetReferralDataUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

public class Util {
    public static RequestParams getPostRequestBody(UserSession userSession){
        RequestParams params = RequestParams.create();
        if(userSession.isLoggedIn()) {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty(GetReferralDataUseCase.Companion.getUserId(), Integer.parseInt(userSession.getUserId()));
            requestBody.addProperty(GetReferralDataUseCase.Companion.getMsisdn(), userSession.getPhoneNumber());
            params.getParameters().put(GetReferralDataUseCase.Companion.getData(), requestBody);
        }
        return params;
    }
}
