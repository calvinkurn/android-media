package com.tokopedia.purchase_platform.features.checkout.view.converter;

import android.text.TextUtils;

import com.tokopedia.logisticcart.shipping.model.CartItemModel;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.GroupShop;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Product;
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.UserAddress;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 25/04/18.
 */

public class RatesDataConverter {

    @Inject
    public RatesDataConverter() {

    }

    public ShipmentDetailData getShipmentDetailData(ShipmentCartItemModel shipmentCartItemModel,
                                                    RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        ShipmentCartData shipmentCartData = shipmentCartItemModel.getShipmentCartData();
        shipmentCartData.setDestinationAddress(recipientAddressModel.getStreet());
        shipmentCartData.setDestinationDistrictId(recipientAddressModel.getDestinationDistrictId());
        shipmentCartData.setDestinationLatitude(recipientAddressModel.getLatitude());
        shipmentCartData.setDestinationLongitude(recipientAddressModel.getLongitude());
        shipmentCartData.setDestinationPostalCode(recipientAddressModel.getPostalCode());
        shipmentDetailData.setShipmentCartData(shipmentCartData);
        int totalQuantity = 0;
        for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
            totalQuantity += cartItemModel.getQuantity();
        }
        shipmentDetailData.setTotalQuantity(totalQuantity);
        shipmentDetailData.setShopId(String.valueOf(shipmentCartItemModel.getShopId()));
        shipmentDetailData.setIsBlackbox(shipmentCartItemModel.getIsBlackbox());
        shipmentDetailData.setAddressId(shipmentCartItemModel.getAddressId());
        shipmentDetailData.setPreorder(shipmentCartItemModel.isProductIsPreorder());
        return shipmentDetailData;
    }

    public ShipmentCartData getShipmentCartData(UserAddress userAddress, GroupShop groupShop,
                                                ShipmentCartItemModel shipmentCartItemModel, String keroToken, String keroUnixTime) {
        ShipmentCartData shipmentCartData = new ShipmentCartData();
        initializeShipmentCartData(userAddress, groupShop, shipmentCartData, keroToken, keroUnixTime);
        int orderValue = 0;
        int totalWeight = 0;
        if (shipmentCartItemModel.getCartItemModels() != null) {
            for (CartItemModel cartItemModel : shipmentCartItemModel.getCartItemModels()) {
                orderValue += (cartItemModel.getQuantity() * cartItemModel.getPrice());
                totalWeight += (cartItemModel.getQuantity() * cartItemModel.getWeight());
            }
        }
        shipmentCartData.setOrderValue(orderValue);
        shipmentCartData.setWeight(totalWeight);

        return shipmentCartData;
    }

    private void initializeShipmentCartData(UserAddress userAddress, GroupShop groupShop,
                                            ShipmentCartData shipmentCartData, String keroToken,
                                            String keroUnixTime) {
        shipmentCartData.setToken(keroToken);
        shipmentCartData.setUt(keroUnixTime);
        shipmentCartData.setDestinationAddress(userAddress.getAddress());
        shipmentCartData.setDestinationDistrictId(String.valueOf(userAddress.getDistrictId()));
        shipmentCartData.setDestinationLatitude(!TextUtils.isEmpty(userAddress.getLatitude()) ?
                userAddress.getLatitude() : null);
        shipmentCartData.setDestinationLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                userAddress.getLongitude() : null);
        shipmentCartData.setDestinationPostalCode(userAddress.getPostalCode());
        shipmentCartData.setOriginDistrictId(String.valueOf(groupShop.getShop().getDistrictId()));
        shipmentCartData.setOriginLatitude(!TextUtils.isEmpty(groupShop.getShop().getLatitude()) ?
                groupShop.getShop().getLatitude() : null);
        shipmentCartData.setOriginLongitude(!TextUtils.isEmpty(groupShop.getShop().getLongitude()) ?
                groupShop.getShop().getLongitude() : null);
        shipmentCartData.setOriginPostalCode(groupShop.getShop().getPostalCode());
        shipmentCartData.setCategoryIds(getCategoryIds(groupShop.getProducts()));
        shipmentCartData.setProductInsurance(isForceInsurance(groupShop.getProducts()) ? 1 : 0);
        shipmentCartData.setShopShipments(groupShop.getShopShipments());
        String shippingNames = getShippingNames(groupShop.getShopShipments());
        shipmentCartData.setShippingNames(shippingNames);
        String shippingServices = getShippingServices(groupShop.getShopShipments());
        shipmentCartData.setShippingServices(shippingServices);
        shipmentCartData.setInsurance(1);
        shipmentCartData.setDeliveryPriceTotal(0);
    }

    private String getCategoryIds(List<Product> products) {
        List<Integer> categoryIds = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            int categoryId = products.get(i).getProductCatId();
            if (!categoryIds.contains(categoryId)) {
                categoryIds.add(categoryId);
            }
        }
        return TextUtils.join(",", categoryIds);
    }

    private boolean isForceInsurance(List<com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Product> products) {
        for (com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.Product product : products) {
            if (product.isProductFinsurance()) {
                return true;
            }
        }
        return false;
    }

    public String getShippingNames(List<ShopShipment> shopShipments) {
        List<String> shippingNames = new ArrayList<>();
        for (int i = 0; i < shopShipments.size(); i++) {
            String shippingName = shopShipments.get(i).getShipCode();
            if (!shippingNames.contains(shippingName)) {
                shippingNames.add(shippingName);
            }
        }
        return TextUtils.join(",", shippingNames);
    }

    public String getShippingServices(List<ShopShipment> shopShipments) {
        List<String> shippingServices = new ArrayList<>();
        for (int i = 0; i < shopShipments.size(); i++) {
            for (int j = 0; j < shopShipments.get(i).getShipProds().size(); j++) {
                String shippingService = shopShipments.get(i).getShipProds().get(j).getShipGroupName();
                if (!shippingServices.contains(shippingService)) {
                    shippingServices.add(shippingService);
                }
            }
        }
        return TextUtils.join(",", shippingServices);
    }

    public static String getLogisticPromoCode(ShipmentCartItemModel itemModel) {
        if (itemModel != null && itemModel.getVoucherLogisticItemUiModel() != null) {
            return itemModel.getVoucherLogisticItemUiModel().getCode();
        } else return "";
    }

}
