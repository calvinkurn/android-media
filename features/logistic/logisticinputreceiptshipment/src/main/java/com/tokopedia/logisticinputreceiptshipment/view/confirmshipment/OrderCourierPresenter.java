package com.tokopedia.logisticinputreceiptshipment.view.confirmshipment;

import android.content.Context;

import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailShipmentModel;


/**
 * Created by kris on 1/3/18. Tokopedia
 */

public interface OrderCourierPresenter {

    void setView(ConfirmShippingView view);

    void onGetCourierList(Context context, OrderDetailData data);

    void onProcessCourier(Context context,
                          OrderDetailShipmentModel editableModel,
                          boolean isChangeCourier);

    void onConfirmShipping(Context context, OrderDetailShipmentModel editableModel);

}
