package com.tokopedia.purchase_platform.features.checkout.view.converter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel;
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.AddressesData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupAddress;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupShop;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Product;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.PurchaseProtectionPlanData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Shop;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.UserAddress;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.ShipmentDonationModel;

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
        if (cartShipmentAddressFormData.getGroupAddress() != null && cartShipmentAddressFormData.getGroupAddress().size() > 0) {
            UserAddress defaultAddress = null;
            UserAddress tradeInAddress = null;
            if (cartShipmentAddressFormData.getAddressesData() != null && cartShipmentAddressFormData.getAddressesData().getData() != null) {
                if (cartShipmentAddressFormData.getAddressesData().getData().getDefaultAddress() != null &&
                        cartShipmentAddressFormData.getAddressesData().getData().getDefaultAddress().getAddressId() != 0) {
                    defaultAddress = cartShipmentAddressFormData.getAddressesData().getData().getDefaultAddress();
                }
                if (cartShipmentAddressFormData.getAddressesData().getData().getTradeInAddress() != null &&
                        cartShipmentAddressFormData.getAddressesData().getData().getTradeInAddress().getAddressId() != 0) {
                    tradeInAddress = cartShipmentAddressFormData.getAddressesData().getData().getTradeInAddress();
                }
            }
            boolean isTradeIn = false;
            boolean isTradeInDropOffEnable = false;
            if (cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop() != null &&
                    cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop().size() > 0) {
                for (GroupShop groupShop : cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop()) {
                    if (groupShop.getProducts() != null && groupShop.getProducts().size() > 0) {
                        boolean foundData = false;
                        for (Product product : groupShop.getProducts()) {
                            if (product.getTradeInInfoData() != null && product.getTradeInInfoData().isValidTradeIn()) {
                                isTradeIn = true;
                                isTradeInDropOffEnable = product.getTradeInInfoData().isDropOffEnable();
                                foundData = true;
                                break;
                            }
                        }
                        if (foundData) break;
                    }
                }
            }
            RecipientAddressModel recipientAddressModel = createRecipientAddressModel(defaultAddress, tradeInAddress, isTradeIn, isTradeInDropOffEnable);
            String disableMultipleAddressMessage = "";
            if (cartShipmentAddressFormData.getDisabledFeaturesDetailData() != null) {
                disableMultipleAddressMessage = cartShipmentAddressFormData.getDisabledFeaturesDetailData().getDisabledMultiAddressMessage();
            }
            recipientAddressModel.setDisabledMultiAddressMessage(disableMultipleAddressMessage);
            recipientAddressModel.setSelectedTabIndex(RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT);
            if (cartShipmentAddressFormData.getAddressesData() != null) {
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

    public RecipientAddressModel getRecipientAddressModel(UserAddress defaultAddress) {
        // Trade in is only available on OCS.
        // OCS is not available to send to multiple address
        return createRecipientAddressModel(defaultAddress, null, false, false);
    }

    public ShipmentDonationModel getShipmentDonationModel(CartShipmentAddressFormData cartShipmentAddressFormData) {
        ShipmentDonationModel shipmentDonationModel = new ShipmentDonationModel();
        shipmentDonationModel.setChecked(cartShipmentAddressFormData.getDonation().isChecked());
        shipmentDonationModel.setDonation(cartShipmentAddressFormData.getDonation());

        return shipmentDonationModel;
    }

    @NonNull
    private RecipientAddressModel createRecipientAddressModel(UserAddress defaultAddress,
                                                              UserAddress tradeInAddress,
                                                              boolean isTradeIn,
                                                              boolean isTradeInDropOffEnable) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        if (defaultAddress != null) {
            recipientAddress.setId(String.valueOf(defaultAddress.getAddressId()));
            recipientAddress.setAddressStatus(defaultAddress.getStatus());
            recipientAddress.setAddressName(defaultAddress.getAddressName());
            recipientAddress.setCountryName(defaultAddress.getCountry());
            recipientAddress.setProvinceName(defaultAddress.getProvinceName());
            recipientAddress.setDestinationDistrictName(defaultAddress.getDistrictName());
            recipientAddress.setCityName(defaultAddress.getCityName());
            recipientAddress.setDestinationDistrictId(String.valueOf(defaultAddress.getDistrictId()));
            recipientAddress.setStreet(defaultAddress.getAddress());
            recipientAddress.setPostalCode(defaultAddress.getPostalCode());
            recipientAddress.setCityId(String.valueOf(defaultAddress.getCityId()));
            recipientAddress.setProvinceId(String.valueOf(defaultAddress.getProvinceId()));

            recipientAddress.setRecipientName(defaultAddress.getReceiverName());
            recipientAddress.setRecipientPhoneNumber(defaultAddress.getPhone());
            recipientAddress.setLatitude(!TextUtils.isEmpty(defaultAddress.getLatitude()) ?
                    defaultAddress.getLatitude() : null);
            recipientAddress.setLongitude(!TextUtils.isEmpty(defaultAddress.getLongitude()) ?
                    defaultAddress.getLongitude() : null);

            recipientAddress.setSelected(defaultAddress.getStatus() == PRIME_ADDRESS);
            recipientAddress.setCornerId(String.valueOf(defaultAddress.getCornerId()));
            recipientAddress.setTradeIn(isTradeIn);
            recipientAddress.setTradeInDropOffEnable(isTradeInDropOffEnable);
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

        int addressIndex = 0;
        ShipmentCartItemModel shipmentCartItemModel = null;
        if (cartShipmentAddressFormData.isMultiple()) {
            for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                UserAddress userAddress = groupAddress.getUserAddress();
                for (GroupShop groupShop : groupAddress.getGroupShop()) {
                    shipmentCartItemModel = new ShipmentCartItemModel();
                    shipmentCartItemModel.setUseCourierRecommendation(cartShipmentAddressFormData
                            .isUseCourierRecommendation());
                    shipmentCartItemModel.setHidingCourier(cartShipmentAddressFormData.isHidingCourier());
                    shipmentCartItemModel.setIsBlackbox(cartShipmentAddressFormData.getIsBlackbox());
                    shipmentCartItemModel.setAddressId(cartShipmentAddressFormData.getGroupAddress()
                            .get(addressIndex).getUserAddress().getAddressId());


                    if (cartShipmentAddressFormData.getAutoApplyStackData() != null) {
                        if (cartShipmentAddressFormData.getAutoApplyStackData().getVoucherOrdersItemDataList() != null) {
                            for (VoucherOrdersItemData voucherOrdersItemData : cartShipmentAddressFormData.getAutoApplyStackData().getVoucherOrdersItemDataList()) {
                                if (groupShop.getCartString().equalsIgnoreCase(voucherOrdersItemData.getUniqueId())) {
                                    if (voucherOrdersItemData.getType().equalsIgnoreCase(MERCHANT_VOUCHER_TYPE)) {
                                        shipmentCartItemModel.setVoucherOrdersItemUiModel(convertFromVoucherOrdersItem(voucherOrdersItemData));
                                    } else if (voucherOrdersItemData.getType().equalsIgnoreCase(LOGISTIC_VOUCHER_TYPE)) {
                                        VoucherLogisticItemUiModel voucherLogisticItemUiModel = new VoucherLogisticItemUiModel();
                                        voucherLogisticItemUiModel.setCode(voucherOrdersItemData.getCode());
                                        voucherLogisticItemUiModel.setCouponDesc(voucherOrdersItemData.getTitleDescription());
                                        voucherLogisticItemUiModel.setCouponAmount(Utils.getFormattedCurrency(voucherOrdersItemData.getDiscountAmount()));
                                        voucherLogisticItemUiModel.setCouponAmountRaw(voucherOrdersItemData.getDiscountAmount());
                                        voucherLogisticItemUiModel.setMessage(convertFromMessage(voucherOrdersItemData.getMessageData()));
                                        shipmentCartItemModel.setVoucherLogisticItemUiModel(voucherLogisticItemUiModel);
                                    }
                                }
                            }
                        }
                    }

                    getShipmentItem(shipmentCartItemModel, userAddress, groupShop,
                            cartShipmentAddressFormData.getKeroToken(),
                            String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), true, hasTradeInDropOffAddress);
                    shipmentCartItemModel.setFulfillment(groupShop.isFulfillment());
                    shipmentCartItemModel.setFulfillmentId(groupShop.getFulfillmentId());
                    shipmentCartItemModel.setFulfillmentName(groupShop.getFulfillmentName());
                    setCartItemModelError(shipmentCartItemModel);
                    shipmentCartItemModels.add(shipmentCartItemModel);
                }
                addressIndex++;
            }
        } else {
            UserAddress userAddress = cartShipmentAddressFormData.getGroupAddress().get(0).getUserAddress();
            for (GroupShop groupShop : cartShipmentAddressFormData.getGroupAddress().get(0).getGroupShop()) {
                shipmentCartItemModel = new ShipmentCartItemModel();
                shipmentCartItemModel.setDropshipperDisable(cartShipmentAddressFormData.isDropshipperDisable());
                shipmentCartItemModel.setOrderPrioritasDisable(cartShipmentAddressFormData.isOrderPrioritasDisable());
                shipmentCartItemModel.setUseCourierRecommendation(cartShipmentAddressFormData.isUseCourierRecommendation());
                shipmentCartItemModel.setIsBlackbox(cartShipmentAddressFormData.getIsBlackbox());
                shipmentCartItemModel.setHidingCourier(cartShipmentAddressFormData.isHidingCourier());
                shipmentCartItemModel.setAddressId(cartShipmentAddressFormData.getGroupAddress()
                        .get(0).getUserAddress().getAddressId());

                if (cartShipmentAddressFormData.getAutoApplyStackData() != null) {
                    if (cartShipmentAddressFormData.getAutoApplyStackData().getVoucherOrdersItemDataList() != null) {
                        for (VoucherOrdersItemData voucherOrdersItemData : cartShipmentAddressFormData.getAutoApplyStackData().getVoucherOrdersItemDataList()) {
                            if (groupShop.getCartString().equalsIgnoreCase(voucherOrdersItemData.getUniqueId())) {
                                if (voucherOrdersItemData.getType().equalsIgnoreCase(MERCHANT_VOUCHER_TYPE)) {
                                    shipmentCartItemModel.setVoucherOrdersItemUiModel(convertFromVoucherOrdersItem(voucherOrdersItemData));
                                } else if (voucherOrdersItemData.getType().equalsIgnoreCase(LOGISTIC_VOUCHER_TYPE)) {
                                    VoucherLogisticItemUiModel voucherLogisticItemUiModel = new VoucherLogisticItemUiModel();
                                    voucherLogisticItemUiModel.setCode(voucherOrdersItemData.getCode());
                                    voucherLogisticItemUiModel.setCouponDesc(voucherOrdersItemData.getTitleDescription());
                                    voucherLogisticItemUiModel.setCouponAmount(Utils.getFormattedCurrency(voucherOrdersItemData.getDiscountAmount()));
                                    voucherLogisticItemUiModel.setCouponAmountRaw(voucherOrdersItemData.getDiscountAmount());
                                    voucherLogisticItemUiModel.setMessage(convertFromMessage(voucherOrdersItemData.getMessageData()));
                                    shipmentCartItemModel.setVoucherLogisticItemUiModel(voucherLogisticItemUiModel);
                                } else {
                                    shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                                    shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
                                }
                            }
                        }
                    }
                }

                getShipmentItem(shipmentCartItemModel, userAddress, groupShop, cartShipmentAddressFormData.getKeroToken(),
                        String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()), false, hasTradeInDropOffAddress);
                shipmentCartItemModel.setFulfillment(groupShop.isFulfillment());
                shipmentCartItemModel.setFulfillmentId(groupShop.getFulfillmentId());
                shipmentCartItemModel.setFulfillmentName(groupShop.getFulfillmentName());
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
                                 String keroToken, String keroUnixTime,
                                 boolean isMultiple, boolean hasTradeInDropOffAddress) {
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

        shipmentCartItemModel.setCartString(groupShop.getCartString());
        shipmentCartItemModel.setShippingId(groupShop.getShippingId());
        shipmentCartItemModel.setSpId(groupShop.getSpId());
        shipmentCartItemModel.setDropshiperName(groupShop.getDropshipperName());
        shipmentCartItemModel.setDropshiperPhone(groupShop.getDropshipperPhone());
        shipmentCartItemModel.setInsurance(groupShop.isUseInsurance());
        shipmentCartItemModel.setHasPromoList(groupShop.isHasPromoList());
        shipmentCartItemModel.setSaveStateFlag(groupShop.isSaveStateFlag());
        shipmentCartItemModel.setIsLeasingProduct(groupShop.getIsLeasingProduct());
        shipmentCartItemModel.setBookingFee(groupShop.getBookingFee());

        shipmentCartItemModel.setHasSetDropOffLocation(hasTradeInDropOffAddress);

        List<Product> products = groupShop.getProducts();
        List<CartItemModel> cartItemModels = convertFromProductList(products);

        // This is something that not well planned
        Fobject fobject = levelUpParametersFromProductToCartSeller(cartItemModels);
        shipmentCartItemModel.setProductFcancelPartial(fobject.isFcancelPartial() == 1);
        shipmentCartItemModel.setCartItemModels(cartItemModels);
        shipmentCartItemModel.setProductIsPreorder(fobject.isPreOrder() == 1);

        // set promo merchant
        if (groupShop.getShop().getVoucherOrdersItemData() != null) {
            if (groupShop.getShop().getVoucherOrdersItemData().getType().equalsIgnoreCase(MERCHANT_VOUCHER_TYPE)) {
                shipmentCartItemModel.setVoucherOrdersItemUiModel(convertFromVoucherOrdersItem(groupShop.getShop().getVoucherOrdersItemData()));
            } else if (groupShop.getShop().getVoucherOrdersItemData().getType().equalsIgnoreCase(LOGISTIC_VOUCHER_TYPE)) {
                VoucherLogisticItemUiModel voucherLogisticItemUiModel = new VoucherLogisticItemUiModel();
                voucherLogisticItemUiModel.setCode(groupShop.getShop().getVoucherOrdersItemData().getCode());
                voucherLogisticItemUiModel.setCouponDesc(groupShop.getShop().getVoucherOrdersItemData().getTitleDescription());
                voucherLogisticItemUiModel.setCouponAmount(Utils.getFormattedCurrency(groupShop.getShop().getVoucherOrdersItemData().getDiscountAmount()));
                voucherLogisticItemUiModel.setCouponAmountRaw(groupShop.getShop().getVoucherOrdersItemData().getDiscountAmount());
                voucherLogisticItemUiModel.setCashbackAmount(groupShop.getShop().getVoucherOrdersItemData().getCashbackWalletAmount());
                voucherLogisticItemUiModel.setDiscountAmount(groupShop.getShop().getVoucherOrdersItemData().getDiscountAmount());
                voucherLogisticItemUiModel.setMessage(convertFromMessage(groupShop.getShop().getVoucherOrdersItemData().getMessageData()));
                shipmentCartItemModel.setVoucherLogisticItemUiModel(voucherLogisticItemUiModel);
            } else {
                shipmentCartItemModel.setVoucherOrdersItemUiModel(null);
                shipmentCartItemModel.setVoucherLogisticItemUiModel(null);
            }
        }

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
        cartItemModel.setOriginalPrice(product.getProductOriginalPrice());
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
        cartItemModel.setFreeShipping(product.isFreeShipping());
        cartItemModel.setFreeShippingBadgeUrl(product.getFreeShippingBadgeUrl());

        if (product.getTradeInInfoData() != null && product.getTradeInInfoData().isValidTradeIn()) {
            cartItemModel.setValidTradeIn(true);
            cartItemModel.setOldDevicePrice(product.getTradeInInfoData().getOldDevicePrice());
            cartItemModel.setNewDevicePrice(product.getTradeInInfoData().getNewDevicePrice());
        }

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
