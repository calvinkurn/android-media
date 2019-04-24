package com.tokopedia.checkout.domain.mapper;

import android.content.Context;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ResetCartData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.datamodel.promostacking.MessageData;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartMultipleAddressDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.Message;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;

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
