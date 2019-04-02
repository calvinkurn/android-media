package com.tokopedia.loyalty.view.view;

import android.content.Context;

import com.tokopedia.loyalty.view.data.VoucherViewModel;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface IPromoCodeView extends IBaseView {

    void checkVoucherSuccessfull(VoucherViewModel voucherModel);

    void checkDigitalVoucherSucessful(VoucherViewModel voucherModel);

    void onGetGeneralError(String errorMessage);

    void onPromoCodeError(String errorMessage);

    Context getContext();

    void sendTrackingOnCheckTrainVoucherError(String errorMessage);

    void sendEventDigitalEventTracking(Context context, String text, String failmsg);
}
