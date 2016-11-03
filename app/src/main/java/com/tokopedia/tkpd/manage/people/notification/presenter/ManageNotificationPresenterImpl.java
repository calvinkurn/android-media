package com.tokopedia.tkpd.manage.people.notification.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.SettingsNotification;
import com.tokopedia.tkpd.manage.ManageConstant;
import com.tokopedia.tkpd.manage.people.notification.interactor.ManageNotificationCacheInteractor;
import com.tokopedia.tkpd.manage.people.notification.interactor.ManageNotificationCacheInteractorImpl;
import com.tokopedia.tkpd.manage.people.notification.interactor.ManageNotificationRetrofitInteractor;
import com.tokopedia.tkpd.manage.people.notification.interactor.ManageNotificationRetrofitInteractorImpl;
import com.tokopedia.tkpd.manage.people.notification.listener.ManageNotificationFragmentView;
import com.tokopedia.tkpd.manage.people.notification.model.Notification;
import com.tokopedia.tkpd.manage.people.notification.model.SettingNotification;
import com.tokopedia.tkpd.util.SessionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 6/22/16.
 */
public class ManageNotificationPresenterImpl implements ManageNotificationPresenter, ManageConstant{

    ManageNotificationFragmentView viewListener;
    ManageNotificationCacheInteractor cacheInteractor;
    ManageNotificationRetrofitInteractor networkInteractor;

    public ManageNotificationPresenterImpl(ManageNotificationFragmentView viewListener) {
        this.viewListener = viewListener;
        this.cacheInteractor = new ManageNotificationCacheInteractorImpl();
        this.networkInteractor = new ManageNotificationRetrofitInteractorImpl();
    }

    @Override
    public void onSaveSetting() {
        viewListener.showLoading();
        networkInteractor.setNotificationSetting(viewListener.getActivity(),
                getSetting().getParamEditNotification(),
                new ManageNotificationRetrofitInteractor.SetNotificationSettingListener() {
                    @Override
                    public void onSuccess(String status) {
                        SettingNotification settingNotification = new SettingNotification();
                        settingNotification.setNotification(getSetting());
                        cacheInteractor.setSettingNotificationCache(settingNotification);

                        viewListener.finishLoading();
                        viewListener.getActivity().getIntent().putExtra(STATUS_MESSAGE, status);
                        viewListener.getActivity().setResult(Activity.RESULT_OK,
                                viewListener.getActivity().getIntent());
                        viewListener.getActivity().finish();
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showSnackbar();
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.showSnackbar(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showSnackbar();
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        viewListener.showSnackbar();
                    }
                });
    }

    private Notification getSetting() {
        Notification setting = new Notification();
        setting.setFlagAdminMessage(viewListener.getFlagAdminMessage());
        setting.setFlagMessage(viewListener.getFlagMessage());
        setting.setFlagNewsletter(viewListener.getFlagNewsletter());
        setting.setFlagReview(viewListener.getFlagReview());
        setting.setFlagTalkProduct(viewListener.getFlagTalkProduct());
        return setting;
    }

    @Override
    public void onGoToSetRing() {
        viewListener.getActivity().startActivity(new Intent(viewListener.getActivity(), SettingsNotification.class));
    }

    @Override
    public void checkCache() {
        cacheInteractor.getSettingNotificationCache(new ManageNotificationCacheInteractor.SettingNotificationCacheListener() {
            @Override
            public void onSuccess(SettingNotification result) {
                viewListener.finishLoading();
                viewListener.setToUI(result);
                getNotificationSetting();

            }

            @Override
            public void onError(Throwable e) {
                viewListener.showLoading();
                getNotificationSetting();
            }
        });
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    private void getNotificationSetting() {
        networkInteractor.getNotificationSetting(viewListener.getActivity(),
                getNotificationSettingParam(),
                new ManageNotificationRetrofitInteractor.NotificationSettingListener() {
                    @Override
                    public void onSuccess(@NonNull SettingNotification result) {
                        cacheInteractor.setSettingNotificationCache(result);
                        viewListener.finishLoading();
                        viewListener.setToUI(result);
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showSnackbar();
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.showSnackbar(error);
                    }

                    @Override
                    public void onNullData() {

                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        viewListener.showSnackbar();
                    }
                });
    }

    private Map<String, String> getNotificationSettingParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("profile_user_id", SessionHandler.getLoginID(viewListener.getActivity()));
        return param;
    }
}
