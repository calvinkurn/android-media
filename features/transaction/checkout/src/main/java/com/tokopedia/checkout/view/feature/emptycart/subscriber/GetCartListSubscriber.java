package com.tokopedia.checkout.view.feature.emptycart.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartApi;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class GetCartListSubscriber extends Subscriber<CartListData> {

    private final EmptyCartContract.View view;
    private final EmptyCartContract.Presenter presenter;

    public GetCartListSubscriber(EmptyCartContract.View view,
                                 EmptyCartContract.Presenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            if (!view.isTraceStopped()) {
                presenter.setLoadApiStatus(EmptyCartApi.CART_LIST, true);
                view.stopTrace();
            }
            view.hideLoading();
            view.showErrorToast(ErrorHandler.getErrorMessage(view.getContext(), e));
        }
    }

    @Override
    public void onNext(CartListData cartListData) {
        if (view != null) {
            if (!view.isTraceStopped()) {
                presenter.setLoadApiStatus(EmptyCartApi.CART_LIST, true);
                view.stopTrace();
            }
            view.hideLoading();
            if (!cartListData.getShopGroupDataList().isEmpty()) {
                view.navigateToCartFragment(cartListData);
            } else {
                String autoApplyMessage = null;
                if (cartListData.getAutoApplyData() != null &&
                        cartListData.getAutoApplyData().isSuccess()) {
                    autoApplyMessage = cartListData.getAutoApplyData().getTitleDescription();
                }
                view.renderEmptyCart(autoApplyMessage);
            }
        }
    }
}
