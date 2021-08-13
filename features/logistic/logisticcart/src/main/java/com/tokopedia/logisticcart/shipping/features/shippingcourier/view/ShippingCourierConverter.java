package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.DynamicPriceData;
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.DynamicPriceModel;
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel;
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.CodProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.MerchantVoucherProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.OntimeDeliveryGuarantee;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierConverter {

    @Inject
    public ShippingCourierConverter() {
    }

    public void updateSelectedCourier(List<ShippingCourierUiModel> courierModels, int spId) {
        if (courierModels != null) {
            for (ShippingCourierUiModel courierModel : courierModels) {
                if (courierModel.getProductData().getShipperProductId() == spId) {
                    courierModel.setSelected(true);
                } else {
                    courierModel.setSelected(false);
                }
            }
        }
    }

    public CourierItemData convertToCourierItemData(ShippingCourierUiModel shippingCourierUiModel) {
        CourierItemData courierItemData = new CourierItemData();
        courierItemData.setShipperId(shippingCourierUiModel.getProductData().getShipperId());
        courierItemData.setServiceId(shippingCourierUiModel.getServiceData().getServiceId());
        courierItemData.setShipperProductId(shippingCourierUiModel.getProductData().getShipperProductId());
        courierItemData.setName(shippingCourierUiModel.getProductData().getShipperName());
        courierItemData.setEstimatedTimeDelivery(shippingCourierUiModel.getServiceData().getServiceName());
        courierItemData.setMinEtd(shippingCourierUiModel.getProductData().getEtd().getMinEtd());
        courierItemData.setMaxEtd(shippingCourierUiModel.getProductData().getEtd().getMaxEtd());
        courierItemData.setShipperPrice(shippingCourierUiModel.getProductData().getPrice().getPrice());
        courierItemData.setShipperFormattedPrice(shippingCourierUiModel.getProductData().getPrice().getFormattedPrice());
        courierItemData.setInsurancePrice(shippingCourierUiModel.getProductData().getInsurance().getInsurancePrice());
        courierItemData.setInsuranceType(shippingCourierUiModel.getProductData().getInsurance().getInsuranceType());
        courierItemData.setInsuranceUsedType(shippingCourierUiModel.getProductData().getInsurance().getInsuranceUsedType());
        courierItemData.setInsuranceUsedInfo(shippingCourierUiModel.getProductData().getInsurance().getInsuranceUsedInfo());
        courierItemData.setInsuranceUsedDefault(shippingCourierUiModel.getProductData().getInsurance().getInsuranceUsedDefault());
        courierItemData.setUsePinPoint(shippingCourierUiModel.getProductData().getIsShowMap() == 1);
        if (!courierItemData.isUsePinPoint()) {
            if (shippingCourierUiModel.getProductData().getError() != null &&
                    shippingCourierUiModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                courierItemData.setUsePinPoint(true);
            }
        }
        if (shippingCourierUiModel.getServiceData().getOrderPriority() != null) {
            courierItemData.setNow(shippingCourierUiModel.getServiceData().getOrderPriority().getNow());
            courierItemData.setPriorityPrice(shippingCourierUiModel.getServiceData().getOrderPriority().getPrice());
            courierItemData.setPriorityFormattedPrice(shippingCourierUiModel.getServiceData().getOrderPriority().getFormattedPrice());
            courierItemData.setPriorityInnactiveMessage(shippingCourierUiModel.getServiceData().getOrderPriority().getInactiveMessage());
            courierItemData.setPriorityDurationMessage(shippingCourierUiModel.getServiceData().getOrderPriority().getStaticMessage().getDurationMessage());
            courierItemData.setPriorityFeeMessage(shippingCourierUiModel.getServiceData().getOrderPriority().getStaticMessage().getFeeMessage());
            courierItemData.setPriorityWarningboxMessage(shippingCourierUiModel.getServiceData().getOrderPriority().getStaticMessage().getWarningBoxMessage());
            courierItemData.setPriorityCheckboxMessage(shippingCourierUiModel.getServiceData().getOrderPriority().getStaticMessage().getCheckboxMessage());
            courierItemData.setPriorityPdpMessage(shippingCourierUiModel.getServiceData().getOrderPriority().getStaticMessage().getPdpMessage());
        }
        courierItemData.setAllowDropshiper(shippingCourierUiModel.isAllowDropshipper());
        courierItemData.setAdditionalPrice(shippingCourierUiModel.getAdditionalFee());
        courierItemData.setPromoCode(shippingCourierUiModel.getProductData().getPromoCode());
        courierItemData.setChecksum(shippingCourierUiModel.getProductData().getCheckSum());
        courierItemData.setUt(shippingCourierUiModel.getProductData().getUnixTime());
        courierItemData.setBlackboxInfo(shippingCourierUiModel.getBlackboxInfo());
        courierItemData.setSelected(true);
        courierItemData.setPreOrderModel(shippingCourierUiModel.getPreOrderModel());

        /*on time delivery*/
        if (shippingCourierUiModel.getProductData().getFeatures().getOntimeDeliveryGuarantee() != null) {
            OntimeDeliveryGuarantee otdPrev = shippingCourierUiModel.getProductData().getFeatures().getOntimeDeliveryGuarantee();
            OntimeDelivery otd = new OntimeDelivery(
                    otdPrev.getAvailable(),
                    otdPrev.getTextLabel(),
                    otdPrev.getTextDetail(),
                    otdPrev.getUrlDetail(),
                    otdPrev.getValue(),
                    otdPrev.getIconUrl()
            );
            courierItemData.setOntimeDelivery(otd);
        }

        /*merchant voucher*/
        if (shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData() != null) {
            MerchantVoucherProductData merchantVoucherProductData = shippingCourierUiModel.getProductData().getFeatures().getMerchantVoucherProductData();
            MerchantVoucherProductModel mvc = new MerchantVoucherProductModel(
                    merchantVoucherProductData.isMvc(),
                    merchantVoucherProductData.getMvcLogo(),
                    merchantVoucherProductData.getMvcErrorMessage()
            );
            courierItemData.setMerchantVoucherProductModel(mvc);
        }

        /*cash on delivert*/
        if (shippingCourierUiModel.getProductData().getCodProductData() != null) {
            CodProductData codProductData = shippingCourierUiModel.getProductData().getCodProductData();
            CashOnDeliveryProduct codProduct = new CashOnDeliveryProduct(
                    codProductData.getIsCodAvailable(),
                    codProductData.getCodText(),
                    codProductData.getCodPrice(),
                    codProductData.getFormattedPrice(),
                    codProductData.getTncText(),
                    codProductData.getTncLink()
            );
            courierItemData.setCodProductData(codProduct);
        }
        if (shippingCourierUiModel.getProductData().getEstimatedTimeArrival() != null) {
            courierItemData.setEtaText(shippingCourierUiModel.getProductData().getEstimatedTimeArrival().getTextEta());
            courierItemData.setEtaErrorCode(shippingCourierUiModel.getProductData().getEstimatedTimeArrival().getErrorCode());
        }
        return courierItemData;

    }

}