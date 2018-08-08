package com.tokopedia.core.manage.people.profile.interactor;

import android.content.Context;

import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.network.NetworkErrorHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by stevenfredian on 4/26/16.
 */
public interface ManagePeopleProfileInteractor {

    void getProfile(Context context, Map<String, String> param, GetProfileListener getProfileListener);

    void saveProfile(Context context, Map<String, String> param, EditProfileListener listener);

    void editEmail(Context context, Map<String, String> param, EditEmailListener listener);

    void sendOTPEditEmail(Context context, Map<String, String> param, RequestOTPListener listener);


    void unSubscribe();

    interface GetProfileListener {

        void onStart();

        void onError(String error);

        void onTimeout(NetworkErrorHelper.RetryClickedListener listener);

        void onSuccess(Profile result);

        void onNullData();
    }

    interface EditProfileListener {
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }

    interface EditEmailListener {
        void onSuccess();

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }

    interface RequestOTPListener {
        void onSuccess();

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }


}
