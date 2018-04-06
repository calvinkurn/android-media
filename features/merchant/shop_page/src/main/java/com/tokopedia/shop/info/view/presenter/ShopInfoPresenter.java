package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.view.listener.ShopInfoView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoPresenter extends BaseDaggerPresenter<ShopInfoView> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final UserSession userSession;

    @Inject
    public ShopInfoPresenter(GetShopInfoUseCase getShopInfoUseCase, UserSession userSession) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.userSession = userSession;
    }

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopInfo(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}