package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.common.util.Strings;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartTickerErrorData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.WholesalePriceData;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartList;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartMapper implements ICartMapper {
    private static final String SHOP_TYPE_OFFICIAL_STORE = "official_store";
    private static final String SHOP_TYPE_GOLD_MERCHANT = "gold_merchant";
    private static final String SHOP_TYPE_REGULER = "reguler";
    private static final String MERCHANT_VOUCHER_TYPE = "merchant";

    @Inject
    public CartMapper() {
    }

    @Override
    public CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse) {
        CartListData cartListData = new CartListData();
        String errorMessage = UtilsKt.convertToString(cartDataListResponse.getErrors());
        boolean hasError = false;
        for (CartList cartList : cartDataListResponse.getCartList()) {
            if (cartList.getErrors().size() > 0) {
                hasError = true;
                if (TextUtils.isEmpty(errorMessage)) {
                    errorMessage = UtilsKt.convertToString(cartList.getErrors());
                }
                break;
            }
        }
        cartListData.setError(!TextUtils.isEmpty(errorMessage) || hasError);
        cartListData.setErrorMessage(errorMessage);
        if (cartListData.isError()) {
            CartTickerErrorData cartTickerErrorData = new CartTickerErrorData();
            cartTickerErrorData.setErrorInfo(context.getString(R.string.cart_error_message));
            cartTickerErrorData.setActionInfo(context.getString(R.string.cart_error_action));
            cartListData.setCartTickerErrorData(cartTickerErrorData);
        }
        cartListData.setDefaultPromoDialogTab(cartDataListResponse.getDefaultPromoDialogTab());

        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartList data : cartDataListResponse.getCartList()) {
            CartItemData cartItemData = new CartItemData();

            CartItemData.OriginData cartItemDataOrigin = new CartItemData.OriginData();
            cartItemDataOrigin.setProductVarianRemark(data.getProduct().getProductNotes());
            cartItemDataOrigin.setCartId(data.getCartId());
            cartItemDataOrigin.setShopId(String.valueOf(data.getShop().getShopId()));
            cartItemDataOrigin.setShopName(data.getShop().getShopName());
            cartItemDataOrigin.setShopCity(data.getShop().getCityName());
            cartItemDataOrigin.setWeightFormatted(data.getProduct().getProductWeightFmt());
            cartItemDataOrigin.setWeightUnit(data.getProduct().getProductWeightUnitCode());
            cartItemDataOrigin.setWeightPlan(data.getProduct().getProductWeight());
            cartItemDataOrigin.setProductName(data.getProduct().getProductName());
            cartItemDataOrigin.setParentId(String.valueOf(data.getProduct().getParentId()));
            cartItemDataOrigin.setProductId(String.valueOf(data.getProduct().getProductId()));
            cartItemDataOrigin.setPriceFormatted(data.getProduct().getProductPriceFmt());
            cartItemDataOrigin.setPricePlan(data.getProduct().getProductPrice());
            cartItemDataOrigin.setPricePlanInt(data.getProduct().getProductPrice());
            cartItemDataOrigin.setShopType(generateShopType(data.getShop()));
            cartItemDataOrigin.setPriceCurrency(data.getProduct().getProductPriceCurrency());
            cartItemDataOrigin.setPreOrder(data.getProduct().isPreorder() == 1);
            data.getProduct().getProductPreorder();
            if (!Strings.isEmptyOrWhitespace(data.getProduct().getProductPreorder().getDurationText())) {
                cartItemDataOrigin.setPreOrderInfo("PO " + data.getProduct().getProductPreorder().getDurationText());
            }
            cartItemDataOrigin.setFavorite(false);
            cartItemDataOrigin.setMinOrder(data.getProduct().getProductMinOrder());
            if (data.getProduct().getProductSwitchInvenage() == 0) {
                cartItemDataOrigin.setMaxOrder(data.getProduct().getProductMaxOrder());
            } else if (data.getProduct().getProductMaxOrder() <= data.getProduct().getProductInvenageValue()) {
                cartItemDataOrigin.setMaxOrder(data.getProduct().getProductMaxOrder());
            } else {
                cartItemDataOrigin.setMaxOrder(data.getProduct().getProductInvenageValue());
            }
            cartItemDataOrigin.setFreeReturn(data.getProduct().isFreereturns() == 1);
            cartItemDataOrigin.setTrackerAttribution(data.getProduct().getProductTrackerData().getAttribution());
            cartItemDataOrigin.setTrackerListName(data.getProduct().getProductTrackerData().getTrackerListName());
            data.getProduct().getFreeReturns();
            cartItemDataOrigin.setFreeReturnLogo(data.getProduct().getFreeReturns().getFreeReturnsLogo());
            cartItemDataOrigin.setCashBack(!UtilsKt.isNullOrEmpty(data.getProduct().getProductCashback()));
            cartItemDataOrigin.setCashBackInfo("Cashback " + data.getProduct().getProductCashback());
            cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());
            cartItemDataOrigin.setCategory(data.getProduct().getCategory());
            cartItemDataOrigin.setCategoryId(String.valueOf(data.getProduct().getCategoryId()));
            cartItemDataOrigin.setGoldMerchant(data.getShop().getGoldMerchant().isGoldBadge());
            cartItemDataOrigin.setGoldMerchantLogoUrl(data.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
            cartItemDataOrigin.setOfficialStore(data.getShop().getOfficialStore().isOfficial() == 1);
            cartItemDataOrigin.setOfficialStoreLogoUrl(data.getShop().getOfficialStore().getOsLogoUrl());

            List<WholesalePriceData> wholesalePriceData = new ArrayList<>();
            for (com.tokopedia.purchase_platform.common.data.model.response.WholesalePrice wholesalePriceDataModel : data.getProduct().getWholesalePrice()) {
                WholesalePriceData wholesalePriceDataDomainModel = new WholesalePriceData();
                wholesalePriceDataDomainModel.setPrdPrc(wholesalePriceDataModel.getPrdPrc());
                wholesalePriceDataDomainModel.setPrdPrcFmt(wholesalePriceDataModel.getPrdPrcFmt());
                wholesalePriceDataDomainModel.setQtyMax(wholesalePriceDataModel.getQtyMax());
                wholesalePriceDataDomainModel.setQtyMaxFmt(wholesalePriceDataModel.getQtyMaxFmt());
                wholesalePriceDataDomainModel.setQtyMin(wholesalePriceDataModel.getQtyMin());
                wholesalePriceDataDomainModel.setQtyMinFmt(wholesalePriceDataModel.getQtyMinFmt());

                wholesalePriceData.add(wholesalePriceDataDomainModel);
            }
            Collections.reverse(wholesalePriceData);
            cartItemDataOrigin.setWholesalePriceData(wholesalePriceData);

            CartItemData.UpdatedData cartItemDataUpdated = new CartItemData.UpdatedData();
            cartItemDataUpdated.setRemark(cartItemDataOrigin.getProductVarianRemark());
            cartItemDataUpdated.setQuantity(data.getProduct().getProductQuantity());
            cartItemDataUpdated.setMaxCharRemark(cartDataListResponse.getMaxCharNote());

            CartItemData.MessageErrorData cartItemMessageErrorData = new CartItemData.MessageErrorData();
            cartItemMessageErrorData.setErrorCheckoutPriceLimit(cartDataListResponse.getMessages().getErrorCheckoutPriceLimit());
            cartItemMessageErrorData.setErrorFieldBetween(cartDataListResponse.getMessages().getErrorFieldBetween());
            cartItemMessageErrorData.setErrorFieldMaxChar(cartDataListResponse.getMessages().getErrorFieldMaxChar());
            cartItemMessageErrorData.setErrorFieldRequired(cartDataListResponse.getMessages().getErrorFieldRequired());
            cartItemMessageErrorData.setErrorProductAvailableStock(cartDataListResponse.getMessages().getErrorProductAvailableStock());
            cartItemMessageErrorData.setErrorProductAvailableStockDetail(cartDataListResponse.getMessages().getErrorProductAvailableStockDetail());
            cartItemMessageErrorData.setErrorProductMaxQuantity(cartDataListResponse.getMessages().getErrorProductMaxQuantity());
            cartItemMessageErrorData.setErrorProductMinQuantity(cartDataListResponse.getMessages().getErrorProductMinQuantity());


            cartItemData.setOriginData(cartItemDataOrigin);
            cartItemData.setUpdatedData(cartItemDataUpdated);
            cartItemData.setMessageErrorData(cartItemMessageErrorData);

            if (data.getErrors().size() > 0) {
                cartItemData.setError(true);
                cartItemData.setErrorMessageTitle(data.getErrors().get(0));

                if (data.getErrors().size() > 1) {
                    cartItemData.setErrorMessageDescription(UtilsKt.convertToString(
                            data.getErrors().subList(1, data.getErrors().size() - 1)));
                }
            }

            if (data.getMessages().size() > 0) {
                cartItemData.setWarning(true);
                cartItemData.setWarningMessageTitle(data.getMessages().get(0));

                if (data.getMessages().size() > 1) {
                    cartItemData.setWarningMessageDescription(UtilsKt.convertToString(
                            data.getMessages().subList(1, data.getMessages().size() - 1)));
                }
            }

            cartItemDataList.add(cartItemData);
        }

        List<ShopGroupAvailableData> shopGroupDataList = new ArrayList<>();
        for (CartItemData cartItemData : cartItemDataList) {
            ShopGroupAvailableData shopGroupData = new ShopGroupAvailableData();
            List<CartItemData> itemDataList = new ArrayList<>();
            itemDataList.add(cartItemData);
            shopGroupData.setCartItemDataList(itemDataList);
            shopGroupDataList.add(shopGroupData);
        }
        cartListData.setShopGroupAvailableDataList(shopGroupDataList);
        cartListData.setPromoCouponActive(cartDataListResponse.getIsCouponActive() == 1);
        cartListData.setShowOnboarding(cartDataListResponse.isShowOnboarding());

        return cartListData;
    }

    private String generateShopType(Shop shop) {
        if (shop.isOfficial() == 1)
            return SHOP_TYPE_OFFICIAL_STORE;
        else if (shop.getGoldMerchant().isGoldBadge())
            return SHOP_TYPE_GOLD_MERCHANT;
        else return SHOP_TYPE_REGULER;
    }
}
