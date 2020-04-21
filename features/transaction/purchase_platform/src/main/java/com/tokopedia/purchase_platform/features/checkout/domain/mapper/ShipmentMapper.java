package com.tokopedia.purchase_platform.features.checkout.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.ShipProd;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyEmptyCartInfoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyErrorDetailUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageInfoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.egold.EgoldTieringData;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.Addresses;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.CampaignTimer;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.CheckoutDisabledFeaturesKt;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.AdditionalInfo;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.CartEmptyInfo;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.Data;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.ErrorDetail;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.LastApply;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.MessageInfo;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.PromoSAFResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.TrackingDetailsItem;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.UsageSummaries;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.AddressesData;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CampaignTimerUi;
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
        if (shipmentAddressFormDataResponse != null) {
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
            if (shipmentAddressFormDataResponse.getDisabledFeatures() != null)
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

            if (shipmentAddressFormDataResponse.getCampaignTimer() != null) {
                CampaignTimer timerResponse = shipmentAddressFormDataResponse.getCampaignTimer();
                CampaignTimerUi timerUi = new CampaignTimerUi(
                        timerResponse.getExpiredTimerMessage().getButton(),
                        timerResponse.getExpiredTimerMessage().getDescription(),
                        timerResponse.getExpiredTimerMessage().getTitle(),
                        timerResponse.getShowTimer(),
                        timerResponse.getTimerDetail().getDeductTime(),
                        timerResponse.getDescription(),
                        timerResponse.getTimerDetail().getExpiredTime(),
                        timerResponse.getTimerDetail().getExpiredDuration(),
                        timerResponse.getTimerDetail().getServerTime(),
                        0,
                        ""
                );
                dataResult.setCampaignTimerUi(timerUi);
            }


            if (shipmentAddressFormDataResponse.getPromoSAFResponse() != null &&
                    shipmentAddressFormDataResponse.getPromoSAFResponse().getLastApply() != null &&
                    shipmentAddressFormDataResponse.getPromoSAFResponse().getLastApply().getData() != null) {
                LastApply lastApply = shipmentAddressFormDataResponse.getPromoSAFResponse().getLastApply();
                if (lastApply.getData() != null) {
                    LastApplyUiModel lastApplyUiModel = new LastApplyUiModel();

                    // set codes
                    if (lastApply.getData().getCodes() != null) {
                        lastApplyUiModel.setCodes(lastApply.getData().getCodes());
                    }

                    // set voucher orders
                    ArrayList<LastApplyVoucherOrdersItemUiModel> listVoucherOrdersUiModel = new ArrayList<>();
                    if (lastApply.getData().getVoucherOrders() != null) {
                        for (int i = 0; i < lastApply.getData().getVoucherOrders().size(); i++) {
                            LastApplyVoucherOrdersItemUiModel lastApplyVoucherOrdersItemUiModel = new LastApplyVoucherOrdersItemUiModel();
                            com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.VoucherOrdersItem voucherOrdersItem = lastApply.getData().getVoucherOrders().get(i);
                            lastApplyVoucherOrdersItemUiModel.setCode(voucherOrdersItem.getCode());
                            lastApplyVoucherOrdersItemUiModel.setUniqueId(voucherOrdersItem.getUniqueId());

                            com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.Message message = voucherOrdersItem.getMessage();
                            LastApplyMessageUiModel lastApplyMessageInfoUiModel = new LastApplyMessageUiModel();
                            lastApplyMessageInfoUiModel.setColor(message.getColor());
                            lastApplyMessageInfoUiModel.setState(message.getState());
                            lastApplyMessageInfoUiModel.setText(message.getText());
                            lastApplyVoucherOrdersItemUiModel.setMessage(lastApplyMessageInfoUiModel);

                            listVoucherOrdersUiModel.add(lastApplyVoucherOrdersItemUiModel);
                        }
                        lastApplyUiModel.setVoucherOrders(listVoucherOrdersUiModel);
                    }

                    // set additional info
                    AdditionalInfo responseAdditionalInfo = lastApply.getData().getAdditionalInfo();
                    CartEmptyInfo responseCartEmptyInfo = responseAdditionalInfo.getCartEmptyInfo();
                    ErrorDetail errorDetail = responseAdditionalInfo.getErrorDetail();
                    MessageInfo messageInfo = responseAdditionalInfo.getMessageInfo();

                    LastApplyAdditionalInfoUiModel lastApplyAdditionalInfoUiModel = new LastApplyAdditionalInfoUiModel();

                    LastApplyEmptyCartInfoUiModel lastApplyEmptyCartInfoUiModel = new LastApplyEmptyCartInfoUiModel();
                    lastApplyEmptyCartInfoUiModel.setDetail(responseCartEmptyInfo.getDetail());
                    lastApplyEmptyCartInfoUiModel.setImgUrl(responseCartEmptyInfo.getImageUrl());
                    lastApplyEmptyCartInfoUiModel.setMessage(responseCartEmptyInfo.getMessage());
                    lastApplyAdditionalInfoUiModel.setEmptyCartInfo(lastApplyEmptyCartInfoUiModel);

                    LastApplyErrorDetailUiModel lastApplyErrorDetailUiModel = new LastApplyErrorDetailUiModel();
                    lastApplyErrorDetailUiModel.setMessage(errorDetail.getMessage());
                    lastApplyAdditionalInfoUiModel.setErrorDetail(lastApplyErrorDetailUiModel);

                    LastApplyMessageInfoUiModel lastApplyMessageInfoUiModel = new LastApplyMessageInfoUiModel();
                    lastApplyMessageInfoUiModel.setDetail(messageInfo.getDetail());
                    lastApplyMessageInfoUiModel.setMessage(messageInfo.getMessage());
                    lastApplyAdditionalInfoUiModel.setMessageInfo(lastApplyMessageInfoUiModel);

                    lastApplyAdditionalInfoUiModel.setMessageInfo(lastApplyMessageInfoUiModel);
                    lastApplyAdditionalInfoUiModel.setErrorDetail(lastApplyErrorDetailUiModel);
                    lastApplyAdditionalInfoUiModel.setEmptyCartInfo(lastApplyEmptyCartInfoUiModel);

                    // set usage summaries
                    ArrayList<LastApplyUsageSummariesUiModel> listUsageSummaries = new ArrayList<>();
                    if (responseAdditionalInfo.getListUsageSummaries() != null) {
                        for (UsageSummaries usageSummaries : responseAdditionalInfo.getListUsageSummaries()) {
                            LastApplyUsageSummariesUiModel lastApplyUsageSummariesUiModel = new LastApplyUsageSummariesUiModel();
                            lastApplyUsageSummariesUiModel.setDescription(usageSummaries.getDesc());
                            lastApplyUsageSummariesUiModel.setType(usageSummaries.getType());
                            lastApplyUsageSummariesUiModel.setAmountStr(usageSummaries.getAmountStr());
                            lastApplyUsageSummariesUiModel.setAmount(usageSummaries.getAmount());
                            listUsageSummaries.add(lastApplyUsageSummariesUiModel);
                        }
                        lastApplyAdditionalInfoUiModel.setUsageSummaries(listUsageSummaries);
                    }

                    lastApplyUiModel.setAdditionalInfo(lastApplyAdditionalInfoUiModel);

                    // set message
                    if (lastApply.getData().getMessage() != null) {
                        com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.Message lastApplyMessage = lastApply.getData().getMessage();
                        LastApplyMessageUiModel lastApplyMessageUiModel = new LastApplyMessageUiModel();
                        lastApplyMessageUiModel.setText(lastApplyMessage.getText());
                        lastApplyMessageUiModel.setState(lastApplyMessage.getState());
                        lastApplyMessageUiModel.setColor(lastApplyMessage.getColor());
                        lastApplyUiModel.setMessage(lastApplyMessageUiModel);

                        ArrayList<String> listRedStates = new ArrayList<>();
                        if (lastApply.getData().getMessage().getState() != null) {
                            if (lastApply.getData().getMessage().getState().equalsIgnoreCase(STATE_RED)) {
                                for (String code : lastApply.getData().getCodes()) {
                                    listRedStates.add(code);
                                }
                            }

                            if (lastApply.getData().getVoucherOrders() != null) {
                                for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.VoucherOrdersItem voucherOrdersItem : lastApply.getData().getVoucherOrders()) {
                                    if (voucherOrdersItem.getMessage().getState().equalsIgnoreCase(STATE_RED)) {
                                        listRedStates.add(voucherOrdersItem.getCode());
                                    }
                                }
                            }
                        }
                        lastApplyUiModel.setListRedPromos(listRedStates);
                    }

                    ArrayList<String> listAllPromoCodes = new ArrayList<>();
                    if (lastApply.getData().getCodes() != null) {
                        listAllPromoCodes.addAll(lastApply.getData().getCodes());
                    }

                    if (lastApply.getData().getVoucherOrders() != null) {
                        for (com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.VoucherOrdersItem voucherOrdersItem : lastApply.getData().getVoucherOrders()) {
                            listAllPromoCodes.add(voucherOrdersItem.getCode());
                        }
                    }

                    lastApplyUiModel.setListAllPromoCodes(listAllPromoCodes);

                    dataResult.setLastApplyData(lastApplyUiModel);
                }
            }

            if (shipmentAddressFormDataResponse.getPromoSAFResponse() != null &&
                    shipmentAddressFormDataResponse.getPromoSAFResponse().getErrorDefault() != null) {
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

                                    if (product.getProductTicker() != null) {
                                        productResult.setShowTicker(product.getProductTicker().isShowTicker());
                                        productResult.setTickerMessage(product.getProductTicker().getMessage());
                                    }

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

                                    PromoSAFResponse promoSAFResponse = shipmentAddressFormDataResponse.getPromoSAFResponse();
                                    if (promoSAFResponse != null && promoSAFResponse.getLastApply() != null && promoSAFResponse.getLastApply().getData() != null &&
                                            promoSAFResponse.getLastApply().getData().getTrackingDetails() != null) {
                                        List<TrackingDetailsItem> trackingDetailsItems = promoSAFResponse.getLastApply().getData().getTrackingDetails();
                                        if (trackingDetailsItems.size() > 0) {
                                            for (TrackingDetailsItem trackingDetail : trackingDetailsItems) {
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

    private ArrayList<String> mapCreateListRedPromosCheckout(Data data) {
        ArrayList<String> listRedPromos = new ArrayList<>();
        if (data.getMessage() != null && data.getMessage().getState() != null && data.getCodes() != null && data.getCodes().size() > 0) {
            if (data.getMessage().getState().equalsIgnoreCase(STATE_RED)) {
                listRedPromos.addAll(data.getCodes());
            }
        }

        if (data.getVoucherOrders() != null && data.getVoucherOrders().size() > 0) {
            for (int j = 0; j < data.getVoucherOrders().size(); j++) {
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
