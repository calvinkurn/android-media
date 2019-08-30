package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view;

import android.content.Context;

import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.MultipleAddressAdapterData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressPresenter {

    void attachView(IMultipleAddressView iMultipleAddressView);

    void detachView();

    void processGetCartList(String cartIds);

    void sendData(Context context, List<MultipleAddressAdapterData> dataList);

    List<MultipleAddressAdapterData> initiateMultipleAddressAdapterData(
            CartListData cartListData,
            RecipientAddressModel recipientAddressModel);

    CartListData getCartListData();

    void setCartListData(CartListData cartListData);

}
