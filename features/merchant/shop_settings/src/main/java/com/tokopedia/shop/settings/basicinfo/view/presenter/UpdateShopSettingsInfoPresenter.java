package com.tokopedia.shop.settings.basicinfo.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase;
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel;
import com.tokopedia.shop.settings.basicinfo.domain.UploadShopImageUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;


public class UpdateShopSettingsInfoPresenter extends BaseDaggerPresenter<UpdateShopSettingsInfoPresenter.View> {

    private GetShopBasicDataUseCase getShopBasicDataUseCase;
    private UpdateShopBasicDataUseCase updateShopBasicDataUseCase;
    private UploadShopImageUseCase uploadShopImageUseCase;

    public interface View extends CustomerView {
        void onSuccessUpdateShopBasicData(String successMessage);
        void onErrorUpdateShopBasicData(Throwable throwable);
        void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel);
        void onErrorGetShopBasicData(Throwable throwable);
        void onErrorUploadShopImage(Throwable throwable);
    }

    @Inject
    public UpdateShopSettingsInfoPresenter(GetShopBasicDataUseCase getShopBasicDataUseCase,
                                           UpdateShopBasicDataUseCase updateShopBasicDataUseCase,
                                           UploadShopImageUseCase uploadShopImageUseCase) {
        this.getShopBasicDataUseCase = getShopBasicDataUseCase;
        this.updateShopBasicDataUseCase = updateShopBasicDataUseCase;
        this.uploadShopImageUseCase = uploadShopImageUseCase;
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

    public void uploadShopImage(String imagePath, String tagline, String description) {
        uploadShopImageUseCase.unsubscribe();
        uploadShopImageUseCase.execute(UploadShopImageUseCase.createRequestParams(imagePath),
                new Subscriber<UploadShopEditImageModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()){
                    getView().onErrorUploadShopImage(e);
                }
            }

            @Override
            public void onNext(UploadShopEditImageModel uploadShopEditImageModel) {
                updateShopBasicData(tagline, description, uploadShopEditImageModel.getData().getImage().getPicCode());
            }
        });
    }

    public void updateShopBasicData(String tagline, String description) {
        updateShopBasicDataUseCase.unsubscribe();
        updateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams(tagline,
                description, null, null, null), createUpdateBasicInfoSubscriber());
    }

    private void updateShopBasicData(String tagline, String description, String logoCode) {
        updateShopBasicDataUseCase.unsubscribe();
        updateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams(tagline,
                description, logoCode, null, null), createUpdateBasicInfoSubscriber());
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
        uploadShopImageUseCase.unsubscribe();
    }


}
