package com.tokopedia.forgotpassword.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.forgotpassword.data.ForgotPasswordURL;
import com.tokopedia.forgotpassword.util.TypeTokenIntializer;
import com.tokopedia.usecase.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;

public class ResetPasswordUseCase extends RestRequestSupportInterceptorUseCase {


    HashMap<String,String> data = new HashMap<>();
    @Inject
    public ResetPasswordUseCase(List<Interceptor> interceptor, @ApplicationContext Context context) {
        super(interceptor,context);
    }


    public RequestParams getRequestParams(String userId,String emailPhone, String newPassword,String repeatPassword, String otpToken) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("user_id",userId);
        try {
            requestParams.putString("emailphone", URLEncoder.encode(emailPhone, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestParams.putString("new-password",newPassword);
        requestParams.putString("repeat-new-password",repeatPassword);
        requestParams.putString("validate_token",otpToken);
        return requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        RestRequest restRequest1 = new RestRequest.Builder(ForgotPasswordURL.FORGOT_PASSWORD_DOMAIN + ForgotPasswordURL.RESET_PASSWORD, TypeTokenIntializer.getResetResponseType())
                .setRequestType(RequestType.POST).setBody(requestParams.getParameters()) .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}