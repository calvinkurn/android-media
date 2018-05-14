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
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItemModel;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItemModel;
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

    //For single address
    public ShipmentAdapter.RequestData generateRequestData(
            List<ShipmentCartItemModel> shipmentCartItemModels, RecipientAddressModel recipientAddress) {

        List<ShopProductCheckoutRequest> shopProductCheckoutRequest = new ArrayList<>();
        List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProductPromoRequest = new ArrayList<>();

//        if(recipientAddress != null) {
            for (ShipmentCartItemModel shipmentCartItemModel : shipmentCartItemModels) {
                shopProductCheckoutRequest.add(getProductCheckoutRequest(shipmentCartItemModel));
                shopProductPromoRequest.add(getShopProductPromoRequest(shipmentCartItemModel));
            }
//        } else {
//            shopProductCheckoutRequest.add(getPromoCodeRequest());
//        }

        ShipmentAdapter.RequestData requestData = new ShipmentAdapter.RequestData();
        requestData.setCheckoutRequestData(createCheckoutRequestData(shopProductCheckoutRequest, recipientAddress));
        requestData.setPromoRequestData(createPromoRequestData(shopProductPromoRequest, recipientAddress));

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
                .shopId(shipmentCartItemModel.getShopId());

        if (shipmentCartItemModel instanceof ShipmentSingleAddressCartItemModel) {
            shopProductCheckoutBuilder.productData(convertToProductDataCheckout(
                    ((ShipmentSingleAddressCartItemModel) shipmentCartItemModel).getCartItemModels()));
        } else if (shipmentCartItemModel instanceof ShipmentMultipleAddressCartItemModel) {
            List<ProductDataCheckoutRequest> productDataCheckoutRequests = new ArrayList<>();
            ProductDataCheckoutRequest.Builder productDataCheckoutRequest = new ProductDataCheckoutRequest.Builder();
            productDataCheckoutRequests.add(
                    productDataCheckoutRequest.productId(
                            Integer.parseInt(((ShipmentMultipleAddressCartItemModel) shipmentCartItemModel)
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
                        .shippingInfo(new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder()
                                .shippingId(courierItemData.getShipperId())
                                .spId(courierItemData.getShipperProductId())
                                .build());

        if (shipmentCartItemModel instanceof ShipmentSingleAddressCartItemModel) {
            shopProductCheckPromoBuilder.productData(convertToProductData(
                    ((ShipmentSingleAddressCartItemModel) shipmentCartItemModel).getCartItemModels()));
        } else if (shipmentCartItemModel instanceof ShipmentMultipleAddressCartItemModel) {


            List<CheckPromoCodeCartShipmentRequest.ProductData> productDataList = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ProductData.Builder productData =
                    new CheckPromoCodeCartShipmentRequest.ProductData.Builder()
                            .productId(Integer.parseInt(((ShipmentMultipleAddressCartItemModel) shipmentCartItemModel).getMultipleAddressItemData().getProductId()))
                            .productNotes(((ShipmentMultipleAddressCartItemModel) shipmentCartItemModel).getMultipleAddressItemData().getProductNotes())
                            .productQuantity(
                                    Integer.parseInt(((ShipmentMultipleAddressCartItemModel) shipmentCartItemModel).getMultipleAddressItemData().getProductQty())
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



    ////// Multiple
    private List<CheckPromoCodeCartShipmentRequest.Data> getPromoCodeRequest(List<ShipmentMultipleAddressCartItemModel> shipmentData){
        CheckPromoCodeCartShipmentRequest.Builder checkoutPromoRequest =
                new CheckPromoCodeCartShipmentRequest.Builder();

        List<CheckPromoCodeCartShipmentRequest.Data> orderDatas = new ArrayList<>();

        for (int i = 0; i < shipmentData.size(); i++) {
            CheckPromoCodeCartShipmentRequest.Data.Builder orderData =
                    new CheckPromoCodeCartShipmentRequest.Data.Builder();
            orderData.addressId(Integer.parseInt(shipmentData.get(i).getMultipleAddressItemData().getAddressId()));

            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProduct =
                    new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder();

            List<CheckPromoCodeCartShipmentRequest.ProductData> productDatas = new ArrayList<>();
            CheckPromoCodeCartShipmentRequest.ProductData.Builder productData =
                    new CheckPromoCodeCartShipmentRequest.ProductData.Builder();
            productData
                    .productId(Integer.parseInt(shipmentData.get(i).getMultipleAddressItemData().getProductId()))
                    .productNotes(shipmentData.get(i).getMultipleAddressItemData().getProductNotes())
                    .productQuantity(
                            Integer.parseInt(shipmentData.get(i).getMultipleAddressItemData().getProductQty()
                            )
                    );
            productDatas.add(productData.build());

            CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder shipmentInfo =
                    new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder();
            shipmentInfo
                    .shippingId(shipmentData.get(i).getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperId())
                    .spId(shipmentData.get(i).getSelectedShipmentDetailData()
                            .getSelectedCourier().getShipperProductId());

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

            orderDatas.add(orderData.build());
        }

        return orderDatas;
    }

}