package com.tokopedia.checkout.domain.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.cartlist.WholesalePrice;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartList;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ResetCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartList;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartMapper implements ICartMapper {
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
            cartItemDataOrigin.setProductId(String.valueOf(data.getProduct().getProductId()));
            cartItemDataOrigin.setPriceFormatted(data.getProduct().getProductPriceFmt());
            cartItemDataOrigin.setPricePlan(data.getProduct().getProductPrice());
            cartItemDataOrigin.setPriceCurrency(data.getProduct().getProductPriceCurrency());
            cartItemDataOrigin.setPreOrder(data.getProduct().getIsPreorder() == 1);
            cartItemDataOrigin.setFavorite(false);
            cartItemDataOrigin.setMinimalQtyOrder(data.getProduct().getProductMinOrder());
            cartItemDataOrigin.setInvenageValue(data.getProduct().getProductInvenageValue());
            cartItemDataOrigin.setFreeReturn(data.getProduct().getIsFreereturns() == 1);
            if (!mapperUtil.isEmpty(data.getProduct().getFreeReturns())) {
                cartItemDataOrigin.setFreeReturnLogo(data.getProduct().getFreeReturns().getFreeReturnsLogo());
            }
            cartItemDataOrigin.setCashBack(!mapperUtil.isEmpty(data.getProduct().getProductCashback()));
            cartItemDataOrigin.setCashBackInfo("Cashback " + data.getProduct().getProductCashback());
            cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());
            cartItemDataOrigin.setCategory(data.getProduct().getCategory());
            cartItemDataOrigin.setCategory(data.getProduct().getCategory().replace(">", "/"));
            cartItemDataOrigin.setCategoryId(String.valueOf(data.getProduct().getCategoryId()));
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

        CartPromoSuggestion cartPromoSuggestion = new CartPromoSuggestion();
        cartPromoSuggestion.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestion.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestion.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestion.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestion.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);


//        cartPromoSuggestion.setCta("Gunakan Sekarang!");
//        cartPromoSuggestion.setCtaColor("#42b549");
//        cartPromoSuggestion.setPromoCode("ajicash");
//        cartPromoSuggestion.setText("[iOS] Cashback hingga 25% menggunakan Promo <b>TOKOCASH</b> !");
//        cartPromoSuggestion.setVisible(true);


        cartListData.setCartItemDataList(cartItemDataList);
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
