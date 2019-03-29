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
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.WholesalePrice;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartList;
import com.tokopedia.transactiondata.entity.response.cartlist.CartMultipleAddressDataListResponse;
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
        int errorItemCount = 0;
        for (ShopGroup shopGroup : cartDataListResponse.getShopGroups()) {
            if (shopGroup.getErrors() != null && shopGroup.getErrors().size() > 0) {
                hasError = true;
                if (shopGroup.getCartDetails() != null) {
                    errorItemCount += shopGroup.getCartDetails().size();
                }
                if (TextUtils.isEmpty(errorMessage)) {
                    errorMessage = mapperUtil.convertToString(shopGroup.getErrors());
                }
            } else if (shopGroup.getCartDetails().size() > 0) {
                for (CartDetail cartDetail : shopGroup.getCartDetails()) {
                    if (cartDetail.getErrors() != null && cartDetail.getErrors().size() > 0) {
                        hasError = true;
                        errorItemCount++;
                        if (TextUtils.isEmpty(errorMessage)) {
                            errorMessage = mapperUtil.convertToString(cartDetail.getErrors());
                        }
                    }
                }
            }
        }
        cartListData.setError(!TextUtils.isEmpty(errorMessage) || hasError);
        cartListData.setErrorMessage(errorMessage);
        if (cartListData.isError()) {
            cartListData.setCartTickerErrorData(
                    new CartTickerErrorData.Builder()
                            .errorInfo(String.format(context.getString(R.string.cart_error_message), errorItemCount))
                            .actionInfo(context.getString(R.string.cart_error_action))
                            .build()
            );
        }
        cartListData.setDefaultPromoDialogTab(cartDataListResponse.getDefaultPromoDialogTab());

        List<ShopGroupData> shopGroupDataList = new ArrayList<>();
        for (ShopGroup shopGroup : cartDataListResponse.getShopGroups()) {
            ShopGroupData shopGroupData = new ShopGroupData();
            shopGroupData.setError(!mapperUtil.isEmpty(shopGroup.getErrors()));
            shopGroupData.setErrorTitle(mapperUtil.convertToString(shopGroup.getErrors()));
            shopGroupData.setShopId(String.valueOf(shopGroup.getShop().getShopId()));
            shopGroupData.setShopName(shopGroup.getShop().getShopName());
            shopGroupData.setShopType(generateShopType(shopGroup.getShop()));
            shopGroupData.setGoldMerchant(shopGroup.getShop().getGoldMerchant().isGoldBadge());
            shopGroupData.setOfficialStore(shopGroup.getShop().getIsOfficial() == 1);
            shopGroupData.setFulfillment(shopGroup.isFulFillment());
            if (shopGroup.getShop().getIsOfficial() == 1) {
                shopGroupData.setShopBadge(shopGroup.getShop().getOfficialStore().getOsLogoUrl());
            } else if (shopGroup.getShop().getGoldMerchant().isGoldBadge()) {
                shopGroupData.setShopBadge(shopGroup.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
            }
            if (shopGroup.getWarehouse() != null) {
                shopGroupData.setFulfillmentName(shopGroup.getWarehouse().getCityName());
            }

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
                if (data.getProduct().getProductPreorder() != null
                        && data.getProduct().getProductPreorder().getDurationText() != null) {
                    cartItemDataOrigin.setPreOrderInfo("PO " + data.getProduct().getProductPreorder().getDurationText());
                }
                cartItemDataOrigin.setCod(data.getProduct().isCod());
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
                cartItemDataOrigin.setOriginalQty(data.getProduct().getProductQuantity());
                cartItemDataOrigin.setShopName(shopGroup.getShop().getShopName());
                cartItemDataOrigin.setGoldMerchant(shopGroup.getShop().getGoldMerchant().isGoldBadge());
                cartItemDataOrigin.setOfficialStore(shopGroup.getShop().getIsOfficial() == 1);
                cartItemDataOrigin.setShopName(shopGroup.getShop().getShopName());
                cartItemDataOrigin.setShopId(String.valueOf(shopGroup.getShop().getShopId()));
                cartItemDataOrigin.setShopType(generateShopType(shopGroup.getShop()));
                cartItemDataOrigin.setWishlisted(data.getProduct().isWishlisted());
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

                cartItemData.setSingleChild(shopGroup.getCartDetails().size() == 1);

                if (data.getErrors() != null && data.getErrors().size() > 0) {
                    cartItemData.setError(true);
                    cartItemData.setErrorMessageTitle(data.getErrors().get(0));

                    if (data.getErrors().size() > 1) {
                        cartItemData.setErrorMessageDescription(mapperUtil.convertToString(
                                data.getErrors().subList(1, data.getErrors().size() - 1)));
                    }
                }

                if (data.getMessages() != null && data.getMessages().size() > 0) {
                    cartItemData.setWarning(true);
                    cartItemData.setWarningMessageTitle(data.getMessages().get(0));

                    if (data.getMessages().size() > 1) {
                        cartItemData.setWarningMessageDescription(mapperUtil.convertToString(
                                data.getMessages().subList(1, data.getMessages().size() - 1)));
                    }
                }

                if (cartItemData.isSingleChild()) {
                    if (!shopGroupData.isError() && !shopGroupData.isWarning()) {
                        cartItemData.setParentHasErrorOrWarning(false);
                        if (cartItemData.isError()) {
                            shopGroupData.setError(true);
                            shopGroupData.setErrorTitle(cartItemData.getErrorMessageTitle());
                            shopGroupData.setErrorDescription(cartItemData.getErrorMessageDescription());
                        } else if (cartItemData.isWarning()) {
                            shopGroupData.setWarning(true);
                            shopGroupData.setWarningTitle(cartItemData.getWarningMessageTitle());
                            shopGroupData.setWarningDescription(cartItemData.getWarningMessageDescription());
                        }
                    } else {
                        cartItemData.setParentHasErrorOrWarning(true);
                    }
                }

                if (!cartItemData.isError() && shopGroupData.isError()) {
                    cartItemData.setError(true);
                }

                cartItemDataList.add(cartItemData);
            }
            shopGroupData.setCartItemDataList(cartItemDataList, shopGroupData.isError());
            shopGroupDataList.add(shopGroupData);
        }
        cartListData.setShopGroupDataList(shopGroupDataList);
        cartListData.setAllSelected(true);
        cartListData.setPromoCouponActive(cartDataListResponse.getIsCouponActive() == 1);

        CartPromoSuggestion cartPromoSuggestion = new CartPromoSuggestion();
        cartPromoSuggestion.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestion.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestion.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestion.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestion.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);
        cartListData.setCartPromoSuggestion(cartPromoSuggestion);

        AutoApplyData autoApplyData = new AutoApplyData();
        autoApplyData.setCode(cartDataListResponse.getAutoapplyV2().getCode());
        autoApplyData.setDiscountAmount(cartDataListResponse.getAutoApply().getDiscountAmount());
        autoApplyData.setIsCoupon(cartDataListResponse.getAutoapplyV2().getIsCoupon());
        autoApplyData.setMessageSuccess(cartDataListResponse.getAutoapplyV2().getMessage().getText());
        int promoId = 0;
        if (!TextUtils.isEmpty(cartDataListResponse.getAutoapplyV2().getPromoCodeId())) {
            Integer.valueOf(cartDataListResponse.getAutoapplyV2().getPromoCodeId());
        }
        autoApplyData.setPromoId(promoId);
        autoApplyData.setSuccess(cartDataListResponse.getAutoApply().isSuccess());
        autoApplyData.setTitleDescription(cartDataListResponse.getAutoapplyV2().getTitleDescription());
        autoApplyData.setState(cartDataListResponse.getAutoapplyV2().getMessage().getState());
        cartListData.setAutoApplyData(autoApplyData);

        return cartListData;
    }

    @Override
    public CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse) {
        CartListData cartListData = new CartListData();
        String errorMessage = mapperUtil.convertToString(cartDataListResponse.getErrors());
        boolean hasError = false;
        for (CartList cartList : cartDataListResponse.getCartList()) {
            if (cartList.getErrors() != null && cartList.getErrors().size() > 0) {
                hasError = true;
                if (TextUtils.isEmpty(errorMessage)) {
                    errorMessage = mapperUtil.convertToString(cartList.getErrors());
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

        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartList data : cartDataListResponse.getCartList()) {
            CartItemData cartItemData = new CartItemData();

            CartItemData.OriginData cartItemDataOrigin = new CartItemData.OriginData();
            cartItemDataOrigin.setProductVarianRemark(
                    data.getProduct().getProductNotes()
            );
            cartItemDataOrigin.setCartId(data.getCartId());
            cartItemDataOrigin.setShopId(String.valueOf(data.getShop().getShopId()));
            cartItemDataOrigin.setShopName(data.getShop().getShopName());
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
            cartItemDataOrigin.setPreOrder(data.getProduct().getIsPreorder() == 1);
            if (data.getProduct().getProductPreorder() != null
                    && data.getProduct().getProductPreorder().getDurationText() != null) {
                cartItemDataOrigin.setPreOrderInfo("PO " + data.getProduct().getProductPreorder().getDurationText());
            }
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
            cartItemDataOrigin.setCashBackInfo("Cashback " + data.getProduct().getProductCashback());
            cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());
            cartItemDataOrigin.setCategory(data.getProduct().getCategory());
            cartItemDataOrigin.setCategoryId(String.valueOf(data.getProduct().getCategoryId()));
            cartItemDataOrigin.setGoldMerchant(data.getShop().getGoldMerchant().isGoldBadge());
            cartItemDataOrigin.setGoldMerchantLogoUrl(data.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
            cartItemDataOrigin.setOfficialStore(data.getShop().getOfficialStore().isOfficial() == 1);
            cartItemDataOrigin.setOfficialStoreLogoUrl(data.getShop().getOfficialStore().getOsLogoUrl());
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

            if (data.getErrors() != null && data.getErrors().size() > 0) {
                cartItemData.setError(true);
                cartItemData.setErrorMessageTitle(data.getErrors().get(0));

                if (data.getErrors().size() > 1) {
                    cartItemData.setErrorMessageDescription(mapperUtil.convertToString(
                            data.getErrors().subList(1, data.getErrors().size() - 1)));
                }
            }

            if (data.getMessages() != null && data.getMessages().size() > 0) {
                cartItemData.setWarning(true);
                cartItemData.setWarningMessageTitle(data.getMessages().get(0));

                if (data.getMessages().size() > 1) {
                    cartItemData.setWarningMessageDescription(mapperUtil.convertToString(
                            data.getMessages().subList(1, data.getMessages().size() - 1)));
                }
            }

            cartItemDataList.add(cartItemData);
        }

        CartPromoSuggestion cartPromoSuggestion = new CartPromoSuggestion();
        cartPromoSuggestion.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestion.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestion.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestion.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestion.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);

        List<ShopGroupData> shopGroupDataList = new ArrayList<>();
        for (CartItemData cartItemData : cartItemDataList) {
            ShopGroupData shopGroupData = new ShopGroupData();
            List<CartItemData> itemDataList = new ArrayList<>();
            itemDataList.add(cartItemData);
            shopGroupData.setCartItemDataList(itemDataList, false);
            shopGroupDataList.add(shopGroupData);
        }
        cartListData.setShopGroupDataList(shopGroupDataList);
        cartListData.setPromoCouponActive(cartDataListResponse.getIsCouponActive() == 1);
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
        else if (shop.getGoldMerchant().isGoldBadge())
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

    @Override
    public UpdateAndRefreshCartListData convertToUpdateAndRefreshCartData(UpdateCartDataResponse updateCartDataResponse) {
        UpdateCartData updateCartData = new UpdateCartData.Builder()
                .goTo(updateCartDataResponse.get_goto())
                .message(updateCartDataResponse.getError())
                .success(updateCartDataResponse.isStatus())
                .build();
        UpdateAndRefreshCartListData updateAndRefreshCartListData = new UpdateAndRefreshCartListData();
        updateAndRefreshCartListData.setUpdateCartData(updateCartData);
        return null;
    }
}
