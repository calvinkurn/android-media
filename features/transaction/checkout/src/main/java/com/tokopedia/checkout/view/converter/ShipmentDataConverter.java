package com.tokopedia.checkout.view.converter;

import androidx.annotation.NonNull;

import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.model.cartshipmentform.Product;
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentInformationData;
import com.tokopedia.checkout.domain.model.cartshipmentform.Shop;
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel;
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.UserAddress;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Originally authored by Kris, Aghny, Angga.
 * Modified by Irfan
 */

public class ShipmentDataConverter {

    private static final int PRIME_ADDRESS = 2;
    private static final String MERCHANT_VOUCHER_TYPE = "merchant";
    private static final String LOGISTIC_VOUCHER_TYPE = "logistic";

    @Inject
    public ShipmentDataConverter() {
    }

    public RecipientAddressModel getRecipientAddressModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (cartShipmentAddressFormData.getGroupAddress().size() > 0) {
            UserAddress defaultAddress = null;
            UserAddress tradeInDropOffAddress = null;
            if (cartShipmentAddressFormData.getAddressesData() != null && cartShipmentAddressFormData.getAddressesData().getData() != null) {
                if (cartShipmentAddressFormData.getAddressesData().getData().getDefaultAddress() != null &&
                        Utils.isNotNullOrEmptyOrZero(cartShipmentAddressFormData.getAddressesData().getData().getDefaultAddress().getAddressId())) {
                    defaultAddress = cartShipmentAddressFormData.getAddressesData().getData().getDefaultAddress();
                }
                if (cartShipmentAddressFormData.getAddressesData().getData().getTradeInAddress() != null &&
                        Utils.isNotNullOrEmptyOrZero(cartShipmentAddressFormData.getAddressesData().getData().getTradeInAddress().getAddressId())) {
                    tradeInDropOffAddress = cartShipmentAddressFormData.getAddressesData().getData().getTradeInAddress();
                }
            }
            boolean isTradeIn = false;
            if (cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop() != null &&
                    cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop().size() > 0) {
                for (GroupShop groupShop : cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop()) {
                    if (groupShop.getProducts() != null && groupShop.getProducts().size() > 0) {
                        boolean foundData = false;
                        for (Product product : groupShop.getProducts()) {
                            if (product.getTradeInInfoData() != null && product.getTradeInInfoData().isValidTradeIn()) {
                                isTradeIn = true;
                                foundData = true;
                                break;
                            }
                        }
                        if (foundData) break;
                    }
                }
            }
            RecipientAddressModel recipientAddressModel = createRecipientAddressModel(defaultAddress, tradeInDropOffAddress, isTradeIn);
            recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT);
            if (cartShipmentAddressFormData.getAddressesData() != null) {
                recipientAddressModel.setDisabledAddress(cartShipmentAddressFormData.getAddressesData().getDisableTabs());
                if (cartShipmentAddressFormData.getAddressesData().getActive().equalsIgnoreCase(AddressesData.DEFAULT_ADDRESS)) {
                    recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT);
                } else if (cartShipmentAddressFormData.getAddressesData().getActive().equalsIgnoreCase(AddressesData.TRADE_IN_ADDRESS)) {
                    recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN);
                }
            }
            return recipientAddressModel;
        }
        return null;
    }

    public ShipmentDonationModel getShipmentDonationModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        if (cartShipmentAddressFormData.getDonation() != null) {
            ShipmentDonationModel shipmentDonationModel = new ShipmentDonationModel();
            shipmentDonationModel.setChecked(cartShipmentAddressFormData.getDonation().isChecked());
            shipmentDonationModel.setDonation(cartShipmentAddressFormData.getDonation());

            return shipmentDonationModel;
        } else {
            return null;
        }
    }

    @NonNull
    private RecipientAddressModel createRecipientAddressModel(UserAddress defaultAddress,
                                                              UserAddress tradeInAddress,
                                                              boolean isTradeIn) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        if (defaultAddress != null) {
            recipientAddress.setId(defaultAddress.getAddressId());
            recipientAddress.setAddressStatus(defaultAddress.getStatus());
            recipientAddress.setAddressName(defaultAddress.getAddressName());
            recipientAddress.setCountryName(defaultAddress.getCountry());
            recipientAddress.setProvinceName(defaultAddress.getProvinceName());
            recipientAddress.setDestinationDistrictName(defaultAddress.getDistrictName());
            recipientAddress.setCityName(defaultAddress.getCityName());
            recipientAddress.setDestinationDistrictId(defaultAddress.getDistrictId());
            recipientAddress.setStreet(defaultAddress.getAddress());
            recipientAddress.setPostalCode(defaultAddress.getPostalCode());
            recipientAddress.setCityId(defaultAddress.getCityId());
            recipientAddress.setProvinceId(defaultAddress.getProvinceId());

            recipientAddress.setRecipientName(defaultAddress.getReceiverName());
            recipientAddress.setRecipientPhoneNumber(defaultAddress.getPhone());
            recipientAddress.setLatitude(!UtilsKt.isNullOrEmpty(defaultAddress.getLatitude()) ?
                    defaultAddress.getLatitude() : null);
            recipientAddress.setLongitude(!UtilsKt.isNullOrEmpty(defaultAddress.getLongitude()) ?
                    defaultAddress.getLongitude() : null);

            recipientAddress.setSelected(defaultAddress.getStatus() == PRIME_ADDRESS);
            recipientAddress.setCornerId(defaultAddress.getCornerId());
            recipientAddress.setTradeIn(isTradeIn);
            recipientAddress.setCornerAddress(defaultAddress.isCorner());
        }

        if (tradeInAddress != null) {
            LocationDataModel locationDataModel = new LocationDataModel();
            locationDataModel.setAddress1(tradeInAddress.getAddress());
            locationDataModel.setAddress2(tradeInAddress.getAddress2());
            locationDataModel.setAddrId(tradeInAddress.getAddressId());
            locationDataModel.setAddrName(tradeInAddress.getAddressName());
            locationDataModel.setCity(tradeInAddress.getCityId());
            locationDataModel.setCityName(tradeInAddress.getCityName());
            locationDataModel.setCountry(tradeInAddress.getCountry());
            locationDataModel.setDistrict(tradeInAddress.getDistrictId());
            locationDataModel.setDistrictName(tradeInAddress.getDistrictName());
            locationDataModel.setLatitude(tradeInAddress.getLatitude());
            locationDataModel.setLongitude(tradeInAddress.getLongitude());
            locationDataModel.setPhone(tradeInAddress.getPhone());
            locationDataModel.setPostalCode(tradeInAddress.getPostalCode());
            locationDataModel.setProvince(tradeInAddress.getProvinceId());
            locationDataModel.setProvinceName(tradeInAddress.getProvinceName());
            locationDataModel.setReceiverName(tradeInAddress.getReceiverName());
            locationDataModel.setStatus(tradeInAddress.getStatus());

            recipientAddress.setDropOffAddressName(locationDataModel.getAddrName());
            recipientAddress.setDropOffAddressDetail(locationDataModel.getAddress1());
            recipientAddress.setLocationDataModel(locationDataModel);
        }

        return recipientAddress;
    }

    public List<ShipmentCartItemModel> getShipmentItems(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                        boolean hasTradeInDropOffAddress) {
        List<ShipmentCartItemModel> shipmentCartItemModels = new ArrayList<>();

        if (cartShipmentAddressFormData.getGroupAddress().isEmpty() || cartShipmentAddressFormData.getGroupAddress().get(0) == null) {
            return shipmentCartItemModels;
        }

        UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
        List<GroupShop> groupShopList = cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop();
        for (GroupShop groupShop : groupShopList) {
            ShipmentCartItemModel shipmentCartItemModel = new ShipmentCartItemModel();
            shipmentCartItemModel.setDropshipperDisable(cartShipmentAddressFormData.isDropshipperDisable());
            shipmentCartItemModel.setOrderPrioritasDisable(cartShipmentAddressFormData.isOrderPrioritasDisable());
            shipmentCartItemModel.setIsBlackbox(cartShipmentAddressFormData.isBlackbox());
            shipmentCartItemModel.setHidingCourier(cartShipmentAddressFormData.isHidingCourier());
            shipmentCartItemModel.setAddressId(cartShipmentAddressFormData.getGroupAddress()
                    .get(0).getUserAddress().getAddressId());

            int orderIndex = 0;
            if (groupShopList.size() > 1) {
                orderIndex = groupShopList.indexOf(groupShop) + 1;
            }
            shipmentCartItemModel.setFulfillment(groupShop.isFulfillment());
            shipmentCartItemModel.setFulfillmentId(groupShop.getFulfillmentId());
            shipmentCartItemModel.setFulfillmentBadgeUrl(groupShop.getFulfillmentBadgeUrl());
            getShipmentItem(shipmentCartItemModel, userAddress, groupShop, cartShipmentAddressFormData.getKeroToken(),
                    String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), hasTradeInDropOffAddress, orderIndex);
            if (groupShop.isFulfillment()) {
                shipmentCartItemModel.setShopLocation(groupShop.getFulfillmentName());
            }
            setCartItemModelError(shipmentCartItemModel);
            shipmentCartItemModel.setEligibleNewShippingExperience(cartShipmentAddressFormData.isEligibleNewShippingExperience());
            shipmentCartItemModel.setDisableChangeCourier(groupShop.isDisableChangeCourier());
            shipmentCartItemModel.setAutoCourierSelection(groupShop.getAutoCourierSelection());
            shipmentCartItemModel.setCourierSelectionErrorTitle(groupShop.getCourierSelectionErrorData().getTitle());
            shipmentCartItemModel.setCourierSelectionErrorDescription(groupShop.getCourierSelectionErrorData().getDescription());
            shipmentCartItemModels.add(shipmentCartItemModel);
        }

        return shipmentCartItemModels;
    }

    private void setCartItemModelError(ShipmentCartItemModel shipmentCartItemModel) {
        if (shipmentCartItemModel.isAllItemError()) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                cartItemModel.setError(true);
                cartItemModel.setShopError(true);
            }
        }
    }

    private void getShipmentItem(ShipmentCartItemModel shipmentCartItemModel,
                                 UserAddress userAddress, GroupShop groupShop,
                                 String keroToken, String keroUnixTime,
                                 boolean hasTradeInDropOffAddress, int orderIndex) {
        shipmentCartItemModel.setShopShipmentList(groupShop.getShopShipments());
        shipmentCartItemModel.setError(groupShop.isError());
        if (shipmentCartItemModel.isError()) {
            shipmentCartItemModel.setAllItemError(true);
        }
        shipmentCartItemModel.setErrorTitle(groupShop.getErrorMessage());
        shipmentCartItemModel.setFirstProductErrorIndex(groupShop.getFirstProductErrorIndex());
        shipmentCartItemModel.setProductErrorCount(groupShop.getProductErrorCount());
        if (orderIndex > 0) {
            shipmentCartItemModel.setOrderNumber(orderIndex);
        }
        ShipmentInformationData shipmentInformationData = groupShop.getShipmentInformationData();
        if (shipmentInformationData != null) {
            if (shipmentInformationData.getPreorder().isPreorder()) {
                shipmentCartItemModel.setPreOrderInfo(shipmentInformationData.getPreorder().getDuration());
            }
            if (shipmentInformationData.getFreeShippingExtra().getEligible()) {
                shipmentCartItemModel.setFreeShippingBadgeUrl(shipmentInformationData.getFreeShippingExtra().getBadgeUrl());
                shipmentCartItemModel.setFreeShippingExtra(true);
            } else if (shipmentInformationData.getFreeShipping().getEligible()) {
                shipmentCartItemModel.setFreeShippingBadgeUrl(shipmentInformationData.getFreeShipping().getBadgeUrl());
            }
            shipmentCartItemModel.setShopLocation(shipmentInformationData.getShopLocation());
        }

        Shop shop = groupShop.getShop();
        shipmentCartItemModel.setShopId(shop.getShopId());
        shipmentCartItemModel.setShopName(shop.getShopName());
        shipmentCartItemModel.setShopAlertMessage(shop.getShopAlertMessage());
        shipmentCartItemModel.setShopTypeInfoData(shop.getShopTypeInfoData());

        shipmentCartItemModel.setCartString(groupShop.getCartString());
        shipmentCartItemModel.setShippingId(groupShop.getShippingId());
        shipmentCartItemModel.setSpId(groupShop.getSpId());
        shipmentCartItemModel.setDropshiperName(groupShop.getDropshipperName());
        shipmentCartItemModel.setDropshiperPhone(groupShop.getDropshipperPhone());
        shipmentCartItemModel.setInsurance(groupShop.isUseInsurance());
        shipmentCartItemModel.setHasPromoList(groupShop.isHasPromoList());
        shipmentCartItemModel.setSaveStateFlag(groupShop.isSaveStateFlag());
        shipmentCartItemModel.setIsLeasingProduct(groupShop.isLeasingProduct());
        shipmentCartItemModel.setBookingFee(groupShop.getBookingFee());
        shipmentCartItemModel.setListPromoCodes(groupShop.getListPromoCodes());

        shipmentCartItemModel.setHasSetDropOffLocation(hasTradeInDropOffAddress);

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
        if (product.getProductWholesalePrice() != 0) {
            cartItemModel.setPrice(product.getProductWholesalePrice());
            cartItemModel.setWholesalePrice(true);
        } else {
            cartItemModel.setPrice(product.getProductPrice());
            cartItemModel.setWholesalePrice(false);
        }
        cartItemModel.setOriginalPrice(product.getProductOriginalPrice());
        cartItemModel.setQuantity(product.getProductQuantity());
        cartItemModel.setWeight(product.getProductWeight());
        cartItemModel.setWeightFmt(product.getProductWeightFmt());
        cartItemModel.setWeightActual(product.getProductWeightActual());
        cartItemModel.setNoteToSeller(product.getProductNotes());
        cartItemModel.setPreOrder(product.isProductIsPreorder());
        cartItemModel.setPreOrderInfo(product.getProductPreOrderInfo());
        cartItemModel.setPreOrderDurationDay(product.getPreOrderDurationDay());
        cartItemModel.setFreeReturn(product.isProductIsFreeReturns());
        cartItemModel.setCashback(product.getProductCashback());
        cartItemModel.setCashback(!UtilsKt.isNullOrEmpty(product.getProductCashback()));
        cartItemModel.setFInsurance(product.isProductFcancelPartial());
        cartItemModel.setFCancelPartial(product.isProductFinsurance());
        cartItemModel.setError(product.isError());
        cartItemModel.setErrorMessage(product.getErrorMessage());
        cartItemModel.setErrorMessageDescription(product.getErrorMessageDescription());
        cartItemModel.setFreeShippingExtra(product.isFreeShippingExtra());
        cartItemModel.setFreeShipping(product.isFreeShipping());
        cartItemModel.setShowTicker(product.isShowTicker());
        cartItemModel.setTickerMessage(product.getTickerMessage());
        cartItemModel.setVariant(product.getVariant());
        cartItemModel.setProductAlertMessage(product.getProductAlertMessage());
        cartItemModel.setProductInformation(product.getProductInformation());

        if (product.getTradeInInfoData() != null && product.getTradeInInfoData().isValidTradeIn()) {
            cartItemModel.setValidTradeIn(true);
            cartItemModel.setOldDevicePrice(product.getTradeInInfoData().getOldDevicePrice());
            cartItemModel.setNewDevicePrice(product.getTradeInInfoData().getNewDevicePrice());
            cartItemModel.setDeviceModel(product.getTradeInInfoData().getDeviceModel());
            cartItemModel.setDiagnosticId(product.getTradeInInfoData().getDiagnosticId());
        }

        if (product.getPurchaseProtectionPlanData() != null && product.getPurchaseProtectionPlanData().isProtectionAvailable()) {
            PurchaseProtectionPlanData ppp = product.getPurchaseProtectionPlanData();
            cartItemModel.setProtectionAvailable(ppp.isProtectionAvailable());
            cartItemModel.setProtectionPricePerProduct(ppp.getProtectionPricePerProduct());
            cartItemModel.setProtectionPrice(ppp.getProtectionPrice());
            cartItemModel.setProtectionTitle(ppp.getProtectionTitle());
            cartItemModel.setProtectionSubTitle(ppp.getProtectionSubtitle());
            cartItemModel.setProtectionLinkText(ppp.getProtectionLinkText());
            cartItemModel.setProtectionLinkUrl(ppp.getProtectionLinkUrl());
            cartItemModel.setProtectionOptIn(ppp.isProtectionOptIn());
            cartItemModel.setProtectionCheckboxDisabled(ppp.isProtectionCheckboxDisabled());
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
            if (cartItem.getFInsurance()) {
                isFcancelPartial = 1;
            }
            if (cartItem.getFCancelPartial()) {
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
