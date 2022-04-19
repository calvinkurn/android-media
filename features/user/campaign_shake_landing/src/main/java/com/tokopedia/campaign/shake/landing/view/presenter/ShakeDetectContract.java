package com.tokopedia.campaign.shake.landing.view.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.utils.permission.PermissionCheckerHelper;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public interface ShakeDetectContract {
    public interface View extends CustomerView{
        void finish();

        void showErrorGetInfo();

        void showErrorNetwork(String message);

        void showMessage(String message);

        boolean isLongShakeTriggered();
        void setInvisibleCounter();
        void showDisableShakeShakeVisible();
        public void makeInvisibleShakeShakeDisableView();
        public void setCancelButtonVisible();

        public Activity getCurrentActivity();

        public void setSnackBarErrorMessage();

        void goToGeneralSetting();

        }

    public interface Presenter extends CustomerPresenter<View>{
        public void onShakeDetect();
        public void onDestroyView();
        public void onDisableShakeShake();
        public void onActivityResult(int requestCode, int resultCode, Intent data);

        void onCancelClick();

        void setPermissionChecker(PermissionCheckerHelper permissionCheckerHelper);
    }
}
