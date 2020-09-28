package com.tokopedia.loginregister.loginthirdparty.facebook;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookRequestData;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.utils.phonenumber.PhoneNumberUtil;

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
    private static final String DEFAULT_FB_PERMISSION = "email";
    private static final String REMOTE_CONFIG_KEY_FB_PERMISSION = "android_user_fb_permission";

    @Inject
    public GetFacebookCredentialUseCase() {
    }

    public void execute(RequestParams requestParams, GetFacebookCredentialSubscriber subscriber) {
        Fragment fragment = (Fragment) requestParams.getObject(PARAM_FRAGMENT);
        CallbackManager callbackManager = (CallbackManager) requestParams.getObject(PARAM_CALLBACK_MANAGER);
        promptFacebookLogin(fragment, callbackManager, subscriber);

    }

    private void promptFacebookLogin(Fragment fragment, CallbackManager callbackManager, final GetFacebookCredentialSubscriber subscriber) {
        FacebookSdk.fullyInitialize();
        LoginManager.getInstance().logInWithReadPermissions(fragment, READ_PERMISSIONS);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (fragment.getContext() == null)
                    return;

                if (TextUtils.isEmpty(loginResult.getAccessToken().getToken())) {
                    LoginManager.getInstance().logOut();
                    subscriber.onError(new MessageErrorException(
                            fragment.getContext().getString(R.string.facebook_error_empty_token)));
                } else {
                    getFacebookEmail(loginResult.getAccessToken(), subscriber, fragment.getActivity());
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
                            fragment.getContext().getString(R.string.facebook_error_not_authorized)));
                } else {
                    subscriber.onError(new MessageErrorException(
                            fragment.getContext().getString(R.string.facebook_error_not_authorized)));
                }
            }
        });

    }

    private void getFacebookEmail(final AccessToken accessToken, final GetFacebookCredentialSubscriber subscriber, Context context) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                ((object, response) -> {
                    if (response.getError() == null && object != null) {
                        Gson gson = new Gson();
                        FacebookRequestData data = gson.fromJson(object.toString(), FacebookRequestData.class);

                        if (data != null) {
                            if (!data.getEmail().isEmpty()) {
                                subscriber.onSuccessEmail(accessToken, data.getEmail());
                            } else if (!data.getPhone().isEmpty()) {
                                subscriber.onSuccessPhone(accessToken,
                                        PhoneNumberUtil.transform(data.getPhone()));
                            } else {
                                LoginManager.getInstance().logOut();
                                subscriber.onError(new MessageErrorException(
                                        context.getString(R.string.error_login_using_facebook)));
                            }
                        }
                    } else {
                        int errorCode = response.getError().getErrorCode();
                        int subErrorCode = response.getError().getSubErrorCode();
                        String errorMessage = context.getString(R.string.error_login_using_facebook)
                                + " " + errorCode + " " + subErrorCode;
                        subscriber.onError(new MessageErrorException(errorMessage));
                    }
                })
        );
        Bundle parameters = new Bundle();
        String permission = getFacebookPermission(context);
        parameters.putString("fields", permission);
        request.setParameters(parameters);
        request.executeAsync();

    }

    private String getFacebookPermission(Context context) {
        FirebaseRemoteConfigImpl firebaseRemoteConfig = new FirebaseRemoteConfigImpl(context);
        String remoteConfigValue = firebaseRemoteConfig.getString(REMOTE_CONFIG_KEY_FB_PERMISSION, "");

        if(remoteConfigValue.isEmpty()) {
            return DEFAULT_FB_PERMISSION;
        } else {
            return remoteConfigValue;
        }
    }

    public static RequestParams getParam(Fragment fragment, CallbackManager callbackManager) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_FRAGMENT, fragment);
        params.putObject(PARAM_CALLBACK_MANAGER, callbackManager);
        return params;
    }
}
