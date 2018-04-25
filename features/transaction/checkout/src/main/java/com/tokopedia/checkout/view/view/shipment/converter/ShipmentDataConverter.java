package com.tokopedia.checkout.view.view.shipment.converter;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Shop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressItem;
import com.tokopedia.design.utils.CurrencyFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Irfan Khoirul on 24/04/18.
 * Originaly authored by Kris, Aghny, Angga
 */

public class ShipmentDataConverter {

    private static final int PRIME_ADDRESS = 2;

    public RecipientAddressModel getRecipientAddressModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
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

        recipientAddress.setRecipientName(userAddress.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(userAddress.getPhone());
        recipientAddress.setLatitude(!TextUtils.isEmpty(userAddress.getLatitude()) ?
                Double.parseDouble(userAddress.getLatitude()) : null);
        recipientAddress.setLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                Double.parseDouble(userAddress.getLongitude()) : null);

        recipientAddress.setSelected(userAddress.getStatus() == PRIME_ADDRESS);

        return recipientAddress;
    }

    public List<ShipmentItem> getShipmentItems(CartShipmentAddressFormData cartShipmentAddressFormData) {
        List<ShipmentItem> shipmentItems = new ArrayList<>();

        ShipmentItem shipmentItem;
        if (cartShipmentAddressFormData.isMultiple()) {
            shipmentItems.addAll(getShipmentMultipleAddressItem(cartShipmentAddressFormData));

        } else {
            shipmentItem = new ShipmentSingleAddressItem();
            UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
            for (GroupShop groupShop : cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop()) {
                getShipmentSingleAddressItem((ShipmentSingleAddressItem) shipmentItem, userAddress, groupShop, cartShipmentAddressFormData.getKeroToken(),
                        String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()));
                shipmentItems.add(shipmentItem);
            }
        }

        return shipmentItems;
    }

    private List<ShipmentItem> getShipmentMultipleAddressItem(CartShipmentAddressFormData cartShipmentAddressFormData) {
        List<ShipmentItem> shipmentItems = new ArrayList<>();
        for (int addressIndex = 0; addressIndex < cartShipmentAddressFormData.getGroupAddress().size(); addressIndex++) {
            GroupAddress currentAddress = cartShipmentAddressFormData.getGroupAddress().get(addressIndex);
            List<GroupShop> groupShopList = cartShipmentAddressFormData.getGroupAddress().get(addressIndex).getGroupShop();
            for (int shopIndex = 0; shopIndex < groupShopList.size(); shopIndex++) {
                GroupShop currentGroupShop = groupShopList.get(shopIndex);
                List<Product> productList = currentGroupShop.getProducts();

                boolean isErrorGroupShop = currentGroupShop.isError();
                String errorMessageGroupShop = currentGroupShop.getErrorMessage();
                boolean isWarningGroupShop = currentGroupShop.isWarning();
                String warningMessageGroupShop = currentGroupShop.getWarningMessage();

                for (int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    ShipmentMultipleAddressItem shipmentMultipleAddressItem = new ShipmentMultipleAddressItem();
                    Product currentProduct = productList.get(productIndex);

                    shipmentMultipleAddressItem.setError(isErrorGroupShop);
                    shipmentMultipleAddressItem.setErrorMessage(errorMessageGroupShop);
                    shipmentMultipleAddressItem.setWarning(isWarningGroupShop);
                    shipmentMultipleAddressItem.setWarningMessage(warningMessageGroupShop);

                    shipmentMultipleAddressItem.setInvoicePosition(shipmentItems.size());
                    shipmentMultipleAddressItem.setShopId(currentGroupShop.getShop().getShopId());
                    shipmentMultipleAddressItem.setProductName(currentProduct.getProductName());
                    shipmentMultipleAddressItem.setProductPriceNumber(currentProduct.getProductPrice());
                    shipmentMultipleAddressItem.setProductPrice(currentProduct.getProductPrice());
                    shipmentMultipleAddressItem.setProductImageUrl(currentProduct.getProductImageSrc200Square());
                    shipmentMultipleAddressItem.setShopName(currentGroupShop.getShop().getShopName());
                    MultipleAddressItemData addressItemData = new MultipleAddressItemData();
                    addressItemData.setCartPosition(productIndex);
                    addressItemData.setAddressPosition(0);
                    addressItemData.setProductId(String.valueOf(currentProduct.getProductId()));
                    addressItemData.setProductWeight(currentProduct.getProductWeightFmt());
                    addressItemData.setProductRawWeight(currentProduct.getProductWeight());
                    addressItemData.setProductNotes(currentProduct.getProductNotes());
                    addressItemData.setProductQty(
                            String.valueOf(currentProduct.getProductQuantity())
                    );
                    addressItemData.setAddressId(
                            String.valueOf(currentAddress.getUserAddress().getAddressId())
                    );
                    addressItemData.setAddressTitle(currentAddress.getUserAddress()
                            .getAddressName());
                    addressItemData.setAddressReceiverName(currentAddress.getUserAddress()
                            .getReceiverName());
                    addressItemData.setAddressStreet(currentAddress.getUserAddress().getAddress());
                    addressItemData.setAddressCityName(currentAddress.getUserAddress().getCityName());
                    addressItemData.setAddressProvinceName(
                            currentAddress.getUserAddress().getProvinceName()
                    );
                    addressItemData.setRecipientPhoneNumber(
                            currentAddress.getUserAddress().getPhone()
                    );
                    addressItemData.setAddressCountryName(currentAddress.getUserAddress()
                            .getCountry());
                    shipmentMultipleAddressItem.setMultipleAddressItemData(addressItemData);
                    shipmentMultipleAddressItem.setShipmentCartData(new RatesDataConverter()
                            .getShipmentCartData(cartShipmentAddressFormData, currentAddress.getUserAddress(),
                                    currentGroupShop, shipmentMultipleAddressItem));

                    shipmentMultipleAddressItem.setShipmentCartData(new RatesDataConverter()
                            .getShipmentCartData(cartShipmentAddressFormData, currentAddress.getUserAddress(),
                                    currentGroupShop, shipmentMultipleAddressItem));

                    shipmentMultipleAddressItem.setProductIsFreeReturns(currentProduct.isProductIsFreeReturns());
                    shipmentMultipleAddressItem.setProductIsPreorder(currentProduct.isProductIsPreorder());
                    shipmentMultipleAddressItem.setProductFcancelPartial(currentProduct.isProductFcancelPartial());
                    shipmentMultipleAddressItem.setProductFinsurance(currentProduct.isProductFinsurance());

                    shipmentItems.add(shipmentMultipleAddressItem);
                }
            }
        }
        return shipmentItems;
    }

    private void getShipmentSingleAddressItem(ShipmentSingleAddressItem shipmentSingleAddressItem,
                                              UserAddress userAddress, GroupShop groupShop,
                                              String keroToken, String keroUnixTime) {

        shipmentSingleAddressItem.setError(groupShop.isError());
        shipmentSingleAddressItem.setErrorMessage(groupShop.getErrorMessage());
        shipmentSingleAddressItem.setWarning(groupShop.isWarning());
        shipmentSingleAddressItem.setWarningMessage(groupShop.getWarningMessage());

        Shop shop = groupShop.getShop();
        shipmentSingleAddressItem.setShopId(shop.getShopId());
        shipmentSingleAddressItem.setShopName(shop.getShopName());

        List<Product> products = groupShop.getProducts();
        List<CartItemModel> cartItemModels = convertFromProductList(products);
        // This is something that not well planned
        Fobject fobject = levelUpParametersFromProductToCartSeller(cartItemModels);
        shipmentSingleAddressItem.setProductFcancelPartial(fobject.isFcancelPartial() == 1);
        shipmentSingleAddressItem.setProductIsPreorder(fobject.isPreOrder() == 1);
        shipmentSingleAddressItem.setCartItemModels(cartItemModels);

        shipmentSingleAddressItem.setShipmentCartData(new RatesDataConverter()
                .getShipmentCartData(userAddress, groupShop, shipmentSingleAddressItem, keroToken, keroUnixTime));
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

        cartItemModel.setId(product.getProductId());
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
