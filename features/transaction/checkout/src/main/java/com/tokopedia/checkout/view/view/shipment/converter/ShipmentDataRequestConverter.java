package com.tokopedia.checkout.view.view.shipment.converter;

import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.DropshipDataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ShippingInfoCheckoutRequest;
import com.tokopedia.transactiondata.entity.request.ShopProductCheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItem;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;

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
            List<ShipmentCartItem> shipmentCartItems, RecipientAddressModel recipientAddress) {

        List<ShopProductCheckoutRequest> shopProductCheckoutRequest = new ArrayList<>();
        List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProductPromoRequest = new ArrayList<>();

        for (ShipmentCartItem shipmentCartItem : shipmentCartItems) {
            shopProductCheckoutRequest.add(getProductCheckoutRequest(shipmentCartItem));
            shopProductPromoRequest.add(getShopProductPromoRequest(shipmentCartItem));
        }

        ShipmentAdapter.RequestData requestData = new ShipmentAdapter.RequestData();

        requestData.setCheckoutRequestData(createCheckoutRequestData(shopProductCheckoutRequest, recipientAddress));
        requestData.setPromoRequestData(createPromoRequestData(shopProductPromoRequest, recipientAddress));

        return requestData;
    }

    private ShopProductCheckoutRequest getProductCheckoutRequest(ShipmentCartItem shipmentCartItem) {
        ShipmentDetailData shipmentDetailData = shipmentCartItem.getSelectedShipmentDetailData();
        CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

        // Create shop product model for shipment
        ShopProductCheckoutRequest.Builder shopProductCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                .shippingInfo(new ShippingInfoCheckoutRequest.Builder()
                        .shippingId(courierItemData.getShipperId())
                        .spId(courierItemData.getShipperProductId())
                        .build())
                .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                .finsurance(shipmentDetailData.getUseInsurance() != null && shipmentDetailData.getUseInsurance() ? 1 : 0)
                .isPreorder(shipmentCartItem.isProductIsPreorder() ? 1 : 0)
                .shopId(shipmentCartItem.getShopId());

        if (shipmentCartItem instanceof ShipmentSingleAddressCartItem) {
            shopProductCheckoutBuilder.productData(convertToProductDataCheckout(
                    ((ShipmentSingleAddressCartItem) shipmentCartItem).getCartItemModels()));
        } else if (shipmentCartItem instanceof ShipmentMultipleAddressCartItem) {
            List<ProductDataCheckoutRequest> productDataCheckoutRequests = new ArrayList<>();
            ProductDataCheckoutRequest.Builder productDataCheckoutRequest = new ProductDataCheckoutRequest.Builder();
            productDataCheckoutRequests.add(
                    productDataCheckoutRequest.productId(
                            Integer.parseInt(((ShipmentMultipleAddressCartItem) shipmentCartItem)
                                    .getMultipleAddressItemData().getProductId()))
                            .build());

            shopProductCheckoutBuilder.productData(productDataCheckoutRequests);
        }

        if (shipmentDetailData.getUseDropshipper()) {
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

    private CheckPromoCodeCartShipmentRequest.ShopProduct getShopProductPromoRequest(ShipmentCartItem shipmentCartItem) {
        ShipmentDetailData shipmentDetailData = shipmentCartItem.getSelectedShipmentDetailData();
        CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

        // Create shop product model for promo request
        CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProductCheckPromoBuilder =
                new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder()
                        .shopId(shipmentCartItem.getShopId())
                        .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                        .finsurance(shipmentDetailData.getUseInsurance() != null &&
                                shipmentDetailData.getUseInsurance() ? 1 : 0)
                        .isPreorder(shipmentCartItem.isProductIsPreorder() ? 1 : 0)
                        .shippingInfo(new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder()
                                .shippingId(courierItemData.getShipperId())
                                .spId(courierItemData.getShipperProductId())
                                .build());

        if (shipmentCartItem instanceof ShipmentSingleAddressCartItem) {
            shopProductCheckPromoBuilder.productData(convertToProductData(
                    ((ShipmentSingleAddressCartItem) shipmentCartItem).getCartItemModels()));
        } else if (shipmentCartItem instanceof ShipmentMultipleAddressCartItem) {
            List<CheckPromoCodeCartShipmentRequest.ProductData> productDataList = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ProductData.Builder productData =
                    new CheckPromoCodeCartShipmentRequest.ProductData.Builder()
                            .productId(Integer.parseInt(((ShipmentMultipleAddressCartItem) shipmentCartItem).getMultipleAddressItemData().getProductId()))
                            .productNotes(((ShipmentMultipleAddressCartItem) shipmentCartItem).getMultipleAddressItemData().getProductNotes())
                            .productQuantity(
                                    Integer.parseInt(((ShipmentMultipleAddressCartItem) shipmentCartItem).getMultipleAddressItemData().getProductQty())
                            );
            productDataList.add(productData.build());
            shopProductCheckPromoBuilder.productData(productDataList);
        }

        if (shipmentDetailData.getUseDropshipper()) {
            shopProductCheckPromoBuilder.dropshipData(new CheckPromoCodeCartShipmentRequest.DropshipData.Builder()
                    .name(shipmentDetailData.getDropshipperName())
                    .telpNo(shipmentDetailData.getDropshipperPhone())
                    .build());
        }

        return shopProductCheckPromoBuilder.build();
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
                .productId(cartItem.getId())
                .build();
    }

    private List<DataCheckoutRequest> createCheckoutRequestData(
            List<ShopProductCheckoutRequest> shopProducts,
            RecipientAddressModel recipientAddress) {

        List<DataCheckoutRequest> checkoutRequestData = new ArrayList<>();
        checkoutRequestData.add(new DataCheckoutRequest.Builder()
                .addressId(Integer.valueOf(recipientAddress.getId()))
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
                .productId(cartItem.getId())
                .productNotes(cartItem.getNoteToSeller())
                .productQuantity(cartItem.getQuantity())
                .build();
    }

    private List<CheckPromoCodeCartShipmentRequest.Data> createPromoRequestData(
            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts,
            RecipientAddressModel recipientAddress) {

        List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData = new ArrayList<>();
        promoRequestData.add(new CheckPromoCodeCartShipmentRequest.Data.Builder()
                .addressId(Integer.valueOf(recipientAddress.getId()))
                .shopProducts(shopProducts)
                .build());

        return promoRequestData;
    }

}