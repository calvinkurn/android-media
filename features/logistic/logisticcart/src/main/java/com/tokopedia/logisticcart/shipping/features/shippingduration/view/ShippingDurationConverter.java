package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.FeaturesData;
import com.tokopedia.logisticcart.shipping.model.DynamicPriceModel;
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel;
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticCommon.data.constant.CourierConstant;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.MerchantVoucherData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PromoStacking;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesDetailData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationConverter {

    private static final int COD_TRUE_VAL = 1;

    @Inject
    public ShippingDurationConverter() {
    }

    public ShippingRecommendationData convertModel(RatesData ratesData) {
        ShippingRecommendationData shippingRecommendationData = new ShippingRecommendationData();

        // Check response not null
        if (ratesData != null && ratesData.getRatesDetailData() != null) {

            // Check if has error
            if (ratesData.getRatesDetailData().getError() != null &&
                    !UtilsKt.isNullOrEmpty(ratesData.getRatesDetailData().getError().getErrorMessage())) {
                shippingRecommendationData.setErrorMessage(ratesData.getRatesDetailData().getError().getErrorMessage());
                shippingRecommendationData.setErrorId(ratesData.getRatesDetailData().getError().getErrorId());
            }

            // Check has service / duration list
            if (ratesData.getRatesDetailData().getServices() != null &&
                    ratesData.getRatesDetailData().getServices().size() > 0) {

                // Setting up for Logistic Promo
                shippingRecommendationData.setLogisticPromo(
                        convertToPromoModel(ratesData.getRatesDetailData().getPromoStacking()));

                // Has service / duration list
                shippingRecommendationData.setShippingDurationViewModels(
                        convertShippingDuration(ratesData.getRatesDetailData()));
            }
        }
        return shippingRecommendationData;
    }

    private List<ShippingDurationUiModel> convertShippingDuration(RatesDetailData ratesDetailData) {
        List<ServiceData> serviceDataList = ratesDetailData.getServices();
        String ratesId = ratesDetailData.getRatesId();
        boolean isPromoStackingApplied = isPromoStackingApplied(ratesDetailData);
        // Check if has blackbox info
        String blackboxInfo = "";
        if (ratesDetailData.getInfo() != null &&
                ratesDetailData.getInfo().getBlackboxInfo() != null &&
                !UtilsKt.isNullOrEmpty(ratesDetailData.getInfo().getBlackboxInfo().getTextInfo())) {
            blackboxInfo = ratesDetailData.getInfo().getBlackboxInfo().getTextInfo();
        }

        List<ShippingDurationUiModel> shippingDurationUiModels = new ArrayList<>();
        for (ServiceData serviceData : serviceDataList) {
            ShippingDurationUiModel shippingDurationUiModel = new ShippingDurationUiModel();
            shippingDurationUiModel.setServiceData(serviceData);
            shippingDurationUiModel.setShowShippingInformation(isCourierInstantOrSameday(serviceData.getServiceId()));
            shippingDurationUiModel.setEtaErrorCode(serviceData.getTexts().getErrorCode());
            List<ShippingCourierUiModel> shippingCourierUiModels =
                    convertToShippingCourierViewModel(shippingDurationUiModel,
                            serviceData.getProducts(), ratesId, blackboxInfo);
            shippingDurationUiModel.setShippingCourierViewModelList(shippingCourierUiModels);
            if (shippingCourierUiModels.size() > 0) {
                shippingDurationUiModels.add(shippingDurationUiModel);
            }
            if (serviceData.getError() != null && !UtilsKt.isNullOrEmpty(serviceData.getError().getErrorMessage())) {
                if (serviceData.getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                    serviceData.getTexts().setTextRangePrice(serviceData.getError().getErrorMessage());
                } else {
                    shippingDurationUiModel.setErrorMessage(serviceData.getError().getErrorMessage());
                }
            }
            if (serviceData.getCodData() != null) {
                shippingDurationUiModel.setCodAvailable(serviceData.getCodData().getIsCod() == COD_TRUE_VAL);
                shippingDurationUiModel.setCodText(serviceData.getCodData().getCodText());
            }
            if (serviceData.getMerchantVoucherData() != null) {
                MerchantVoucherData merchantVoucherData = serviceData.getMerchantVoucherData();
                MerchantVoucherModel merchantVoucherModel = new MerchantVoucherModel(
                        merchantVoucherData.isMvc(),
                        merchantVoucherData.getMvcTitle(),
                        merchantVoucherData.getMvcLogo(),
                        merchantVoucherData.getMvcErrorMessage()
                );
               shippingDurationUiModel.setMerchantVoucherModel(merchantVoucherModel);
            }

            if (serviceData.getFeatures() != null) {
                FeaturesData featuresData = serviceData.getFeatures();

                DynamicPriceModel dynamicPriceModel = new DynamicPriceModel(
                        featuresData.getDynamicPricing().getTextLabel()
                );
                shippingDurationUiModel.setDynamicPriceModel(dynamicPriceModel);
            }
        }

        return shippingDurationUiModels;
    }

    private boolean isCourierInstantOrSameday(int shipperId) {
        int[] ids = CourierConstant.INSTANT_SAMEDAY_DURATION;
        for (int id : ids) {
            if (shipperId == id) return true;
        }
        return false;
    }

    private List<ShippingCourierUiModel> convertToShippingCourierViewModel(ShippingDurationUiModel shippingDurationUiModel,
                                                                           List<ProductData> productDataList,
                                                                           String ratesId,
                                                                           String blackboxInfo) {
        List<ShippingCourierUiModel> shippingCourierUiModels = new ArrayList<>();
        for (ProductData productData : productDataList) {
            addShippingCourierViewModel(shippingDurationUiModel, ratesId,
                    shippingCourierUiModels, productData, blackboxInfo);
        }

        return shippingCourierUiModels;
    }

    private void addShippingCourierViewModel(ShippingDurationUiModel shippingDurationUiModel,
                                             String ratesId,
                                             List<ShippingCourierUiModel> shippingCourierUiModels,
                                             ProductData productData, String blackboxInfo) {
        ShippingCourierUiModel shippingCourierUiModel = new ShippingCourierUiModel();
        shippingCourierUiModel.setProductData(productData);
        shippingCourierUiModel.setBlackboxInfo(blackboxInfo);
        shippingCourierUiModel.setServiceData(shippingDurationUiModel.getServiceData());
        shippingCourierUiModel.setRatesId(ratesId);
        shippingCourierUiModels.add(shippingCourierUiModel);
    }

    private LogisticPromoUiModel convertToPromoModel(PromoStacking promo) {
        if (promo == null || promo.getIsPromo() != 1) return null;
        boolean applied = promo.getIsApplied() == 1;
        return new LogisticPromoUiModel(
                promo.getPromoCode(), promo.getTitle(), promo.getBenefitDesc(),
                promo.getShipperName(), promo.getServiceId(), promo.getShipperId(),
                promo.getShipperProductId(), promo.getShipperDesc(), promo.getShipperDisableText(),
                promo.getPromoTncHtml(), applied, promo.getImageUrl(), promo.getDiscontedRate(),
                promo.getShippingRate(), promo.getBenefitAmount(), promo.isDisabled(), promo.isHideShipperName(),
                promo.getCod(), promo.getEta(), promo.isBebasOngkirExtra());
    }

    private boolean isPromoStackingApplied(RatesDetailData ratesDetailData) {
        if (ratesDetailData.getPromoStacking() == null) return false;
        return ratesDetailData.getPromoStacking().getIsApplied() == 1;
    }


}
