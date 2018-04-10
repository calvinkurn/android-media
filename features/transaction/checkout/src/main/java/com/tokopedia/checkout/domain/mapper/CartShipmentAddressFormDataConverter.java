package com.tokopedia.checkout.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Shop;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.SingleShipmentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 08/02/18,
 *         Aghny A. Putra on 08/02/18.
 */

public class CartShipmentAddressFormDataConverter
        extends ConverterData<CartShipmentAddressFormData, SingleShipmentData> {

    private static final int FIRST_ELEMENT = 0;
    private static final int PRIME_ADDRESS = 2;

    private CartShipmentAddressFormData cartItemDataList;
    private UserAddress userAddress;

    @Inject
    public CartShipmentAddressFormDataConverter() {

    }

    @Override
    public SingleShipmentData convert(CartShipmentAddressFormData cartItemDataList) {
        this.cartItemDataList = cartItemDataList;
        GroupAddress groupAddress = cartItemDataList.getGroupAddress().get(FIRST_ELEMENT);

        userAddress = groupAddress.getUserAddress();
        List<GroupShop> groupShops = groupAddress.getGroupShop();

        RecipientAddressModel recipientAddressModel = convertFromUserAddress(userAddress);
        List<CartSellerItemModel> cartSellerItemModels = convertFromGroupShopList(groupShops);
        ShipmentCostModel shipmentCostModel = getTotalPayableDetail(cartSellerItemModels);

        SingleShipmentData singleShipmentData = new SingleShipmentData();

        singleShipmentData.setError(groupAddress.isError());
        singleShipmentData.setErrorMessage(groupAddress.getErrorMessage());
        singleShipmentData.setWarning(groupAddress.isWarning());
        singleShipmentData.setWarningMessage(groupAddress.getWarningMessage());

        singleShipmentData.setRecipientAddress(recipientAddressModel);
        singleShipmentData.setCartItem(cartSellerItemModels);
        singleShipmentData.setShipmentCost(shipmentCostModel);
        return singleShipmentData;
    }

    private List<CartSellerItemModel> convertFromGroupShopList(List<GroupShop> groupShops) {
        List<CartSellerItemModel> cartSellerItemModels = new ArrayList<>();

        for (GroupShop groupShop : groupShops) {
            cartSellerItemModels.add(convertFromGroupShop(groupShop));
        }

        return cartSellerItemModels;
    }

    private CartSellerItemModel convertFromGroupShop(GroupShop groupShop) {
        CartSellerItemModel sellerItemModel = new CartSellerItemModel();

        sellerItemModel.setError(groupShop.isError());
        sellerItemModel.setErrorMessage(groupShop.getErrorMessage());
        sellerItemModel.setWarning(groupShop.isWarning());
        sellerItemModel.setWarningMessage(groupShop.getWarningMessage());

        Shop shop = groupShop.getShop();
        sellerItemModel.setShopId(String.valueOf(shop.getShopId()));
        sellerItemModel.setShopName(shop.getShopName());

        List<Product> products = groupShop.getProducts();
        List<CartItemModel> cartItemModels = convertFromProductList(products);

        // This is something that not well planned
        Fobject fobject = levelUpParametersFromProductToCartSeller(cartItemModels);

        sellerItemModel.setCartItemModels(cartItemModels);

        int totalQuantity = 0;
        double totalItemPrice = 0, totalWeight = 0;
        for (CartItemModel cartItemModel : cartItemModels) {
            totalItemPrice += cartItemModel.getPrice() * cartItemModel.getQuantity();
            totalQuantity += cartItemModel.getQuantity();
            totalWeight += cartItemModel.getWeight() * cartItemModel.getQuantity();
        }
        sellerItemModel.setError(groupShop.isError());
        sellerItemModel.setErrorMessage(groupShop.getErrorMessage());

        sellerItemModel.setTotalItemPrice(totalItemPrice);
        sellerItemModel.setTotalQuantity(totalQuantity);
        sellerItemModel.setTotalWeight(totalWeight);
        sellerItemModel.setShipmentCartData(new ShipmentRatesDataMapper()
                .getShipmentCartData(cartItemDataList, userAddress, groupShop, sellerItemModel));

        sellerItemModel.setInsuranceFee(fobject.isFinsurance());
        sellerItemModel.setIsFcancelPartial(fobject.isFcancelPartial());
        sellerItemModel.setIsPreOrder(fobject.isPreOrder());

        return sellerItemModel;
    }

    private CartItemModel convertFromProduct(Product product) {
        CartItemModel cartItemModel = new CartItemModel();

        cartItemModel.setId(product.getProductId());
        cartItemModel.setName(product.getProductName());
        cartItemModel.setImageUrl(product.getProductImageSrc200Square());
        cartItemModel.setCurrency(product.getProductPriceCurrency());
        cartItemModel.setPrice(product.getProductPrice());
        cartItemModel.setQuantity(product.getProductQuantity());
        cartItemModel.setWeight(product.getProductWeight());
        cartItemModel.setWeightFmt(product.getProductWeightFmt());
        cartItemModel.setNoteToSeller(product.getProductNotes());
        cartItemModel.setPreOrder(product.isProductIsPreorder());
        cartItemModel.setFreeReturn(product.isProductIsFreeReturns());
        cartItemModel.setCashback(product.getProductCashback());
        cartItemModel.setCashback(!TextUtils.isEmpty(product.getProductCashback()));
        cartItemModel.setFreeReturnLogo(product.getFreeReturnLogo());
        cartItemModel.setfInsurance(product.isProductFcancelPartial());
        cartItemModel.setfCancelPartial(product.isProductFinsurance());

        return cartItemModel;
    }

    private List<CartItemModel> convertFromProductList(List<Product> products) {
        List<CartItemModel> cartItemModels = new ArrayList<>();

        for (Product product : products) {
            cartItemModels.add(convertFromProduct(product));
        }

        return cartItemModels;
    }

    private RecipientAddressModel convertFromUserAddress(UserAddress userAddress) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        recipientAddress.setId(String.valueOf(userAddress.getAddressId()));
        recipientAddress.setAddressStatus(userAddress.getStatus());
        recipientAddress.setAddressName(userAddress.getAddressName());
        recipientAddress.setAddressCountryName(userAddress.getCountry());
        recipientAddress.setAddressProvinceName(userAddress.getProvinceName());
        recipientAddress.setDestinationDistrictName(userAddress.getDistrictName());
        recipientAddress.setAddressCityName(userAddress.getCityName());
        recipientAddress.setDestinationDistrictId(String.valueOf(userAddress.getDistrictId()));
        recipientAddress.setAddressStreet(userAddress.getAddress());
        recipientAddress.setAddressPostalCode(userAddress.getPostalCode());

        recipientAddress.setRecipientName(userAddress.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(userAddress.getPhone());
        recipientAddress.setLatitude(!TextUtils.isEmpty(userAddress.getLatitude()) ?
                Double.parseDouble(userAddress.getLatitude()) : null);
        recipientAddress.setLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                Double.parseDouble(userAddress.getLongitude()) : null);

        recipientAddress.setSelected(userAddress.getStatus() == PRIME_ADDRESS);

        return recipientAddress;
    }

    private ShipmentCostModel getTotalPayableDetail(List<CartSellerItemModel> cartSellerItemModels) {
        ShipmentCostModel cartPayable = new ShipmentCostModel();

        for (CartSellerItemModel itemModel : cartSellerItemModels) {
            cartPayable.setTotalItem(cartPayable.getTotalItem() + itemModel.getTotalQuantity());
            cartPayable.setTotalItemPrice(cartPayable.getTotalItemPrice() + itemModel.getTotalItemPrice());
            cartPayable.setTotalWeight(cartPayable.getTotalWeight() + itemModel.getTotalWeight());
        }

        return cartPayable;
    }

    private List<CartSellerItemModel> groupItemBySeller(List<CartItemModel> cartItemModels) {
        Map<String, CartSellerItemModel> itemGroupMap = new HashMap<>(cartItemModels.size());

        for (CartItemModel cartItemModel : cartItemModels) {
            String shopId = cartItemModel.getShopId();

            if (!itemGroupMap.containsKey(shopId)) {
                CartSellerItemModel cartSellerItemModel = new CartSellerItemModel();
                List<CartItemModel> cartItemModelList = new ArrayList<>();
                cartItemModelList.add(cartItemModel);

                cartSellerItemModel.setShopName(cartItemModel.getShopName());
                cartSellerItemModel.setTotalQuantity(cartItemModel.getQuantity());
                cartSellerItemModel.setTotalWeight(cartItemModel.getWeight());
                cartSellerItemModel.setWeightUnit(cartItemModel.getWeightUnit());
                cartSellerItemModel.setTotalPrice(cartItemModel.getPrice());
                cartSellerItemModel.setCartItemModels(cartItemModelList);

                itemGroupMap.put(shopId, cartSellerItemModel);

            } else {
                CartSellerItemModel cartSellerItemModel = itemGroupMap.get(shopId);

                cartSellerItemModel.getCartItemModels().add(cartItemModel);

                int totalItem = cartSellerItemModel.getTotalQuantity()
                        + cartItemModel.getQuantity();
                cartSellerItemModel.setTotalQuantity(totalItem);

                double weightSum = cartSellerItemModel.getTotalWeight()
                        + cartItemModel.getWeight();
                cartSellerItemModel.setTotalWeight(weightSum);

                double priceSum = cartSellerItemModel.getTotalPrice()
                        + cartItemModel.getPrice();
                cartSellerItemModel.setTotalPrice(priceSum);
            }
        }

        return convertModelMapToList(itemGroupMap);
    }

    private List<CartSellerItemModel> convertModelMapToList(Map<String, CartSellerItemModel> map) {
        List<CartSellerItemModel> cartSellerItemModels = new ArrayList<>();

        for (Map.Entry<String, CartSellerItemModel> entry : map.entrySet()) {
            cartSellerItemModels.add(entry.getValue());
        }

        return cartSellerItemModels;
    }

    private CartItemModel convertCartItem(CartItemData cartItemData) {
        CartItemModel cartItemModel = new CartItemModel();

        cartItemModel.setFreeReturn(cartItemData.getOriginData().isFreeReturn());
        cartItemModel.setPreOrder(cartItemData.getOriginData().isPreOrder());
        cartItemModel.setCashback(cartItemData.getOriginData().isCashBack());
        cartItemModel.setCashback(cartItemData.getOriginData().getCashBackInfo());

        cartItemModel.setImageUrl(cartItemData.getOriginData().getProductImage());

        cartItemModel.setShopName(cartItemData.getOriginData().getShopName());
        cartItemModel.setShopId(cartItemData.getOriginData().getShopId());
        cartItemModel.setId(Integer.parseInt(cartItemData.getOriginData().getProductId()));
        cartItemModel.setName(cartItemData.getOriginData().getProductName());

        cartItemModel.setCurrency(cartItemData.getOriginData().getPriceCurrency());
        cartItemModel.setPrice(cartItemData.getOriginData().getPricePlan());

        cartItemModel.setWeight(cartItemData.getOriginData().getWeightPlan());
        cartItemModel.setWeightUnit(cartItemData.getOriginData().getWeightUnit());

        cartItemModel.setNoteToSeller(cartItemData.getUpdatedData().getRemark());
        cartItemModel.setQuantity(cartItemData.getUpdatedData().getQuantity());

        return cartItemModel;
    }

    private Fobject levelUpParametersFromProductToCartSeller(List<CartItemModel> cartItemList) {

        int isPreOrder = 0;
        int isFcancelPartial = 0;
        int isFinsurance = 0;

        for (CartItemModel cartItem : cartItemList) {
            if (cartItem.isPreOrder()) {
                isPreOrder = 1;
            }
            if (cartItem.isfInsurance()) {
                isFcancelPartial = 1;
            }
            if (cartItem.isfCancelPartial()) {
                isFinsurance = 1;
            }
        }

        return new Fobject(isPreOrder, isFcancelPartial, isFinsurance);
    }

    private class Fobject {

        private int isPreOrder;
        private int isFcancelPartial;
        private int isFinsurance;

        Fobject(int isPreOrder, int isFcancelPartial, int isFinsurance) {
            this.isPreOrder = isPreOrder;
            this.isFcancelPartial = isFcancelPartial;
            this.isFinsurance = isFinsurance;
        }

        public int isPreOrder() {
            return isPreOrder;
        }

        public void setPreOrder(int preOrder) {
            isPreOrder = preOrder;
        }

        public int isFcancelPartial() {
            return isFcancelPartial;
        }

        public void setFcancelPartial(int fcancelPartial) {
            isFcancelPartial = fcancelPartial;
        }

        public int isFinsurance() {
            return isFinsurance;
        }

        public void setFinsurance(int finsurance) {
            isFinsurance = finsurance;
        }
    }

}