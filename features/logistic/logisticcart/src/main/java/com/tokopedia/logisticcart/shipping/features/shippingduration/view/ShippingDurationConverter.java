package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import android.text.TextUtils;

import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.PromoStacking;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesDetailData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

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
            // Check has service / duration list
            if (ratesData.getRatesDetailData().getServices() != null &&
                    ratesData.getRatesDetailData().getServices().size() > 0) {
                // Check if has error
                if (ratesData.getRatesDetailData().getError() != null &&
                        !TextUtils.isEmpty(ratesData.getRatesDetailData().getError().getErrorMessage())) {
                    shippingRecommendationData.setErrorMessage(ratesData.getRatesDetailData().getError().getErrorMessage());
                    shippingRecommendationData.setErrorId(ratesData.getRatesDetailData().getError().getErrorId());
                }

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

    private List<ShippingDurationViewModel> convertShippingDuration(RatesDetailData ratesDetailData) {
        List<ServiceData> serviceDataList = ratesDetailData.getServices();
        String ratesId = ratesDetailData.getRatesId();
        boolean isPromoStackingApplied = isPromoStackingApplied(ratesDetailData);
        // Check if has blackbox info
        String blackboxInfo = "";
        if (ratesDetailData.getInfo() != null &&
                ratesDetailData.getInfo().getBlackboxInfo() != null &&
                !TextUtils.isEmpty(ratesDetailData.getInfo().getBlackboxInfo().getTextInfo())) {
            blackboxInfo = ratesDetailData.getInfo().getBlackboxInfo().getTextInfo();
        }

        List<ShippingDurationViewModel> shippingDurationViewModels = new ArrayList<>();
        for (ServiceData serviceData : serviceDataList) {
            ShippingDurationViewModel shippingDurationViewModel = new ShippingDurationViewModel();
            shippingDurationViewModel.setServiceData(serviceData);
            List<ShippingCourierViewModel> shippingCourierViewModels =
                    convertToShippingCourierViewModel(shippingDurationViewModel,
                            serviceData.getProducts(), ratesId, blackboxInfo);
            shippingDurationViewModel.setShippingCourierViewModelList(shippingCourierViewModels);
            if (shippingCourierViewModels.size() > 0) {
                shippingDurationViewModels.add(shippingDurationViewModel);
            }
            if (serviceData.getError() != null && !TextUtils.isEmpty(serviceData.getError().getErrorMessage())) {
                if (serviceData.getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                    serviceData.getTexts().setTextRangePrice(serviceData.getError().getErrorMessage());
                } else {
                    shippingDurationViewModel.setErrorMessage(serviceData.getError().getErrorMessage());
                }
            }
            if (serviceData.getCodData() != null) {
                shippingDurationViewModel.setCodAvailable(serviceData.getCodData().getIsCod() == COD_TRUE_VAL);
                shippingDurationViewModel.setCodText(serviceData.getCodData().getCodText());
            }
        }

        return shippingDurationViewModels;
    }

    private List<ShippingCourierViewModel> convertToShippingCourierViewModel(ShippingDurationViewModel shippingDurationViewModel,
                                                                             List<ProductData> productDataList,
                                                                             String ratesId,
                                                                             String blackboxInfo) {
        List<ShippingCourierViewModel> shippingCourierViewModels = new ArrayList<>();
        for (ProductData productData : productDataList) {
            addShippingCourierViewModel(shippingDurationViewModel, ratesId,
                    shippingCourierViewModels, productData, blackboxInfo);
        }

        return shippingCourierViewModels;
    }

    private void addShippingCourierViewModel(ShippingDurationViewModel shippingDurationViewModel,
                                             String ratesId,
                                             List<ShippingCourierViewModel> shippingCourierViewModels,
                                             ProductData productData, String blackboxInfo) {
        ShippingCourierViewModel shippingCourierViewModel = new ShippingCourierViewModel();
        shippingCourierViewModel.setProductData(productData);
        shippingCourierViewModel.setBlackboxInfo(blackboxInfo);
        shippingCourierViewModel.setServiceData(shippingDurationViewModel.getServiceData());
        shippingCourierViewModel.setRatesId(ratesId);
        shippingCourierViewModels.add(shippingCourierViewModel);
    }

    private LogisticPromoViewModel convertToPromoModel(PromoStacking promo) {
        if (promo == null || promo.getIsPromo() != 1) return null;
        boolean applied = promo.getIsApplied() == 1;
        return new LogisticPromoViewModel(
                promo.getPromoCode(), promo.getTitle(), promo.getBenefitDesc(),
                promo.getShipperName(), promo.getServiceId(), promo.getShipperId(),
                promo.getShipperProductId(), promo.getShipperDesc(), promo.getShipperDisableText(),
                promo.getPromoTncHtml(), applied, promo.getImageUrl(), promo.getDiscontedRate(),
                promo.getShippingRate(), promo.getBenefitAmount(), promo.isDisabled(), promo.isHideShipperName());
    }

    private boolean isPromoStackingApplied(RatesDetailData ratesDetailData) {
        if (ratesDetailData.getPromoStacking() == null) return false;
        return ratesDetailData.getPromoStacking().getIsApplied() == 1;
    }


}
