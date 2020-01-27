package com.tokopedia.shop.address.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.address.view.listener.ShopAddressListView;
import com.tokopedia.shop.address.view.mapper.ShopAddressViewModelMapper;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopAddressListPresenter extends BaseDaggerPresenter<ShopAddressListView> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final ShopAddressViewModelMapper shopAddressViewModelMapper;
    private final UserSessionInterface userSession;

    @Inject
    public ShopAddressListPresenter(GetShopInfoUseCase getShopInfoUseCase, ShopAddressViewModelMapper shopAddressViewModelMapper, UserSessionInterface userSession) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.shopAddressViewModelMapper = shopAddressViewModelMapper;
        this.userSession = userSession;
    }

    public void getshopAddressList(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId, userSession.getUserId(), userSession.getDeviceId()), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().renderList(shopAddressViewModelMapper.transform(shopInfo), false);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
    }
}