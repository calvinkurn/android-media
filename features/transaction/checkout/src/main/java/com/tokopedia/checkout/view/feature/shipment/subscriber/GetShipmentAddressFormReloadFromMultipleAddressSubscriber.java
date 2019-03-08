package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.transactiondata.apiservice.CartHttpErrorException;
import com.tokopedia.transactiondata.apiservice.CartResponseDataNullException;
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 01/11/18.
 */

public class GetShipmentAddressFormReloadFromMultipleAddressSubscriber extends Subscriber<CartShipmentAddressFormData> {

    private final ShipmentPresenter shipmentPresenter;
    private final ShipmentContract.View view;
    private final RecipientAddressModel oldRecipientAddressModel;
    private final List<ShipmentCartItemModel> oldShipmentCartItemModels;

    public GetShipmentAddressFormReloadFromMultipleAddressSubscriber(ShipmentPresenter shipmentPresenter,
                                                                     ShipmentContract.View view,
                                                                     RecipientAddressModel oldRecipientAddressModel,
                                                                     List<ShipmentCartItemModel> oldShipmentCartItemModels) {
        this.shipmentPresenter = shipmentPresenter;
        this.view = view;
        this.oldRecipientAddressModel = oldRecipientAddressModel;
        this.oldShipmentCartItemModels = oldShipmentCartItemModels;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.hideLoading();

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
    }

    @Override
    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
        view.hideLoading();

        if (cartShipmentAddressFormData.isError()) {
            view.showToastError(cartShipmentAddressFormData.getErrorMessage());
        } else {
            shipmentPresenter.setEgoldAttributeModel(cartShipmentAddressFormData.getEgoldAttributes());
            if (cartShipmentAddressFormData.getGroupAddress() == null || cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
                view.renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
            } else {
                RecipientAddressModel newRecipientAddressModel =
                        view.getShipmentDataConverter().getRecipientAddressModel(cartShipmentAddressFormData);
                List<ShipmentCartItemModel> shipmentCartItemModelList =
                        view.getShipmentDataConverter().getShipmentItems(cartShipmentAddressFormData);
                if (shipmentPresenter.checkAddressHasChanged(oldRecipientAddressModel, newRecipientAddressModel) ||
                        shipmentPresenter.checkShipmentItemHasChanged(oldShipmentCartItemModels, shipmentCartItemModelList)) {
                    shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                    shipmentPresenter.setShipmentCartItemModelList(shipmentCartItemModelList);
                    view.renderDataChanged();
                }
            }
        }
    }
}
