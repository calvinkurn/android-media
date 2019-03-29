package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.content.Context;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressPresenter {

    void processGetCartList(String cartIds);

    void sendData(Context context, List<MultipleAddressAdapterData> dataList);

    List<MultipleAddressAdapterData> initiateMultipleAddressAdapterData(
            CartListData cartListData,
            RecipientAddressModel recipientAddressModel);

    void onUnsubscribe();

    CartListData getCartListData();

    void setCartListData(CartListData cartListData);

}
