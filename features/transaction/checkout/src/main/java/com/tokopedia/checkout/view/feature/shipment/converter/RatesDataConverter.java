package com.tokopedia.checkout.view.feature.shipment.converter;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.logisticdata.data.entity.rates.Attribute;
import com.tokopedia.logisticdata.data.entity.rates.RatesResponse;
import com.tokopedia.shipping_recommendation.domain.shipping.CartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipProd;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;

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
        shipmentDetailData.setPreorder(shipmentCartItemModel.getPreorder());
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

    private boolean isForceInsurance(List<com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product> products) {
        for (com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product product : products) {
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

    public ShipmentDetailData getShipmentDetailData(ShipmentDetailData shipmentDetailData,
                                                    List<ShopShipment> shopShipmentList,
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

        for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
            List<CourierItemData> activeCourierItemDataList = new ArrayList<>();
            for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                if (isCourierActive(shopShipmentList, courierItemData)) {
                    activeCourierItemDataList.add(courierItemData);
                }
            }
            shipmentItemData.setCourierItemData(activeCourierItemDataList);
        }

        shipmentDetailData.setShipmentItemData(shipmentItemDataList);
        return shipmentDetailData;
    }

    private boolean isCourierActive(List<ShopShipment> shopShipmentList, CourierItemData courierItemData) {
        for (ShopShipment shopShipment : shopShipmentList) {
            if (shopShipment.getShipId() == courierItemData.getShipperId()) {
                for (ShipProd shipProd : shopShipment.getShipProds()) {
                    if (shipProd.getShipProdId() == courierItemData.getShipperProductId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<ShipmentItemData> getShipmentItemDataList(RatesResponse ratesResponse) {
        List<ShipmentItemData> shipmentItemDataList = new ArrayList<>();
        if (ratesResponse != null && ratesResponse.getData() != null &&
                ratesResponse.getData().getAttributes() != null) {
            for (Attribute attribute : ratesResponse.getData().getAttributes()) {
                shipmentItemDataList.add(getShipmentItemData(attribute));
            }
        }
        return shipmentItemDataList;
    }

    private ShipmentItemData getShipmentItemData(Attribute attribute) {
        ShipmentItemData shipmentItemData = new ShipmentItemData();
        shipmentItemData.setServiceId(attribute.getServiceId());
        shipmentItemData.setType(WordUtils.capitalize(attribute.getServiceName()));
        shipmentItemData.setMultiplePriceRange(attribute.getServiceRangePrice());
        shipmentItemData.setDeliveryTimeRange(attribute.getServiceEtd());
        shipmentItemData.setCourierItemData(getCourierItemDataList(attribute.getProducts(), shipmentItemData));
        return shipmentItemData;
    }

    private List<CourierItemData> getCourierItemDataList(List<com.tokopedia.logisticdata.data.entity.rates.Product> products,
                                                         ShipmentItemData shipmentItemData) {
        List<CourierItemData> courierItemDataList = new ArrayList<>();
        for (com.tokopedia.logisticdata.data.entity.rates.Product product : products) {
            courierItemDataList.add(getCourierItemData(product, shipmentItemData));
        }
        return courierItemDataList;
    }

    private CourierItemData getCourierItemData(com.tokopedia.logisticdata.data.entity.rates.Product product,
                                               ShipmentItemData shipmentItemData) {
        CourierItemData courierItemData = new CourierItemData();
        courierItemData.setUsePinPoint(product.getIsShowMap() == 1);
        courierItemData.setName(product.getShipperName());
        courierItemData.setShipperId(product.getShipperId());
        courierItemData.setShipperProductId(product.getShipperProductId());
        courierItemData.setInsuranceUsedInfo(product.getInsuranceUsedInfo());
        courierItemData.setInsurancePrice(product.getInsurancePrice());
        courierItemData.setInsuranceType(product.getInsuranceType());
        courierItemData.setInsuranceUsedDefault(product.getInsuranceUsedDefault());
        courierItemData.setCourierInfo(product.getShipperProductDesc());
        courierItemData.setInsuranceUsedType(product.getInsuranceUsedType());
        courierItemData.setShipperPrice(product.getShipperPrice());
        courierItemData.setEstimatedTimeDelivery(product.getShipperEtd());
        courierItemData.setMinEtd(product.getMinEtd());
        courierItemData.setMaxEtd(product.getMaxEtd());
        courierItemData.setShipmentItemDataEtd(shipmentItemData.getDeliveryTimeRange());
        courierItemData.setShipmentItemDataType(shipmentItemData.getType());
        courierItemData.setShipperFormattedPrice(product.getShipperFormattedPrice());
        courierItemData.setChecksum(product.getCheckSum());
        courierItemData.setUt(product.getUt());

        return courierItemData;
    }

}
