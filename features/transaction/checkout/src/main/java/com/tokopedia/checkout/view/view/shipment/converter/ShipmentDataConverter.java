package com.tokopedia.checkout.view.view.shipment.converter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Shop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Originally authored by Kris, Aghny, Angga.
 * Modified by Irfan
 */

public class ShipmentDataConverter {

    private static final int PRIME_ADDRESS = 2;

    public RecipientAddressModel getRecipientAddressModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
        return createRecipientAddressModel(userAddress);
    }

    public RecipientAddressModel getRecipientAddressModel(UserAddress userAddress) {
        return createRecipientAddressModel(userAddress);
    }

    @NonNull
    private RecipientAddressModel createRecipientAddressModel(UserAddress userAddress) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        recipientAddress.setId(String.valueOf(userAddress.getAddressId()));
        recipientAddress.setAddressStatus(userAddress.getStatus());
        recipientAddress.setAddressName(userAddress.getAddressName());
        recipientAddress.setAddressCountryName(userAddress.getCountry());
        recipientAddress.setAddressProvinceName(userAddress.getProvinceName());
        recipientAddress.setDestinationDistrictName(userAddress.getDistrictName());
        recipientAddress.setAddressCityName(userAddress.getCityName());
        recipientAddress.setDestinationDistrictId(String.valueOf(userAddress.getDistrictId()));
        recipientAddress.setAddressStreet(userAddress.getAddress());
        recipientAddress.setAddressPostalCode(userAddress.getPostalCode());
        recipientAddress.setCityId(String.valueOf(userAddress.getCityId()));
        recipientAddress.setProvinceId(String.valueOf(userAddress.getProvinceId()));

        recipientAddress.setRecipientName(userAddress.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(userAddress.getPhone());
        recipientAddress.setLatitude(!TextUtils.isEmpty(userAddress.getLatitude()) ?
                Double.parseDouble(userAddress.getLatitude()) : null);
        recipientAddress.setLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                Double.parseDouble(userAddress.getLongitude()) : null);

        recipientAddress.setSelected(userAddress.getStatus() == PRIME_ADDRESS);

        return recipientAddress;
    }

    public List<ShipmentCartItemModel> getShipmentItems(CartShipmentAddressFormData cartShipmentAddressFormData) {
        List<ShipmentCartItemModel> shipmentCartItemModels = new ArrayList<>();

        ShipmentCartItemModel shipmentCartItemModel;
        if (cartShipmentAddressFormData.isMultiple()) {
            for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                UserAddress userAddress = groupAddress.getUserAddress();
                for (GroupShop groupShop : groupAddress.getGroupShop()) {
                    shipmentCartItemModel = new ShipmentCartItemModel();
                    getShipmentItem(shipmentCartItemModel, userAddress, groupShop, cartShipmentAddressFormData.getKeroToken(),
                            String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), true);
                    shipmentCartItemModels.add(shipmentCartItemModel);
                }
            }
        } else {
            UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
            for (GroupShop groupShop : cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop()) {
                shipmentCartItemModel = new ShipmentCartItemModel();
                getShipmentItem(shipmentCartItemModel, userAddress, groupShop, cartShipmentAddressFormData.getKeroToken(),
                        String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), false);
                shipmentCartItemModels.add(shipmentCartItemModel);
            }
        }

        return shipmentCartItemModels;
    }

    private void getShipmentItem(ShipmentCartItemModel shipmentCartItemModel,
                                 UserAddress userAddress, GroupShop groupShop,
                                 String keroToken, String keroUnixTime, boolean isMultiple) {
        if (isMultiple) {
            shipmentCartItemModel.setRecipientAddressModel(getRecipientAddressModel(userAddress));
        }

        shipmentCartItemModel.setError(groupShop.isError());
        shipmentCartItemModel.setErrorMessage(groupShop.getErrorMessage());
        shipmentCartItemModel.setWarning(groupShop.isWarning());
        shipmentCartItemModel.setWarningMessage(groupShop.getWarningMessage());

        Shop shop = groupShop.getShop();
        shipmentCartItemModel.setShopId(shop.getShopId());
        shipmentCartItemModel.setShopName(shop.getShopName());

        List<Product> products = groupShop.getProducts();
        List<CartItemModel> cartItemModels = convertFromProductList(products);

        // This is something that not well planned
        Fobject fobject = levelUpParametersFromProductToCartSeller(cartItemModels);
        shipmentCartItemModel.setProductFcancelPartial(fobject.isFcancelPartial() == 1);
        shipmentCartItemModel.setCartItemModels(cartItemModels);
        shipmentCartItemModel.setProductIsPreorder(fobject.isPreOrder() == 1);

        shipmentCartItemModel.setShipmentCartData(new RatesDataConverter()
                .getShipmentCartData(userAddress, groupShop, shipmentCartItemModel, keroToken, keroUnixTime));
    }

    private List<CartItemModel> convertFromProductList(List<Product> products) {
        List<CartItemModel> cartItemModels = new ArrayList<>();

        for (Product product : products) {
            cartItemModels.add(convertFromProduct(product));
        }

        return cartItemModels;
    }

    private CartItemModel convertFromProduct(Product product) {
        CartItemModel cartItemModel = new CartItemModel();

        cartItemModel.setCartId(product.getCartId());
        cartItemModel.setProductId(product.getProductId());
        cartItemModel.setName(product.getProductName());
        cartItemModel.setImageUrl(product.getProductImageSrc200Square());
        cartItemModel.setCurrency(product.getProductPriceCurrency());
        cartItemModel.setPrice(product.getProductPrice());
        cartItemModel.setQuantity(product.getProductQuantity());
        cartItemModel.setWeight(product.getProductWeight());
        cartItemModel.setWeightFmt(product.getProductWeightFmt());
        cartItemModel.setNoteToSeller(product.getProductNotes());
        cartItemModel.setPreOrder(product.isProductIsPreorder());
        cartItemModel.setFreeReturn(product.isProductIsFreeReturns());
        cartItemModel.setCashback(product.getProductCashback());
        cartItemModel.setCashback(!TextUtils.isEmpty(product.getProductCashback()));
        cartItemModel.setFreeReturnLogo(product.getFreeReturnLogo());
        cartItemModel.setfInsurance(product.isProductFcancelPartial());
        cartItemModel.setfCancelPartial(product.isProductFinsurance());

        return cartItemModel;
    }

    private Fobject levelUpParametersFromProductToCartSeller(List<CartItemModel> cartItemList) {

        int isPreOrder = 0;
        int isFcancelPartial = 0;
        int isFinsurance = 0;

        for (CartItemModel cartItem : cartItemList) {
            if (cartItem.isPreOrder()) {
                isPreOrder = 1;
            }
            if (cartItem.isfInsurance()) {
                isFcancelPartial = 1;
            }
            if (cartItem.isfCancelPartial()) {
                isFinsurance = 1;
            }
        }

        return new Fobject(isPreOrder, isFcancelPartial, isFinsurance);
    }

    private class Fobject {

        private int isPreOrder;
        private int isFcancelPartial;
        private int isFinsurance;

        Fobject(int isPreOrder, int isFcancelPartial, int isFinsurance) {
            this.isPreOrder = isPreOrder;
            this.isFcancelPartial = isFcancelPartial;
            this.isFinsurance = isFinsurance;
        }

        public int isPreOrder() {
            return isPreOrder;
        }

        public void setPreOrder(int preOrder) {
            isPreOrder = preOrder;
        }

        public int isFcancelPartial() {
            return isFcancelPartial;
        }

        public void setFcancelPartial(int fcancelPartial) {
            isFcancelPartial = fcancelPartial;
        }

        public int isFinsurance() {
            return isFinsurance;
        }

        public void setFinsurance(int finsurance) {
            isFinsurance = finsurance;
        }
    }


}
