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
        ShippingInfoCheckoutRequest shippingInfoCheckoutRequest = new ShippingInfoCheckoutRequest();
        shippingInfoCheckoutRequest.setShippingId(0);
        shippingInfoCheckoutRequest.setSpId(0);
        shippingInfoCheckoutRequest.setRatesId("");
        shippingInfoCheckoutRequest.setChecksum("");
        shippingInfoCheckoutRequest.setUt("");
        shippingInfoCheckoutRequest.setAnalyticsDataShippingCourierPrice("");

        ShopProductCheckoutRequest shopProductCheckout = new ShopProductCheckoutRequest();
        shopProductCheckout.setShippingInfo(shippingInfoCheckoutRequest);
        shopProductCheckout.setFcancelPartial(0);
        shopProductCheckout.setFinsurance(0);
        shopProductCheckout.setOrderPriority(0);
        shopProductCheckout.setPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0);
        shopProductCheckout.setShopId(shipmentCartItemModel.getShopId());
        shopProductCheckout.setWarehouseId(shipmentCartItemModel.getFulfillmentId());
        shopProductCheckout.setCartString(shipmentCartItemModel.getCartString());
        shopProductCheckout.setProductData(convertToProductDataCheckout(shipmentCartItemModel));

        return shopProductCheckout;
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
                ShippingInfoCheckoutRequest shippingInfoCheckoutRequest = new ShippingInfoCheckoutRequest();
                shippingInfoCheckoutRequest.setShippingId(courierItemData.getShipperId());
                shippingInfoCheckoutRequest.setSpId(courierItemData.getShipperProductId());
                shippingInfoCheckoutRequest.setRatesId(
                        shipmentDetailData.getShippingCourierViewModels() != null ?
                                shipmentDetailData.getShippingCourierViewModels().get(0).getRatesId() : ""
                );
                shippingInfoCheckoutRequest.setChecksum(courierItemData.getChecksum());
                shippingInfoCheckoutRequest.setUt(courierItemData.getUt());
                shippingInfoCheckoutRequest.setAnalyticsDataShippingCourierPrice(String.valueOf(courierItemData.getShipperPrice()));
                shippingInfoCheckoutRequest.setRatesFeature(ratesFeature);

                ShopProductCheckoutRequest shopProductCheckout = new ShopProductCheckoutRequest();
                shopProductCheckout.setShippingInfo(shippingInfoCheckoutRequest);
                shopProductCheckout.setFcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0);
                shopProductCheckout.setFinsurance((shipmentDetailData.getUseInsurance() != null && shipmentDetailData.getUseInsurance()) ? 1 : 0);
                shopProductCheckout.setOrderPriority((shipmentDetailData.isOrderPriority() != null && shipmentDetailData.isOrderPriority() ? 1 : 0));
                shopProductCheckout.setPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0);
                shopProductCheckout.setShopId(shipmentCartItemModel.getShopId());
                shopProductCheckout.setWarehouseId(shipmentCartItemModel.getFulfillmentId());
                shopProductCheckout.setCartString(shipmentCartItemModel.getCartString());
                shopProductCheckout.setProductData(convertToProductDataCheckout(shipmentCartItemModel));

                ArrayList<String> promoCodes = new ArrayList<>();
                List<PromoRequest> promoRequests = new ArrayList<>();

                if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                    promoCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    PromoRequest promoRequest = new PromoRequest();
                    promoRequest.setCode(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
                    promoRequest.setType(PromoRequest.TYPE_LOGISTIC);
                    promoRequests.add(promoRequest);
                }
                shopProductCheckout.setPromos(promoRequests);

                if (promoCodes.size() > 0) {
                    shopProductCheckout.setPromoCodes(promoCodes);
                }

                if (shipmentDetailData.getUseDropshipper() != null && shipmentDetailData.getUseDropshipper()) {
                    DropshipDataCheckoutRequest dropshipDataCheckoutRequest = new DropshipDataCheckoutRequest();
                    dropshipDataCheckoutRequest.setName(shipmentDetailData.getDropshipperName());
                    dropshipDataCheckoutRequest.setTelpNo(shipmentDetailData.getDropshipperPhone());
                    shopProductCheckout.setDropship(1);
                    shopProductCheckout.setDropshipData(dropshipDataCheckoutRequest);
                } else {
                    shopProductCheckout.setDropship(0);
                }

                return shopProductCheckout;
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

        ProductDataCheckoutRequest productDataCheckoutRequest = new ProductDataCheckoutRequest();
        productDataCheckoutRequest.setProductId(cartItem.getProductId());
        productDataCheckoutRequest.setPurchaseProtection(cartItem.isProtectionOptIn());
        productDataCheckoutRequest.setProductName(cartItem.getAnalyticsProductCheckoutData().getProductName());
        productDataCheckoutRequest.setProductPrice(cartItem.getAnalyticsProductCheckoutData().getProductPrice());
        productDataCheckoutRequest.setProductBrand(cartItem.getAnalyticsProductCheckoutData().getProductBrand());
        productDataCheckoutRequest.setProductCategory(cartItem.getAnalyticsProductCheckoutData().getProductCategory());
        productDataCheckoutRequest.setProductVariant(cartItem.getAnalyticsProductCheckoutData().getProductVariant());
        productDataCheckoutRequest.setProductQuantity(cartItem.getAnalyticsProductCheckoutData().getProductQuantity());
        productDataCheckoutRequest.setProductShopId(cartItem.getAnalyticsProductCheckoutData().getProductShopId());
        productDataCheckoutRequest.setProductShopType(cartItem.getAnalyticsProductCheckoutData().getProductShopType());
        productDataCheckoutRequest.setProductShopName(cartItem.getAnalyticsProductCheckoutData().getProductShopName());
        productDataCheckoutRequest.setProductCategoryId(cartItem.getAnalyticsProductCheckoutData().getProductCategoryId());
        productDataCheckoutRequest.setProductListName(cartItem.getAnalyticsProductCheckoutData().getProductListName());
        productDataCheckoutRequest.setProductAttribution(cartItem.getAnalyticsProductCheckoutData().getProductAttribution());
        productDataCheckoutRequest.setCartId(cartItem.getCartId());
        productDataCheckoutRequest.setWarehouseId(cartItem.getAnalyticsProductCheckoutData().getWarehouseId());
        productDataCheckoutRequest.setProductWeight(cartItem.getAnalyticsProductCheckoutData().getProductWeight());
        productDataCheckoutRequest.setPromoCode(cartItem.getAnalyticsProductCheckoutData().getPromoCode());
        productDataCheckoutRequest.setPromoDetails(cartItem.getAnalyticsProductCheckoutData().getPromoDetails());
        productDataCheckoutRequest.setBuyerAddressId(cartItem.getAnalyticsProductCheckoutData().getBuyerAddressId());
        productDataCheckoutRequest.setShippingDuration(serviceId);
        productDataCheckoutRequest.setCourier(courierId);
        productDataCheckoutRequest.setShippingPrice(shippingPrice);
        productDataCheckoutRequest.setCodFlag(cartItem.getAnalyticsProductCheckoutData().getCodFlag());
        productDataCheckoutRequest.setTokopediaCornerFlag(cartItem.getAnalyticsProductCheckoutData().getTokopediaCornerFlag());
        productDataCheckoutRequest.setFulfillment(cartItem.getAnalyticsProductCheckoutData().isFulfillment());
        productDataCheckoutRequest.setDiscountedPrice(cartItem.getAnalyticsProductCheckoutData().isDiscountedPrice());
        productDataCheckoutRequest.setFreeShipping(cartItem.isFreeShipping());
        productDataCheckoutRequest.setFreeShippingExtra(cartItem.isFreeShippingExtra());
        productDataCheckoutRequest.setCampaignId(cartItem.getAnalyticsProductCheckoutData().getCampaignId());

        return productDataCheckoutRequest;
    }

    private List<DataCheckoutRequest> createCheckoutRequestData(
            List<ShopProductCheckoutRequest> shopProducts,
            RecipientAddressModel recipientAddress) {

        String addressId = getSelectedAddressId(recipientAddress);
        List<DataCheckoutRequest> checkoutRequestData = new ArrayList<>();
        DataCheckoutRequest dataCheckoutRequest = new DataCheckoutRequest();
        dataCheckoutRequest.setAddressId(addressId);
        dataCheckoutRequest.setShopProducts(shopProducts);
        checkoutRequestData.add(dataCheckoutRequest);

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
