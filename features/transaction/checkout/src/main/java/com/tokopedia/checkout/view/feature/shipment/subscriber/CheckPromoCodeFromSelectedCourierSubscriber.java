package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 15/10/18.
 */

public class CheckPromoCodeFromSelectedCourierSubscriber extends Subscriber<PromoCodeCartListData> {

    private final ShipmentContract.View view;
    private final int itemPosition;
    private final boolean noToast;

    public CheckPromoCodeFromSelectedCourierSubscriber(ShipmentContract.View view,
                                                       int itemPosition, boolean noToast) {
        this.view = view;
        this.itemPosition = itemPosition;
        this.noToast = noToast;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            if (e instanceof CheckPromoCodeException) {
                view.showToastError(e.getMessage());
            }else {
                view.showToastError(ErrorHandler.getErrorMessage(view.getActivityContext(), e));
            }
        }
    }

    @Override
    public void onNext(PromoCodeCartListData promoCodeCartListData) {
        if (view != null) {
            if (!promoCodeCartListData.isError()) {
                view.renderCheckPromoCodeFromCourierSuccess(promoCodeCartListData, itemPosition, noToast);
            } else {
                if (!noToast) {
                    view.showToastError(promoCodeCartListData.getErrorMessage());
                }
            }
        }
    }
}
