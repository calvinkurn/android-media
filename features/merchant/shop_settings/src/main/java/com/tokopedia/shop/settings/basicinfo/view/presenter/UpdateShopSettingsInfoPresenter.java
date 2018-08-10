package com.tokopedia.shop.settings.basicinfo.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;


public class UpdateShopSettingsInfoPresenter extends BaseDaggerPresenter<UpdateShopSettingsInfoPresenter.View> {

    private GetShopBasicDataUseCase getShopBasicDataUseCase;
    private UpdateShopBasicDataUseCase updateShopBasicDataUseCase;

    public interface View extends CustomerView {
        void onSuccessUpdateShopBasicData(String successMessage);
        void onErrorUpdateShopBasicData(Throwable throwable);
        void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel);
        void onErrorGetShopBasicData(Throwable throwable);
    }

    @Inject
    public UpdateShopSettingsInfoPresenter(GetShopBasicDataUseCase getShopBasicDataUseCase,
                                           UpdateShopBasicDataUseCase updateShopBasicDataUseCase) {
        this.getShopBasicDataUseCase = getShopBasicDataUseCase;
        this.updateShopBasicDataUseCase = updateShopBasicDataUseCase;
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

    public void updateShopBasicData(String tagline, String description, String logoCode) {
        updateShopBasicDataUseCase.unsubscribe();
        updateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams(tagline,
                description, logoCode, null, null), createUpdateBasicInfoSubscriber());
    }

    public void updateShopBasicData(String tagline, String description, String filePath, String fileName) {
        updateShopBasicDataUseCase.unsubscribe();
        updateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams(tagline,
                description, null, filePath, fileName), createUpdateBasicInfoSubscriber());
    }

    private Subscriber<String> createUpdateBasicInfoSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorUpdateShopBasicData(e);
                }
            }

            @Override
            public void onNext(String successMessage) {
                if (isViewAttached()) {
                    getView().onSuccessUpdateShopBasicData(successMessage);
                }
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopBasicDataUseCase.unsubscribe();
        updateShopBasicDataUseCase.unsubscribe();
    }


}
