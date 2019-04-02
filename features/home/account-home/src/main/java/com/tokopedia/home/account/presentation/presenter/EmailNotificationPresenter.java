package com.tokopedia.home.account.presentation.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.home.account.data.model.AppNotificationSettingModel;
import com.tokopedia.home.account.data.model.SettingEditResponse;
import com.tokopedia.home.account.domain.GetEmailNotifUseCase;
import com.tokopedia.home.account.domain.SaveEmailNotifUseCase;
import com.tokopedia.home.account.presentation.listener.EmailNotificationView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class EmailNotificationPresenter extends BaseDaggerPresenter<EmailNotificationView> {

    private GetEmailNotifUseCase getEmailNotifUseCase;
    private SaveEmailNotifUseCase saveEmailNotifUseCase;

    @Inject
    public EmailNotificationPresenter(GetEmailNotifUseCase getEmailNotifUseCase,
                                      SaveEmailNotifUseCase saveEmailNotifUseCase) {
        this.getEmailNotifUseCase = getEmailNotifUseCase;
        this.saveEmailNotifUseCase = saveEmailNotifUseCase;
    }

    public void getEmailNotifSetting(boolean forceRefresh){
        getEmailNotifUseCase.setForceRequest(forceRefresh);
        getEmailNotifUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()){
                    getView().onErrorGetNotifSetting(throwable);
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
               Type token = new TypeToken<DataResponse<AppNotificationSettingModel.Response>>() {}.getType();
               RestResponse response = typeRestResponseMap.get(token);
               if (!response.isError()) {
                   DataResponse<AppNotificationSettingModel.Response> dataResponse = response.getData();
                   if (isViewAttached()) {
                       getView().setupNotifSetting(dataResponse.getData().notification);
                   }
               }
            }
        });
    }

    public void saveEmailNotifUseCase(HashMap<String, Integer> setting){
        saveEmailNotifUseCase.setBodyParams(setting);
        saveEmailNotifUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()){
                    getView().onErrorSaveNotifSetting(throwable);
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<SettingEditResponse>() {}.getType();
                RestResponse response = typeRestResponseMap.get(token);
                if (!response.isError()) {
                    SettingEditResponse dataResponse = response.getData();
                    if (isViewAttached()){
                        getView().onSuccesSaveSetting(dataResponse);
                    }
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getEmailNotifUseCase.unsubscribe();
        saveEmailNotifUseCase.unsubscribe();
    }
}
