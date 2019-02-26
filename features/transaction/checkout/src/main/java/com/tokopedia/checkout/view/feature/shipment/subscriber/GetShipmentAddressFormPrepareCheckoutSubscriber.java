package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.checkout.R;
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

public class GetShipmentAddressFormPrepareCheckoutSubscriber extends Subscriber<CartShipmentAddressFormData> {

    private final ShipmentPresenter shipmentPresenter;
    private final ShipmentContract.View view;
    private final RecipientAddressModel recipientAddressModel;
    private final boolean isNeedToRemoveErrorProduct;

    public GetShipmentAddressFormPrepareCheckoutSubscriber(ShipmentPresenter shipmentPresenter,
                                                           ShipmentContract.View view,
                                                           RecipientAddressModel recipientAddressModel,
                                                           boolean isNeedToRemoveErrorProduct) {
        this.shipmentPresenter = shipmentPresenter;
        this.view = view;
        this.recipientAddressModel = recipientAddressModel;
        this.isNeedToRemoveErrorProduct = isNeedToRemoveErrorProduct;
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
        if (cartShipmentAddressFormData.getGroupAddress() == null ||
                cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
            view.renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
        } else {
            if (cartShipmentAddressFormData.isHasError()) {
                prepareDataAfterProcessShipmentPrepareCheckout(cartShipmentAddressFormData, isNeedToRemoveErrorProduct);
            } else {
                RecipientAddressModel newRecipientAddressModel =
                        view.getShipmentDataConverter().getRecipientAddressModel(cartShipmentAddressFormData);
                List<ShipmentCartItemModel> shipmentCartItemModelList =
                        view.getShipmentDataConverter().getShipmentItems(cartShipmentAddressFormData);

                if (!cartShipmentAddressFormData.isMultiple() && shipmentPresenter.checkAddressHasChanged(recipientAddressModel, newRecipientAddressModel)) {
                    view.hideLoading();
                    view.showToastError(view.getActivityContext().getString(R.string.error_message_checkout_failed));
                        shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                    view.renderDataChanged();
                } else if (shipmentPresenter.checkShipmentItemHasChanged(shipmentPresenter.getShipmentCartItemModelList(), shipmentCartItemModelList)) {
                    view.hideLoading();
                    view.showToastError(view.getActivityContext().getString(R.string.error_message_checkout_failed));
                    shipmentPresenter.initializePresenterData(cartShipmentAddressFormData);
                    shipmentPresenter.setShipmentCartItemModelList(shipmentCartItemModelList);
                    view.renderDataChanged();
                } else {
                    view.renderCheckShipmentPrepareCheckoutSuccess();
                }
            }
        }
    }

    private void prepareDataAfterProcessShipmentPrepareCheckout(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                               boolean isNeedToRemoveErrorProduct) {
        List<ShipmentCartItemModel> newShipmentCartItemModelList = view.getShipmentDataConverter().getShipmentItems(
                cartShipmentAddressFormData
        );
        List<ShipmentCartItemModel> oldShipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        try {
            for (int i = 0; i < newShipmentCartItemModelList.size(); i++) {
                if (newShipmentCartItemModelList.get(i).isError()) {
                    oldShipmentCartItemModelList.get(i).setError(true);
                    oldShipmentCartItemModelList.get(i).setErrorTitle(newShipmentCartItemModelList.get(i).getErrorTitle());
                }
                for (int j = 0; j < newShipmentCartItemModelList.get(i).getCartItemModels().size(); j++) {
                    if (newShipmentCartItemModelList.get(i).isAllItemError()) {
                        oldShipmentCartItemModelList.get(i).getCartItemModels().get(j).setError(true);
                    } else {
                        if (newShipmentCartItemModelList.get(i).getCartItemModels().get(j).isError()) {
                            oldShipmentCartItemModelList.get(i).getCartItemModels().get(j).setError(true);
                            oldShipmentCartItemModelList.get(i).getCartItemModels().get(j).setErrorMessage(
                                    newShipmentCartItemModelList.get(i).getCartItemModels().get(j).getErrorMessage());
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        view.hideLoading();
        view.renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                cartShipmentAddressFormData, !isNeedToRemoveErrorProduct
        );
    }

}
