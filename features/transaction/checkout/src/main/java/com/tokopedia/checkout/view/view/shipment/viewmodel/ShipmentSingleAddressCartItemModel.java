package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentSingleAddressCartItemModel extends ShipmentCartItemModel {

    private List<CartItemModel> cartItemModels = new ArrayList<>();

    public ShipmentSingleAddressCartItemModel() {
    }

    public List<CartItemModel> getCartItemModels() {
        return cartItemModels;
    }

    public void setCartItemModels(List<CartItemModel> cartItemModels) {
        this.cartItemModels = cartItemModels;
    }
}
