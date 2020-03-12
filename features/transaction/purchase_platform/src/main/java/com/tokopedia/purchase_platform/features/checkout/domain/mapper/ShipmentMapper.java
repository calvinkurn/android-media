package com.tokopedia.purchase_platform.features.checkout.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.ShipProd;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant;
import com.tokopedia.purchase_platform.common.data.model.response.TrackingDetail;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoApplyStack;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.Message;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.VoucherOrdersItem;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData;
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.LastApplyData;
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.PromoCheckoutErrorDefault;
import com.tokopedia.purchase_platform.common.feature.promo_global.domain.model.GlobalCouponAttrData;
import com.tokopedia.purchase_platform.common.feature.ticker_announcement.TickerData;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;
import com.tokopedia.purchase_platform.features.cart.data.model.response.Ticker;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.egold.EgoldTieringData;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.AdditionalInfo;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.Addresses;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.CheckoutDisabledFeaturesKt;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.Data;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.LastApply;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.SummariesItem;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.AddressesData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.DataAddressData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.DisabledFeaturesDetailData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Donation;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupAddress;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupShop;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Product;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.ProductShipment;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.ProductShipmentMapping;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.PurchaseProtectionPlanData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.ServiceId;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Shop;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.TradeInInfoData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.UserAddress;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.EgoldTieringModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.STATE_RED;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.TYPE_CASHBACK;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class ShipmentMapper implements IShipmentMapper {
    private static final String SHOP_TYPE_OFFICIAL_STORE = "official_store";
    private static final String SHOP_TYPE_GOLD_MERCHANT = "gold_merchant";
    private static final String SHOP_TYPE_REGULER = "reguler";

    @Inject
    public ShipmentMapper() {
    }

    @Override
    public CartShipmentAddressFormData convertToShipmentAddressFormData(
            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
    ) {

        CartShipmentAddressFormData dataResult = new CartShipmentAddressFormData();
        dataResult.setKeroDiscomToken(shipmentAddressFormDataResponse.getKeroDiscomToken());
        dataResult.setKeroToken(shipmentAddressFormDataResponse.getKeroToken());
        dataResult.setKeroUnixTime(shipmentAddressFormDataResponse.getKeroUnixTime());
        dataResult.setMultiple(shipmentAddressFormDataResponse.getIsMultiple() == 1);
        dataResult.setUseCourierRecommendation(shipmentAddressFormDataResponse.getIsRobinhood() == 1);
        dataResult.setHidingCourier(shipmentAddressFormDataResponse.getHideCourier());
        dataResult.setIsBlackbox(shipmentAddressFormDataResponse.getIsBlackbox() == 1);
        dataResult.setErrorCode(shipmentAddressFormDataResponse.getErrorCode());
        dataResult.setError(!UtilsKt.isNullOrEmpty(shipmentAddressFormDataResponse.getErrors()));
        dataResult.setErrorMessage(UtilsKt.convertToString(shipmentAddressFormDataResponse.getErrors()));
        dataResult.setShowOnboarding(shipmentAddressFormDataResponse.isShowOnboarding());
        dataResult.setIneligbilePromoDialogEnabled(shipmentAddressFormDataResponse.isIneligbilePromoDialogEnabled());

        if (shipmentAddressFormDataResponse.getDisabledFeatures() != null &&
                shipmentAddressFormDataResponse.getDisabledFeatures().contains(CheckoutDisabledFeaturesKt.multiAddress) &&
                shipmentAddressFormDataResponse.getDisabledFeaturesDetail() != null) {
            DisabledFeaturesDetailData disabledFeaturesDetailData = new DisabledFeaturesDetailData();
            disabledFeaturesDetailData.setDisabledMultiAddressMessage(
                    shipmentAddressFormDataResponse.getDisabledFeaturesDetail().getDisabledMultiAddressMessage()
            );
            dataResult.setDisabledFeaturesDetailData(disabledFeaturesDetailData);
        }

        boolean isDisableEgold = false;
        boolean isDisablePPP = false;
        boolean isDisableDonation = false;

        for (String disabledFeature : shipmentAddressFormDataResponse.getDisabledFeatures()) {
            switch (disabledFeature) {
                case CheckoutDisabledFeaturesKt.dropshipper:
                    dataResult.setDropshipperDisable(true);
                    break;
                case CheckoutDisabledFeaturesKt.multiAddress:
                    dataResult.setMultipleDisable(true);
                    break;
                case CheckoutDisabledFeaturesKt.orderPrioritas:
                    dataResult.setOrderPrioritasDisable(true);
                    break;
                case CheckoutDisabledFeaturesKt.egold:
                    isDisableEgold = true;
                    break;
                case CheckoutDisabledFeaturesKt.ppp:
                    isDisablePPP = true;
                    break;
                case CheckoutDisabledFeaturesKt.donation:
                    isDisableDonation = true;
                    break;
            }
        }

        Addresses addresses = shipmentAddressFormDataResponse.getAddresses();
        DataAddressData dataAddressData = new DataAddressData();
        if (addresses != null) {
            if (addresses.getData() != null) {
                if (addresses.getData().getDefaultAddress() != null && addresses.getActive().equals(AddressesData.TRADE_IN_ADDRESS)) {
                    com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.UserAddress defaultAddress =
                            addresses.getData().getDefaultAddress();
                    UserAddress defaultAddressData = getUserAddress(defaultAddress);
                    dataAddressData.setDefaultAddress(defaultAddressData);
                } else if (shipmentAddressFormDataResponse.getIsMultiple() == 0) {
                    com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.UserAddress defaultAddress =
                            shipmentAddressFormDataResponse.getGroupAddress().get(0).getUserAddress();
                    UserAddress defaultAddressData = getUserAddress(defaultAddress);
                    dataAddressData.setDefaultAddress(defaultAddressData);
                }

                if (addresses.getData().getTradeInAddress() != null) {
                    com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.UserAddress tradeInAddress =
                            addresses.getData().getTradeInAddress();
                    UserAddress tradeInAddressData = getUserAddress(tradeInAddress);
                    dataAddressData.setTradeInAddress(tradeInAddressData);
                }
            }
        } else {
            if (shipmentAddressFormDataResponse.getIsMultiple() == 0) {
                com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.UserAddress defaultAddress =
                        shipmentAddressFormDataResponse.getGroupAddress().get(0).getUserAddress();
                UserAddress defaultAddressData = getUserAddress(defaultAddress);
                dataAddressData.setDefaultAddress(defaultAddressData);
            }
        }

        AddressesData addressesData = new AddressesData();
        addressesData.setActive(addresses.getActive() != null ? addresses.getActive() : "");
        addressesData.setData(dataAddressData);

        dataResult.setAddressesData(addressesData);

        if (shipmentAddressFormDataResponse.getTickers() != null && !shipmentAddressFormDataResponse.getTickers().isEmpty()) {
            Ticker ticker = shipmentAddressFormDataResponse.getTickers().get(0);
            dataResult.setTickerData(new TickerData(ticker.getId(), ticker.getMessage(), ticker.getPage()));
        }

        if (!isDisableEgold) {
            if (shipmentAddressFormDataResponse.getEgoldAttributes() != null) {
                EgoldAttributeModel egoldAttributeModel = new EgoldAttributeModel();
                egoldAttributeModel.setEligible(shipmentAddressFormDataResponse.getEgoldAttributes().isEligible());
                egoldAttributeModel.setTiering(shipmentAddressFormDataResponse.getEgoldAttributes().isTiering());
                if (shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldRange() != null) {
                    egoldAttributeModel.setMinEgoldRange(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldRange().getMinEgoldValue());
                    egoldAttributeModel.setMaxEgoldRange(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldRange().getMaxEgoldValue());
                }
                if (shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage() != null) {
                    egoldAttributeModel.setTitleText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getTitleText());
                    egoldAttributeModel.setSubText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getSubText());
                    egoldAttributeModel.setTickerText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getTickerText());
                    egoldAttributeModel.setTooltipText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getTooltipText());
                }

                if (shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldTieringDataArrayList() != null) {
                    EgoldTieringModel egoldTieringModel;
                    ArrayList<EgoldTieringModel> egoldTieringModelArrayList = new ArrayList<>();

                    for (EgoldTieringData data : shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldTieringDataArrayList()) {
                        egoldTieringModel = new EgoldTieringModel();
                        egoldTieringModel.setBasisAmount(data.getBasisAmount());
                        egoldTieringModel.setMaxAmount(data.getMaxAmount());
                        egoldTieringModel.setMinAmount(data.getMinAmount());
                        egoldTieringModel.setMinTotalAmount(data.getMinTotalAmount());
                        egoldTieringModelArrayList.add(egoldTieringModel);
                    }
                    egoldAttributeModel.setEgoldTieringModelArrayList(egoldTieringModelArrayList);
                }

                dataResult.setEgoldAttributes(egoldAttributeModel);
            }
        }

        if (shipmentAddressFormDataResponse.getAutoapplyStack() != null) {
            AutoApplyStackData autoApplyStackData = new AutoApplyStackData();
            if (shipmentAddressFormDataResponse.getAutoapplyStack().getCodes() != null) {
                if (shipmentAddressFormDataResponse.getAutoapplyStack().getCodes().size() > 0) {
                    autoApplyStackData.setCode(shipmentAddressFormDataResponse.getAutoapplyStack().getCodes().get(0));
                    autoApplyStackData.setDiscountAmount(shipmentAddressFormDataResponse.getAutoapplyStack().getDiscountAmount());
                    autoApplyStackData.setIsCoupon(shipmentAddressFormDataResponse.getAutoapplyStack().getIsCoupon());
                    autoApplyStackData.setMessageSuccess(shipmentAddressFormDataResponse.getAutoapplyStack().getMessage().getText());
                    autoApplyStackData.setPromoCodeId(shipmentAddressFormDataResponse.getAutoapplyStack().getPromoCodeId());
                    autoApplyStackData.setSuccess(shipmentAddressFormDataResponse.getAutoapplyStack().isSuccess());
                    autoApplyStackData.setTitleDescription(shipmentAddressFormDataResponse.getAutoapplyStack().getTitleDescription());
                    autoApplyStackData.setState(shipmentAddressFormDataResponse.getAutoapplyStack().getMessage().getState());
                }
            }

            if (shipmentAddressFormDataResponse.getAutoapplyStack().getVoucherOrders() != null) {
                if (shipmentAddressFormDataResponse.getAutoapplyStack().getVoucherOrders().size() > 0) {
                    List<VoucherOrdersItemData> voucherOrdersItemDataList = new ArrayList<>();
                    for (VoucherOrdersItem voucherOrdersItem : shipmentAddressFormDataResponse.getAutoapplyStack().getVoucherOrders()) {
                        VoucherOrdersItemData voucherOrdersItemData = new VoucherOrdersItemData();
                        voucherOrdersItemData.setCode(voucherOrdersItem.getCode());
                        voucherOrdersItemData.setSuccess(voucherOrdersItem.isSuccess());
                        voucherOrdersItemData.setUniqueId(voucherOrdersItem.getUniqueId());
                        voucherOrdersItemData.setCartId(voucherOrdersItem.getCartId());
                        voucherOrdersItemData.setShopId(voucherOrdersItem.getShopId());
                        voucherOrdersItemData.setIsPO(voucherOrdersItem.getIsPo());
                        voucherOrdersItemData.setAddressId(voucherOrdersItem.getAddressId());
                        voucherOrdersItemData.setType(voucherOrdersItem.getType());
                        voucherOrdersItemData.setCashbackWalletAmount(voucherOrdersItem.getCashbackWalletAmount());
                        voucherOrdersItemData.setDiscountAmount(voucherOrdersItem.getDiscountAmount());
                        voucherOrdersItemData.setInvoiceDescription(voucherOrdersItem.getInvoiceDescription());
                        voucherOrdersItemData.setMessageData(convertToMessageData(voucherOrdersItem.getMessage()));
                        voucherOrdersItemData.setTitleDescription(voucherOrdersItem.getTitleDescription());
                        voucherOrdersItemData.setIsAutoapply(true);
                        voucherOrdersItemDataList.add(voucherOrdersItemData);
                    }
                    autoApplyStackData.setVoucherOrdersItemDataList(voucherOrdersItemDataList);
                }
            }
            dataResult.setAutoApplyStackData(autoApplyStackData);
        }

        if (shipmentAddressFormDataResponse.getGlobalCouponAttr() != null) {
            GlobalCouponAttrData globalCouponAttrData = new GlobalCouponAttrData();
            if (shipmentAddressFormDataResponse.getGlobalCouponAttr() != null) {
                if (shipmentAddressFormDataResponse.getGlobalCouponAttr().getDescription() != null) {
                    globalCouponAttrData.setDescription(shipmentAddressFormDataResponse.getGlobalCouponAttr().getDescription());
                }
                globalCouponAttrData.setQuantityLabel(shipmentAddressFormDataResponse.getGlobalCouponAttr().getQuantityLabel());
            }
            dataResult.setGlobalCouponAttrData(globalCouponAttrData);
        }

        if (!isDisableDonation) {
            if (shipmentAddressFormDataResponse.getDonation() != null) {
                Donation donation = new Donation();
                donation.setTitle(shipmentAddressFormDataResponse.getDonation().getTitle());
                donation.setDescription(shipmentAddressFormDataResponse.getDonation().getDescription());
                donation.setNominal(shipmentAddressFormDataResponse.getDonation().getNominal());
                donation.setChecked(shipmentAddressFormDataResponse.isDonationCheckboxStatus());
                dataResult.setDonation(donation);
            }
        }

        if (shipmentAddressFormDataResponse.getCod() != null) {
            CodModel cod = new CodModel();
            cod.setCod(shipmentAddressFormDataResponse.getCod().isCod());
            cod.setCounterCod(shipmentAddressFormDataResponse.getCod().getCounterCod());
            cod.setMessageInfo(shipmentAddressFormDataResponse.getMessage().getMessageInfo());
            cod.setMessageLink(shipmentAddressFormDataResponse.getMessage().getMessageLink());
            cod.setMessageLogo(shipmentAddressFormDataResponse.getMessage().getMessageLogo());
            dataResult.setCod(cod);
        }

        if (shipmentAddressFormDataResponse.getPromoSAFResponse().getLastApply().getData() != null) {
            LastApply lastApply = shipmentAddressFormDataResponse.getPromoSAFResponse().getLastApply();
            if (lastApply.getData() != null) {
                AdditionalInfo responseAdditionalInfo = lastApply.getData().getAdditionalInfo();
                LastApplyData lastApplyData = new LastApplyData();
                lastApplyData.setAdditionalInfoMsg(responseAdditionalInfo.getMessageInfo().getMessage());
                lastApplyData.setAdditionalInfoDetailMsg(responseAdditionalInfo.getMessageInfo().getDetail());
                lastApplyData.setErrorDetailMsg(responseAdditionalInfo.getErrorDetail().getMessage());

                if (lastApply.getData().getBenefitSummaryInfo() != null && lastApply.getData().getBenefitSummaryInfo().getSummaries() != null) {
                    if (lastApply.getData().getBenefitSummaryInfo().getSummaries().size() > 0) {
                        for (int i=0; i<lastApply.getData().getBenefitSummaryInfo().getSummaries().size(); i++) {
                            SummariesItem summariesItem = lastApply.getData().getBenefitSummaryInfo().getSummaries().get(i);
                            if (summariesItem.getType().equalsIgnoreCase(TYPE_CASHBACK)) {
                                lastApplyData.setFinalBenefitText(summariesItem.getDescription());
                                lastApplyData.setFinalBenefitAmount(summariesItem.getAmountStr());
                                break;
                            }
                        }
                    }
                }
                lastApplyData.setListRedPromos(mapCreateListRedPromosCheckout(lastApply.getData()));
                dataResult.setLastApplyData(lastApplyData);
            }
        }

        if (shipmentAddressFormDataResponse.getPromoSAFResponse().getErrorDefault() != null) {
            PromoCheckoutErrorDefault promoCheckoutErrorDefault = new PromoCheckoutErrorDefault();
            promoCheckoutErrorDefault.setTitle(shipmentAddressFormDataResponse.getPromoSAFResponse().getErrorDefault().getTitle());
            promoCheckoutErrorDefault.setDesc(shipmentAddressFormDataResponse.getPromoSAFResponse().getErrorDefault().getDescription());
            dataResult.setPromoCheckoutErrorDefault(promoCheckoutErrorDefault);
        }

        if (!UtilsKt.isNullOrEmpty(shipmentAddressFormDataResponse.getGroupAddress())) {
            List<GroupAddress> groupAddressListResult = new ArrayList<>();
            for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.GroupAddress
                    groupAddress : shipmentAddressFormDataResponse.getGroupAddress()) {
                GroupAddress groupAddressResult = new GroupAddress();
                groupAddressResult.setError(!UtilsKt.isNullOrEmpty(groupAddress.getErrors()));
                groupAddressResult.setErrorMessage(UtilsKt.convertToString(groupAddress.getErrors()));
                groupAddressResult.setWarning(!UtilsKt.isNullOrEmpty(groupAddress.getMessages()));
                groupAddressResult.setWarningMessage(UtilsKt.convertToString(groupAddress.getMessages()));

                if (groupAddress.getUserAddress() != null) {
                    com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.UserAddress userAddressResult =
                            new com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.UserAddress();
                    userAddressResult.setStatus(groupAddress.getUserAddress().getStatus());
                    userAddressResult.setAddress(groupAddress.getUserAddress().getAddress());
                    userAddressResult.setAddress2(groupAddress.getUserAddress().getAddress2());
                    userAddressResult.setAddressId(groupAddress.getUserAddress().getAddressId());
                    userAddressResult.setAddressName(groupAddress.getUserAddress().getAddressName());
                    userAddressResult.setCityId(groupAddress.getUserAddress().getCityId());
                    userAddressResult.setCityName(groupAddress.getUserAddress().getCityName());
                    userAddressResult.setCountry(groupAddress.getUserAddress().getCountry());
                    userAddressResult.setDistrictId(groupAddress.getUserAddress().getDistrictId());
                    userAddressResult.setDistrictName(groupAddress.getUserAddress().getDistrictName());
                    userAddressResult.setLatitude(groupAddress.getUserAddress().getLatitude());
                    userAddressResult.setLongitude(groupAddress.getUserAddress().getLongitude());
                    userAddressResult.setPhone(groupAddress.getUserAddress().getPhone());
                    userAddressResult.setPostalCode(groupAddress.getUserAddress().getPostalCode());
                    userAddressResult.setProvinceId(groupAddress.getUserAddress().getProvinceId());
                    userAddressResult.setProvinceName(groupAddress.getUserAddress().getProvinceName());
                    userAddressResult.setReceiverName(groupAddress.getUserAddress().getReceiverName());
                    userAddressResult.setCornerId(groupAddress.getUserAddress().getCornerId());
                    userAddressResult.setCorner(groupAddress.getUserAddress().isCorner());

                    groupAddressResult.setUserAddress(userAddressResult);
                }

                if (!UtilsKt.isNullOrEmpty(groupAddress.getGroupShop())) {
                    List<GroupShop> groupShopListResult = new ArrayList<>();
                    for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.GroupShop
                            groupShop : groupAddress.getGroupShop()) {
                        com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupShop groupShopResult =
                                new com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupShop();

                        groupShopResult.setError(!UtilsKt.isNullOrEmpty(groupShop.getErrors()));
                        groupShopResult.setErrorMessage(UtilsKt.convertToString(groupShop.getErrors()));
                        groupShopResult.setWarning(!UtilsKt.isNullOrEmpty(groupShop.getMessages()));
                        groupShopResult.setWarningMessage(UtilsKt.convertToString(groupShop.getMessages()));

                        groupShopResult.setShippingId(groupShop.getShippingId());
                        groupShopResult.setSpId(groupShop.getSpId());
                        groupShopResult.setDropshipperName(groupShop.getDropshiper() != null ? groupShop.getDropshiper().getName() : "");
                        groupShopResult.setDropshipperPhone(groupShop.getDropshiper() != null ? groupShop.getDropshiper().getTelpNo() : "");
                        groupShopResult.setUseInsurance(groupShop.isInsurance());
                        groupShopResult.setCartString(groupShop.getCartString() != null ? groupShop.getCartString() : "");
                        groupShopResult.setHasPromoList(groupShop.isHasPromoList());
                        groupShopResult.setSaveStateFlag(groupShop.isSaveStateFlag());

                        if (groupShop.getVehicleLeasing() != null) {
                            groupShopResult.setIsLeasingProduct(groupShop.getVehicleLeasing().isLeasingProduct());
                            groupShopResult.setBookingFee(groupShop.getVehicleLeasing().getBookingFee());
                        }

                        if (groupShop.getListPromoCodes() != null && groupShop.getListPromoCodes().size() > 0) {
                            groupShopResult.setListPromoCodes(groupShop.getListPromoCodes());
                        }

                        groupShopResult.setFulfillment(groupShop.isFulfillment());
                        if (groupShop.getWarehouse() != null) {
                            groupShopResult.setFulfillmentId(groupShop.getWarehouse().getWarehouseId());
                            groupShopResult.setFulfillmentName(groupShop.getWarehouse().getCityName());
                        }

                        if (groupShop.getShop() != null) {
                            Shop shopResult = new Shop();

                            shopResult.setShopId(groupShop.getShop().getShopId());
                            shopResult.setUserId(groupShop.getShop().getUserId());
                            shopResult.setShopName(groupShop.getShop().getShopName());
                            shopResult.setShopImage(groupShop.getShop().getShopImage());
                            shopResult.setShopUrl(groupShop.getShop().getShopUrl());
                            shopResult.setShopStatus(groupShop.getShop().getShopStatus());
                            shopResult.setGold(groupShop.getShop().getGoldMerchant().isGoldBadge());
                            shopResult.setGoldBadge(groupShop.getShop().getGoldMerchant().isGoldBadge());
                            shopResult.setOfficial(groupShop.getShop().getIsOfficial() == 1);
                            if (groupShop.getShop().getIsOfficial() == 1) {
                                shopResult.setShopBadge(groupShop.getShop().getOfficialStore().getOsLogoUrl());
                            } else if (groupShop.getShop().getGoldMerchant().isGold() == 1) {
                                shopResult.setShopBadge(groupShop.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
                            }
                            shopResult.setFreeReturns(groupShop.getShop().getIsFreeReturns() == 1);
                            shopResult.setAddressId(groupShop.getShop().getAddressId());
                            shopResult.setPostalCode(groupShop.getShop().getPostalCode());
                            shopResult.setLatitude(groupShop.getShop().getLatitude());
                            shopResult.setLongitude(groupShop.getShop().getLongitude());
                            shopResult.setDistrictId(groupShop.getShop().getDistrictId());
                            shopResult.setDistrictName(groupShop.getShop().getDistrictName());
                            shopResult.setOrigin(groupShop.getShop().getOrigin());
                            shopResult.setAddressStreet(groupShop.getShop().getAddressStreet());
                            shopResult.setProvinceId(groupShop.getShop().getProvinceId());
                            shopResult.setCityId(groupShop.getShop().getCityId());
                            shopResult.setCityName(groupShop.getShop().getCityName());

                            if (shipmentAddressFormDataResponse.getAutoapplyStack().getVoucherOrders() != null) {
                                for (VoucherOrdersItem voucherOrdersItem : shipmentAddressFormDataResponse.getAutoapplyStack().getVoucherOrders()) {
                                    if (voucherOrdersItem.getUniqueId().equals(groupShop.getCartString())) {
                                        VoucherOrdersItemData voucherOrdersItemData = new VoucherOrdersItemData();
                                        voucherOrdersItemData.setCode(voucherOrdersItem.getCode());
                                        voucherOrdersItemData.setSuccess(voucherOrdersItem.isSuccess());
                                        voucherOrdersItemData.setUniqueId(voucherOrdersItem.getUniqueId());
                                        voucherOrdersItemData.setCartId(voucherOrdersItem.getCartId());
                                        voucherOrdersItemData.setShopId(voucherOrdersItem.getShopId());
                                        voucherOrdersItemData.setIsPO(voucherOrdersItem.getIsPo());
                                        voucherOrdersItemData.setAddressId(voucherOrdersItem.getAddressId());
                                        voucherOrdersItemData.setType(voucherOrdersItem.getType());
                                        voucherOrdersItemData.setCashbackWalletAmount(voucherOrdersItem.getCashbackWalletAmount());
                                        voucherOrdersItemData.setDiscountAmount(voucherOrdersItem.getDiscountAmount());
                                        voucherOrdersItemData.setInvoiceDescription(voucherOrdersItem.getInvoiceDescription());
                                        voucherOrdersItemData.setMessageData(convertToMessageData(voucherOrdersItem.getMessage()));
                                        voucherOrdersItemData.setTitleDescription(voucherOrdersItem.getTitleDescription());
                                        voucherOrdersItemData.setIsAutoapply(true);
                                        shopResult.setVoucherOrdersItemData(voucherOrdersItemData);
                                    }
                                }
                            }
                            groupShopResult.setShop(shopResult);
                        }

                        if (!UtilsKt.isNullOrEmpty(groupShop.getShopShipments())) {
                            List<ShopShipment> shopShipmentListResult = new ArrayList<>();
                            for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShopShipment shopShipment :
                                    groupShop.getShopShipments()) {
                                ShopShipment shopShipmentResult = new ShopShipment();
                                shopShipmentResult.setDropshipEnabled(shopShipment.getIsDropshipEnabled() == 1);
                                shopShipmentResult.setShipCode(shopShipment.getShipCode());
                                shopShipmentResult.setShipId(shopShipment.getShipId());
                                shopShipmentResult.setShipLogo(shopShipment.getShipLogo());
                                shopShipmentResult.setShipName(shopShipment.getShipName());
                                if (!UtilsKt.isNullOrEmpty(shopShipment.getShipProds())) {
                                    List<ShipProd> shipProdListResult = new ArrayList<>();
                                    for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipProd shipProd :
                                            shopShipment.getShipProds()) {
                                        ShipProd shipProdResult = new ShipProd();
                                        shipProdResult.setAdditionalFee(shipProd.getAdditionalFee());
                                        shipProdResult.setMinimumWeight(shipProd.getMinimumWeight());
                                        shipProdResult.setShipGroupId(shipProd.getShipGroupId());
                                        shipProdResult.setShipGroupName(shipProd.getShipGroupName());
                                        shipProdResult.setShipProdId(shipProd.getShipProdId());
                                        shipProdResult.setShipProdName(shipProd.getShipProdName());
                                        shipProdListResult.add(shipProdResult);
                                    }
                                    shopShipmentResult.setShipProds(shipProdListResult);
                                }
                                shopShipmentListResult.add(shopShipmentResult);
                            }
                            groupShopResult.setShopShipments(shopShipmentListResult);
                        }

                        if (!UtilsKt.isNullOrEmpty(groupShop.getProducts())) {
                            List<Product> productListResult = new ArrayList<>();
                            for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.Product product
                                    : groupShop.getProducts()) {
                                Product productResult = new Product();

                                AnalyticsProductCheckoutData analyticsProductCheckoutData = new AnalyticsProductCheckoutData();
                                analyticsProductCheckoutData.setProductId(String.valueOf(product.getProductId()));

                                if (product.getProductTrackerData() != null) {
                                    analyticsProductCheckoutData.setProductAttribution(product.getProductTrackerData().getAttribution());
                                    analyticsProductCheckoutData.setProductListName(product.getProductTrackerData().getTrackerListName());
                                }

                                analyticsProductCheckoutData.setProductCategory(product.getProductCategory());
                                analyticsProductCheckoutData.setProductCategoryId(String.valueOf(product.getProductCatId()));
                                analyticsProductCheckoutData.setProductName(product.getProductName());
                                analyticsProductCheckoutData.setProductPrice(String.valueOf(product.getProductPrice()));
                                if (product.getTradeInInfo() != null && product.getTradeInInfo().isValidTradeIn()) {
                                    analyticsProductCheckoutData.setProductPrice(String.valueOf(product.getTradeInInfo().getNewDevicePrice()));
                                }
                                analyticsProductCheckoutData.setProductShopId(String.valueOf(groupShop.getShop().getShopId()));
                                analyticsProductCheckoutData.setProductShopName(groupShop.getShop().getShopName());
                                analyticsProductCheckoutData.setProductShopType(generateShopType(groupShop.getShop()));
                                analyticsProductCheckoutData.setProductVariant("");
                                analyticsProductCheckoutData.setProductBrand("");
                                analyticsProductCheckoutData.setProductQuantity(product.getProductQuantity());
                                analyticsProductCheckoutData.setWarehouseId(String.valueOf(groupShop.getWarehouse().getWarehouseId()));
                                analyticsProductCheckoutData.setProductWeight(String.valueOf(product.getProductWeight()));

                                if (groupAddressResult.getUserAddress() != null) {
                                    analyticsProductCheckoutData.setBuyerAddressId(String.valueOf(groupAddressResult.getUserAddress().getAddressId()));
                                }
                                analyticsProductCheckoutData.setShippingDuration("");
                                analyticsProductCheckoutData.setCourier("");
                                analyticsProductCheckoutData.setShippingPrice("");
                                if (dataResult.getCod() != null) {
                                    analyticsProductCheckoutData.setCodFlag(String.valueOf(dataResult.getCod().isCod()));
                                } else {
                                    analyticsProductCheckoutData.setCodFlag(String.valueOf(false));
                                }
                                if (groupAddressResult.getUserAddress() != null && groupAddressResult.getUserAddress().getCornerId() != 0) {
                                    analyticsProductCheckoutData.setTokopediaCornerFlag(String.valueOf(true));
                                } else {
                                    analyticsProductCheckoutData.setTokopediaCornerFlag(String.valueOf(false));
                                }
                                analyticsProductCheckoutData.setIsFulfillment(String.valueOf(groupShop.isFulfillment()));
                                analyticsProductCheckoutData.setDiscountedPrice(product.getProductOriginalPrice() > 0);

                                productResult.setError(!UtilsKt.isNullOrEmpty(product.getErrors()));
                                if (product.getErrors() != null) {
                                    productResult.setErrorMessage(product.getErrors().size() >= 1 ? product.getErrors().get(0) : "");
                                    productResult.setErrorMessageDescription(product.getErrors().size() >= 2 ? product.getErrors().get(1) : "");
                                }

                                productResult.setProductId(product.getProductId());
                                productResult.setCartId(product.getCartId());
                                productResult.setProductName(product.getProductName());
                                productResult.setProductPriceFmt(product.getProductPriceFmt());
                                productResult.setProductPrice(product.getProductPrice());
                                productResult.setProductOriginalPrice(product.getProductOriginalPrice());
                                if (product.getTradeInInfo() != null && product.getTradeInInfo().isValidTradeIn()) {
                                    productResult.setProductPrice(product.getTradeInInfo().getNewDevicePrice());
                                }
                                productResult.setProductWholesalePrice(product.getProductWholesalePrice());
                                productResult.setProductWholesalePriceFmt(product.getProductWholesalePriceFmt());
                                productResult.setProductWeightFmt(product.getProductWeightFmt());
                                productResult.setProductWeight(product.getProductWeight());
                                productResult.setProductCondition(product.getProductCondition());
                                productResult.setProductUrl(product.getProductUrl());
                                productResult.setProductReturnable(product.getProductReturnable() == 1);
                                productResult.setProductIsFreeReturns(product.getProductIsFreeReturns() == 1);
                                productResult.setProductIsPreorder(product.getProductIsPreorder() == 1);
                                productResult.setPreOrderDurationDay(product.getProductPreorder() != null ?
                                        product.getProductPreorder().getDurationDay() : 0);
                                if (product.getProductPreorder() != null
                                        && product.getProductPreorder().getDurationText() != null) {
                                    productResult.setProductPreOrderInfo("PO " + product.getProductPreorder().getDurationText());
                                }
                                productResult.setProductCashback(product.getProductCashback());
                                productResult.setProductMinOrder(product.getProductMinOrder());
                                productResult.setProductInvenageValue(product.getProductInvenageValue());
                                productResult.setProductSwitchInvenage(product.getProductSwitchInvenage());
                                productResult.setProductPriceCurrency(product.getProductPriceCurrency());
                                productResult.setProductImageSrc200Square(product.getProductImageSrc200Square());
                                productResult.setProductNotes(product.getProductNotes());
                                productResult.setProductQuantity(product.getProductQuantity());
                                productResult.setProductMenuId(product.getProductMenuId());
                                productResult.setProductFinsurance(product.getProductFinsurance() == 1);
                                productResult.setProductFcancelPartial(product.getProductFcancelPartial() == 1);
                                productResult.setProductCatId(product.getProductCatId());
                                productResult.setProductCatalogId(product.getProductCatalogId());
                                productResult.setAnalyticsProductCheckoutData(analyticsProductCheckoutData);

                                if (product.getFreeShipping() != null && product.getFreeShipping().getEligible() &&
                                        !TextUtils.isEmpty(product.getFreeShipping().getBadgeUrl())) {
                                    productResult.setFreeShipping(true);
                                    productResult.setFreeShippingBadgeUrl(product.getFreeShipping().getBadgeUrl());
                                }

                                if (product.getTradeInInfo() != null && product.getTradeInInfo().isValidTradeIn()) {
                                    TradeInInfoData tradeInInfoData = new TradeInInfoData();
                                    tradeInInfoData.setValidTradeIn(product.getTradeInInfo().isValidTradeIn());
                                    tradeInInfoData.setNewDevicePrice(product.getTradeInInfo().getNewDevicePrice());
                                    tradeInInfoData.setNewDevicePriceFmt(product.getTradeInInfo().getNewDevicePriceFmt());
                                    tradeInInfoData.setOldDevicePrice(product.getTradeInInfo().getOldDevicePrice());
                                    tradeInInfoData.setOldDevicePriceFmt(product.getTradeInInfo().getOldDevicePriceFmt());
                                    tradeInInfoData.setDropOffEnable(product.getTradeInInfo().isDropOffEnable());

                                    productResult.setTradeInInfoData(tradeInInfoData);
                                }

                                if (!isDisablePPP) {
                                    if (product.getPurchaseProtectionPlanData() != null) {
                                        PurchaseProtectionPlanData purchaseProtectionPlanData = new PurchaseProtectionPlanData();
                                        com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.PurchaseProtectionPlanData pppDataMapping =
                                                product.getPurchaseProtectionPlanData();

                                        purchaseProtectionPlanData.setProtectionAvailable(pppDataMapping.getProtectionAvailable());
                                        purchaseProtectionPlanData.setProtectionLinkText(pppDataMapping.getProtectionLinkText());
                                        purchaseProtectionPlanData.setProtectionLinkUrl(pppDataMapping.getProtectionLinkUrl());
                                        purchaseProtectionPlanData.setProtectionOptIn(pppDataMapping.getProtectionOptIn());
                                        purchaseProtectionPlanData.setProtectionPrice(pppDataMapping.getProtectionPrice());
                                        purchaseProtectionPlanData.setProtectionPricePerProduct(pppDataMapping.getProtectionPricePerProduct());
                                        purchaseProtectionPlanData.setProtectionSubtitle(pppDataMapping.getProtectionSubtitle());
                                        purchaseProtectionPlanData.setProtectionTitle(pppDataMapping.getProtectionTitle());
                                        purchaseProtectionPlanData.setProtectionTypeId(pppDataMapping.getProtectionTypeId());
                                        purchaseProtectionPlanData.setProtectionCheckboxDisabled(pppDataMapping.getProtectionCheckboxDisabled());

                                        productResult.setPurchaseProtectionPlanData(purchaseProtectionPlanData);
                                    }
                                }

                                if (product.getFreeReturns() != null) {
                                    productResult.setFreeReturnLogo(product.getFreeReturns().getFreeReturnsLogo());
                                }

                                if (!UtilsKt.isNullOrEmpty(product.getProductShipment())) {
                                    List<ProductShipment> productShipmentListResult = new ArrayList<>();
                                    for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ProductShipment
                                            productShipment : product.getProductShipment()) {
                                        ProductShipment productShipmentResult = new ProductShipment();
                                        productShipmentResult.setServiceId(productShipment.getServiceId());
                                        productShipmentResult.setShipmentId(productShipment.getShipmentId());
                                        productShipmentListResult.add(productShipmentResult);
                                    }
                                    productResult.setProductShipment(productShipmentListResult);
                                }

                                AutoApplyStack autoapplyStack = shipmentAddressFormDataResponse.getAutoapplyStack();
                                if (autoapplyStack != null) {
                                    if (autoapplyStack.getTrackingDetails() != null && autoapplyStack.getTrackingDetails().size() > 0) {
                                        for (TrackingDetail trackingDetail : autoapplyStack.getTrackingDetails()) {
                                            if (trackingDetail.getProductId() == productResult.getProductId()) {
                                                analyticsProductCheckoutData.setPromoCode(trackingDetail.getPromoCodesTracking());
                                                analyticsProductCheckoutData.setPromoDetails(trackingDetail.getPromoDetailsTracking());
                                            }
                                        }
                                    }
                                }

                                if (!UtilsKt.isNullOrEmpty(product.getProductShipmentMapping())) {

                                    List<ProductShipmentMapping> productShipmentMappingListResult = new ArrayList<>();
                                    for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ProductShipmentMapping
                                            productShipmentMapping : product.getProductShipmentMapping()) {
                                        ProductShipmentMapping productShipmentMappingResult = new ProductShipmentMapping();
                                        productShipmentMappingResult.setShipmentId(productShipmentMapping.getShipmentId());
                                        productShipmentMappingResult.setShippingIds(productShipmentMapping.getShippingIds());

                                        if (!UtilsKt.isNullOrEmpty(productShipmentMapping.getServiceIds())) {
                                            List<ServiceId> serviceIdListResult = new ArrayList<>();
                                            for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ServiceId serviceId :
                                                    productShipmentMapping.getServiceIds()) {
                                                ServiceId serviceIdResult = new ServiceId();
                                                serviceIdResult.setServiceId(serviceId.getServiceId());
                                                serviceIdResult.setSpIds(serviceId.getSpIds());
                                                serviceIdListResult.add(serviceIdResult);
                                            }
                                            productShipmentMappingResult.setServiceIds(serviceIdListResult);
                                        }
                                        productShipmentMappingListResult.add(productShipmentMappingResult);
                                    }
                                    productResult.setProductShipmentMapping(productShipmentMappingListResult);
                                }
                                productListResult.add(productResult);
                            }
                            groupShopResult.setProducts(productListResult);
                        }
                        groupShopListResult.add(groupShopResult);
                    }
                    groupAddressResult.setGroupShop(groupShopListResult);
                }
                groupAddressListResult.add(groupAddressResult);
            }
            dataResult.setGroupAddress(groupAddressListResult);
            dataResult.setHasError(checkCartHasError(dataResult));
        }

        return dataResult;
    }

    @NotNull
    private UserAddress getUserAddress(com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.UserAddress defaultAddress) {
        UserAddress defaultAddressData = new UserAddress();
        defaultAddressData.setAddressId(defaultAddress.getAddressId());
        defaultAddressData.setAddressName(defaultAddress.getAddressName());
        defaultAddressData.setAddress(defaultAddress.getAddress());
        defaultAddressData.setAddress2(defaultAddress.getAddress2());
        defaultAddressData.setCityId(defaultAddress.getCityId());
        defaultAddressData.setCityName(defaultAddress.getCityName());
        defaultAddressData.setCorner(defaultAddress.isCorner());
        defaultAddressData.setCornerId(defaultAddress.getCornerId());
        defaultAddressData.setCountry(defaultAddress.getCountry());
        defaultAddressData.setDistrictId(defaultAddress.getDistrictId());
        defaultAddressData.setDistrictName(defaultAddress.getDistrictName());
        defaultAddressData.setLatitude(defaultAddress.getLatitude());
        defaultAddressData.setLongitude(defaultAddress.getLongitude());
        defaultAddressData.setPhone(defaultAddress.getPhone());
        defaultAddressData.setPostalCode(defaultAddress.getPostalCode());
        defaultAddressData.setProvinceId(defaultAddress.getProvinceId());
        defaultAddressData.setProvinceName(defaultAddress.getProvinceName());
        defaultAddressData.setReceiverName(defaultAddress.getReceiverName());
        defaultAddressData.setStatus(defaultAddress.getStatus());
        return defaultAddressData;
    }

    private boolean checkCartHasError(CartShipmentAddressFormData cartShipmentAddressFormData) {
        boolean hasError = false;
        for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
            if (groupAddress.isError() || groupAddress.isWarning()) {
                hasError = true;
                break;
            }
            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                if (groupShop.isError() || groupShop.isWarning()) {
                    hasError = true;
                    break;
                }
                int totalProductError = 0;
                String defaultErrorMessage = "";
                for (Product product : groupShop.getProducts()) {
                    if (product.isError() || !TextUtils.isEmpty(product.getErrorMessage())) {
                        hasError = true;
                        totalProductError++;
                        if (TextUtils.isEmpty(defaultErrorMessage)) {
                            defaultErrorMessage = product.getErrorMessage();
                        }
                    }
                }
                if (totalProductError == groupShop.getProducts().size()) {
                    groupShop.setError(true);
                    groupShop.setErrorMessage(defaultErrorMessage);
                    for (Product product : groupShop.getProducts()) {
                        product.setError(false);
                        product.setErrorMessage("");
                    }
                }
            }
        }

        return hasError;
    }

    private String generateShopType(com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.Shop shop) {
        if (shop.getIsOfficial() == 1)
            return SHOP_TYPE_OFFICIAL_STORE;
        else if (shop.getGoldMerchant().isGoldBadge())
            return SHOP_TYPE_GOLD_MERCHANT;
        else return SHOP_TYPE_REGULER;
    }

    private MessageData convertToMessageData(Message message) {
        MessageData messageData = new MessageData();
        messageData.setColor(message.getColor());
        messageData.setState(message.getState());
        messageData.setText(message.getText());
        return messageData;
    }

    private ArrayList<String> mapCreateListRedPromosCheckout(Data data) {
        ArrayList<String> listRedPromos = new ArrayList<>();
        if (data.getMessage() != null && data.getMessage().getState() != null && data.getCodes() != null && data.getCodes().size() > 0) {
            if (data.getMessage().getState().equalsIgnoreCase(STATE_RED)) {
                listRedPromos.addAll(data.getCodes());
            }
        }

        if (data.getVoucherOrders() != null && data.getVoucherOrders().size() > 0) {
            for (int j=0; j<data.getVoucherOrders().size(); j++) {
                if (data.getVoucherOrders().get(j) != null) {
                    if (data.getVoucherOrders().get(j).getMessage() != null) {
                        if (Objects.requireNonNull(data.getVoucherOrders().get(j).getMessage()).getState() != null) {
                            if (Objects.requireNonNull(Objects.requireNonNull(data.getVoucherOrders().get(j).getMessage()).getState()).equalsIgnoreCase(STATE_RED)) {
                                listRedPromos.add(data.getVoucherOrders().get(j).getCode());
                            }
                        }
                    }
                }
            }
        }

        return listRedPromos;
    }
}
