package com.tokopedia.checkout.view.feature.shipment.converter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.promostacking.MessageData;
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.PurchaseProtectionPlanData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Shop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Originally authored by Kris, Aghny, Angga.
 * Modified by Irfan
 */

public class ShipmentDataConverter {

    private static final int PRIME_ADDRESS = 2;

    public RecipientAddressModel getRecipientAddressModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (cartShipmentAddressFormData.getGroupAddress() != null && cartShipmentAddressFormData.getGroupAddress().size() > 0) {
            UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
            return createRecipientAddressModel(userAddress);
        }
        return null;
    }

    public RecipientAddressModel getRecipientAddressModel(UserAddress userAddress) {
        return createRecipientAddressModel(userAddress);
    }

    public ShipmentDonationModel getShipmentDonationModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        ShipmentDonationModel shipmentDonationModel = new ShipmentDonationModel();
        shipmentDonationModel.setChecked(false);
        shipmentDonationModel.setDonation(cartShipmentAddressFormData.getDonation());

        return shipmentDonationModel;
    }

    @NonNull
    private RecipientAddressModel createRecipientAddressModel(UserAddress userAddress) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        recipientAddress.setId(String.valueOf(userAddress.getAddressId()));
        recipientAddress.setAddressStatus(userAddress.getStatus());
        recipientAddress.setAddressName(userAddress.getAddressName());
        recipientAddress.setCountryName(userAddress.getCountry());
        recipientAddress.setProvinceName(userAddress.getProvinceName());
        recipientAddress.setDestinationDistrictName(userAddress.getDistrictName());
        recipientAddress.setCityName(userAddress.getCityName());
        recipientAddress.setDestinationDistrictId(String.valueOf(userAddress.getDistrictId()));
        recipientAddress.setStreet(userAddress.getAddress());
        recipientAddress.setPostalCode(userAddress.getPostalCode());
        recipientAddress.setCityId(String.valueOf(userAddress.getCityId()));
        recipientAddress.setProvinceId(String.valueOf(userAddress.getProvinceId()));

        recipientAddress.setRecipientName(userAddress.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(userAddress.getPhone());
        recipientAddress.setLatitude(!TextUtils.isEmpty(userAddress.getLatitude()) ?
                userAddress.getLatitude() : null);
        recipientAddress.setLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                userAddress.getLongitude() : null);

        recipientAddress.setSelected(userAddress.getStatus() == PRIME_ADDRESS);
        recipientAddress.setCornerId(String.valueOf(userAddress.getCornerId()));

        return recipientAddress;
    }

    public List<ShipmentCartItemModel> getShipmentItems(CartShipmentAddressFormData cartShipmentAddressFormData) {
        List<ShipmentCartItemModel> shipmentCartItemModels = new ArrayList<>();

        int addressIndex = 0;
        ShipmentCartItemModel shipmentCartItemModel = null;
        if (cartShipmentAddressFormData.isMultiple()) {
            for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                UserAddress userAddress = groupAddress.getUserAddress();
                for (GroupShop groupShop : groupAddress.getGroupShop()) {
                    shipmentCartItemModel = new ShipmentCartItemModel();
                    shipmentCartItemModel.setUseCourierRecommendation(cartShipmentAddressFormData
                            .isUseCourierRecommendation());
                    shipmentCartItemModel.setIsBlackbox(cartShipmentAddressFormData.getIsBlackbox());
                    shipmentCartItemModel.setAddressId(cartShipmentAddressFormData.getGroupAddress()
                            .get(addressIndex).getUserAddress().getAddressId());


                    for (VoucherOrdersItemData voucherOrdersItemData : cartShipmentAddressFormData.getAutoApplyStackData().getVoucherOrders()) {
                        if (groupShop.getCartString().equalsIgnoreCase(voucherOrdersItemData.getUniqueId())) {
                            shipmentCartItemModel.setPromoMerchantData(convertFromVoucherOrdersItem(voucherOrdersItemData));
                        }
                    }

                    getShipmentItem(shipmentCartItemModel, userAddress, groupShop,
                            cartShipmentAddressFormData.getKeroToken(),
                            String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), true);
                    setCartItemModelError(shipmentCartItemModel);
                    shipmentCartItemModels.add(shipmentCartItemModel);
                }
                addressIndex++;
            }
        } else {
            UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
            for (GroupShop groupShop : cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop()) {
                shipmentCartItemModel = new ShipmentCartItemModel();
                shipmentCartItemModel.setUseCourierRecommendation(cartShipmentAddressFormData.isUseCourierRecommendation());
                shipmentCartItemModel.setIsBlackbox(cartShipmentAddressFormData.getIsBlackbox());
                shipmentCartItemModel.setAddressId(cartShipmentAddressFormData.getGroupAddress()
                        .get(0).getUserAddress().getAddressId());

                getShipmentItem(shipmentCartItemModel, userAddress, groupShop, cartShipmentAddressFormData.getKeroToken(),
                        String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), false);
                setCartItemModelError(shipmentCartItemModel);
                shipmentCartItemModels.add(shipmentCartItemModel);
            }
        }

        return shipmentCartItemModels;
    }

    private void setCartItemModelError(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isAllItemError()) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                cartItemModel.setError(true);
            }
        }
    }

    private void getShipmentItem(ShipmentCartItemModel shipmentCartItemModel,
                                 UserAddress userAddress, GroupShop groupShop,
                                 String keroToken, String keroUnixTime, boolean isMultiple) {
        if (isMultiple) {
            shipmentCartItemModel.setRecipientAddressModel(getRecipientAddressModel(userAddress));
        }

        shipmentCartItemModel.setShopShipmentList(groupShop.getShopShipments());
        shipmentCartItemModel.setError(groupShop.isError());
        if (shipmentCartItemModel.isError()) {
            shipmentCartItemModel.setAllItemError(true);
        }
        shipmentCartItemModel.setErrorTitle(groupShop.getErrorMessage());
        shipmentCartItemModel.setWarning(groupShop.isWarning());
        shipmentCartItemModel.setWarningTitle(groupShop.getWarningMessage());

        Shop shop = groupShop.getShop();
        shipmentCartItemModel.setShopId(shop.getShopId());
        shipmentCartItemModel.setShopName(shop.getShopName());
        shipmentCartItemModel.setOfficialStore(shop.isOfficial());
        shipmentCartItemModel.setGoldMerchant(shop.isGold());
        shipmentCartItemModel.setShopBadge(shop.getShopBadge());

        shipmentCartItemModel.setShippingId(groupShop.getShippingId());
        shipmentCartItemModel.setSpId(groupShop.getSpId());
        shipmentCartItemModel.setDropshiperName(groupShop.getDropshipperName());
        shipmentCartItemModel.setDropshiperPhone(groupShop.getDropshipperPhone());
        shipmentCartItemModel.setInsurance(groupShop.isUseInsurance());

        List<Product> products = groupShop.getProducts();
        List<CartItemModel> cartItemModels = convertFromProductList(products);

        // This is something that not well planned
        Fobject fobject = levelUpParametersFromProductToCartSeller(cartItemModels);
        shipmentCartItemModel.setProductFcancelPartial(fobject.isFcancelPartial() == 1);
        shipmentCartItemModel.setCartItemModels(cartItemModels);
        shipmentCartItemModel.setProductIsPreorder(fobject.isPreOrder() == 1);

        // set promo merchant
        shipmentCartItemModel.setPromoMerchantData(convertFromVoucherOrdersItem(groupShop.getShop().getVoucherOrdersItemData()));

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

    private VoucherOrdersItemUiModel convertFromVoucherOrdersItem(VoucherOrdersItemData voucherOrdersItemData) {
        VoucherOrdersItemUiModel voucherOrdersItemUiModel = new VoucherOrdersItemUiModel();
        voucherOrdersItemUiModel.setCartId(voucherOrdersItemData.getCartId());
        voucherOrdersItemUiModel.setCashbackWalletAmount(voucherOrdersItemData.getCashbackWalletAmount());
        voucherOrdersItemUiModel.setCode(voucherOrdersItemData.getCode());
        voucherOrdersItemUiModel.setDiscountAmount(voucherOrdersItemData.getDiscountAmount());
        voucherOrdersItemUiModel.setInvoiceDescription(voucherOrdersItemData.getInvoiceDescription());
        voucherOrdersItemUiModel.setSuccess(voucherOrdersItemData.isSuccess());
        voucherOrdersItemUiModel.setMessage(convertFromMessage(voucherOrdersItemData.getMessageData()));
        return voucherOrdersItemUiModel;
    }

    private MessageUiModel convertFromMessage(MessageData messageData) {
        MessageUiModel messageUiModel = new MessageUiModel();
        messageUiModel.setColor(messageData.getColor());
        messageUiModel.setState(messageData.getState());
        messageUiModel.setText(messageData.getText());
        return messageUiModel;
    }

    private CartItemModel convertFromProduct(Product product) {
        CartItemModel cartItemModel = new CartItemModel();

        cartItemModel.setCartId(product.getCartId());
        cartItemModel.setProductId(product.getProductId());
        cartItemModel.setName(product.getProductName());
        cartItemModel.setImageUrl(product.getProductImageSrc200Square());
        cartItemModel.setCurrency(product.getProductPriceCurrency());
        if (product.getProductWholesalePrice() != 0) {
            cartItemModel.setPrice(product.getProductWholesalePrice());
        } else {
            cartItemModel.setPrice(product.getProductPrice());
        }
        cartItemModel.setQuantity(product.getProductQuantity());
        cartItemModel.setWeight(product.getProductWeight());
        cartItemModel.setWeightFmt(product.getProductWeightFmt());
        cartItemModel.setNoteToSeller(product.getProductNotes());
        cartItemModel.setPreOrder(product.isProductIsPreorder());
        cartItemModel.setPreOrderInfo(product.getProductPreOrderInfo());
        cartItemModel.setPreOrderDurationDay(product.getPreOrderDurationDay());
        cartItemModel.setFreeReturn(product.isProductIsFreeReturns());
        cartItemModel.setCashback(product.getProductCashback());
        cartItemModel.setCashback(!TextUtils.isEmpty(product.getProductCashback()));
        cartItemModel.setFreeReturnLogo(product.getFreeReturnLogo());
        cartItemModel.setfInsurance(product.isProductFcancelPartial());
        cartItemModel.setfCancelPartial(product.isProductFinsurance());
        cartItemModel.setError(product.isError());
        cartItemModel.setErrorMessage(product.getErrorMessage());
        cartItemModel.setErrorMessageDescription(product.getErrorMessageDescription());

        if (product.getPurchaseProtectionPlanData() != null) {
            PurchaseProtectionPlanData ppp = product.getPurchaseProtectionPlanData();
            cartItemModel.setProtectionAvailable(ppp.isProtectionAvailable());
            cartItemModel.setProtectionPricePerProduct(ppp.getProtectionPricePerProduct());
            cartItemModel.setProtectionPrice(ppp.getProtectionPrice());
            cartItemModel.setProtectionTitle(ppp.getProtectionTitle());
            cartItemModel.setProtectionSubTitle(ppp.getProtectionSubtitle());
            cartItemModel.setProtectionLinkText(ppp.getProtectionLinkText());
            cartItemModel.setProtectionLinkUrl(ppp.getProtectionLinkUrl());
            cartItemModel.setProtectionOptIn(ppp.isProtectionOptIn());
        }

        cartItemModel.setAnalyticsProductCheckoutData(product.getAnalyticsProductCheckoutData());
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
