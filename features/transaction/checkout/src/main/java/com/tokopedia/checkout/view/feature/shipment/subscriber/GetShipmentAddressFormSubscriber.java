package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.transactiondata.apiservice.CartHttpErrorException;
import com.tokopedia.transactiondata.apiservice.CartResponseDataNullException;
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
        if (e instanceof UnknownHostException) {
            view.showToastError(
                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
            );
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            view.showToastError(
                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            );
        } else if (e instanceof CartResponseErrorException) {
            view.showToastError(e.getMessage());
        } else if (e instanceof CartResponseDataNullException) {
            view.showToastError(e.getMessage());
        } else if (e instanceof CartHttpErrorException) {
            view.showToastError(e.getMessage());
        } else if (e instanceof ResponseCartApiErrorException) {
            view.showToastError(e.getMessage());
        } else {
            view.showToastError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
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
