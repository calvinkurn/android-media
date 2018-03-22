package com.tokopedia.core.manage.people.profile.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created on 6/27/16.
 */
public interface ManagePeopleProfileFragmentPresenter {

    void setOnFirstTimeLaunch(Context context);

    void setOnRequestError(String messageError);

    void setOnRequestTimeOut(NetworkErrorHelper.RetryClickedListener listener);

    void setOnRequestSuccess(Profile result);

    void onSaveButtonClicked(Context context);

    void setOnAvatarClickListener(Context context);

    void setOnChangeEmailButtonClick(Context context, String userEmail);

    void setOnChangePhoneButtonClick(Context context, String userPhone);

    void setOnVerificationButtonClick(Context context, String userPhone);

    void setOnAddEmailClick(Context click);

    void setOnChangeNameClick(Context context);

    void setOnNotifiedEmailChanged(Context context);

    void refreshPage(Context context);

    void setOnFragmentDestroyed(Context context);

    void processResult(Context context, int resultCode, Bundle resultData);

    void setOnUserFinishPickImage(String data);

    void setOnRequestStart();
}
