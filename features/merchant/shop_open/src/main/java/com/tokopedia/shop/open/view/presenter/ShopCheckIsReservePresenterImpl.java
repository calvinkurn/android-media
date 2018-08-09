package com.tokopedia.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.shop.open.domain.interactor.ShopIsReserveDomainUseCase;
import com.tokopedia.shop.open.view.listener.ShopOpenCheckDomainView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopCheckIsReservePresenterImpl extends BaseDaggerPresenter<ShopOpenCheckDomainView>
        implements ShopOpenCheckIsReservePresenter {

    private final ShopIsReserveDomainUseCase shopIsReserveDomainUseCase;

    @Inject
    public ShopCheckIsReservePresenterImpl(ShopIsReserveDomainUseCase shopIsReserveDomainUseCase) {
        this.shopIsReserveDomainUseCase = shopIsReserveDomainUseCase;
    }

    @Override
    public void isReservingDomain() {
        shopIsReserveDomainUseCase.execute(null, new Subscriber<ResponseIsReserveDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached() && getView() != null && getView().getActivity() != null) {
                    getView().onErrorCheckReserveDomain(e);
                }
            }

            @Override
            public void onNext(ResponseIsReserveDomain responseIsReserveDomain) {
                if (isViewAttached() && getView() != null && getView().getActivity() != null) {
                    getView().onSuccessCheckReserveDomain(responseIsReserveDomain);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        shopIsReserveDomainUseCase.unsubscribe();
    }
}
