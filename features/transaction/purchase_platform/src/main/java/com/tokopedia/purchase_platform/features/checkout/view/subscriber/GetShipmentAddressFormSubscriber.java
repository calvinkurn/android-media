package com.tokopedia.purchase_platform.features.checkout.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter;
import com.tokopedia.purchase_platform.common.data.apiservice.CartResponseErrorException;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 31/10/18.
 */

public class GetShipmentAddressFormSubscriber extends Subscriber<CartShipmentAddressFormData> {

    private final ShipmentPresenter shipmentPresenter;
    private final ShipmentContract.View view;
    private final boolean isReloadData;
    private final boolean isOneClickShipment;

    public GetShipmentAddressFormSubscriber(ShipmentPresenter shipmentPresenter,
                                            ShipmentContract.View view,
                                            boolean isReloadData,
                                            boolean isOneClickShipment) {
        this.shipmentPresenter = shipmentPresenter;
        this.view = view;
        this.isReloadData = isReloadData;
        this.isOneClickShipment = isOneClickShipment;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isReloadData) {
            view.hideLoading();
        } else {
            view.hideInitialLoading();
        }

        String errorMessage = e.getMessage();
        if (!(e instanceof CartResponseErrorException)) {
            errorMessage = ErrorHandler.getErrorMessage(view.getActivityContext(), e);
        }
        view.showToastError(errorMessage);

        view.stopTrace();
    }

    @Override
    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (isReloadData) {
            view.hideLoading();
        } else {
            view.hideInitialLoading();
        }

        if (cartShipmentAddressFormData.isError()) {
            view.showToastError(cartShipmentAddressFormData.getErrorMessage());
        } else {
            if (cartShipmentAddressFormData.getGroupAddress() == null || cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                view.renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
            } else {
                shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                view.renderCheckoutPage(!isReloadData, isOneClickShipment);
                view.stopTrace();
            }
        }
    }
}
