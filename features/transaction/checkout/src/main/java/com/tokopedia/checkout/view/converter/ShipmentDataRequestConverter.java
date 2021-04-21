package com.tokopedia.checkout.view.converter;

import com.tokopedia.checkout.view.adapter.ShipmentAdapter;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.checkout.data.model.request.checkout.DataCheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.DropshipDataCheckoutRequest;
import com.tokopedia.checkout.data.model.request.common.OntimeDeliveryGuarantee;
import com.tokopedia.checkout.data.model.request.checkout.ProductDataCheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.PromoRequest;
import com.tokopedia.checkout.data.model.request.common.RatesFeature;
import com.tokopedia.checkout.data.model.request.checkout.ShippingInfoCheckoutRequest;
import com.tokopedia.checkout.data.model.request.checkout.ShopProductCheckoutRequest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 07/03/18
 * Originally Authored by Kris, Aghny
 */

public class ShipmentDataRequestConverter {

    @Inject
    public ShipmentDataRequestConverter() {

    }

    public ShipmentAdapter.RequestData generateRequestData(List<ShipmentCartItemModel> shipmentCartItemModels,
                                                           RecipientAddressModel recipientAddress,
                                                           boolean isAnalyticsPurpose, boolean isTradeInPickup) {
        ShipmentAdapter.RequestData requestData = new ShipmentAdapter.RequestData();
        if (shipmentCartItemModels != null && shipmentCartItemModels.size() > 0) {
            List<ShopProductCheckoutRequest> shopProductCheckoutRequestList = new ArrayList<>();
            if (recipientAddress != null) {
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                        shopProductCheckoutRequestList.add(getProductCheckoutRequest(shipmentCartItemModel, isTradeInPickup));
                    } else if (isAnalyticsPurpose) {
                        shopProductCheckoutRequestList.add(getProductCheckoutRequestForAnalytics(shipmentCartItemModel));
                    }
                }
                requestData.setCheckoutRequestData(createCheckoutRequestData(shopProductCheckoutRequestList, recipientAddress));
            }
        }

        return requestData;
    }

    private ShopProductCheckoutRequest getProductCheckoutRequestForAnalytics(ShipmentCartItemModel shipmentCartItemModel) {
        // Create shop product model for shipment
        ShopProductCheckoutRequest.Builder shopProductCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                .shippingInfo(new ShippingInfoCheckoutRequest.Builder()
                        .shippingId(0)
                        .spId(0)
                        .ratesId("")
                        .checksum("")
                        .ut("")
                        .analyticsDataShippingCourierPrice("")
                        .build())
                .fcancelPartial(0)
                .finsurance(0)
                .isOrderPriority(0)
                .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                .shopId(shipmentCartItemModel.getShopId())
                .warehouseId(shipmentCartItemModel.getFulfillmentId())
                .cartString(shipmentCartItemModel.getCartString())
                .productData(convertToProductDataCheckout(shipmentCartItemModel));

        return shopProductCheckoutBuilder.build();
    }

    private ShopProductCheckoutRequest getProductCheckoutRequest(ShipmentCartItemModel shipmentCartItemModel,
                                                                 boolean isTradeInPickup) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        if (shipmentDetailData != null && (shipmentDetailData.getSelectedCourier() != null ||
                shipmentDetailData.getSelectedCourierTradeInDropOff() != null)) {
            CourierItemData courierItemData = null;
            if (isTradeInPickup && shipmentDetailData.getSelectedCourierTradeInDropOff() != null) {
                courierItemData = shipmentDetailData.getSelectedCourierTradeInDropOff();
            } else if (!isTradeInPickup && shipmentDetailData.getSelectedCourier() != null) {
                courierItemData = shipmentDetailData.getSelectedCourier();
            }

            if (courierItemData != null) {
                RatesFeature ratesFeature = generateRatesFeature(courierItemData);

                // Create shop product model for shipment
                ShopProductCheckoutRequest.Builder shopProductCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                        .shippingInfo(new ShippingInfoCheckoutRequest.Builder()
                                .shippingId(courierItemData.getShipperId())
                                .spId(courierItemData.getShipperProductId())
                                .ratesId(
                                        shipmentDetailData.getShippingCourierViewModels() != null ?
                                                shipmentDetailData.getShippingCourierViewModels().get(0).getRatesId() : ""
                                )
                                .checksum(courierItemData.getChecksum())
                                .ut(courierItemData.getUt())
                                .analyticsDataShippingCourierPrice(String.valueOf(courierItemData.getShipperPrice()))
                                .ratesFeature(ratesFeature)
                                .build())
                        .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                        .finsurance((shipmentDetailData.getUseInsurance() != null && shipmentDetailData.getUseInsurance()) ? 1 : 0)
                        .isOrderPriority((shipmentDetailData.isOrderPriority() != null && shipmentDetailData.isOrderPriority() ? 1 : 0))
                        .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                        .shopId(shipmentCartItemModel.getShopId())
                        .warehouseId(shipmentCartItemModel.getFulfillmentId())
                        .cartString(shipmentCartItemModel.getCartString())
                        .productData(convertToProductDataCheckout(shipmentCartItemModel));

                ArrayList<String> promoCodes = new ArrayList<>();
                List<PromoRequest> promoRequests = new ArrayList<>();

                if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                    promoCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    PromoRequest promoRequest = new PromoRequest();
                    promoRequest.setCode(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    promoRequest.setType(PromoRequest.TYPE_LOGISTIC);
                    promoRequests.add(promoRequest);
                }
                shopProductCheckoutBuilder.promos(promoRequests);

                if (promoCodes.size() > 0) {
                    shopProductCheckoutBuilder.promoCodes(promoCodes);
                }

                if (shipmentDetailData.getUseDropshipper() != null && shipmentDetailData.getUseDropshipper()) {
                    shopProductCheckoutBuilder.isDropship(1)
                            .dropshipData(new DropshipDataCheckoutRequest.Builder()
                                    .name(shipmentDetailData.getDropshipperName())
                                    .telpNo(shipmentDetailData.getDropshipperPhone())
                                    .build());
                } else {
                    shopProductCheckoutBuilder.isDropship(0);
                }

                return shopProductCheckoutBuilder.build();
            }
            return null;
        }
        return null;
    }

    public static RatesFeature generateRatesFeature(CourierItemData courierItemData) {
        RatesFeature result = new RatesFeature();
        OntimeDeliveryGuarantee otdg = new OntimeDeliveryGuarantee();
        if (courierItemData.getOntimeDelivery() != null) {
            otdg.setAvailable(courierItemData.getOntimeDelivery().getAvailable());
            otdg.setDuration(courierItemData.getOntimeDelivery().getValue());
        }
        result.setOntimeDeliveryGuarantee(otdg);
        return result;
    }

    private List<ProductDataCheckoutRequest> convertToProductDataCheckout(ShipmentCartItemModel shipmentCartItemModel) {
        List<ProductDataCheckoutRequest> productDataList = new ArrayList<>();
        for (CartItemModel cartItem : shipmentCartItemModel.getCartItemModels()) {
            productDataList.add(convertToProductDataCheckout(cartItem, shipmentCartItemModel.getSelectedShipmentDetailData()));
        }

        return productDataList;
    }

    private ProductDataCheckoutRequest convertToProductDataCheckout(CartItemModel cartItem, ShipmentDetailData shipmentDetailData) {
        String courierId = "";
        String serviceId = "";
        String shippingPrice = "";
        if (shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null) {
            courierId = String.valueOf(shipmentDetailData.getSelectedCourier().getShipperProductId());
            serviceId = String.valueOf(shipmentDetailData.getSelectedCourier().getServiceId());
            shippingPrice = String.valueOf(shipmentDetailData.getSelectedCourier().getShipperPrice());
        }

        return new ProductDataCheckoutRequest.Builder()
                .productId(cartItem.getProductId())
                .purchaseProtection(cartItem.isProtectionOptIn())
                .productName(cartItem.getAnalyticsProductCheckoutData().getProductName())
                .productPrice(cartItem.getAnalyticsProductCheckoutData().getProductPrice())
                .productBrand(cartItem.getAnalyticsProductCheckoutData().getProductBrand())
                .productCategory(cartItem.getAnalyticsProductCheckoutData().getProductCategory())
                .productVariant(cartItem.getAnalyticsProductCheckoutData().getProductVariant())
                .productQuantity(cartItem.getAnalyticsProductCheckoutData().getProductQuantity())
                .productShopId(cartItem.getAnalyticsProductCheckoutData().getProductShopId())
                .productShopType(cartItem.getAnalyticsProductCheckoutData().getProductShopType())
                .productShopName(cartItem.getAnalyticsProductCheckoutData().getProductShopName())
                .productCategoryId(cartItem.getAnalyticsProductCheckoutData().getProductCategoryId())
                .productListName(cartItem.getAnalyticsProductCheckoutData().getProductListName())
                .productAttribution(cartItem.getAnalyticsProductCheckoutData().getProductAttribution())
                .cartId(cartItem.getCartId())
                .warehouseId(cartItem.getAnalyticsProductCheckoutData().getWarehouseId())
                .productWeight(cartItem.getAnalyticsProductCheckoutData().getProductWeight())
                .promoCode(cartItem.getAnalyticsProductCheckoutData().getPromoCode())
                .promoDetails(cartItem.getAnalyticsProductCheckoutData().getPromoDetails())
                .buyerAddressId(cartItem.getAnalyticsProductCheckoutData().getBuyerAddressId())
                .shippingDuration(serviceId)
                .courier(courierId)
                .shippingPrice(shippingPrice)
                .codFlag(cartItem.getAnalyticsProductCheckoutData().getCodFlag())
                .tokopediaCornerFlag(cartItem.getAnalyticsProductCheckoutData().getTokopediaCornerFlag())
                .isFulfillment(cartItem.getAnalyticsProductCheckoutData().isFulfillment())
                .setDiscountedPrice(cartItem.getAnalyticsProductCheckoutData().isDiscountedPrice())
                .isFreeShipping(cartItem.isFreeShipping())
                .isFreeShippingExtra(cartItem.isFreeShippingExtra())
                .campaignId(cartItem.getAnalyticsProductCheckoutData().getCampaignId())
                .build();
    }

    private List<DataCheckoutRequest> createCheckoutRequestData(
            List<ShopProductCheckoutRequest> shopProducts,
            RecipientAddressModel recipientAddress) {

        String addressId = getSelectedAddressId(recipientAddress);
        List<DataCheckoutRequest> checkoutRequestData = new ArrayList<>();
        checkoutRequestData.add(new DataCheckoutRequest.Builder()
                .addressId(addressId)
                .shopProducts(shopProducts)
                .build());

        return checkoutRequestData;
    }

    private String getSelectedAddressId(RecipientAddressModel recipientAddress) {
        if (recipientAddress != null) {
            if (recipientAddress.getSelectedTabIndex() == 1 && recipientAddress.getLocationDataModel() != null) {
                return recipientAddress.getLocationDataModel().getAddrId();
            } else {
                return recipientAddress.getId();
            }
        }
        return "0";
    }

}
