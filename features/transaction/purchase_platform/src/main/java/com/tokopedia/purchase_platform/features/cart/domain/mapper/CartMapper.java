package com.tokopedia.purchase_platform.features.cart.domain.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.data.model.response.TrackingDetail;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoapplyStack;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.Message;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.VoucherOrdersItem;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData;
import com.tokopedia.purchase_platform.common.feature.promo_global.domain.model.GlobalCouponAttrData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.SimilarProductData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.TickerData;
import com.tokopedia.purchase_platform.common.utils.UtilsKt;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDetail;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartList;
import com.tokopedia.purchase_platform.features.cart.data.model.response.DeleteCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.ResetCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.Shop;
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupAvailable;
import com.tokopedia.purchase_platform.features.cart.data.model.response.Ticker;
import com.tokopedia.purchase_platform.features.cart.data.model.response.UpdateCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartTickerErrorData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ResetCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.WholesalePriceData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;

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
    public CartMapper() {}

    @Override
    public CartListData convertToCartItemDataList(Context context, CartDataListResponse cartDataListResponse) {
        CartListData cartListData = new CartListData();
        String errorMessage = UtilsKt.convertToString(cartDataListResponse.getErrors());
        boolean hasError = false;
        int errorItemCount = 0;
        for (ShopGroupAvailable shopGroupAvailable : cartDataListResponse.getShopGroupAvailables()) {
            if (shopGroupAvailable.getErrors() != null && shopGroupAvailable.getErrors().size() > 0) {
                hasError = true;
                if (shopGroupAvailable.getCartDetails() != null) {
                    errorItemCount += shopGroupAvailable.getCartDetails().size();
                }
                if (TextUtils.isEmpty(errorMessage)) {
                    errorMessage = UtilsKt.convertToString(shopGroupAvailable.getErrors());
                }
            } else if (shopGroupAvailable.getCartDetails().size() > 0) {
                for (CartDetail cartDetail : shopGroupAvailable.getCartDetails()) {
                    if (cartDetail.getErrors() != null && cartDetail.getErrors().size() > 0) {
                        hasError = true;
                        errorItemCount++;
                        if (TextUtils.isEmpty(errorMessage)) {
                            errorMessage = UtilsKt.convertToString(cartDetail.getErrors());
                        }
                    }
                }
            }
        }
        cartListData.setError(hasError);
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

        if (cartDataListResponse.getTickers() != null && !cartDataListResponse.getTickers().isEmpty()) {
            Ticker ticker = cartDataListResponse.getTickers().get(0);
            cartListData.setTickerData(new TickerData(ticker.getId(), ticker.getMessage(), ticker.getPage()));
        }

        List<ShopGroupAvailableData> shopGroupDataList = new ArrayList<>();
        boolean isDisableAllProducts = true;
        for (ShopGroupAvailable shopGroupAvailable : cartDataListResponse.getShopGroupAvailables()) {
            ShopGroupAvailableData shopGroupData = new ShopGroupAvailableData();

            shopGroupData.setError(!UtilsKt.isNullOrEmpty(shopGroupAvailable.getErrors()));
            isDisableAllProducts = true;

            if (!shopGroupData.isError()) {
                int errorItemCountPerShop = 0;
                for (CartDetail cartDetail : shopGroupAvailable.getCartDetails()) {
                    if (cartDetail.getErrors() != null && cartDetail.getErrors().size() > 0) {
                        errorItemCountPerShop++;
                    }
                }

                boolean shopError = false;
                if (errorItemCountPerShop == shopGroupAvailable.getCartDetails().size()) {
                    shopError = true;
                    isDisableAllProducts = true;
                } else {
                    isDisableAllProducts = false;
                }
                shopGroupData.setError(shopError);
            }
            shopGroupData.setShopId(String.valueOf(shopGroupAvailable.getShop().getShopId()));
            shopGroupData.setShopName(shopGroupAvailable.getShop().getShopName());
            shopGroupData.setShopType(generateShopType(shopGroupAvailable.getShop()));
            shopGroupData.setGoldMerchant(shopGroupAvailable.getShop().getGoldMerchant().isGoldBadge());
            shopGroupData.setOfficialStore(shopGroupAvailable.getShop().getIsOfficial() == 1);
            shopGroupData.setFulfillment(shopGroupAvailable.isFulFillment());
            if (shopGroupAvailable.getShop().getIsOfficial() == 1) {
                shopGroupData.setShopBadge(shopGroupAvailable.getShop().getOfficialStore().getOsLogoUrl());
            } else if (shopGroupAvailable.getShop().getGoldMerchant().isGoldBadge()) {
                shopGroupData.setShopBadge(shopGroupAvailable.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
            }
            if (shopGroupAvailable.getWarehouse() != null) {
                shopGroupData.setFulfillmentName(shopGroupAvailable.getWarehouse().getCityName());
            }
            shopGroupData.setCartString(shopGroupAvailable.getCartString());
            shopGroupData.setHasPromoList(shopGroupAvailable.getHasPromoList());

            if (cartDataListResponse.getAutoapplyStack() != null && cartDataListResponse.getAutoapplyStack().getVoucherOrders() != null) {
                for (VoucherOrdersItem voucherOrdersItem : cartDataListResponse.getAutoapplyStack().getVoucherOrders()) {
                    if (voucherOrdersItem.getUniqueId().equals(shopGroupAvailable.getCartString())
                            && !voucherOrdersItem.getType().isEmpty()
                            && voucherOrdersItem.getType().equalsIgnoreCase(MERCHANT_VOUCHER_TYPE)) {
                        VoucherOrdersItemData voucherOrdersItemData = new VoucherOrdersItemData();
                        voucherOrdersItemData.setCode(voucherOrdersItem.getCode());
                        voucherOrdersItemData.setSuccess(voucherOrdersItem.isSuccess());
                        voucherOrdersItemData.setUniqueId(voucherOrdersItem.getUniqueId());
                        voucherOrdersItemData.setCartId(voucherOrdersItem.getCartId());
                        voucherOrdersItemData.setShopId(voucherOrdersItem.getShopId());
                        voucherOrdersItemData.setIsPO(voucherOrdersItem.getIsPo());
                        voucherOrdersItemData.setAddressId(voucherOrdersItem.getAddressId());
                        voucherOrdersItemData.setType(voucherOrdersItem.getType());
                        voucherOrdersItemData.setCashbackWalletAmount(voucherOrdersItem.getCashbackWalletAmount());
                        voucherOrdersItemData.setDiscountAmount(voucherOrdersItem.getDiscountAmount());
                        voucherOrdersItemData.setInvoiceDescription(voucherOrdersItem.getInvoiceDescription());
                        voucherOrdersItemData.setVariant(voucherOrdersItem.getType());
                        voucherOrdersItemData.setMessageData(convertToMessageData(voucherOrdersItem.getMessage()));
                        voucherOrdersItemData.setIsAutoapply(true);
                        shopGroupData.setVoucherOrdersItemData(voucherOrdersItemData);
                        break;
                    }
                }
            }

            List<CartItemData> cartItemDataList = new ArrayList<>();
            for (CartDetail data : shopGroupAvailable.getCartDetails()) {
                CartItemData cartItemData = new CartItemData();

                CartItemData.OriginData cartItemDataOrigin = new CartItemData.OriginData();
                cartItemDataOrigin.setCheckboxState(data.isCheckboxState());
                cartItemDataOrigin.setProductVarianRemark(
                        data.getProduct().getProductNotes()
                );
                cartItemDataOrigin.setCartId(data.getCartId());
                cartItemDataOrigin.setCartString(shopGroupAvailable.getCartString());
                cartItemDataOrigin.setWeightFormatted(data.getProduct().getProductWeightFmt());
                cartItemDataOrigin.setWeightUnit(data.getProduct().getProductWeightUnitCode());
                cartItemDataOrigin.setWeightPlan(data.getProduct().getProductWeight());
                cartItemDataOrigin.setProductName(data.getProduct().getProductName());
                cartItemDataOrigin.setParentId(String.valueOf(data.getProduct().getParentId()));
                cartItemDataOrigin.setProductId(String.valueOf(data.getProduct().getProductId()));
                cartItemDataOrigin.setPriceFormatted(data.getProduct().getProductPriceFmt());
                cartItemDataOrigin.setPricePlan(data.getProduct().getProductPrice());
                cartItemDataOrigin.setPricePlanInt(data.getProduct().getProductPrice());
                cartItemDataOrigin.setPriceOriginal(data.getProduct().getProductOriginalPrice());
                cartItemDataOrigin.setPriceCurrency(data.getProduct().getProductPriceCurrency());
                cartItemDataOrigin.setPreOrder(data.getProduct().getIsPreorder() == 1);
                if (data.getProduct().getProductPreorder() != null
                        && data.getProduct().getProductPreorder().getDurationText() != null) {
                    cartItemDataOrigin.setPreOrderInfo("PO " + data.getProduct().getProductPreorder().getDurationText());
                }
                cartItemDataOrigin.setCod(data.getProduct().isCod());
                cartItemDataOrigin.setFavorite(false);
                cartItemDataOrigin.setMinOrder(data.getProduct().getProductMinOrder());
                if (data.getProduct().getProductSwitchInvenage() == 0) {
                    cartItemDataOrigin.setMaxOrder(data.getProduct().getProductMaxOrder());
                } else if (data.getProduct().getProductMaxOrder() <= data.getProduct().getProductInvenageValue()) {
                    cartItemDataOrigin.setMaxOrder(data.getProduct().getProductMaxOrder());
                } else {
                    cartItemDataOrigin.setMaxOrder(data.getProduct().getProductInvenageValue());
                }
                cartItemDataOrigin.setPriceChangesState(data.getProduct().getPriceChanges().getChangesState());
                cartItemDataOrigin.setPriceChangesDesc(data.getProduct().getPriceChanges().getDescription());
                cartItemDataOrigin.setProductInvenageByUserText(data.getProduct().getProductInvenageTotal().getByUserText().getComplete());
                cartItemDataOrigin.setProductInvenageByUserInCart(data.getProduct().getProductInvenageTotal().getByUser().getInCart());
                cartItemDataOrigin.setProductInvenageByUserLastStockLessThan(data.getProduct().getProductInvenageTotal().getByUser().getLastStockLessThan());
                cartItemDataOrigin.setFreeReturn(data.getProduct().getIsFreereturns() == 1);
                cartItemDataOrigin.setTrackerAttribution(data.getProduct().getProductTrackerData().getAttribution());
                cartItemDataOrigin.setTrackerListName(data.getProduct().getProductTrackerData().getTrackerListName());
                if (data.getProduct().getFreeReturns() != null) {
                    cartItemDataOrigin.setFreeReturnLogo(data.getProduct().getFreeReturns().getFreeReturnsLogo());
                }
                cartItemDataOrigin.setCashBack(!UtilsKt.isNullOrEmpty(data.getProduct().getProductCashback()));
                cartItemDataOrigin.setProductCashBack(data.getProduct().getProductCashback());
                cartItemDataOrigin.setCashBackInfo("Cashback " + data.getProduct().getProductCashback());
                cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());
                cartItemDataOrigin.setCategory(data.getProduct().getCategory());
                cartItemDataOrigin.setCategoryId(String.valueOf(data.getProduct().getCategoryId()));
                cartItemDataOrigin.setOriginalRemark(cartItemDataOrigin.getProductVarianRemark());
                cartItemDataOrigin.setOriginalQty(data.getProduct().getProductQuantity());
                cartItemDataOrigin.setShopName(shopGroupAvailable.getShop().getShopName());
                cartItemDataOrigin.setShopCity(shopGroupAvailable.getShop().getCityName());
                cartItemDataOrigin.setGoldMerchant(shopGroupAvailable.getShop().getGoldMerchant().isGoldBadge());
                cartItemDataOrigin.setOfficialStore(shopGroupAvailable.getShop().getIsOfficial() == 1);
                cartItemDataOrigin.setShopId(String.valueOf(shopGroupAvailable.getShop().getShopId()));
                cartItemDataOrigin.setShopType(generateShopType(shopGroupAvailable.getShop()));
                cartItemDataOrigin.setWishlisted(data.getProduct().isWishlisted());
                cartItemDataOrigin.setWarehouseId(shopGroupAvailable.getWarehouse().getWarehouseId());
                if (data.getProduct().getFreeShipping() != null && data.getProduct().getFreeShipping().getEligible() &&
                        !TextUtils.isEmpty(data.getProduct().getFreeShipping().getBadgeUrl())) {
                    cartItemDataOrigin.setFreeShipping(true);
                    cartItemDataOrigin.setFreeShippingBadgeUrl(data.getProduct().getFreeShipping().getBadgeUrl());
                }
                if (data.getProduct().getWholesalePrice() != null) {
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
                }

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
                cartItemData.setFulfillment(shopGroupAvailable.isFulFillment());

                cartItemData.setSingleChild(shopGroupAvailable.getCartDetails().size() == 1);

                if (data.getErrors() != null && data.getErrors().size() > 0) {
                    cartItemData.setError(true);
                    cartItemData.setErrorMessageTitle(data.getErrors().get(0));
                    com.tokopedia.purchase_platform.features.cart.data.model.response.SimilarProduct dataSimilarProduct = data.getSimilarProduct();
                    if (dataSimilarProduct != null && !TextUtils.isEmpty(dataSimilarProduct.getText()) && !TextUtils.isEmpty(dataSimilarProduct.getUrl())) {
                        cartItemData.setSimilarProductData(new SimilarProductData(dataSimilarProduct.getText(), dataSimilarProduct.getUrl()));
                    }

                    if (data.getErrors().size() > 1) {
                        cartItemData.setErrorMessageDescription(UtilsKt.convertToString(
                                data.getErrors().subList(1, data.getErrors().size() - 1)));
                    }
                }

                if (data.getMessages() != null && data.getMessages().size() > 0) {
                    cartItemData.setWarning(true);
                    cartItemData.setWarningMessageTitle(data.getMessages().get(0));

                    if (data.getMessages().size() > 1) {
                        cartItemData.setWarningMessageDescription(UtilsKt.convertToString(
                                data.getMessages().subList(1, data.getMessages().size() - 1)));
                    }
                }

                if (!shopGroupData.isError() && !shopGroupData.isWarning()) {
                    cartItemData.setParentHasErrorOrWarning(false);
                } else {
                    cartItemData.setParentHasErrorOrWarning(true);
                }
                cartItemData.setDisableAllProducts(isDisableAllProducts);

                if (!cartItemData.isError() && shopGroupData.isError()) {
                    cartItemData.setError(true);
                }

                cartItemDataList.add(cartItemData);
            }
            if (shopGroupData.isError() && TextUtils.isEmpty(shopGroupData.getErrorTitle())) {
                shopGroupData.setErrorTitle(UtilsKt.convertToString(shopGroupAvailable.getErrors()));
            }
            shopGroupData.setCartItemDataList(cartItemDataList);
            shopGroupData.setChecked(shopGroupAvailable.getCheckboxState());
            shopGroupDataList.add(shopGroupData);
        }
        cartListData.setShopGroupAvailableDataList(shopGroupDataList);
        cartListData.setAllSelected(cartDataListResponse.isGlobalCheckboxState());
        cartListData.setPromoCouponActive(cartDataListResponse.getIsCouponActive() == 1);

        CartPromoSuggestionHolderData cartPromoSuggestionHolderData = new CartPromoSuggestionHolderData();
        cartPromoSuggestionHolderData.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestionHolderData.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestionHolderData.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestionHolderData.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestionHolderData.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);
        cartListData.setCartPromoSuggestionHolderData(cartPromoSuggestionHolderData);

        GlobalCouponAttrData globalCouponAttrData = new GlobalCouponAttrData();
        if (cartDataListResponse.getGlobalCouponAttr() != null && cartDataListResponse.getGlobalCouponAttr().getDescription() != null) {
            globalCouponAttrData.setDescription(cartDataListResponse.getGlobalCouponAttr().getDescription());
            globalCouponAttrData.setQuantityLabel(cartDataListResponse.getGlobalCouponAttr().getQuantityLabel());
        }
        cartListData.setGlobalCouponAttrData(globalCouponAttrData);

        AutoApplyStackData autoApplyStackData = new AutoApplyStackData();
        AutoapplyStack autoapplyStack = cartDataListResponse.getAutoapplyStack();
        if (autoapplyStack != null) {
            autoApplyStackData = new AutoApplyStackData();
            if (autoapplyStack.getCodes() != null) {
                if (autoapplyStack.getCodes().size() > 0) {
                    autoApplyStackData.setCode(autoapplyStack.getCodes().get(0));
                }
                autoApplyStackData.setDiscountAmount(autoapplyStack.getDiscountAmount());
                autoApplyStackData.setIsCoupon(autoapplyStack.getIsCoupon());
                if (autoapplyStack.getMessage() != null) {
                    autoApplyStackData.setMessageSuccess(autoapplyStack.getMessage().getText());
                    autoApplyStackData.setState(autoapplyStack.getMessage().getState());
                }
                autoApplyStackData.setPromoCodeId(autoapplyStack.getPromoCodeId());
                autoApplyStackData.setSuccess(autoapplyStack.isSuccess());
                autoApplyStackData.setTitleDescription(autoapplyStack.getTitleDescription());
                if (autoapplyStack.getVoucherOrders() != null) {
                    if (autoapplyStack.getVoucherOrders().size() > 0) {
                        List<VoucherOrdersItemData> voucherOrdersItemDataList = new ArrayList<>();
                        for (VoucherOrdersItem voucherOrdersItem : autoapplyStack.getVoucherOrders()) {
                            VoucherOrdersItemData voucherOrdersItemData = new VoucherOrdersItemData();
                            voucherOrdersItemData.setCode(voucherOrdersItem.getCode());
                            voucherOrdersItemData.setSuccess(voucherOrdersItem.isSuccess());
                            voucherOrdersItemData.setUniqueId(voucherOrdersItem.getUniqueId());
                            voucherOrdersItemData.setCartId(voucherOrdersItem.getCartId());
                            voucherOrdersItemData.setShopId(voucherOrdersItem.getShopId());
                            voucherOrdersItemData.setIsPO(voucherOrdersItem.getIsPo());
                            voucherOrdersItemData.setAddressId(voucherOrdersItem.getAddressId());
                            voucherOrdersItemData.setType(voucherOrdersItem.getType());
                            voucherOrdersItemData.setCashbackWalletAmount(voucherOrdersItem.getCashbackWalletAmount());
                            voucherOrdersItemData.setDiscountAmount(voucherOrdersItem.getDiscountAmount());
                            voucherOrdersItemData.setInvoiceDescription(voucherOrdersItem.getInvoiceDescription());
                            voucherOrdersItemData.setMessageData(convertToMessageData(voucherOrdersItem.getMessage()));
                            voucherOrdersItemData.setIsAutoapply(true);
                            voucherOrdersItemDataList.add(voucherOrdersItemData);
                        }
                        autoApplyStackData.setVoucherOrdersItemDataList(voucherOrdersItemDataList);
                    }
                }
            }

            if (autoapplyStack.getTrackingDetails() != null && autoapplyStack.getTrackingDetails().size() > 0) {
                for (TrackingDetail trackingDetail : autoapplyStack.getTrackingDetails()) {
                    for (ShopGroupAvailableData shopGroupData : shopGroupDataList) {
                        for (CartItemHolderData cartItemHolderData : shopGroupData.getCartItemDataList()) {
                            CartItemData.OriginData originData = cartItemHolderData.getCartItemData().getOriginData();
                            if (originData.getProductId().equalsIgnoreCase(String.valueOf(trackingDetail.getProductId()))) {
                                originData.setPromoCodes(trackingDetail.getPromoCodesTracking());
                                originData.setPromoDetails(trackingDetail.getPromoDetailsTracking());
                            }
                        }
                    }
                }
            }

        }
        cartListData.setAutoApplyStackData(autoApplyStackData);

        return cartListData;
    }

    @Override
    public CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse) {
        CartListData cartListData = new CartListData();
        String errorMessage = UtilsKt.convertToString(cartDataListResponse.getErrors());
        boolean hasError = false;
        for (CartList cartList : cartDataListResponse.getCartList()) {
            if (cartList.getErrors() != null && cartList.getErrors().size() > 0) {
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
            cartItemDataOrigin.setPreOrder(data.getProduct().getIsPreorder() == 1);
            if (data.getProduct().getProductPreorder() != null
                    && data.getProduct().getProductPreorder().getDurationText() != null) {
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
            cartItemDataOrigin.setFreeReturn(data.getProduct().getIsFreereturns() == 1);
            cartItemDataOrigin.setTrackerAttribution(data.getProduct().getProductTrackerData().getAttribution());
            cartItemDataOrigin.setTrackerListName(data.getProduct().getProductTrackerData().getTrackerListName());
            if (data.getProduct().getFreeReturns() != null) {
                cartItemDataOrigin.setFreeReturnLogo(data.getProduct().getFreeReturns().getFreeReturnsLogo());
            }
            cartItemDataOrigin.setCashBack(!UtilsKt.isNullOrEmpty(data.getProduct().getProductCashback()));
            cartItemDataOrigin.setCashBackInfo("Cashback " + data.getProduct().getProductCashback());
            cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());
            cartItemDataOrigin.setCategory(data.getProduct().getCategory());
            cartItemDataOrigin.setCategoryId(String.valueOf(data.getProduct().getCategoryId()));
            cartItemDataOrigin.setGoldMerchant(data.getShop().getGoldMerchant().isGoldBadge());
            cartItemDataOrigin.setGoldMerchantLogoUrl(data.getShop().getGoldMerchant().getGoldMerchantLogoUrl());
            cartItemDataOrigin.setOfficialStore(data.getShop().getOfficialStore().isOfficial() == 1);
            cartItemDataOrigin.setOfficialStoreLogoUrl(data.getShop().getOfficialStore().getOsLogoUrl());
            if (data.getProduct().getWholesalePrice() != null) {
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
            }

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

            if (data.getErrors() != null && data.getErrors().size() > 0) {
                cartItemData.setError(true);
                cartItemData.setErrorMessageTitle(data.getErrors().get(0));

                if (data.getErrors().size() > 1) {
                    cartItemData.setErrorMessageDescription(UtilsKt.convertToString(
                            data.getErrors().subList(1, data.getErrors().size() - 1)));
                }
            }

            if (data.getMessages() != null && data.getMessages().size() > 0) {
                cartItemData.setWarning(true);
                cartItemData.setWarningMessageTitle(data.getMessages().get(0));

                if (data.getMessages().size() > 1) {
                    cartItemData.setWarningMessageDescription(UtilsKt.convertToString(
                            data.getMessages().subList(1, data.getMessages().size() - 1)));
                }
            }

            cartItemDataList.add(cartItemData);
        }

        CartPromoSuggestionHolderData cartPromoSuggestionHolderData = new CartPromoSuggestionHolderData();
        cartPromoSuggestionHolderData.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestionHolderData.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestionHolderData.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestionHolderData.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestionHolderData.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);

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
        cartListData.setCartPromoSuggestionHolderData(cartPromoSuggestionHolderData);

        if (cartDataListResponse.getAutoapplyStack() != null) {
            AutoApplyStackData autoApplyStackData = new AutoApplyStackData();
            autoApplyStackData.setCode(cartDataListResponse.getAutoapplyStack().getCodes().get(0));
            autoApplyStackData.setDiscountAmount(cartDataListResponse.getAutoapplyStack().getDiscountAmount());
            autoApplyStackData.setIsCoupon(cartDataListResponse.getAutoapplyStack().getIsCoupon());
            autoApplyStackData.setMessageSuccess(cartDataListResponse.getAutoapplyStack().getMessage().getText());
            autoApplyStackData.setPromoCodeId(cartDataListResponse.getAutoapplyStack().getPromoCodeId());
            autoApplyStackData.setSuccess(cartDataListResponse.getAutoapplyStack().isSuccess());
            autoApplyStackData.setTitleDescription(cartDataListResponse.getAutoapplyStack().getTitleDescription());
            autoApplyStackData.setState(cartDataListResponse.getAutoapplyStack().getMessage().getState());

            List<VoucherOrdersItemData> voucherOrdersItemDataList = new ArrayList<>();
            for (VoucherOrdersItem voucherOrdersItem : cartDataListResponse.getAutoapplyStack().getVoucherOrders()) {
                VoucherOrdersItemData voucherOrdersItemData = new VoucherOrdersItemData();
                voucherOrdersItemData.setCode(voucherOrdersItem.getCode());
                voucherOrdersItemData.setSuccess(voucherOrdersItem.isSuccess());
                voucherOrdersItemData.setUniqueId(voucherOrdersItem.getUniqueId());
                voucherOrdersItemData.setCartId(voucherOrdersItem.getCartId());
                voucherOrdersItemData.setShopId(voucherOrdersItem.getShopId());
                voucherOrdersItemData.setIsPO(voucherOrdersItem.getIsPo());
                voucherOrdersItemData.setAddressId(voucherOrdersItem.getAddressId());
                voucherOrdersItemData.setType(voucherOrdersItem.getType());
                voucherOrdersItemData.setCashbackWalletAmount(voucherOrdersItem.getCashbackWalletAmount());
                voucherOrdersItemData.setDiscountAmount(voucherOrdersItem.getDiscountAmount());
                voucherOrdersItemData.setInvoiceDescription(voucherOrdersItem.getInvoiceDescription());
                voucherOrdersItemData.setMessageData(convertToMessageData(voucherOrdersItem.getMessage()));
                voucherOrdersItemData.setIsAutoapply(true);
                voucherOrdersItemDataList.add(voucherOrdersItemData);
            }
            autoApplyStackData.setVoucherOrdersItemDataList(voucherOrdersItemDataList);
            cartListData.setAutoApplyStackData(autoApplyStackData);
        }

        cartListData.setShowOnboarding(cartDataListResponse.isShowOnboarding());

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

    @Override
    public MessageData convertToMessageData(Message message) {
        MessageData messageData = new MessageData();
        messageData.setColor(message.getColor());
        messageData.setState(message.getState());
        messageData.setText(message.getText());
        return messageData;
    }
}
