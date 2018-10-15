package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationConverter {

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
            if (serviceData.getProducts() != null && serviceData.getProducts().size() > 0) {
                for (ProductData product : serviceData.getProducts()) {
                    if (product.getError() != null &&
                            product.getError().getErrorMessage() != null &&
                            product.getError().getErrorId() != null) {
                        if (product.getError().getErrorId().equals(ErrorData.ERROR_PINPOINT_NEEDED)) {
                            serviceData.getTexts().setTextRangePrice(product.getError().getErrorMessage());
                        } else {
                            shippingDurationViewModel.setErrorMessage(product.getError().getErrorMessage());
                        }
                    }
                }
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
            if (selectedServiceId == shippingDurationViewModel.getServiceData().getServiceId()) {
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
