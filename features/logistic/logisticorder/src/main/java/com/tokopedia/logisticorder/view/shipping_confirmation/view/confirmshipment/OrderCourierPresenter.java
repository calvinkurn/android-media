package com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment;

import android.content.Context;

import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.OrderDetailData;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.OrderDetailShipmentModel;


/**
 * Created by kris on 1/3/18. Tokopedia
 */

public interface OrderCourierPresenter {

    void setView(ConfirmShippingView view);

    void detachView();

    void onGetCourierList(Context context, OrderDetailData data);

    void onProcessCourier(Context context,
                          OrderDetailShipmentModel editableModel,
                          boolean isChangeCourier);

    void onConfirmShipping(Context context, OrderDetailShipmentModel editableModel);

}
