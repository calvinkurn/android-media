package com.tokopedia.checkout.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Donation;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ProductShipment;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ProductShipmentMapping;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.PurchaseProtectionPlanData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ServiceId;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Shop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.TradeInInfo;
import com.tokopedia.checkout.domain.datamodel.promostacking.AutoApplyStackData;
import com.tokopedia.checkout.domain.datamodel.promostacking.GlobalCouponAttrData;
import com.tokopedia.checkout.domain.datamodel.promostacking.MessageData;
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.EgoldAttributeModel;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.EgoldTieringModel;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.shipping_recommendation.domain.shipping.AnalyticsProductCheckoutData;
import com.tokopedia.shipping_recommendation.domain.shipping.CodModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipProd;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.transactiondata.entity.response.cartlist.EgoldTieringData;
import com.tokopedia.transactiondata.entity.response.cartlist.Message;
import com.tokopedia.transactiondata.entity.response.cartlist.VoucherOrdersItem;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class ShipmentMapper implements IShipmentMapper {
    private static final String SHOP_TYPE_OFFICIAL_STORE = "official_store";
    private static final String SHOP_TYPE_GOLD_MERCHANT = "gold_merchant";
    private static final String SHOP_TYPE_REGULER = "reguler";
    private final IMapperUtil mapperUtil;

    @Inject
    public ShipmentMapper(IMapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
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
        dataResult.setError(!mapperUtil.isEmpty(shipmentAddressFormDataResponse.getErrors()));
        dataResult.setErrorMessage(mapperUtil.convertToString(shipmentAddressFormDataResponse.getErrors()));
        dataResult.setShowOnboarding(shipmentAddressFormDataResponse.isShowOnboarding());

        if (shipmentAddressFormDataResponse.getPromoSuggestion() != null) {
            CartPromoSuggestion cartPromoSuggestion = new CartPromoSuggestion();
            cartPromoSuggestion.setCta(shipmentAddressFormDataResponse.getPromoSuggestion().getCta());
            cartPromoSuggestion.setCtaColor(shipmentAddressFormDataResponse.getPromoSuggestion().getCtaColor());
            cartPromoSuggestion.setPromoCode(shipmentAddressFormDataResponse.getPromoSuggestion().getPromoCode());
            cartPromoSuggestion.setText(shipmentAddressFormDataResponse.getPromoSuggestion().getText());
            cartPromoSuggestion.setVisible(shipmentAddressFormDataResponse.getPromoSuggestion().getIsVisible() == 1);
            dataResult.setCartPromoSuggestion(cartPromoSuggestion);
        }

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

            EgoldTieringModel egoldTieringModel;
            ArrayList<EgoldTieringModel> egoldTieringModelArrayList = new ArrayList<>();

            if (shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldTieringDataArrayList() != null) {
                for (EgoldTieringData data : shipmentAddressFormDataResponse.getEgoldAttributes().getEgoldTieringDataArrayList()) {
                    egoldTieringModel = new EgoldTieringModel();
                    egoldTieringModel.setBasisAmount(data.getBasisAmount());
                    egoldTieringModel.setMaxAmount(data.getMaxAmount());
                    egoldTieringModel.setMinAmount(data.getMinAmount());
                    egoldTieringModel.setMinTotalAmount(data.getMinToalAmount());
                    egoldTieringModelArrayList.add(egoldTieringModel);
                }
            }

            egoldAttributeModel.setEgoldTieringModelArrayList(egoldTieringModelArrayList);

            dataResult.setEgoldAttributes(egoldAttributeModel);
        }

        if (shipmentAddressFormDataResponse.getAutoApply() != null) {
            AutoApplyData autoApplyData = new AutoApplyData();
            autoApplyData.setCode(shipmentAddressFormDataResponse.getAutoapplyV2().getCode());
            autoApplyData.setDiscountAmount(shipmentAddressFormDataResponse.getAutoApply().getDiscountAmount());
            autoApplyData.setIsCoupon(shipmentAddressFormDataResponse.getAutoapplyV2().getIsCoupon());
            autoApplyData.setMessageSuccess(shipmentAddressFormDataResponse.getAutoapplyV2().getMessage().getText());
            int promoId = 0;
            if (!TextUtils.isEmpty(shipmentAddressFormDataResponse.getAutoapplyV2().getPromoCodeId())) {
                Integer.valueOf(shipmentAddressFormDataResponse.getAutoapplyV2().getPromoCodeId());
            }
            autoApplyData.setPromoId(promoId);
            autoApplyData.setSuccess(shipmentAddressFormDataResponse.getAutoApply().isSuccess());
            autoApplyData.setTitleDescription(shipmentAddressFormDataResponse.getAutoapplyV2().getTitleDescription());
            autoApplyData.setState(shipmentAddressFormDataResponse.getAutoapplyV2().getMessage().getState());
            dataResult.setAutoApplyData(autoApplyData);
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
                    autoApplyStackData.setVoucherOrders(voucherOrdersItemDataList);
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

        if (shipmentAddressFormDataResponse.getDonation() != null) {
            Donation donation = new Donation();
            donation.setTitle(shipmentAddressFormDataResponse.getDonation().getTitle());
            donation.setDescription(shipmentAddressFormDataResponse.getDonation().getDescription());
            donation.setNominal(shipmentAddressFormDataResponse.getDonation().getNominal());
            dataResult.setDonation(donation);
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

        if (!mapperUtil.isEmpty(shipmentAddressFormDataResponse.getGroupAddress())) {
            List<GroupAddress> groupAddressListResult = new ArrayList<>();
            for (com.tokopedia.transactiondata.entity.response.shippingaddressform.GroupAddress
                    groupAddress : shipmentAddressFormDataResponse.getGroupAddress()) {
                GroupAddress groupAddressResult = new GroupAddress();
                groupAddressResult.setError(!mapperUtil.isEmpty(groupAddress.getErrors()));
                groupAddressResult.setErrorMessage(mapperUtil.convertToString(groupAddress.getErrors()));
                groupAddressResult.setWarning(!mapperUtil.isEmpty(groupAddress.getMessages()));
                groupAddressResult.setWarningMessage(mapperUtil.convertToString(groupAddress.getMessages()));

                if (groupAddress.getUserAddress() != null) {
                    com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress userAddressResult =
                            new com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress();
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

                    groupAddressResult.setUserAddress(userAddressResult);
                }

                if (!mapperUtil.isEmpty(groupAddress.getGroupShop())) {
                    List<GroupShop> groupShopListResult = new ArrayList<>();
                    for (com.tokopedia.transactiondata.entity.response.shippingaddressform.GroupShop
                            groupShop : groupAddress.getGroupShop()) {
                        com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop groupShopResult =
                                new com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop();

                        groupShopResult.setError(!mapperUtil.isEmpty(groupShop.getErrors()));
                        groupShopResult.setErrorMessage(mapperUtil.convertToString(groupShop.getErrors()));
                        groupShopResult.setWarning(!mapperUtil.isEmpty(groupShop.getMessages()));
                        groupShopResult.setWarningMessage(mapperUtil.convertToString(groupShop.getMessages()));

                        groupShopResult.setShippingId(groupShop.getShippingId());
                        groupShopResult.setSpId(groupShop.getSpId());
                        groupShopResult.setDropshipperName(groupShop.getDropshiper() != null ? groupShop.getDropshiper().getName() : "");
                        groupShopResult.setDropshipperPhone(groupShop.getDropshiper() != null ? groupShop.getDropshiper().getTelpNo() : "");
                        groupShopResult.setUseInsurance(groupShop.isInsurance());
                        groupShopResult.setCartString(groupShop.getCartString() != null ? groupShop.getCartString() : "");
                        groupShopResult.setHasPromoList(groupShop.isHasPromoList());
                        groupShopResult.setSaveStateFlag(groupShop.isSaveStateFlag());

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
                            } else if (groupShop.getShop().getGoldMerchant().getIsGold() == 1) {
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

                        if (!mapperUtil.isEmpty(groupShop.getShopShipments())) {
                            List<ShopShipment> shopShipmentListResult = new ArrayList<>();
                            for (com.tokopedia.transactiondata.entity.response.shippingaddressform.ShopShipment shopShipment :
                                    groupShop.getShopShipments()) {
                                ShopShipment shopShipmentResult = new ShopShipment();
                                shopShipmentResult.setDropshipEnabled(shopShipment.getIsDropshipEnabled() == 1);
                                shopShipmentResult.setShipCode(shopShipment.getShipCode());
                                shopShipmentResult.setShipId(shopShipment.getShipId());
                                shopShipmentResult.setShipLogo(shopShipment.getShipLogo());
                                shopShipmentResult.setShipName(shopShipment.getShipName());
                                if (!mapperUtil.isEmpty(shopShipment.getShipProds())) {
                                    List<ShipProd> shipProdListResult = new ArrayList<>();
                                    for (com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipProd shipProd :
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

                        if (!mapperUtil.isEmpty(groupShop.getProducts())) {
                            List<Product> productListResult = new ArrayList<>();
                            for (com.tokopedia.transactiondata.entity.response.shippingaddressform.Product product
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
                                StringBuilder promoCodes = new StringBuilder();
                                StringBuilder promoDetails = new StringBuilder();
                                if (dataResult.getAutoApplyStackData() != null) {
                                    if (!TextUtils.isEmpty(dataResult.getAutoApplyStackData().getCode())) {
                                        promoCodes.append(dataResult.getAutoApplyStackData().getCode());
                                        int amount = 0;
                                        int type = 0;
                                        if (shipmentAddressFormDataResponse.getAutoapplyStack().getDiscountAmount() > 0) {
                                            amount = shipmentAddressFormDataResponse.getAutoapplyStack().getDiscountAmount();
                                            type = PromoStackingData.CREATOR.getTYPE_COUPON();
                                        } else if (shipmentAddressFormDataResponse.getAutoapplyStack().getCashbackWalletAmount() > 0) {
                                            amount = shipmentAddressFormDataResponse.getAutoapplyStack().getCashbackWalletAmount();
                                            type = PromoStackingData.CREATOR.getTYPE_VOUCHER();
                                        }
                                        promoDetails.append(type)
                                                .append(":")
                                                .append(amount)
                                                .append(":")
                                                .append(shipmentAddressFormDataResponse.getAutoapplyStack().getMessage().getState());
                                    }
                                    if (dataResult.getAutoApplyStackData().getVoucherOrders() != null) {
                                        for (VoucherOrdersItemData voucherOrdersItemData : dataResult.getAutoApplyStackData().getVoucherOrders()) {
                                            if (voucherOrdersItemData.getUniqueId().equalsIgnoreCase(groupShop.getCartString())) {
                                                if (!TextUtils.isEmpty(promoCodes)) {
                                                    promoCodes.append("|");
                                                }
                                                promoCodes.append(voucherOrdersItemData.getCode());
                                                int amount = 0;
                                                int type = 0;
                                                if (voucherOrdersItemData.getDiscountAmount() > 0) {
                                                    amount = voucherOrdersItemData.getDiscountAmount();
                                                    type = PromoStackingData.CREATOR.getTYPE_COUPON();
                                                } else if (voucherOrdersItemData.getCashbackWalletAmount() > 0) {
                                                    amount = voucherOrdersItemData.getCashbackWalletAmount();
                                                    type = PromoStackingData.CREATOR.getTYPE_VOUCHER();
                                                }

                                                if (!TextUtils.isEmpty(promoDetails)) {
                                                    promoDetails.append("|");
                                                }
                                                promoDetails.append(type)
                                                        .append(":")
                                                        .append(amount)
                                                        .append(":")
                                                        .append(voucherOrdersItemData.getMessageData().getState());
                                                break;
                                            }
                                        }
                                    }
                                }

                                if (!TextUtils.isEmpty(promoCodes)) {
                                    analyticsProductCheckoutData.setPromoCode1(promoCodes.toString());
                                }
                                if (!TextUtils.isEmpty(promoDetails)) {
                                    analyticsProductCheckoutData.setPromoDetails(promoDetails.toString());
                                }
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

                                productResult.setError(!mapperUtil.isEmpty(product.getErrors()));
                                if (product.getErrors() != null) {
                                    productResult.setErrorMessage(product.getErrors().size() >= 1 ? product.getErrors().get(0) : "");
                                    productResult.setErrorMessageDescription(product.getErrors().size() >= 2 ? product.getErrors().get(1) : "");
                                }

                                productResult.setProductId(product.getProductId());
                                productResult.setCartId(product.getCartId());
                                productResult.setProductName(product.getProductName());
                                productResult.setProductPriceFmt(product.getProductPriceFmt());
                                productResult.setProductPrice(product.getProductPrice());
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

                                if (product.getTradeInInfo() != null && product.getTradeInInfo().isValidTradeIn()) {
                                    TradeInInfo tradeInInfo = new TradeInInfo();
                                    tradeInInfo.setValidTradeIn(product.getTradeInInfo().isValidTradeIn());
                                    tradeInInfo.setNewDevicePrice(product.getTradeInInfo().getNewDevicePrice());
                                    tradeInInfo.setNewDevicePriceFmt(product.getTradeInInfo().getNewDevicePriceFmt());
                                    tradeInInfo.setOldDevicePrice(product.getTradeInInfo().getOldDevicePrice());
                                    tradeInInfo.setOldDevicePriceFmt(product.getTradeInInfo().getOldDevicePriceFmt());

                                    productResult.setTradeInInfo(tradeInInfo);
                                }

                                if (!mapperUtil.isEmpty(product.getPurchaseProtectionPlanData())) {
                                    PurchaseProtectionPlanData purchaseProtectionPlanData = new PurchaseProtectionPlanData();
                                    com.tokopedia.transactiondata.entity.response.shippingaddressform.PurchaseProtectionPlanData pppDataMapping =
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

                                    productResult.setPurchaseProtectionPlanData(purchaseProtectionPlanData);
                                }

                                if (!mapperUtil.isEmpty(product.getFreeReturns())) {
                                    productResult.setFreeReturnLogo(product.getFreeReturns().getFreeReturnsLogo());
                                }

                                if (!mapperUtil.isEmpty(product.getProductShipment())) {
                                    List<ProductShipment> productShipmentListResult = new ArrayList<>();
                                    for (com.tokopedia.transactiondata.entity.response.shippingaddressform.ProductShipment
                                            productShipment : product.getProductShipment()) {
                                        ProductShipment productShipmentResult = new ProductShipment();
                                        productShipmentResult.setServiceId(productShipment.getServiceId());
                                        productShipmentResult.setShipmentId(productShipment.getShipmentId());
                                        productShipmentListResult.add(productShipmentResult);
                                    }
                                    productResult.setProductShipment(productShipmentListResult);
                                }

                                if (!mapperUtil.isEmpty(product.getProductShipmentMapping())) {

                                    List<ProductShipmentMapping> productShipmentMappingListResult = new ArrayList<>();
                                    for (com.tokopedia.transactiondata.entity.response.shippingaddressform.ProductShipmentMapping
                                            productShipmentMapping : product.getProductShipmentMapping()) {
                                        ProductShipmentMapping productShipmentMappingResult = new ProductShipmentMapping();
                                        productShipmentMappingResult.setShipmentId(productShipmentMapping.getShipmentId());
                                        productShipmentMappingResult.setShippingIds(productShipmentMapping.getShippingIds());

                                        if (!mapperUtil.isEmpty(productShipmentMapping.getServiceIds())) {
                                            List<ServiceId> serviceIdListResult = new ArrayList<>();
                                            for (com.tokopedia.transactiondata.entity.response.shippingaddressform.ServiceId serviceId :
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

    private String generateShopType(com.tokopedia.transactiondata.entity.response.shippingaddressform.Shop shop) {
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

}
