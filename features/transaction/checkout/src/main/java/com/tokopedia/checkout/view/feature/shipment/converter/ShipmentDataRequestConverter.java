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

    public ShipmentAdapter.RequestData generateRequestData(
            List<ShipmentCartItemModel> shipmentCartItemModels, RecipientAddressModel recipientAddress) {
        ShipmentAdapter.RequestData requestData = new ShipmentAdapter.RequestData();
        if (shipmentCartItemModels != null && shipmentCartItemModels.size() > 0) {
            List<ShopProductCheckoutRequest> shopProductCheckoutRequestList = new ArrayList<>();
            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProductPromoRequest = new ArrayList<>();
            if (recipientAddress != null) {
                List<DataChangeAddressRequest> changeAddressRequestData = new ArrayList<>();
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    shopProductCheckoutRequestList.add(getProductCheckoutRequest(shipmentCartItemModel));
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
                            .build())
                    .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                    .finsurance((shipmentDetailData.getUseInsurance() != null && shipmentDetailData.getUseInsurance()) ? 1 : 0)
                    .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                    .shopId(shipmentCartItemModel.getShopId())
                    .productData(convertToProductDataCheckout(shipmentCartItemModel.getCartItemModels()));

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

    private List<ProductDataCheckoutRequest> convertToProductDataCheckout(List<CartItemModel> cartItems) {
        List<ProductDataCheckoutRequest> productDataList = new ArrayList<>();
        for (CartItemModel cartItem : cartItems) {
            productDataList.add(convertToProductDataCheckout(cartItem));
        }

        return productDataList;
    }

    private ProductDataCheckoutRequest convertToProductDataCheckout(CartItemModel cartItem) {
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
