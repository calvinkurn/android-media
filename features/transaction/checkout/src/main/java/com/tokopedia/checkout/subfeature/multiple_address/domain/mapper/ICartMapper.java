package com.tokopedia.checkout.subfeature.multiple_address.domain.mapper;

import android.content.Context;

import com.tokopedia.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;
import com.tokopedia.checkout.subfeature.multiple_address.domain.model.cartlist.CartListData;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    CartListData convertToCartItemDataList(Context context, CartMultipleAddressDataListResponse cartDataListResponse);

}
