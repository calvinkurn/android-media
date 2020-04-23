package com.tokopedia.checkout.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.ShipmentContract;
import com.tokopedia.checkout.view.ShipmentPresenter;
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException;

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
                        shipmentPresenter.getShipmentDataConverter().getRecipientAddressModel(cartShipmentAddressFormData);
                List<ShipmentCartItemModel> shipmentCartItemModelList =
                        shipmentPresenter.getShipmentDataConverter().getShipmentItems(cartShipmentAddressFormData, false);
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
