package com.tokopedia.checkout.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.view.ShipmentContract;
import com.tokopedia.checkout.view.ShipmentPresenter;
import com.tokopedia.logisticCommon.data.entity.address.UserAddress;
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;

import java.util.List;

import rx.Subscriber;
import timber.log.Timber;

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
        Timber.d(e);
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
            List<GroupAddress> groupAddressList = cartShipmentAddressFormData.getGroupAddress();
            if (groupAddressList.isEmpty() || groupAddressList.get(0) == null || groupAddressList.get(0).getUserAddress() == null) {
                view.renderCheckoutPageNoAddress(cartShipmentAddressFormData);
            } else {
                UserAddress userAddress = groupAddressList.get(0).getUserAddress();
                renderCheckoutPage(cartShipmentAddressFormData, userAddress);
                view.stopTrace();
            }
        }
    }

    private void renderCheckoutPage(CartShipmentAddressFormData cartShipmentAddressFormData, UserAddress userAddress) {
        switch (userAddress.getState()) {
            case UserAddress.STATE_ADDRESS_ID_NOT_MATCH:
                shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                view.renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment);
                if (!UtilsKt.isNullOrEmpty(cartShipmentAddressFormData.getPopUpMessage())) {
                    view.showToastNormal(cartShipmentAddressFormData.getPopUpMessage());
                }
                view.updateLocalCacheAddressData(userAddress);
                break;
            case UserAddress.STATE_DISTRICT_ID_NOT_MATCH:
                view.renderCheckoutPageNoMatchedAddress(cartShipmentAddressFormData);
                break;
            case UserAddress.STATE_NO_ADDRESS:
                view.renderCheckoutPageNoAddress(cartShipmentAddressFormData);
                break;
            case UserAddress.STATE_CHOSEN_ADDRESS_MATCH:
            default:
                shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                view.renderCheckoutPage(!isReloadData, isReloadAfterPriceChangeHinger, isOneClickShipment);
                break;
        }
    }
}
