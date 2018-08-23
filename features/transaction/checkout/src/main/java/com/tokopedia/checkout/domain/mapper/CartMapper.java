package com.tokopedia.checkout.domain.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ResetCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.WholesalePrice;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartList;
import com.tokopedia.transactiondata.entity.response.cartlist.Shop;
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.CartDetail;
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.ShopGroup;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;

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
    private final IMapperUtil mapperUtil;

    @Inject
    public CartMapper(IMapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CartListData convertToCartItemDataList(Context context, CartDataListResponse cartDataListResponse) {
        CartListData cartListData = new CartListData();
        String errorMessage = mapperUtil.convertToString(cartDataListResponse.getErrors());
        boolean hasError = false;
        for (ShopGroup shopGroup : cartDataListResponse.getShopGroups()) {
            if (shopGroup.getErrors() != null && shopGroup.getErrors().size() > 0) {
                hasError = true;
                if (TextUtils.isEmpty(errorMessage)) {
                    errorMessage = mapperUtil.convertToString(shopGroup.getErrors());
                }
                break;
            }
        }
        cartListData.setError(!TextUtils.isEmpty(errorMessage) || hasError);
        cartListData.setErrorMessage(errorMessage);
        if (cartListData.isError()) {
            cartListData.setCartTickerErrorData(
                    new CartTickerErrorData.Builder()
                            .errorInfo(context.getString(R.string.cart_error_message))
                            .actionInfo(context.getString(R.string.cart_error_action))
                            .build()
            );
        }
        cartListData.setDefaultPromoDialogTab(cartDataListResponse.getDefaultPromoDialogTab());

        List<ShopGroupData> shopGroupDataList = new ArrayList<>();
        for (ShopGroup shopGroup : cartDataListResponse.getShopGroups()) {
            ShopGroupData shopGroupData = new ShopGroupData();
            shopGroupData.setError(!mapperUtil.isEmpty(shopGroup.getErrors()));
            shopGroupData.setErrorMessage(mapperUtil.convertToString(shopGroup.getErrors()));
            shopGroupData.setShopId(String.valueOf(shopGroup.getShop().getShopId()));
            shopGroupData.setShopName(shopGroup.getShop().getShopName());
            shopGroupData.setShopType(generateShopType(shopGroup.getShop()));
            shopGroupData.setGoldMerchant(shopGroup.getShop().getIsGold() == 1);
            shopGroupData.setOfficialStore(shopGroup.getShop().getIsOfficial() == 1);

            List<CartItemData> cartItemDataList = new ArrayList<>();
            for (CartDetail data : shopGroup.getCartDetails()) {
                CartItemData cartItemData = new CartItemData();

                CartItemData.OriginData cartItemDataOrigin = new CartItemData.OriginData();
                cartItemDataOrigin.setProductVarianRemark(
                        data.getProduct().getProductNotes()
                );
                cartItemDataOrigin.setCartId(data.getCartId());
                cartItemDataOrigin.setWeightFormatted(data.getProduct().getProductWeightFmt());
                cartItemDataOrigin.setWeightUnit(data.getProduct().getProductWeightUnitCode());
                cartItemDataOrigin.setWeightPlan(data.getProduct().getProductWeight());
                cartItemDataOrigin.setProductName(data.getProduct().getProductName());
                cartItemDataOrigin.setParentId(String.valueOf(data.getProduct().getParentId()));
                cartItemDataOrigin.setProductId(String.valueOf(data.getProduct().getProductId()));
                cartItemDataOrigin.setPriceFormatted(data.getProduct().getProductPriceFmt());
                cartItemDataOrigin.setPricePlan(data.getProduct().getProductPrice());
                cartItemDataOrigin.setPricePlanInt(data.getProduct().getProductPrice());
                cartItemDataOrigin.setPriceCurrency(data.getProduct().getProductPriceCurrency());
                cartItemDataOrigin.setPreOrder(data.getProduct().getIsPreorder() == 1);
                cartItemDataOrigin.setFavorite(false);
                cartItemDataOrigin.setMinimalQtyOrder(data.getProduct().getProductMinOrder());
                cartItemDataOrigin.setInvenageValue(data.getProduct().getProductInvenageValue());
                cartItemDataOrigin.setFreeReturn(data.getProduct().getIsFreereturns() == 1);
                cartItemDataOrigin.setTrackerAttribution(data.getProduct().getProductTrackerData().getAttribution());
                cartItemDataOrigin.setTrackerListName(data.getProduct().getProductTrackerData().getTrackerListName());
                if (!mapperUtil.isEmpty(data.getProduct().getFreeReturns())) {
                    cartItemDataOrigin.setFreeReturnLogo(data.getProduct().getFreeReturns().getFreeReturnsLogo());
                }
                cartItemDataOrigin.setCashBack(!mapperUtil.isEmpty(data.getProduct().getProductCashback()));
                cartItemDataOrigin.setProductCashBack(data.getProduct().getProductCashback());
                cartItemDataOrigin.setCashBackInfo("Cashback " + data.getProduct().getProductCashback());
                cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());
                cartItemDataOrigin.setCategory(data.getProduct().getCategory());
                cartItemDataOrigin.setCategoryId(String.valueOf(data.getProduct().getCategoryId()));
                cartItemDataOrigin.setOriginalRemark(cartItemDataOrigin.getProductVarianRemark());
                if (data.getProduct().getWholesalePrice() != null) {
                    List<WholesalePrice> wholesalePrices = new ArrayList<>();
                    for (com.tokopedia.transactiondata.entity.response.cartlist.WholesalePrice wholesalePriceDataModel : data.getProduct().getWholesalePrice()) {
                        WholesalePrice wholesalePriceDomainModel = new WholesalePrice();
                        wholesalePriceDomainModel.setPrdPrc(wholesalePriceDataModel.getPrdPrc());
                        wholesalePriceDomainModel.setPrdPrcFmt(wholesalePriceDataModel.getPrdPrcFmt());
                        wholesalePriceDomainModel.setQtyMax(wholesalePriceDataModel.getQtyMax());
                        wholesalePriceDomainModel.setQtyMaxFmt(wholesalePriceDataModel.getQtyMaxFmt());
                        wholesalePriceDomainModel.setQtyMin(wholesalePriceDataModel.getQtyMin());
                        wholesalePriceDomainModel.setQtyMinFmt(wholesalePriceDataModel.getQtyMinFmt());

                        wholesalePrices.add(wholesalePriceDomainModel);
                    }
                    Collections.reverse(wholesalePrices);
                    cartItemDataOrigin.setWholesalePrice(wholesalePrices);
                }

                CartItemData.UpdatedData cartItemDataUpdated = new CartItemData.UpdatedData();
                cartItemDataUpdated.setRemark(cartItemDataOrigin.getProductVarianRemark());
                cartItemDataUpdated.setQuantity(data.getProduct().getProductQuantity());
                cartItemDataUpdated.setMaxCharRemark(cartDataListResponse.getMaxCharNote());
                cartItemDataUpdated.setMaxQuantity(cartDataListResponse.getMaxQuantity());

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
                cartItemData.setErrorData(cartItemMessageErrorData);

                cartItemData.setError(!mapperUtil.isEmpty(data.getErrors()));
                cartItemData.setErrorMessage(mapperUtil.convertToString(data.getErrors()));

                cartItemData.setWarning(!mapperUtil.isEmpty(data.getMessages()));
                cartItemData.setWarningMessage(mapperUtil.convertToString(data.getMessages()));

                cartItemDataList.add(cartItemData);
            }
            shopGroupData.setCartItemDataList(cartItemDataList);
            shopGroupDataList.add(shopGroupData);
        }
        cartListData.setShopGroupDataList(shopGroupDataList);
        cartListData.setPromoCouponActive(cartDataListResponse.getIsCouponActive() == 1);

        CartPromoSuggestion cartPromoSuggestion = new CartPromoSuggestion();
        cartPromoSuggestion.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestion.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestion.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestion.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestion.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);
        cartListData.setCartPromoSuggestion(cartPromoSuggestion);

        AutoApplyData autoApplyData = new AutoApplyData();
        autoApplyData.setCode(cartDataListResponse.getAutoApply().getCode());
        autoApplyData.setDiscountAmount(cartDataListResponse.getAutoApply().getDiscountAmount());
        autoApplyData.setIsCoupon(cartDataListResponse.getAutoApply().getIsCoupon());
        autoApplyData.setMessageSuccess(cartDataListResponse.getAutoApply().getMessageSuccess());
        autoApplyData.setPromoId(cartDataListResponse.getAutoApply().getPromoId());
        autoApplyData.setSuccess(cartDataListResponse.getAutoApply().isSuccess());
        autoApplyData.setTitleDescription(cartDataListResponse.getAutoApply().getTitleDescription());
        cartListData.setAutoApplyData(autoApplyData);

        return cartListData;
    }

    private String generateShopType(Shop shop) {
        if (shop.getIsOfficial() == 1)
            return SHOP_TYPE_OFFICIAL_STORE;
        else if (shop.getIsGold() == 1)
            return SHOP_TYPE_GOLD_MERCHANT;
        else return SHOP_TYPE_REGULER;
    }

    @Override
    public DeleteCartData convertToDeleteCartData(DeleteCartDataResponse deleteCartDataResponse) {
        return new DeleteCartData.Builder()
                .message(deleteCartDataResponse.getMessage())
                .success(deleteCartDataResponse.getSuccess() == 1)
                .build();
    }

    @Override
    public UpdateCartData convertToUpdateCartData(UpdateCartDataResponse updateCartDataResponse) {
        return new UpdateCartData.Builder()
                .goTo(updateCartDataResponse.get_goto())
                .message(updateCartDataResponse.getError())
                .success(updateCartDataResponse.isStatus())
                .build();
    }

    @Override
    public ResetCartData convertToResetCartData(ResetCartDataResponse resetCartDataResponse) {
        ResetCartData resetCartData = new ResetCartData();
        resetCartData.setSuccess(resetCartDataResponse.getSuccess() == 1);
        return resetCartData;
    }
}
