package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
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
                                                              ShipmentDetailData shipmentDetailData) {
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
                            shopShipmentList, selectedSpId);
            shippingDurationViewModel.setShippingCourierViewModelList(shippingCourierViewModels);
            if (shippingCourierViewModels.size() > 0) {
                shippingDurationViewModels.add(shippingDurationViewModel);
            }
        }

        return shippingDurationViewModels;
    }

    private List<ShippingCourierViewModel> convertToShippingCourierViewModel(ShippingDurationViewModel shippingDurationViewModel,
                                                                             List<ProductData> productDataList,
                                                                             List<ShopShipment> shopShipmentList,
                                                                             int selectedSpId) {
        List<ShippingCourierViewModel> shippingCourierViewModels = new ArrayList<>();
        for (ProductData productData : productDataList) {
            if (productData.getError() != null) {
                if (!TextUtils.isEmpty(productData.getError().getErrorId())) {
                    if (productData.getError().getErrorId().equals(ErrorData.ERROR_PINPOINT_NEEDED)) {
                        addShippingCourierViewModel(shippingDurationViewModel, shopShipmentList, selectedSpId, shippingCourierViewModels, productData);
                    }
                } else {
                    addShippingCourierViewModel(shippingDurationViewModel, shopShipmentList, selectedSpId, shippingCourierViewModels, productData);
                }
            } else {
                addShippingCourierViewModel(shippingDurationViewModel, shopShipmentList, selectedSpId, shippingCourierViewModels, productData);
            }
        }

        return shippingCourierViewModels;
    }

    private void addShippingCourierViewModel(ShippingDurationViewModel shippingDurationViewModel,
                                             List<ShopShipment> shopShipmentList,
                                             int selectedSpId,
                                             List<ShippingCourierViewModel> shippingCourierViewModels,
                                             ProductData productData) {
        ShippingCourierViewModel shippingCourierViewModel = new ShippingCourierViewModel();
        shippingCourierViewModel.setProductData(productData);
        if (selectedSpId != 0) {
            if (selectedSpId == productData.getShipperProductId()) {
                shippingCourierViewModel.setSelected(true);
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
