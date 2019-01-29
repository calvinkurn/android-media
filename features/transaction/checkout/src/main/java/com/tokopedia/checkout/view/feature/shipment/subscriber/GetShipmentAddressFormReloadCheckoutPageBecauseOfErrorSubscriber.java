package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipProd;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 01/11/18.
 */

public class GetShipmentAddressFormReloadCheckoutPageBecauseOfErrorSubscriber extends Subscriber<CartShipmentAddressFormData> {

    private final ShipmentPresenter shipmentPresenter;
    private final ShipmentContract.View view;

    public GetShipmentAddressFormReloadCheckoutPageBecauseOfErrorSubscriber(ShipmentPresenter shipmentPresenter,
                                                                            ShipmentContract.View view) {
        this.shipmentPresenter = shipmentPresenter;
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.hideLoading();
    }

    @Override
    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (cartShipmentAddressFormData.getGroupAddress() == null ||
                cartShipmentAddressFormData.getGroupAddress().isEmpty()) {
            view.renderNoRecipientAddressShipmentForm(cartShipmentAddressFormData);
        } else {
            prepareDataAfterReloadCheckoutPage(cartShipmentAddressFormData);
        }
    }

    private void prepareDataAfterReloadCheckoutPage(CartShipmentAddressFormData cartShipmentAddressFormData) {
        List<ShipmentCartItemModel> newShipmentCartItemModelList = view.getShipmentDataConverter().getShipmentItems(
                cartShipmentAddressFormData
        );
        List<ShipmentCartItemModel> oldShipmentCartItemModelList = shipmentPresenter.getShipmentCartItemModelList();
        for (ShipmentCartItemModel oldShipmentCartItemModel : oldShipmentCartItemModelList) {
            for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                if (oldShipmentCartItemModel.equals(newShipmentCartItemModel)) {
                    oldShipmentCartItemModel.setError(newShipmentCartItemModel.isError());
                    oldShipmentCartItemModel.setAllItemError(newShipmentCartItemModel.isAllItemError());
                    oldShipmentCartItemModel.setErrorTitle(newShipmentCartItemModel.getErrorTitle());
                    for (CartItemModel newCartItemModel : newShipmentCartItemModel.getCartItemModels()) {
                        for (CartItemModel oldCartItemModel : oldShipmentCartItemModel.getCartItemModels()) {
                            if (newCartItemModel.getProductId() == oldCartItemModel.getProductId()) {
                                oldCartItemModel.setError(newCartItemModel.isError());
                                oldCartItemModel.setErrorMessage(newCartItemModel.getErrorMessage());
                                if (oldShipmentCartItemModel.isAllItemError()) {
                                    oldCartItemModel.setError(oldShipmentCartItemModel.isError());
                                }
                            }
                        }
                    }
                    boolean breakFromNewShipmentCartItemModelLoop = false;
                    for (ShopShipment shopShipment : newShipmentCartItemModel.getShipmentCartData().getShopShipments()) {
                        if (oldShipmentCartItemModel.getSelectedShipmentDetailData() != null &&
                                oldShipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperId() == shopShipment.getShipId()) {
                            boolean breakFromShopShipmentLoop = false;
                            for (ShipProd shipProd : shopShipment.getShipProds()) {
                                if (oldShipmentCartItemModel.getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId() == shipProd.getShipProdId()) {
                                    newShipmentCartItemModel.setSelectedShipmentDetailData(oldShipmentCartItemModel.getSelectedShipmentDetailData());
                                    newShipmentCartItemModel.setStateDropshipperHasError(oldShipmentCartItemModel.isStateDropshipperHasError());
                                    newShipmentCartItemModel.setStateDropshipperDetailExpanded(oldShipmentCartItemModel.isStateDropshipperDetailExpanded());
                                    breakFromShopShipmentLoop = true;
                                    break;
                                }
                            }
                            if (breakFromShopShipmentLoop) {
                                breakFromNewShipmentCartItemModelLoop = true;
                                break;
                            }
                        }
                    }
                    if (breakFromNewShipmentCartItemModelLoop) {
                        break;
                    }
                }
            }
        }

        for (ShipmentCartItemModel oldShipmentCartItemModel : oldShipmentCartItemModelList) {
            for (ShipmentCartItemModel newShipmentCartItemModel : newShipmentCartItemModelList) {
                if (oldShipmentCartItemModel.equals(newShipmentCartItemModel) &&
                        newShipmentCartItemModel.getSelectedShipmentDetailData() == null) {
                    oldShipmentCartItemModel.setSelectedShipmentDetailData(null);
                    oldShipmentCartItemModel.setShipmentCartData(newShipmentCartItemModel.getShipmentCartData());
                }
            }
        }

        view.hideLoading();
        view.renderErrorDataHasChangedAfterCheckout(oldShipmentCartItemModelList);
    }

}
