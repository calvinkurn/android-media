package com.tokopedia.logisticinputreceiptshipment.view.confirmshipment;

import com.tokopedia.transaction.common.data.order.ListCourierViewModel;


/**
 * Created by kris on 1/5/18. Tokopedia
 */

public interface ConfirmShippingView {

    void receiveShipmentData(ListCourierViewModel model);

    void onSuccessConfirm(String successMessage);

    void showLoading();

    void hideLoading();

    void onShowError(String errorMessage);

    void onShowErrorConfirmShipping(String message);
}
