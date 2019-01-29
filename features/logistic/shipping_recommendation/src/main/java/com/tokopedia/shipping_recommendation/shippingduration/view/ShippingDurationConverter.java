package com.tokopedia.shipping_recommendation.shippingduration.view;

import android.text.TextUtils;


import com.tokopedia.shipping_recommendation.domain.shipping.ShipProd;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationConverter {

    private static final int COD_TRUE_VAL = 1;

    public List<ShippingDurationViewModel> convertToViewModel(List<ServiceData> serviceDataList,
                                                              List<ShopShipment> shopShipmentList,
                                                              ShipmentDetailData shipmentDetailData,
                                                              String ratesId,
                                                              int selectedServiceId) {
        int selectedSpId = 0;
        if (shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null) {
            selectedSpId = shipmentDetailData.getSelectedCourier().getShipperProductId();
        }
        List<ShippingDurationViewModel> shippingDurationViewModels = new ArrayList<>();
        for (ServiceData serviceData : serviceDataList) {
            ShippingDurationViewModel shippingDurationViewModel = new ShippingDurationViewModel();
            shippingDurationViewModel.setServiceData(serviceData);
            List<ShippingCourierViewModel> shippingCourierViewModels =
                    convertToShippingCourierViewModel(shippingDurationViewModel, serviceData.getProducts(),
                            shopShipmentList, ratesId, selectedSpId, selectedServiceId);
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
            if(serviceData.getCodData() != null) {
                shippingDurationViewModel.setCodAvailable(serviceData.getCodData().getIsCod() == COD_TRUE_VAL);
                shippingDurationViewModel.setCodText(serviceData.getCodData().getCodText());
            }
        }

        return shippingDurationViewModels;
    }

    private List<ShippingCourierViewModel> convertToShippingCourierViewModel(ShippingDurationViewModel shippingDurationViewModel,
                                                                             List<ProductData> productDataList,
                                                                             List<ShopShipment> shopShipmentList,
                                                                             String ratesId,
                                                                             int selectedSpId, int selectedServiceId) {
        List<ShippingCourierViewModel> shippingCourierViewModels = new ArrayList<>();
        for (ProductData productData : productDataList) {
            addShippingCourierViewModel(shippingDurationViewModel, shopShipmentList, ratesId, selectedSpId,
                    selectedServiceId, shippingCourierViewModels, productData);
        }

        return shippingCourierViewModels;
    }

    private void addShippingCourierViewModel(ShippingDurationViewModel shippingDurationViewModel,
                                             List<ShopShipment> shopShipmentList,
                                             String ratesId,
                                             int selectedSpId, int selectedServiceId,
                                             List<ShippingCourierViewModel> shippingCourierViewModels,
                                             ProductData productData) {
        ShippingCourierViewModel shippingCourierViewModel = new ShippingCourierViewModel();
        shippingCourierViewModel.setProductData(productData);
        shippingCourierViewModel.setServiceData(shippingDurationViewModel.getServiceData());
        shippingCourierViewModel.setRatesId(ratesId);
        if (selectedSpId != 0) {
            if (selectedSpId == productData.getShipperProductId()) {
                shippingCourierViewModel.setSelected(true);
                shippingDurationViewModel.setSelected(true);
            }
        } else if (selectedServiceId != 0) {
            if (!(shippingDurationViewModel.getServiceData().getError() != null &&
                    !TextUtils.isEmpty(shippingDurationViewModel.getServiceData().getError().getErrorId())) &&
                    selectedServiceId == shippingDurationViewModel.getServiceData().getServiceId()) {
                shippingDurationViewModel.setSelected(true);
            }
        } else {
            shippingCourierViewModel.setSelected(productData.isRecommend());
            shippingDurationViewModel.setSelected(false);
        }
        shippingCourierViewModel.setAdditionalFee(getAdditionalFee(productData, shopShipmentList));
        shippingCourierViewModel.setAllowDropshipper(isAllowDropshipper(productData, shopShipmentList));
        shippingCourierViewModels.add(shippingCourierViewModel);
    }

    private int getAdditionalFee(ProductData productData, List<ShopShipment> shopShipmentList) {
        for (ShopShipment shopShipment : shopShipmentList) {
            if (shopShipment.getShipProds() != null) {
                for (ShipProd shipProd : shopShipment.getShipProds()) {
                    if (shipProd.getShipProdId() == productData.getShipperProductId()) {
                        return shipProd.getAdditionalFee();
                    }
                }
            }
        }
        return 0;
    }

    private boolean isAllowDropshipper(ProductData productData, List<ShopShipment> shopShipmentList) {
        for (ShopShipment shopShipment : shopShipmentList) {
            if (shopShipment.getShipId() == productData.getShipperId()) {
                return shopShipment.isDropshipEnabled();
            }
        }

        return false;
    }


}
