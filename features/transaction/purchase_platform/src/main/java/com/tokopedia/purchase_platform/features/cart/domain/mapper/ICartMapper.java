package com.tokopedia.purchase_platform.features.cart.domain.mapper;

import android.content.Context;

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse);

}
