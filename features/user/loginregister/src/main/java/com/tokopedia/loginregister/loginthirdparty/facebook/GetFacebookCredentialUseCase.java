package com.tokopedia.loginregister.loginthirdparty.facebook;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.loginregister.R;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 10/11/17.
 */

public class GetFacebookCredentialUseCase {

    private static final String PARAM_CALLBACK_MANAGER = "PARAM_CALLBACK_MANAGER";
    private static final String PARAM_FRAGMENT = "PARAM_FRAGMENT";
    private static final List<String> READ_PERMISSIONS = Arrays.asList("public_profile", "email",
            "user_birthday");

    @Inject
    public GetFacebookCredentialUseCase() {
    }

    public void execute(RequestParams requestParams, GetFacebookCredentialSubscriber subscriber) {
        Fragment fragment = (Fragment) requestParams.getObject(PARAM_FRAGMENT);
        CallbackManager callbackManager = (CallbackManager) requestParams.getObject(PARAM_CALLBACK_MANAGER);
        promptFacebookLogin(fragment, callbackManager, subscriber);

    }

    private void promptFacebookLogin(Fragment fragment, CallbackManager callbackManager,
                                     final GetFacebookCredentialSubscriber subscriber) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, READ_PERMISSIONS);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (fragment.getContext() == null)
                    return;

                if (TextUtils.isEmpty(loginResult.getAccessToken().getToken())) {
                    LoginManager.getInstance().logOut();
                    subscriber.onError(new MessageErrorException(
                            fragment.getContext().getString(R.string.facebook_error_not_authorized),
                            String.valueOf(ErrorHandlerSession.ErrorCode.EMPTY_ACCESS_TOKEN)));
                } else {
                    getFacebookEmail(loginResult.getAccessToken(), subscriber, fragment.getContext());
                }
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException e) {
                if (fragment.getContext() == null)
                    return;

                LoginManager.getInstance().logOut();
                if (e instanceof FacebookAuthorizationException) {
                    subscriber.onError(new MessageErrorException(
                            fragment.getContext().getString(R.string.facebook_error_not_authorized),
                            String.valueOf(ErrorHandlerSession.ErrorCode
                                    .FACEBOOK_AUTHORIZATION_EXCEPTION)));
                } else {
                    subscriber.onError(new MessageErrorException(
                            fragment.getContext().getString(R.string.facebook_error_not_authorized),
                            String.valueOf(ErrorHandlerSession.ErrorCode.FACEBOOK_EXCEPTION)));
                }
            }
        });

    }

    private void getFacebookEmail(final AccessToken accessToken, final GetFacebookCredentialSubscriber subscriber, Context context) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (object, response) -> {
                    try {
                        String email = object.getString("email");
                        subscriber.onSuccess(accessToken, email);
                    } catch (JSONException | NullPointerException e) {
                        LoginManager.getInstance().logOut();
                        subscriber.onError(new MessageErrorException(
                                context.getString(R.string.facebook_error_not_authorized),
                                String.valueOf(ErrorHandlerSession.ErrorCode
                                        .FACEBOOK_EXCEPTION)));
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public static RequestParams getParam(Fragment fragment, CallbackManager callbackManager) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_FRAGMENT, fragment);
        params.putObject(PARAM_CALLBACK_MANAGER, callbackManager);
        return params;
    }
}
