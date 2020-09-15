package com.tokopedia.checkout.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.ShipmentContract;
import com.tokopedia.checkout.view.ShipmentPresenter;
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 31/10/18.
 */

public class GetShipmentAddressFormSubscriber extends Subscriber<CartShipmentAddressFormData> {

    private final ShipmentPresenter shipmentPresenter;
    private final ShipmentContract.View view;
    private final boolean isReloadData;
    private final boolean isOneClickShipment;
    private final boolean isReloadAfterPriceChangeHinger;

    public GetShipmentAddressFormSubscriber(ShipmentPresenter shipmentPresenter,
                                            ShipmentContract.View view,
                                            boolean isReloadData,
                                            boolean isReloadAfterPriceChangeHinger,
                                            boolean isOneClickShipment) {
        this.shipmentPresenter = shipmentPresenter;
        this.view = view;
        this.isReloadData = isReloadData;
        this.isReloadAfterPriceChangeHinger = isReloadAfterPriceChangeHinger;
        this.isOneClickShipment = isOneClickShipment;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isReloadData) {
            view.setHasRunningApiCall(false);
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
            view.setHasRunningApiCall(false);
            view.resetPromoBenefit();
            view.clearTotalBenefitPromoStacking();
            view.hideLoading();
        } else {
            view.hideInitialLoading();
        }

        if (cartShipmentAddressFormData.isError()) {
            if (cartShipmentAddressFormData.isOpenPrerequisiteSite()) {
                view.onCacheExpired(cartShipmentAddressFormData.getErrorMessage());
            } else {
                view.showToastError(cartShipmentAddressFormData.getErrorMessage());
            }
        } else {
            if (cartShipmentAddressFormData.getGroupAddress() == null || cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                view.renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
            } else {
                shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                view.renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment);
                view.stopTrace();
            }
        }
    }
}
