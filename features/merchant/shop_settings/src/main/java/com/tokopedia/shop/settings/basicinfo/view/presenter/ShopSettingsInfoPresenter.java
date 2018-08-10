package com.tokopedia.shop.settings.basicinfo.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

public class ShopSettingsInfoPresenter extends BaseDaggerPresenter<ShopSettingsInfoPresenter.View> {

    private GetShopBasicDataUseCase getShopBasicDataUseCase;

    public interface View extends CustomerView {
        void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel);
        void onErrorGetShopBasicData(Throwable throwable);
    }

    @Inject
    public ShopSettingsInfoPresenter(GetShopBasicDataUseCase getShopBasicDataUseCase) {
        this.getShopBasicDataUseCase = getShopBasicDataUseCase;
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

    @Override
    public void detachView() {
        super.detachView();
        getShopBasicDataUseCase.unsubscribe();
    }
}
