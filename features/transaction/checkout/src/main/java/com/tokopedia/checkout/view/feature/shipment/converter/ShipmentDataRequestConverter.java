package com.tokopedia.checkout.view.feature.shipment.converter;

import com.tokopedia.checkout.view.feature.shipment.adapter.ShipmentAdapter;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataChangeAddressRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.DropshipDataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ShippingInfoCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ShopProductCheckoutRequest;

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
                                                           boolean isAnalyticsPurpose) {
        ShipmentAdapter.RequestData requestData = new ShipmentAdapter.RequestData();
        if (shipmentCartItemModels != null && shipmentCartItemModels.size() > 0) {
            List<ShopProductCheckoutRequest> shopProductCheckoutRequestList = new ArrayList<>();
            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProductPromoRequest = new ArrayList<>();
            if (recipientAddress != null) {
                List<DataChangeAddressRequest> changeAddressRequestData = new ArrayList<>();
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    if (shipmentCartItemModel.getSelectedShipmentDetailData() != null) {
                        shopProductCheckoutRequestList.add(getProductCheckoutRequest(shipmentCartItemModel));
                    } else if (isAnalyticsPurpose) {
                        shopProductCheckoutRequestList.add(getProductCheckoutRequestForAnalytics(shipmentCartItemModel));
                    }
                    shopProductPromoRequest.add(getShopProductPromoRequest(shipmentCartItemModel));
                    createChangeAddressRequestData(changeAddressRequestData, shipmentCartItemModel, recipientAddress);
                }
                requestData.setCheckoutRequestData(createCheckoutRequestData(shopProductCheckoutRequestList, recipientAddress));
                requestData.setPromoRequestData(createPromoRequestData(shopProductPromoRequest, recipientAddress));
                requestData.setChangeAddressRequestData(changeAddressRequestData);
            } else {
                List<DataCheckoutRequest> checkoutRequestData = new ArrayList<>();
                List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData = new ArrayList<>();
                List<DataChangeAddressRequest> changeAddressRequestData = new ArrayList<>();
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    shopProductCheckoutRequestList.add(getProductCheckoutRequest(shipmentCartItemModel));
                    shopProductPromoRequest.add(getShopProductPromoRequest(shipmentCartItemModel));
                    createChangeAddressRequestData(changeAddressRequestData, shipmentCartItemModel, null);
                }

                for (int i = 0; i < shopProductCheckoutRequestList.size(); i++) {
                    DataCheckoutRequest.Builder dataCheckoutRequestBuilder = new DataCheckoutRequest.Builder();
                    List<ShopProductCheckoutRequest> shopProductCheckoutRequests = new ArrayList<>();
                    shopProductCheckoutRequests.add(shopProductCheckoutRequestList.get(i));
                    dataCheckoutRequestBuilder.shopProducts(shopProductCheckoutRequests);
                    dataCheckoutRequestBuilder.addressId(Integer.parseInt(shipmentCartItemModels.get(i).getRecipientAddressModel().getId()));
                    checkoutRequestData.add(dataCheckoutRequestBuilder.build());
                }

                for (int i = 0; i < shopProductPromoRequest.size(); i++) {
                    CheckPromoCodeCartShipmentRequest.Data.Builder promoDataBuilder = new CheckPromoCodeCartShipmentRequest.Data.Builder();
                    List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts = new ArrayList<>();
                    shopProducts.add(shopProductPromoRequest.get(i));
                    promoDataBuilder.addressId(Integer.parseInt(shipmentCartItemModels.get(i).getRecipientAddressModel().getId()));
                    promoDataBuilder.shopProducts(shopProducts);
                    promoRequestData.add(promoDataBuilder.build());
                }

                requestData.setCheckoutRequestData(checkoutRequestData);
                requestData.setPromoRequestData(promoRequestData);
                requestData.setChangeAddressRequestData(changeAddressRequestData);
            }
        }

        return requestData;
    }

    private void createChangeAddressRequestData(List<DataChangeAddressRequest> changeAddressRequestData,
                                                ShipmentCartItemModel shipmentCartItemModel,
                                                RecipientAddressModel recipientAddressModel) {
        for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
            DataChangeAddressRequest dataChangeAddressRequest = new DataChangeAddressRequest();
            dataChangeAddressRequest.setQuantity(cartItemModel.getQuantity());
            dataChangeAddressRequest.setProductId(cartItemModel.getProductId());
            dataChangeAddressRequest.setNotes(cartItemModel.getNoteToSeller());
            dataChangeAddressRequest.setAddressId(recipientAddressModel != null ?
                    Integer.parseInt(recipientAddressModel.getId()) :
                    Integer.parseInt(shipmentCartItemModel.getRecipientAddressModel().getId()));
            dataChangeAddressRequest.setCartId(cartItemModel.getCartId());
            changeAddressRequestData.add(dataChangeAddressRequest);
        }
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

        ArrayList<String> promoCodes = new ArrayList<>();
        if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
            promoCodes.add(shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode());
        }

        if (promoCodes.size() > 0) {
            shopProductCheckoutBuilder.promoCodes(promoCodes);
        }

        return shopProductCheckoutBuilder.build();
    }

    private ShopProductCheckoutRequest getProductCheckoutRequest(ShipmentCartItemModel shipmentCartItemModel) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        if (shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null) {
            CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

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
            if (shipmentCartItemModel.getVoucherOrdersItemUiModel() != null) {
                promoCodes.add(shipmentCartItemModel.getVoucherOrdersItemUiModel().getCode());
            }

            if (shipmentCartItemModel.getVoucherLogisticItemUiModel() != null) {
                promoCodes.add(shipmentCartItemModel.getVoucherLogisticItemUiModel().getCode());
            }

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

    private CheckPromoCodeCartShipmentRequest.ShopProduct getShopProductPromoRequest(ShipmentCartItemModel shipmentCartItemModel) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        if (shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null) {
            CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

            // Create shop product model for promo request
            CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProductCheckPromoBuilder =
                    new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder()
                            .shopId(shipmentCartItemModel.getShopId())
                            .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                            .finsurance((shipmentDetailData.getUseInsurance() != null &&
                                    shipmentDetailData.getUseInsurance()) ? 1 : 0)
                            .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                            .productData(convertToProductData(shipmentCartItemModel.getCartItemModels()))
                            .shippingInfo(new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder()
                                    .shippingId(courierItemData.getShipperId())
                                    .spId(courierItemData.getShipperProductId())
                                    .build());

            if (shipmentDetailData.getUseDropshipper() != null && shipmentDetailData.getUseDropshipper()) {
                shopProductCheckPromoBuilder.dropshipData(new CheckPromoCodeCartShipmentRequest.DropshipData.Builder()
                        .name(shipmentDetailData.getDropshipperName())
                        .telpNo(shipmentDetailData.getDropshipperPhone())
                        .build());
            }

            return shopProductCheckPromoBuilder.build();
        }
        return null;
    }

    private List<ProductDataCheckoutRequest> convertToProductDataCheckout(ShipmentCartItemModel shipmentCartItemModel) {
        List<ProductDataCheckoutRequest> productDataList = new ArrayList<>();
        for (CartItemModel cartItem : shipmentCartItemModel.getCartItemModels()) {
            productDataList.add(convertToProductDataCheckout(cartItem, shipmentCartItemModel.getSelectedShipmentDetailData()));
        }

        return productDataList;
    }

    private ProductDataCheckoutRequest convertToProductDataCheckout(CartItemModel cartItem, ShipmentDetailData shipmentDetailData) {
        String courierName = "";
        String duration = "";
        String shippingPrice = "";
        if (shipmentDetailData != null && shipmentDetailData.getSelectedCourier() != null) {
            courierName = shipmentDetailData.getSelectedCourier().getName();
            duration = shipmentDetailData.getSelectedCourier().getEstimatedTimeDelivery();
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
                .shippingDuration(duration)
                .courier(courierName)
                .shippingPrice(shippingPrice)
                .codFlag(cartItem.getAnalyticsProductCheckoutData().getCodFlag())
                .tokopediaCornerFlag(cartItem.getAnalyticsProductCheckoutData().getTokopediaCornerFlag())
                .isFulfillment(cartItem.getAnalyticsProductCheckoutData().getIsFulfillment())
                .build();
    }

    private List<DataCheckoutRequest> createCheckoutRequestData(
            List<ShopProductCheckoutRequest> shopProducts,
            RecipientAddressModel recipientAddress) {

        List<DataCheckoutRequest> checkoutRequestData = new ArrayList<>();
        checkoutRequestData.add(new DataCheckoutRequest.Builder()
                .addressId(recipientAddress != null ? Integer.valueOf(recipientAddress.getId()) : 0)
                .shopProducts(shopProducts)
                .build());

        return checkoutRequestData;
    }

    private List<CheckPromoCodeCartShipmentRequest.ProductData> convertToProductData(List<CartItemModel> cartItems) {
        List<CheckPromoCodeCartShipmentRequest.ProductData> productDataList = new ArrayList<>();
        for (CartItemModel cartItem : cartItems) {
            productDataList.add(convertToProductData(cartItem));
        }

        return productDataList;
    }

    private CheckPromoCodeCartShipmentRequest.ProductData convertToProductData(CartItemModel cartItem) {
        return new CheckPromoCodeCartShipmentRequest.ProductData.Builder()
                .productId(cartItem.getProductId())
                .productNotes(cartItem.getNoteToSeller())
                .productQuantity(cartItem.getQuantity())
                .build();
    }

    private List<CheckPromoCodeCartShipmentRequest.Data> createPromoRequestData(
            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts,
            RecipientAddressModel recipientAddress) {

        List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData = new ArrayList<>();
        promoRequestData.add(new CheckPromoCodeCartShipmentRequest.Data.Builder()
                .addressId(recipientAddress != null ? Integer.valueOf(recipientAddress.getId()) : 0)
                .shopProducts(shopProducts)
                .build());

        return promoRequestData;
    }

}
