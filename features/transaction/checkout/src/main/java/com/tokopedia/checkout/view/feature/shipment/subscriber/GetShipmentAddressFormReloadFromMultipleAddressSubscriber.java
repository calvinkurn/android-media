package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.logisticcart.domain.shipping.RecipientAddressModel;
import com.tokopedia.logisticcart.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException;

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

        String errorMessage = e.getMessage();
        if (!(e instanceof CartResponseErrorException)) {
            errorMessage = ErrorHandler.getErrorMessage(view.getActivityContext(), e);
        }

        view.showToastError(errorMessage);
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
