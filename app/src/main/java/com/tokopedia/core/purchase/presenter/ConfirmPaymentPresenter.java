package com.tokopedia.core.purchase.presenter;

import android.content.Context;

import com.tokopedia.core.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.core.purchase.model.ConfirmPaymentData;
import com.tokopedia.core.purchase.model.ConfirmationData;
import com.tokopedia.core.purchase.model.response.formconfirmpayment.Form;
import com.tokopedia.core.purchase.model.response.formconfirmpayment.FormEdit;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public interface ConfirmPaymentPresenter {
    void processChooseBank(Context context, ConfirmPaymentActivity.OnNewAccountBankSelected listener);

    void processGetEditPaymentForm(Context context, String confirmationId);

    void processGetConfirmPaymentForm(Context context, String confirmationId);

    void processSubmitConfirmation(Context context, ConfirmPaymentData data, Form formData, FormEdit formEditData);

    void setLocalyticsFlow(Context context, ConfirmationData data);

    void onDestroyView();

}
