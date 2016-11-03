package com.tokopedia.tkpd.manage.people.profile.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.manage.people.profile.datamanager.DataManager;
import com.tokopedia.tkpd.manage.people.profile.datamanager.DataManagerImpl;
import com.tokopedia.tkpd.manage.people.profile.intentservice.ManagePeopleProfileIntentService;
import com.tokopedia.tkpd.manage.people.profile.listener.ManagePeopleProfileFragmentView;
import com.tokopedia.tkpd.manage.people.profile.model.PeopleProfilePass;
import com.tokopedia.tkpd.manage.people.profile.model.Profile;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.prototype.ShopSettingCache;
import com.tokopedia.tkpd.var.TkpdState;

/**
 * Created on 6/27/16.
 */
public class ManagePeopleProfileFragmentImpl implements ManagePeopleProfileFragmentPresenter {

    private final ManagePeopleProfileFragmentView view;
    private final DataManager dataManager;

    public ManagePeopleProfileFragmentImpl(ManagePeopleProfileFragmentView view) {
        this.view = view;
        this.dataManager = new DataManagerImpl(this);
    }

    @Override
    public void setOnFirstTimeLaunch(Context context) {
        dataManager.getProfile(context);
    }

    @Override
    public void setOnRequestStart() {
        view.setLoadingView(true);
        view.setMainView(false);
    }

    @Override
    public void setOnRequestError(String messageError) {
        view.setLoadingView(false);
        view.setMainView(false);
        view.setErrorView(messageError);
    }

    @Override
    public void setOnRequestTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
        view.setLoadingView(false);
        view.setMainView(false);
        view.setTimeOutView(listener);
    }

    @Override
    public void setOnRequestSuccess(Profile result) {
        view.setProfileData(result);
        view.renderData();
        view.setLoadingView(false);
        view.setMainView(true);
        view.storeImageToDrawer(result.getDataUser().getUserImage100());
    }

    @Override
    public void onSaveButtonClicked(Context context) {
        view.resetError();
        if (isValid(context)) {
            view.setLoadingView(true);
            view.setMainView(false);
            view.dismissKeyboard();
            view.callListenerToSave(getPeopleProfilePassParam());
        }
    }

    private PeopleProfilePass getPeopleProfilePassParam() {
        PeopleProfilePass param = new PeopleProfilePass();
        param.setDay(
                generateDay(view.getBirthDay())
        );
        param.setMonth(
                generateMonth(view.getBirthDay())
        );
        param.setYear(
                generateYear(view.getBirthDay())
        );
        param.setEmail(view.getEmail());
        param.setFullName(view.getUserName());
        param.setGender(
                generateGenderCode(view.getGender())
        );
        param.setHobby(view.getHobby());
        param.setMessenger(view.getMessanger());
        param.setMsisdn(view.getVerifiedPhone());
        param.setVerifiedPhone(view.getPhone());
        param.setPassword(view.getPassword());
        param.setImagePath(view.getImagePath());
        return param;
    }

    private String generateDay(String birthDay) {
        String str[] = birthDay.split("/");
        return str[0];
    }

    private String generateMonth(String birthDay) {
        String str[] = birthDay.split("/");
        return str[1];
    }

    private String generateYear(String birthDay) {
        String str[] = birthDay.split("/");
        return str[2];
    }

    private String generateGenderCode(int gender) {
        if (gender == R.id.male) return String.valueOf(TkpdState.Gender.MALE);
        else if (gender == R.id.female) return String.valueOf(TkpdState.Gender.FEMALE);
        else return "";
    }

    private boolean isValid(Context context) {
        boolean validation = true;

        if (view.getPassword().isEmpty()) {
            view.setPasswordError(context.getString(R.string.error_field_required));
            validation = false;
        }

        if (view.getPhone().isEmpty()) {
            view.setPhoneError(context.getString(R.string.error_field_required));
            validation = false;
        }

        if (view.getEmail().isEmpty()) {
            view.setEmailError(context.getString(R.string.error_field_required));
            validation = false;
        } else if (!CommonUtils.EmailValidation(view.getEmail())) {
            view.setEmailError(context.getString(R.string.error_invalid_email));
            validation = false;
        }

        return validation;
    }

    @Override
    public void setOnAvatarClickListener(Context context) {
        view.showUploadDialog();
    }

    @Override
    public void setOnChangeEmailButtonClick(Context context, String userEmail) {
        view.showEmailVerificationDialog(userEmail);
    }

    @Override
    public void setOnChangePhoneButtonClick(Context context, String userPhone) {
        view.showManualPhoneVerificationDialog(userPhone);
    }

    @Override
    public void setOnVerificationButtonClick(Context context, String userPhone) {
        if (!userPhone.isEmpty()) {
            view.showPhoneVerificationDialog(userPhone);
        } else {
            view.setVerificationError(context.getString(R.string.error_field_required));
        }
    }

    @Override
    public void setOnNotifiedEmailChanged(Context context) {
        refreshPage(context);
        deleteCache(context);
    }

    @Override
    public void setOnNotifiedPhoneVerified(Context context) {
        refreshPage(context);
        deleteCache(context);
    }

    @Override
    public void refreshPage(Context context) {
        dataManager.refreshProfile(context);
    }

    private void deleteCache(Context context) {
        ShopSettingCache.DeleteCache(ShopSettingCache.CODE_PROFILE, context);
    }

    @Override
    public void setOnFragmentDestroyed(Context context) {
        dataManager.unSubscribe();
    }

    @Override
    public void processResult(Context context, int resultCode, Bundle resultData) {
        if (resultCode == ManagePeopleProfileIntentService.STATUS_SUCCESS) {
            deleteCache(context);
            view.setActivityResultSuccess();
            view.finishActivity();
        } else {
            view.setLoadingView(false);
            view.setMainView(true);
            if (resultData.getInt(ManagePeopleProfileIntentService.EXTRA_PARAM_NETWORK_ERROR_TYPE) == ManagePeopleProfileIntentService.STATUS_TIME_OUT) {
                view.showSnackBarView(null);
            } else {
                view.showSnackBarView(resultData.getString(ManagePeopleProfileIntentService.EXTRA_PARAM_NETWORK_ERROR_MESSAGE));
            }
        }
    }

    @Override
    public void setOnUserFinishPickImage(String data) {
        view.prepareToUploadAvatar(data);
        view.setImagePath(data);
    }
}
