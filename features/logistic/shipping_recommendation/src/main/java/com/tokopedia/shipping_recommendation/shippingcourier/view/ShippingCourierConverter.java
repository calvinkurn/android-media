package com.tokopedia.shipping_recommendation.shippingcourier.view;


import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierConverter {

    public CourierItemData convertToCourierItemData(ShippingCourierViewModel shippingCourierViewModel) {
        CourierItemData courierItemData = new CourierItemData();
        courierItemData.setShipperId(shippingCourierViewModel.getProductData().getShipperId());
        courierItemData.setShipperProductId(shippingCourierViewModel.getProductData().getShipperProductId());
        courierItemData.setName(shippingCourierViewModel.getProductData().getShipperName());
        courierItemData.setEstimatedTimeDelivery(shippingCourierViewModel.getServiceData().getServiceName());
        courierItemData.setMinEtd(shippingCourierViewModel.getProductData().getEtd().getMinEtd());
        courierItemData.setMaxEtd(shippingCourierViewModel.getProductData().getEtd().getMaxEtd());
        courierItemData.setShipperPrice(shippingCourierViewModel.getProductData().getPrice().getPrice());
        courierItemData.setShipperFormattedPrice(shippingCourierViewModel.getProductData().getPrice().getFormattedPrice());
        courierItemData.setInsurancePrice(shippingCourierViewModel.getProductData().getInsurance().getInsurancePrice());
        courierItemData.setInsuranceType(shippingCourierViewModel.getProductData().getInsurance().getInsuranceType());
        courierItemData.setInsuranceUsedType(shippingCourierViewModel.getProductData().getInsurance().getInsuranceUsedType());
        courierItemData.setInsuranceUsedInfo(shippingCourierViewModel.getProductData().getInsurance().getInsuranceUsedInfo());
        courierItemData.setInsuranceUsedDefault(shippingCourierViewModel.getProductData().getInsurance().getInsuranceUsedDefault());
        courierItemData.setUsePinPoint(shippingCourierViewModel.getProductData().getIsShowMap() == 1);
        if (!courierItemData.isUsePinPoint()) {
            if (shippingCourierViewModel.getProductData().getError() != null &&
                    shippingCourierViewModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                courierItemData.setUsePinPoint(true);
            }
        }
        courierItemData.setAllowDropshiper(shippingCourierViewModel.isAllowDropshipper());
        courierItemData.setAdditionalPrice(shippingCourierViewModel.getAdditionalFee());
        courierItemData.setPromoCode(shippingCourierViewModel.getProductData().getPromoCode());
        courierItemData.setChecksum(shippingCourierViewModel.getProductData().getCheckSum());
        courierItemData.setUt(shippingCourierViewModel.getProductData().getUnixTime());
        courierItemData.setSelected(true);

        return courierItemData;
    }

}
