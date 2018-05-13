package com.tokopedia.checkout.view.view.shipment.converter;

import android.text.TextUtils;

import com.tokopedia.logisticdata.data.entity.rates.Attribute;
import com.tokopedia.logisticdata.data.entity.rates.RatesResponse;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressCartItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressCartItem;

import org.apache.commons.lang3.text.WordUtils;

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

    public ShipmentDetailData getShipmentDetailData(ShipmentCartItem shipmentCartItem,
                                                    RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        if (shipmentCartItem instanceof ShipmentSingleAddressCartItem) {
            ShipmentCartData shipmentCartData = shipmentCartItem.getShipmentCartData();
            shipmentCartData.setDestinationAddress(recipientAddressModel.getAddressStreet());
            shipmentCartData.setDestinationDistrictId(recipientAddressModel.getDestinationDistrictId());
            shipmentCartData.setDestinationLatitude(recipientAddressModel.getLatitude());
            shipmentCartData.setDestinationLongitude(recipientAddressModel.getLongitude());
            shipmentCartData.setDestinationPostalCode(recipientAddressModel.getAddressPostalCode());
            shipmentDetailData.setShipmentCartData(shipmentCartData);
            int totalQuantity = 0;
            for (CartItemModel cartItemModel : ((ShipmentSingleAddressCartItem) shipmentCartItem).getCartItemModels()) {
                totalQuantity += cartItemModel.getQuantity();
            }
            shipmentDetailData.setTotalQuantity(totalQuantity);
        } else if (shipmentCartItem instanceof ShipmentMultipleAddressCartItem) {
            ShipmentMultipleAddressCartItem shipmentMultipleAddressItem = (ShipmentMultipleAddressCartItem) shipmentCartItem;
            shipmentDetailData.setShipmentCartData(shipmentMultipleAddressItem.getShipmentCartData());
        }
        return shipmentDetailData;
    }

    public ShipmentCartData getShipmentCartData(UserAddress userAddress, GroupShop groupShop,
                                                ShipmentCartItem shipmentCartItem, String keroToken, String keroUnixTime) {
        ShipmentCartData shipmentCartData = new ShipmentCartData();
        initializeShipmentCartData(userAddress, groupShop, shipmentCartData, keroToken, keroUnixTime);
        int orderValue = 0;
        int totalWeight = 0;
        if (shipmentCartItem instanceof ShipmentSingleAddressCartItem) {
            for (CartItemModel cartItemModel : ((ShipmentSingleAddressCartItem) shipmentCartItem).getCartItemModels()) {
                orderValue += (cartItemModel.getQuantity() * cartItemModel.getPrice());
                totalWeight += (cartItemModel.getQuantity() * cartItemModel.getWeight());
            }
        } else {
            int productQuantity = Integer.parseInt(((ShipmentMultipleAddressCartItem) shipmentCartItem)
                    .getMultipleAddressItemData().getProductQty());
            orderValue = ((ShipmentMultipleAddressCartItem) shipmentCartItem).getProductPrice() * productQuantity;
            totalWeight = ((ShipmentMultipleAddressCartItem) shipmentCartItem).getMultipleAddressItemData().getProductRawWeight()
                    * Integer.parseInt(((ShipmentMultipleAddressCartItem) shipmentCartItem).getMultipleAddressItemData().getProductQty());
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
                Double.parseDouble(userAddress.getLatitude()) : null);
        shipmentCartData.setDestinationLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                Double.parseDouble(userAddress.getLongitude()) : null);
        shipmentCartData.setDestinationPostalCode(userAddress.getPostalCode());
        shipmentCartData.setOriginDistrictId(String.valueOf(groupShop.getShop().getDistrictId()));
        shipmentCartData.setOriginLatitude(!TextUtils.isEmpty(groupShop.getShop().getLatitude()) ?
                Double.parseDouble(groupShop.getShop().getLatitude()) : null);
        shipmentCartData.setOriginLongitude(!TextUtils.isEmpty(groupShop.getShop().getLongitude()) ?
                Double.parseDouble(groupShop.getShop().getLongitude()) : null);
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

    private boolean isForceInsurance(List<com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product> products) {
        for (com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product product : products) {
            if (product.isProductFinsurance()) {
                return true;
            }
        }
        return false;
    }

    private String getShippingNames(List<ShopShipment> shopShipments) {
        List<String> shippingNames = new ArrayList<>();
        for (int i = 0; i < shopShipments.size(); i++) {
            String shippingName = shopShipments.get(i).getShipCode();
            if (!shippingNames.contains(shippingName)) {
                shippingNames.add(shippingName);
            }
        }
        return TextUtils.join(",", shippingNames);
    }

    private String getShippingServices(List<ShopShipment> shopShipments) {
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

    public ShipmentDetailData getShipmentDetailData(ShipmentDetailData shipmentDetailData,
                                                    RatesResponse ratesResponse) {
        if (shipmentDetailData == null) {
            shipmentDetailData = new ShipmentDetailData();
        }
        List<ShipmentItemData> shipmentItemDataList = getShipmentItemDataList(ratesResponse);

        List<ShipProd> allShipProds = new ArrayList<>();
        for (ShopShipment shopShipment : shipmentDetailData.getShipmentCartData().getShopShipments()) {
            allShipProds.addAll(shopShipment.getShipProds());
            for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
                for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                    if (courierItemData.getShipperId() == shopShipment.getShipId()) {
                        courierItemData.setAllowDropshiper(shopShipment.isDropshipEnabled());
                    }
                }
            }
        }

        for (ShipProd shipProd : allShipProds) {
            for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
                for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                    if (shipProd.getShipProdId() == courierItemData.getShipperProductId()) {
                        courierItemData.setAdditionalPrice(shipProd.getAdditionalFee());
                    }
                }
            }
        }

        shipmentDetailData.setShipmentItemData(shipmentItemDataList);
        return shipmentDetailData;
    }

    private List<ShipmentItemData> getShipmentItemDataList(RatesResponse ratesResponse) {
        List<ShipmentItemData> shipmentItemDataList = new ArrayList<>();
        for (Attribute attribute : ratesResponse.getData().getAttributes()) {
            shipmentItemDataList.add(getShipmentItemData(attribute));
        }
        return shipmentItemDataList;
    }

    private ShipmentItemData getShipmentItemData(Attribute attribute) {
        ShipmentItemData shipmentItemData = new ShipmentItemData();
        shipmentItemData.setServiceId(attribute.getServiceId());
        shipmentItemData.setCourierItemData(getCourierItemDataList(attribute.getProducts()));
        shipmentItemData.setServiceId(attribute.getServiceId());
        shipmentItemData.setType(WordUtils.capitalize(attribute.getServiceName()));
        shipmentItemData.setMultiplePriceRange(attribute.getServiceRangePrice());
        shipmentItemData.setDeliveryTimeRange(attribute.getServiceEtd());
        return shipmentItemData;
    }

    private List<CourierItemData> getCourierItemDataList(List<com.tokopedia.logisticdata.data.entity.rates.Product> products) {
        List<CourierItemData> courierItemDataList = new ArrayList<>();
        for (com.tokopedia.logisticdata.data.entity.rates.Product product : products) {
            courierItemDataList.add(getCourierItemData(product));
        }
        return courierItemDataList;
    }

    private CourierItemData getCourierItemData(com.tokopedia.logisticdata.data.entity.rates.Product product) {
        CourierItemData courierItemData = new CourierItemData();
        courierItemData.setUsePinPoint(product.getIsShowMap() == 1);
        courierItemData.setName(product.getShipperName() + " " + product.getShipperProductName());
        courierItemData.setShipperId(product.getShipperId());
        courierItemData.setShipperProductId(product.getShipperProductId());
        courierItemData.setInsuranceUsedInfo(product.getInsuranceUsedInfo());
        courierItemData.setInsurancePrice(product.getInsurancePrice());
        courierItemData.setInsuranceType(product.getInsuranceType());
        courierItemData.setInsuranceUsedDefault(product.getInsuranceUsedDefault());
        courierItemData.setCourierInfo(product.getShipperProductDesc());
        courierItemData.setInsuranceUsedType(product.getInsuranceUsedType());
        courierItemData.setDeliveryPrice(product.getShipperPrice());
        courierItemData.setEstimatedTimeDelivery(product.getShipperEtd());
        courierItemData.setMinEtd(product.getMinEtd());
        courierItemData.setMaxEtd(product.getMaxEtd());

        return courierItemData;
    }

}
