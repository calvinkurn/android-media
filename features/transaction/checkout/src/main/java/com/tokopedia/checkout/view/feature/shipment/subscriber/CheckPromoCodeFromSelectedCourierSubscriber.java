package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 15/10/18.
 */

public class CheckPromoCodeFromSelectedCourierSubscriber extends Subscriber<PromoCodeCartListData> {

    private final ShipmentContract.View view;
    private final int itemPosition;

    public CheckPromoCodeFromSelectedCourierSubscriber(ShipmentContract.View view,
                                                       int itemPosition) {
        this.view = view;
        this.itemPosition = itemPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.showToastError(ErrorHandler.getErrorMessage(view.getActivityContext(), e));
        }
    }

    @Override
    public void onNext(PromoCodeCartListData promoCodeCartListData) {
        if (view != null) {
            if (!promoCodeCartListData.isError()) {
                view.renderCheckPromoCodeFromCourierSuccess(promoCodeCartListData, itemPosition);
            } else {
                view.showToastError(promoCodeCartListData.getErrorMessage());
            }
        }
    }
}
