package com.tokopedia.purchase_platform.features.cart.domain.mapper;

import android.content.Context;

import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.Message;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData;
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse);

    UpdateCartData convertToUpdateCartData(UpdateCartDataResponse updateCartDataResponse);

    MessageData convertToMessageData(Message message);
}
