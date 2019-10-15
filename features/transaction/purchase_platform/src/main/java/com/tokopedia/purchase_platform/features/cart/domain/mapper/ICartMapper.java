package com.tokopedia.purchase_platform.features.cart.domain.mapper;

import android.content.Context;

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ResetCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.Message;
import com.tokopedia.purchase_platform.features.cart.data.model.response.DeleteCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.ResetCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.UpdateCartDataResponse;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    CartListData convertToCartItemDataList(Context context, CartDataListResponse cartDataListResponse);

    CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse);

    DeleteCartData convertToDeleteCartData(DeleteCartDataResponse deleteCartDataResponse);

    UpdateCartData convertToUpdateCartData(UpdateCartDataResponse updateCartDataResponse);

    UpdateAndRefreshCartListData convertToUpdateAndRefreshCartData(UpdateCartDataResponse updateCartDataResponse);

    ResetCartData convertToResetCartData(ResetCartDataResponse resetCartDataResponse);

    MessageData convertToMessageData(Message message);
}
