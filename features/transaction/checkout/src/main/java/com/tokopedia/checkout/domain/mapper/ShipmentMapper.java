package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.data.model.response.egold.EgoldTieringData;
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Addresses;
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CampaignTimer;
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CheckoutDisabledFeaturesKt;
import com.tokopedia.checkout.data.model.response.shipmentaddressform.FreeShipping;
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentInformation;
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressData;
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData;
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi;
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation;
import com.tokopedia.checkout.domain.model.cartshipmentform.FreeShippingData;
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.model.cartshipmentform.PreorderData;
import com.tokopedia.checkout.domain.model.cartshipmentform.Product;
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentInformationData;
import com.tokopedia.checkout.domain.model.cartshipmentform.Shop;
import com.tokopedia.checkout.domain.model.cartshipmentform.TradeInInfoData;
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel;
import com.tokopedia.logisticCommon.data.entity.address.UserAddress;
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData;
import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.logisticcart.shipping.model.ShipProd;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.AdditionalInfo;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.CartEmptyInfo;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.ErrorDefault;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.ErrorDetail;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.LastApply;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.MessageInfo;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.MvcShippingBenefit;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSpId;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.TrackingDetailsItem;
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.UsageSummaries;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyEmptyCartInfoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyErrorDetailUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageInfoUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel;
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse;
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker;
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData;
import com.tokopedia.purchase_platform.common.utils.Utils;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
            dataResult.setHidingCourier(shipmentAddressFormDataResponse.getHideCourier());
            dataResult.setBlackbox(shipmentAddressFormDataResponse.isBlackbox() == 1);
            dataResult.setErrorCode(shipmentAddressFormDataResponse.getErrorCode());
            dataResult.setError(!UtilsKt.isNullOrEmpty(shipmentAddressFormDataResponse.getErrors()));
            dataResult.setErrorMessage(UtilsKt.convertToString(shipmentAddressFormDataResponse.getErrors()));
            dataResult.setShowOnboarding(shipmentAddressFormDataResponse.isShowOnboarding());
            dataResult.setIneligiblePromoDialogEnabled(shipmentAddressFormDataResponse.isIneligiblePromoDialogEnabled());
            dataResult.setOpenPrerequisiteSite(shipmentAddressFormDataResponse.isOpenPrerequisiteSite());
            dataResult.setEligibleNewShippingExperience(shipmentAddressFormDataResponse.isEligibleNewShippingExperience());

            boolean isDisableEgold = false;
            boolean isDisablePPP = false;
            boolean isDisableDonation = false;
            for (String disabledFeature : shipmentAddressFormDataResponse.getDisabledFeatures()) {
                switch (disabledFeature) {
                    case CheckoutDisabledFeaturesKt.dropshipper:
                        dataResult.setDropshipperDisable(true);
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

            AddressesData addressesData = getAddressesData(shipmentAddressFormDataResponse);
            dataResult.setAddressesData(addressesData);

            if (!shipmentAddressFormDataResponse.getTickers().isEmpty()) {
                Ticker ticker = shipmentAddressFormDataResponse.getTickers().get(0);
                dataResult.setTickerData(new TickerData(ticker.getId(), ticker.getMessage(), ticker.getPage(), ""));
            }

            if (!isDisableEgold) {
                EgoldAttributeModel egoldAttributeModel = new EgoldAttributeModel();
                egoldAttributeModel.setEligible(shipmentAddressFormDataResponse.getEgoldAttributes().isEligible());
                egoldAttributeModel.setTiering(shipmentAddressFormDataResponse.getEgoldAttributes().isTiering());
                egoldAttributeModel.setChecked(shipmentAddressFormDataResponse.getEgoldAttributes().isOptIn());
                egoldAttributeModel.setMinEgoldRange(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldRange().getMinEgoldValue());
                egoldAttributeModel.setMaxEgoldRange(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldRange().getMaxEgoldValue());
                egoldAttributeModel.setTitleText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getTitleText());
                egoldAttributeModel.setSubText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getSubText());
                egoldAttributeModel.setTickerText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getTickerText());
                egoldAttributeModel.setTooltipText(shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldMessage().getTooltipText());

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

                dataResult.setEgoldAttributes(egoldAttributeModel);

            }

            if (!isDisableDonation) {
                Donation donation = new Donation();
                donation.setTitle(shipmentAddressFormDataResponse.getDonation().getTitle());
                donation.setDescription(shipmentAddressFormDataResponse.getDonation().getDescription());
                donation.setNominal(shipmentAddressFormDataResponse.getDonation().getNominal());
                donation.setChecked(shipmentAddressFormDataResponse.isDonationCheckboxStatus());
                dataResult.setDonation(donation);
            }

            CodModel cod = new CodModel();
            cod.setCod(shipmentAddressFormDataResponse.getCod().isCod());
            cod.setCounterCod(shipmentAddressFormDataResponse.getCod().getCounterCod());
            dataResult.setCod(cod);

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

            if (shipmentAddressFormDataResponse.getPromoSAFResponse().getLastApply() != null &&
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
                            com.tokopedia.purchase_platform.common.feature.promo.domain.model.VoucherOrdersItem voucherOrdersItem = lastApply.getData().getVoucherOrders().get(i);
                            lastApplyVoucherOrdersItemUiModel.setCode(voucherOrdersItem.getCode() != null ? voucherOrdersItem.getCode() : "");
                            lastApplyVoucherOrdersItemUiModel.setUniqueId(voucherOrdersItem.getUniqueId() != null ? voucherOrdersItem.getUniqueId() : "");

                            com.tokopedia.purchase_platform.common.feature.promo.domain.model.Message message = voucherOrdersItem.getMessage();
                            if (message != null) {
                                LastApplyMessageUiModel lastApplyMessageInfoUiModel = new LastApplyMessageUiModel();
                                lastApplyMessageInfoUiModel.setColor(message.getColor() != null ? message.getColor() : "");
                                lastApplyMessageInfoUiModel.setState(message.getState() != null ? message.getState() : "");
                                lastApplyMessageInfoUiModel.setText(message.getText() != null ? message.getText() : "");
                                lastApplyVoucherOrdersItemUiModel.setMessage(lastApplyMessageInfoUiModel);
                            }
                            listVoucherOrdersUiModel.add(lastApplyVoucherOrdersItemUiModel);
                        }
                        lastApplyUiModel.setVoucherOrders(listVoucherOrdersUiModel);
                    }

                    // set additional info
                    AdditionalInfo responseAdditionalInfo = lastApply.getData().getAdditionalInfo();
                    if (responseAdditionalInfo != null) {
                        LastApplyAdditionalInfoUiModel lastApplyAdditionalInfoUiModel = new LastApplyAdditionalInfoUiModel();
                        CartEmptyInfo responseCartEmptyInfo = responseAdditionalInfo.getCartEmptyInfo();
                        if (responseCartEmptyInfo != null) {
                            LastApplyEmptyCartInfoUiModel lastApplyEmptyCartInfoUiModel = new LastApplyEmptyCartInfoUiModel();
                            lastApplyEmptyCartInfoUiModel.setDetail(responseCartEmptyInfo.getDetail() != null ? responseCartEmptyInfo.getDetail() : "");
                            lastApplyEmptyCartInfoUiModel.setImgUrl(responseCartEmptyInfo.getImageUrl() != null ? responseCartEmptyInfo.getImageUrl() : "");
                            lastApplyEmptyCartInfoUiModel.setMessage(responseCartEmptyInfo.getMessage() != null ? responseCartEmptyInfo.getMessage() : "");
                            lastApplyAdditionalInfoUiModel.setEmptyCartInfo(lastApplyEmptyCartInfoUiModel);
                        }

                        ErrorDetail errorDetail = responseAdditionalInfo.getErrorDetail();
                        if (errorDetail != null) {
                            LastApplyErrorDetailUiModel lastApplyErrorDetailUiModel = new LastApplyErrorDetailUiModel();
                            lastApplyErrorDetailUiModel.setMessage(errorDetail.getMessage() != null ? errorDetail.getMessage() : "");
                            lastApplyAdditionalInfoUiModel.setErrorDetail(lastApplyErrorDetailUiModel);
                        }

                        MessageInfo messageInfo = responseAdditionalInfo.getMessageInfo();
                        if (messageInfo != null) {
                            LastApplyMessageInfoUiModel lastApplyMessageInfoUiModel = new LastApplyMessageInfoUiModel();
                            lastApplyMessageInfoUiModel.setDetail(messageInfo.getDetail() != null ? messageInfo.getDetail() : "");
                            lastApplyMessageInfoUiModel.setMessage(messageInfo.getMessage() != null ? messageInfo.getMessage() : "");
                            lastApplyAdditionalInfoUiModel.setMessageInfo(lastApplyMessageInfoUiModel);
                        }

                        lastApplyAdditionalInfoUiModel.setPromoSpIds(mapPromoSpId(responseAdditionalInfo));

                        // set usage summaries
                        ArrayList<LastApplyUsageSummariesUiModel> listUsageSummaries = new ArrayList<>();
                        if (responseAdditionalInfo.getListUsageSummaries() != null) {
                            for (UsageSummaries usageSummaries : responseAdditionalInfo.getListUsageSummaries()) {
                                LastApplyUsageSummariesUiModel lastApplyUsageSummariesUiModel = new LastApplyUsageSummariesUiModel();
                                lastApplyUsageSummariesUiModel.setDescription(usageSummaries.getDesc() != null ? usageSummaries.getDesc() : "");
                                lastApplyUsageSummariesUiModel.setType(usageSummaries.getType() != null ? usageSummaries.getType() : "");
                                lastApplyUsageSummariesUiModel.setAmountStr(usageSummaries.getAmountStr() != null ? usageSummaries.getAmountStr() : "");
                                lastApplyUsageSummariesUiModel.setAmount(usageSummaries.getAmount() != null ? usageSummaries.getAmount() : 0);
                                lastApplyUsageSummariesUiModel.setCurrencyDetailsStr(usageSummaries.getCurrencyDetailsStr());
                                listUsageSummaries.add(lastApplyUsageSummariesUiModel);
                            }
                            lastApplyAdditionalInfoUiModel.setUsageSummaries(listUsageSummaries);
                        }

                        lastApplyUiModel.setAdditionalInfo(lastApplyAdditionalInfoUiModel);
                    }

                    // set message
                    if (lastApply.getData().getMessage() != null) {
                        com.tokopedia.purchase_platform.common.feature.promo.domain.model.Message lastApplyMessage = lastApply.getData().getMessage();
                        LastApplyMessageUiModel lastApplyMessageUiModel = new LastApplyMessageUiModel();
                        lastApplyMessageUiModel.setText(lastApplyMessage.getText() != null ? lastApplyMessage.getText() : "");
                        lastApplyMessageUiModel.setState(lastApplyMessage.getState() != null ? lastApplyMessage.getState() : "");
                        lastApplyMessageUiModel.setColor(lastApplyMessage.getColor() != null ? lastApplyMessage.getColor() : "");
                        lastApplyUiModel.setMessage(lastApplyMessageUiModel);

                        ArrayList<String> listRedStates = new ArrayList<>();
                        if (lastApply.getData().getMessage().getState() != null) {
                            if (lastApply.getData().getMessage().getState().equalsIgnoreCase(STATE_RED)) {
                                listRedStates.addAll(lastApply.getData().getCodes());
                            }

                            if (lastApply.getData().getVoucherOrders() != null) {
                                for (com.tokopedia.purchase_platform.common.feature.promo.domain.model.VoucherOrdersItem voucherOrdersItem : lastApply.getData().getVoucherOrders()) {
                                    if (voucherOrdersItem.getMessage() != null && voucherOrdersItem.getMessage().getState() != null &&
                                            voucherOrdersItem.getMessage().getState().equalsIgnoreCase(STATE_RED)) {
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
                        for (com.tokopedia.purchase_platform.common.feature.promo.domain.model.VoucherOrdersItem voucherOrdersItem : lastApply.getData().getVoucherOrders()) {
                            listAllPromoCodes.add(voucherOrdersItem.getCode());
                        }
                    }

                    lastApplyUiModel.setListAllPromoCodes(listAllPromoCodes);

                    dataResult.setLastApplyData(lastApplyUiModel);
                }
            }

            ErrorDefault errorDefault = shipmentAddressFormDataResponse.getPromoSAFResponse().getErrorDefault();
            if (errorDefault != null) {
                PromoCheckoutErrorDefault promoCheckoutErrorDefault = new PromoCheckoutErrorDefault();
                promoCheckoutErrorDefault.setTitle(errorDefault.getTitle() != null ? errorDefault.getTitle() : "");
                promoCheckoutErrorDefault.setDesc(errorDefault.getDescription() != null ? errorDefault.getDescription() : "");
                dataResult.setPromoCheckoutErrorDefault(promoCheckoutErrorDefault);
            }

            if (!UtilsKt.isNullOrEmpty(shipmentAddressFormDataResponse.getGroupAddress())) {
                List<GroupAddress> groupAddressListResult = new ArrayList<>();
                for (com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupAddress
                        groupAddress : shipmentAddressFormDataResponse.getGroupAddress()) {
                    GroupAddress groupAddressResult = new GroupAddress();
                    groupAddressResult.setError(!UtilsKt.isNullOrEmpty(groupAddress.getErrors()));
                    groupAddressResult.setErrorMessage(UtilsKt.convertToString(groupAddress.getErrors()));

                    UserAddress userAddressResult = new UserAddress();
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
                    userAddressResult.setState(groupAddress.getUserAddress().getState());
                    userAddressResult.setStateDetail(groupAddress.getUserAddress().getStateDetail());

                    groupAddressResult.setUserAddress(userAddressResult);

                    if (!UtilsKt.isNullOrEmpty(groupAddress.getGroupShop())) {
                        List<GroupShop> groupShopListResult = new ArrayList<>();
                        for (com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupShop
                                groupShop : groupAddress.getGroupShop()) {
                            com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop groupShopResult =
                                    new com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop();

                            groupShopResult.setError(!UtilsKt.isNullOrEmpty(groupShop.getErrors()));
                            groupShopResult.setErrorMessage(UtilsKt.convertToString(groupShop.getErrors()));

                            groupShopResult.setShippingId(groupShop.getShippingId());
                            groupShopResult.setSpId(groupShop.getSpId());
                            groupShopResult.setDropshipperName(groupShop.getDropshiper().getName());
                            groupShopResult.setDropshipperPhone(groupShop.getDropshiper().getTelpNo());
                            groupShopResult.setUseInsurance(groupShop.isInsurance());
                            groupShopResult.setCartString(groupShop.getCartString());
                            groupShopResult.setHasPromoList(groupShop.isHasPromoList());
                            groupShopResult.setSaveStateFlag(groupShop.isSaveStateFlag());

                            groupShopResult.setLeasingProduct(groupShop.getVehicleLeasing().isLeasingProduct());
                            groupShopResult.setBookingFee(groupShop.getVehicleLeasing().getBookingFee());

                            if (groupShop.getListPromoCodes().size() > 0) {
                                groupShopResult.setListPromoCodes(groupShop.getListPromoCodes());
                            }

                            groupShopResult.setFulfillment(groupShop.isFulfillment());
                            groupShopResult.setFulfillmentId(groupShop.getWarehouse().getWarehouseId());

                            TokoCabangInfo tokoCabangInfo = groupShop.getTokoCabangInfo();
                            groupShopResult.setFulfillmentBadgeUrl(tokoCabangInfo.getBadgeUrl());
                            groupShopResult.setFulfillmentName(tokoCabangInfo.getMessage());

                            ShipmentInformation shipmentInformation = groupShop.getShipmentInformation();
                            FreeShippingData freeShippingData = new FreeShippingData();
                            freeShippingData.setBadgeUrl(shipmentInformation.getFreeShipping().getBadgeUrl());
                            freeShippingData.setEligible(shipmentInformation.getFreeShipping().getEligible());

                            FreeShipping freeShippingExtra = shipmentInformation.getFreeShippingExtra();
                            FreeShippingData freeShippingExtraData = new FreeShippingData(freeShippingExtra.getEligible(), freeShippingExtra.getBadgeUrl());

                            PreorderData preorderData = new PreorderData();
                            preorderData.setDuration(shipmentInformation.getPreorder().getDuration());
                            preorderData.setPreorder(shipmentInformation.getPreorder().isPreorder());

                            ShipmentInformationData shipmentInformationData = new ShipmentInformationData();
                            shipmentInformationData.setEstimation(shipmentInformation.getEstimation());
                            shipmentInformationData.setShopLocation(shipmentInformation.getShopLocation());
                            shipmentInformationData.setFreeShipping(freeShippingData);
                            shipmentInformationData.setFreeShippingExtra(freeShippingExtraData);
                            shipmentInformationData.setPreorder(preorderData);

                            groupShopResult.setShipmentInformationData(shipmentInformationData);

                            Shop shopResult = new Shop();

                            shopResult.setShopId(groupShop.getShop().getShopId());
                            shopResult.setUserId(groupShop.getShop().getUserId());
                            shopResult.setShopName(groupShop.getShop().getShopName());
                            shopResult.setShopImage(groupShop.getShop().getShopImage());
                            shopResult.setShopUrl(groupShop.getShop().getShopUrl());
                            shopResult.setShopStatus(groupShop.getShop().getShopStatus());
                            shopResult.setGold(groupShop.getShop().getGoldMerchant().isGoldBadge());
                            shopResult.setGoldBadge(groupShop.getShop().getGoldMerchant().isGoldBadge());
                            shopResult.setOfficial(groupShop.getShop().isOfficial() == 1);
                            if (groupShop.getShop().isOfficial() == 1) {
                                shopResult.setShopBadge(groupShop.getShop().getOfficialStore().getOsLogoUrl());
                            } else if (groupShop.getShop().getGoldMerchant().isGold() == 1) {
                                shopResult.setShopBadge(groupShop.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
                            }
                            shopResult.setFreeReturns(groupShop.getShop().isFreeReturns() == 1);
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
                            shopResult.setShopAlertMessage(groupShop.getShop().getShopAlertMessage());

                            groupShopResult.setShop(shopResult);

                            if (!UtilsKt.isNullOrEmpty(groupShop.getShopShipments())) {
                                List<ShopShipment> shopShipmentListResult = new ArrayList<>();
                                for (com.tokopedia.checkout.data.model.response.shipmentaddressform.ShopShipment shopShipment :
                                        groupShop.getShopShipments()) {
                                    ShopShipment shopShipmentResult = new ShopShipment();
                                    shopShipmentResult.setDropshipEnabled(shopShipment.isDropshipEnabled() == 1);
                                    shopShipmentResult.setShipCode(shopShipment.getShipCode());
                                    shopShipmentResult.setShipId(shopShipment.getShipId());
                                    shopShipmentResult.setShipLogo(shopShipment.getShipLogo());
                                    shopShipmentResult.setShipName(shopShipment.getShipName());
                                    if (!UtilsKt.isNullOrEmpty(shopShipment.getShipProds())) {
                                        List<ShipProd> shipProdListResult = new ArrayList<>();
                                        for (com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipProd shipProd :
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
                                for (com.tokopedia.checkout.data.model.response.shipmentaddressform.Product product
                                        : groupShop.getProducts()) {
                                    Product productResult = new Product();

                                    AnalyticsProductCheckoutData analyticsProductCheckoutData = new AnalyticsProductCheckoutData();
                                    analyticsProductCheckoutData.setProductId(String.valueOf(product.getProductId()));

                                    analyticsProductCheckoutData.setProductAttribution(product.getProductTrackerData().getAttribution());
                                    analyticsProductCheckoutData.setProductListName(product.getProductTrackerData().getTrackerListName());

                                    analyticsProductCheckoutData.setProductCategory(product.getProductCategory());
                                    analyticsProductCheckoutData.setProductCategoryId(String.valueOf(product.getProductCatId()));
                                    analyticsProductCheckoutData.setProductName(product.getProductName());
                                    analyticsProductCheckoutData.setProductPrice(String.valueOf(product.getProductPrice()));
                                    if (product.getTradeInInfo().isValidTradeIn()) {
                                        analyticsProductCheckoutData.setProductPrice(String.valueOf(product.getTradeInInfo().getNewDevicePrice()));
                                        productResult.setProductPrice(product.getTradeInInfo().getNewDevicePrice());
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
                                        analyticsProductCheckoutData.setBuyerAddressId(groupAddressResult.getUserAddress().getAddressId());
                                    }
                                    analyticsProductCheckoutData.setShippingDuration("");
                                    analyticsProductCheckoutData.setCourier("");
                                    analyticsProductCheckoutData.setShippingPrice("");
                                    if (dataResult.getCod() != null) {
                                        analyticsProductCheckoutData.setCodFlag(String.valueOf(dataResult.getCod().isCod()));
                                    } else {
                                        analyticsProductCheckoutData.setCodFlag(String.valueOf(false));
                                    }
                                    if (groupAddressResult.getUserAddress() != null && Utils.isNotNullOrEmptyOrZero(groupAddressResult.getUserAddress().getCornerId())) {
                                        analyticsProductCheckoutData.setTokopediaCornerFlag(String.valueOf(true));
                                    } else {
                                        analyticsProductCheckoutData.setTokopediaCornerFlag(String.valueOf(false));
                                    }
                                    analyticsProductCheckoutData.setFulfillment(String.valueOf(groupShop.isFulfillment()));
                                    analyticsProductCheckoutData.setDiscountedPrice(product.getProductOriginalPrice() > 0);
                                    analyticsProductCheckoutData.setCampaignId(product.getCampaignId());

                                    productResult.setError(!UtilsKt.isNullOrEmpty(product.getErrors()));
                                    productResult.setErrorMessage(product.getErrors().size() >= 1 ? product.getErrors().get(0) : "");
                                    productResult.setErrorMessageDescription(product.getErrors().size() >= 2 ? product.getErrors().get(1) : "");

                                    productResult.setProductId(product.getProductId());
                                    productResult.setCartId(product.getCartId());
                                    productResult.setProductName(product.getProductName());
                                    productResult.setProductPriceFmt(product.getProductPriceFmt());
                                    productResult.setProductPrice(product.getProductPrice());
                                    productResult.setProductOriginalPrice(product.getProductOriginalPrice());
                                    productResult.setProductWholesalePrice(product.getProductWholesalePrice());
                                    productResult.setProductWholesalePriceFmt(product.getProductWholesalePriceFmt());
                                    productResult.setProductWeightFmt(product.getProductWeightFmt());
                                    productResult.setProductWeight(product.getProductWeight());
                                    productResult.setProductCondition(product.getProductCondition());
                                    productResult.setProductUrl(product.getProductUrl());
                                    productResult.setProductReturnable(product.getProductReturnable() == 1);
                                    productResult.setProductIsFreeReturns(product.getProductIsFreeReturns() == 1);
                                    productResult.setProductIsPreorder(product.getProductIsPreorder() == 1);
                                    productResult.setPreOrderDurationDay(product.getProductPreorder().getDurationDay());
                                    if (product.getProductPreorder().getDurationText().length() > 0) {
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

                                    productResult.setShowTicker(product.getProductTicker().isShowTicker());
                                    productResult.setTickerMessage(product.getProductTicker().getMessage());

                                    if (product.getFreeShippingExtra().getEligible()) {
                                        productResult.setFreeShippingExtra(true);
                                    }
                                    if (product.getFreeShipping().getEligible()) {
                                        productResult.setFreeShipping(true);
                                    }

                                    if (product.getTradeInInfo().isValidTradeIn()) {
                                        TradeInInfoData tradeInInfoData = new TradeInInfoData();
                                        tradeInInfoData.setValidTradeIn(product.getTradeInInfo().isValidTradeIn());
                                        tradeInInfoData.setNewDevicePrice(product.getTradeInInfo().getNewDevicePrice());
                                        tradeInInfoData.setNewDevicePriceFmt(product.getTradeInInfo().getNewDevicePriceFmt());
                                        tradeInInfoData.setOldDevicePrice(product.getTradeInInfo().getOldDevicePrice());
                                        tradeInInfoData.setOldDevicePriceFmt(product.getTradeInInfo().getOldDevicePriceFmt());
                                        tradeInInfoData.setDropOffEnable(product.getTradeInInfo().isDropOffEnable());
                                        tradeInInfoData.setDeviceModel(product.getTradeInInfo().getDeviceModel());
                                        tradeInInfoData.setDiagnosticId(product.getTradeInInfo().getDiagnosticId());

                                        productResult.setTradeInInfoData(tradeInInfoData);
                                    }

                                    if (!isDisablePPP) {
                                        if (product.getPurchaseProtectionPlanDataResponse().getProtectionAvailable()) {
                                            PurchaseProtectionPlanData purchaseProtectionPlanData = new PurchaseProtectionPlanData();
                                            PurchaseProtectionPlanDataResponse pppDataMapping = product.getPurchaseProtectionPlanDataResponse();

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

                                    productResult.setVariant(product.getVariantDescriptionDetail().getVariantDescription());

                                    productResult.setProductAlertMessage(product.getProductAlertMessage());
                                    productResult.setProductInformation(product.getProductInformation());

                                    PromoSAFResponse promoSAFResponse = shipmentAddressFormDataResponse.getPromoSAFResponse();
                                    if (promoSAFResponse.getLastApply() != null && promoSAFResponse.getLastApply().getData() != null &&
                                            promoSAFResponse.getLastApply().getData().getTrackingDetails() != null) {
                                        List<TrackingDetailsItem> trackingDetailsItems = promoSAFResponse.getLastApply().getData().getTrackingDetails();
                                        if (trackingDetailsItems.size() > 0) {
                                            for (TrackingDetailsItem trackingDetail : trackingDetailsItems) {
                                                if (trackingDetail.getProductId() != null && trackingDetail.getProductId() == productResult.getProductId()) {
                                                    analyticsProductCheckoutData.setPromoCode(trackingDetail.getPromoCodesTracking());
                                                    analyticsProductCheckoutData.setPromoDetails(trackingDetail.getPromoDetailsTracking());
                                                }
                                            }
                                        }
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

            dataResult.setPopUpMessage(shipmentAddressFormDataResponse.getPopUpMessage());
        }

        return dataResult;
    }

    private List<PromoSpIdUiModel> mapPromoSpId(AdditionalInfo responseAdditionalInfo) {
        List<PromoSpIdUiModel> promoSpIdUiModels = new ArrayList<>();
        List<PromoSpId> promoSpIds = responseAdditionalInfo.getPromoSpIds();
        if (promoSpIds.size() > 0) {
            for (PromoSpId promoSpId : promoSpIds) {
                PromoSpIdUiModel promoSpIdUiModel = new PromoSpIdUiModel();
                promoSpIdUiModel.setUniqueId(promoSpId.getUniqueId());
                List<MvcShippingBenefit> mvcShippingBenefits = promoSpId.getMvcShippingBenefits();
                if (mvcShippingBenefits.size() > 0) {
                    List<MvcShippingBenefitUiModel> mvcShippingBenefitUiModels = new ArrayList<>();
                    for (MvcShippingBenefit mvcShippingBenefit : mvcShippingBenefits) {
                        MvcShippingBenefitUiModel mvcShippingBenefitUiModel = new MvcShippingBenefitUiModel();
                        mvcShippingBenefitUiModel.setBenefitAmount(mvcShippingBenefit.getBenefitAmount());
                        mvcShippingBenefitUiModel.setSpId(mvcShippingBenefit.getSpId());
                        mvcShippingBenefitUiModels.add(mvcShippingBenefitUiModel);
                    }
                    promoSpIdUiModel.setMvcShippingBenefits(mvcShippingBenefitUiModels);
                }
                promoSpIdUiModels.add(promoSpIdUiModel);
            }
        }

        return promoSpIdUiModels;
    }

    @NotNull
    private AddressesData getAddressesData(ShipmentAddressFormDataResponse shipmentAddressFormDataResponse) {
        AddressesData addressesData = new AddressesData();

        // Set default address for normal checkout or tradein checkout
        AddressData addressData = new AddressData();
        if (shipmentAddressFormDataResponse.getGroupAddress().size() > 0) {
            com.tokopedia.checkout.data.model.response.shipmentaddressform.UserAddress defaultAddress =
                    shipmentAddressFormDataResponse.getGroupAddress().get(0).getUserAddress();
            UserAddress defaultAddressData = getUserAddress(defaultAddress);
            addressData.setDefaultAddress(defaultAddressData);
            addressesData.setData(addressData);
        }

        // Get trade in address if available
        Addresses addresses = shipmentAddressFormDataResponse.getAddresses();
        if (!UtilsKt.isNullOrEmpty(addresses.getActive())) {
            com.tokopedia.checkout.data.model.response.shipmentaddressform.UserAddress tradeInDefaultAddress = null;
            com.tokopedia.checkout.data.model.response.shipmentaddressform.UserAddress tradeInDropOffAddress = null;

            for (com.tokopedia.checkout.data.model.response.shipmentaddressform.Data dataAddress : addresses.getData()) {
                if (dataAddress.getKey().equals(AddressesData.DEFAULT_ADDRESS)) {
                    tradeInDefaultAddress = dataAddress.getValue();
                } else if (dataAddress.getKey().equals(AddressesData.TRADE_IN_ADDRESS)) {
                    tradeInDropOffAddress = dataAddress.getValue();
                }
            }

            if (tradeInDefaultAddress != null) {
                addressData.setDefaultAddress(getUserAddress(tradeInDefaultAddress));
            }

            if (tradeInDropOffAddress != null) {
                addressData.setTradeInAddress(getUserAddress(tradeInDropOffAddress));
            }

            addressesData.setDisableTabs(addresses.getDisableTabs());
            addressesData.setActive(addresses.getActive());
            addressesData.setData(addressData);
        }

        return addressesData;
    }

    @NotNull
    private UserAddress getUserAddress(com.tokopedia.checkout.data.model.response.shipmentaddressform.UserAddress defaultAddress) {
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
            if (groupAddress.isError()) {
                hasError = true;
                break;
            }
            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                if (groupShop.isError()) {
                    hasError = true;
                    break;
                }
                int totalProductError = 0;
                String defaultErrorMessage = "";
                for (Product product : groupShop.getProducts()) {
                    if (product.isError() || !UtilsKt.isNullOrEmpty(product.getErrorMessage())) {
                        hasError = true;
                        totalProductError++;
                        if (UtilsKt.isNullOrEmpty(defaultErrorMessage)) {
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

    private String generateShopType(com.tokopedia.checkout.data.model.response.shipmentaddressform.Shop shop) {
        if (shop.isOfficial() == 1)
            return SHOP_TYPE_OFFICIAL_STORE;
        else if (shop.getGoldMerchant().isGoldBadge())
            return SHOP_TYPE_GOLD_MERCHANT;
        else return SHOP_TYPE_REGULER;
    }

}
