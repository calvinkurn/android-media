package com.tokopedia.shop.settings.basicinfo.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.constant.ShopScheduleActionDef;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

public class UpdateShopShedulePresenter extends BaseDaggerPresenter<UpdateShopShedulePresenter.View> {

    private GetShopBasicDataUseCase getShopBasicDataUseCase;
    private UpdateShopScheduleUseCase updateShopScheduleUseCase;

    public interface View extends CustomerView {
        void onSuccessUpdateShopSchedule(String successMessage);
        void onErrorUpdateShopSchedule(Throwable throwable);
        void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel);
        void onErrorGetShopBasicData(Throwable throwable);
    }

    @Inject
    public UpdateShopShedulePresenter(GetShopBasicDataUseCase getShopBasicDataUseCase,
                                      UpdateShopScheduleUseCase updateShopScheduleUseCase) {
        this.getShopBasicDataUseCase = getShopBasicDataUseCase;
        this.updateShopScheduleUseCase = updateShopScheduleUseCase;
    }

    public void getShopBasicData(){
        getShopBasicDataUseCase.unsubscribe();
        getShopBasicDataUseCase.execute(RequestParams.EMPTY, new Subscriber<ShopBasicDataModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopBasicData(e);
                }
            }

            @Override
            public void onNext(ShopBasicDataModel shopBasicDataModel) {
                if (isViewAttached()) {
                    getView().onSuccessGetShopBasicData(shopBasicDataModel);
                }
            }
        });
    }

    public void updateShopSchedule(@ShopScheduleActionDef int action,
                                   String closeStart,
                                   String closeEnd,
                                   String closeNote) {
        updateShopScheduleUseCase.unsubscribe();
        updateShopScheduleUseCase.execute(UpdateShopScheduleUseCase.createRequestParams(
                action, closeStart, closeEnd, closeNote
        ), createUpdateShopScheduleSubscriber());
    }

    private Subscriber<String> createUpdateShopScheduleSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorUpdateShopSchedule(e);
                }
            }

            @Override
            public void onNext(String successMessage) {
                if (isViewAttached()) {
                    getView().onSuccessUpdateShopSchedule(successMessage);
                }
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopBasicDataUseCase.unsubscribe();
        updateShopScheduleUseCase.unsubscribe();
    }


}
