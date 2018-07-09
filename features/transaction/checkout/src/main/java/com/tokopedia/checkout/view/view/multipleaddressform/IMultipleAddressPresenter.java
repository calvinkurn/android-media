package com.tokopedia.checkout.view.view.multipleaddressform;

import android.content.Context;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressPresenter {

    void sendData(Context context, List<MultipleAddressAdapterData> dataList);

    List<MultipleAddressAdapterData> initiateMultipleAddressAdapterData(
            CartListData cartListData,
            RecipientAddressModel recipientAddressModel
    );

    void onUnsubscribe();

}
