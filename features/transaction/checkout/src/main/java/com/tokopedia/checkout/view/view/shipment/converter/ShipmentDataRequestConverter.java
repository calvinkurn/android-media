package com.tokopedia.checkout.view.view.shipment.converter;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.view.shipment.ShipmentAdapter;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItemModel;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
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

        List<ShopProductCheckoutRequest> shopProductCheckoutRequest = new ArrayList<>();

        ShipmentAdapter.RequestData requestData = new ShipmentAdapter.RequestData();
        if (shipmentCartItemModels != null && shipmentCartItemModels.size() > 0) {
            if (shipmentCartItemModels.get(0) instanceof ShipmentSingleAddressCartItemModel) {
                List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProductPromoRequest = new ArrayList<>();
                for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                    shopProductCheckoutRequest.add(getProductCheckoutRequest(shipmentCartItemModel));
                    shopProductPromoRequest.add(getShopProductPromoRequest(shipmentCartItemModel));
                }
                requestData.setCheckoutRequestData(createCheckoutRequestData(shopProductCheckoutRequest, recipientAddress));
                requestData.setPromoRequestData(createPromoRequestData(shopProductPromoRequest, recipientAddress));
            } else {
                requestData.setPromoRequestData(createPromoCodeRequestData(shipmentCartItemModels));
                requestData.setCheckoutRequestData(createCheckoutRequestData(shipmentCartItemModels));
            }
        }

        return requestData;
    }

    private ShopProductCheckoutRequest getProductCheckoutRequest(ShipmentCartItemModel shipmentCartItemModel) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

        // Create shop product model for shipment
        ShopProductCheckoutRequest.Builder shopProductCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                .shippingInfo(new ShippingInfoCheckoutRequest.Builder()
                        .shippingId(courierItemData.getShipperId())
                        .spId(courierItemData.getShipperProductId())
                        .build())
                .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                .finsurance((shipmentDetailData.getUseInsurance() != null && shipmentDetailData.getUseInsurance()) ? 1 : 0)
                .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                .shopId(shipmentCartItemModel.getShopId())
                .productData(convertToProductDataCheckout(
                        ((ShipmentSingleAddressCartItemModel) shipmentCartItemModel).getCartItemModels()));

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

    private CheckPromoCodeCartShipmentRequest.ShopProduct getShopProductPromoRequest(ShipmentCartItemModel shipmentCartItemModel) {
        ShipmentDetailData shipmentDetailData = shipmentCartItemModel.getSelectedShipmentDetailData();
        CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

        // Create shop product model for promo request
        CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProductCheckPromoBuilder =
                new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder()
                        .shopId(shipmentCartItemModel.getShopId())
                        .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0)
                        .finsurance((shipmentDetailData.getUseInsurance() != null &&
                                shipmentDetailData.getUseInsurance()) ? 1 : 0)
                        .isPreorder(shipmentCartItemModel.isProductIsPreorder() ? 1 : 0)
                        .productData(convertToProductData(
                                ((ShipmentSingleAddressCartItemModel) shipmentCartItemModel).getCartItemModels()))
                        .shippingInfo(new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder()
                                .shippingId(courierItemData.getShipperId())
                                .spId(courierItemData.getShipperProductId())
                                .build());

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
                .addressId(recipientAddress != null ? Integer.valueOf(recipientAddress.getId()) : 0)
                .shopProducts(shopProducts)
                .build());

        return promoRequestData;
    }

    private List<CheckPromoCodeCartShipmentRequest.Data> createPromoCodeRequestData(List<ShipmentCartItemModel> shipmentData) {
        List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData = new ArrayList<>();

        for (int i = 0; i < shipmentData.size(); i++) {
            CheckPromoCodeCartShipmentRequest.Data.Builder orderData =
                    new CheckPromoCodeCartShipmentRequest.Data.Builder();
            ShipmentMultipleAddressCartItemModel shipmentMultipleAddressCartItemModel =
                    ((ShipmentMultipleAddressCartItemModel) shipmentData.get(i));
            orderData.addressId(Integer.parseInt(shipmentMultipleAddressCartItemModel.getMultipleAddressItemData().getAddressId()));

            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProduct =
                    new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder();

            List<CheckPromoCodeCartShipmentRequest.ProductData> productDatas = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ProductData.Builder productData =
                    new CheckPromoCodeCartShipmentRequest.ProductData.Builder();
            productData.productId(Integer.parseInt(shipmentMultipleAddressCartItemModel.getMultipleAddressItemData().getProductId()))
                    .productNotes(shipmentMultipleAddressCartItemModel.getMultipleAddressItemData().getProductNotes())
                    .productQuantity(Integer.parseInt(shipmentMultipleAddressCartItemModel.getMultipleAddressItemData().getProductQty()));

            productDatas.add(productData.build());

            CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder shipmentInfo =
                    new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder();
            shipmentInfo.shippingId(shipmentData.get(i).getSelectedShipmentDetailData()
                    .getSelectedCourier().getShipperId())
                    .spId(shipmentData.get(i).getSelectedShipmentDetailData().getSelectedCourier().getShipperProductId());

            shopProduct.productData(productDatas)
                    .shopId(shipmentData.get(i).getShopId())
                    .fcancelPartial(shipmentData.get(i).getSelectedShipmentDetailData().getUsePartialOrder() ? 1 : 0)
                    .finsurance((shipmentData.get(i).getSelectedShipmentDetailData().getUseInsurance() != null &&
                            shipmentData.get(i).getSelectedShipmentDetailData().getUseInsurance()) ? 1 : 0)
                    .isPreorder(shipmentData.get(i).isProductIsPreorder() ? 1 : 0)
                    .shippingInfo(shipmentInfo.build());

            if (shipmentData.get(i).getSelectedShipmentDetailData().getUseDropshipper()) {
                CheckPromoCodeCartShipmentRequest.DropshipData.Builder dropshipData =
                        new CheckPromoCodeCartShipmentRequest.DropshipData.Builder();

                dropshipData
                        .name(shipmentData.get(i).getSelectedShipmentDetailData()
                                .getDropshipperName())
                        .telpNo(shipmentData.get(i).getSelectedShipmentDetailData()
                                .getDropshipperPhone());
            }

            shopProducts.add(shopProduct.build());
            orderData.shopProducts(shopProducts);
            promoRequestData.add(orderData.build());
        }

        return promoRequestData;
    }

    private List<DataCheckoutRequest> createCheckoutRequestData(List<ShipmentCartItemModel> shipmentCartItemModels) {
        List<DataCheckoutRequest> dataCheckoutRequests = new ArrayList<>();
        for (int i = 0; i < shipmentCartItemModels.size(); i++) {
            ShipmentMultipleAddressCartItemModel currentShipmentAdapterData =
                    (ShipmentMultipleAddressCartItemModel) shipmentCartItemModels.get(i);
            ShipmentDetailData currentShipmentDetailData = currentShipmentAdapterData
                    .getSelectedShipmentDetailData();

            DataCheckoutRequest.Builder checkoutData = new DataCheckoutRequest.Builder();

            List<ProductDataCheckoutRequest> productDataCheckoutRequests = new ArrayList<>();
            ProductDataCheckoutRequest.Builder productDataCheckoutRequest =
                    new ProductDataCheckoutRequest.Builder();
            productDataCheckoutRequests.add(productDataCheckoutRequest
                    .productId(Integer.parseInt(
                            currentShipmentAdapterData.getMultipleAddressItemData().getProductId())
                    ).build());

            ShopProductCheckoutRequest.Builder shopCheckoutBuilder;
            shopCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                    .productData(productDataCheckoutRequests)
                    .shippingInfo(setShippingInfoRequest(currentShipmentDetailData))
                    .fcancelPartial(currentShipmentDetailData.getUsePartialOrder() ? 1 : 0)
                    .finsurance(currentShipmentDetailData.getUseInsurance() != null &&
                            currentShipmentDetailData.getUseInsurance() ? 1 : 0)
                    .isPreorder(currentShipmentAdapterData.isProductIsPreorder() ? 1 : 0)
                    .isDropship(currentShipmentDetailData.getUseDropshipper() ? 1 : 0)
                    .shopId(currentShipmentAdapterData.getShopId());

            if (currentShipmentDetailData.getUseDropshipper()) {
                shopCheckoutBuilder.dropshipData(setDropshipDataCheckoutRequest(currentShipmentDetailData));
            }

            List<ShopProductCheckoutRequest> shopCheckoutRequests = new ArrayList<>();
            shopCheckoutRequests.add(shopCheckoutBuilder.build());

            checkoutData.addressId(Integer
                    .parseInt(currentShipmentAdapterData.getMultipleAddressItemData().getAddressId()))
                    .shopProducts(shopCheckoutRequests).build();
            dataCheckoutRequests.add(checkoutData.build());
        }
        return dataCheckoutRequests;
    }

    private DropshipDataCheckoutRequest setDropshipDataCheckoutRequest(ShipmentDetailData data) {
        return new DropshipDataCheckoutRequest.Builder()
                .name(data.getDropshipperName())
                .telpNo(data.getDropshipperPhone()).build();
    }

    private ShippingInfoCheckoutRequest setShippingInfoRequest(ShipmentDetailData data) {
        return new ShippingInfoCheckoutRequest.Builder()
                .shippingId(data.getSelectedCourier().getShipperId())
                .spId(data.getSelectedCourier().getShipperProductId())
                .build();
    }


}