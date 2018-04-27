package com.tokopedia.tkpd.campaign.view.presenter;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public interface ShakeDetectContract {
    public interface View extends CustomerView{
        void finish();

        void sendBroadcast(Intent intent);


        void showProgressDialog();

        void hideProgressDialog();

        void showErrorGetInfo(String message);

        void showErrorNetwork(String message);

        void showMessage(String message);

        void setResult(int resultCode,Intent data);

        void updateTimer(Long l);

        boolean isLongShakeTriggered();
        void setInvisibleCounter();
    }

    public interface Presenter extends CustomerPresenter<View>{
        public void onShakeDetect();
        public void onDestroyView();
        public void onRetryClick();
        public void onActivityResult(int requestCode, int resultCode, Intent data);

        void onCancelClick();
    }
}
