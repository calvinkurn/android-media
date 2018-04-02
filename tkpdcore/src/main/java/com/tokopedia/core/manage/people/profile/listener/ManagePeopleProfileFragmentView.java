package com.tokopedia.core.manage.people.profile.listener;

import android.os.Bundle;

import com.tokopedia.core.manage.people.profile.model.PeopleProfilePass;
import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created on 6/27/16.
 */
public interface ManagePeopleProfileFragmentView {

    void setLoadingView(boolean isVisible);

    void setMainView(boolean isVisible);

    Profile getProfileData();

    void renderData();

    void setTimeOutView(NetworkErrorHelper.RetryClickedListener listener);

    void setErrorView(String messageError);

    void callListenerToSave(PeopleProfilePass paramPass);

    void resetError();

    void showUploadDialog();

    void showEmailVerificationDialog(String userEmail);

    void showPhoneVerificationDialog(String userPhone);

    void setReceiveResult(int resultCode, Bundle resultData);

    void showSnackBarView(String message);

    String getPhone();

    void setPhoneError(String errorMessage);

    String getEmail();

    void setEmailError(String errorMessage);

    String getVerifiedPhone();

    void setVerificationError(String string);

    String getMessanger();

    String getHobby();

    String getUserName();

    String getBirthDay();

    void setBirthDayError(String errorMessage);

    int getGender();

    void prepareToUploadAvatar(String data);

    String getImagePath();

    void setImagePath(String imagePathData);

    void dismissKeyboard();

    void setProfileData(Profile result);

    void storeImageToDrawer(String userImage100);

    void setActivityResultSuccess();

    void finishActivity();

    void startChangePhoneNumber();

    void storeImageToUserSession(String userImage);
}
