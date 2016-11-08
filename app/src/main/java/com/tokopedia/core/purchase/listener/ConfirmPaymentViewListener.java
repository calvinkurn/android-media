package com.tokopedia.core.purchase.listener;

import android.view.View;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.core.purchase.model.ConfirmationData;
import com.tokopedia.core.purchase.model.response.formconfirmpayment.FormConfPaymentData;
import com.tokopedia.core.purchase.model.response.formconfirmpayment.FormEditPaymentData;

/**
 * ConfirmPaymentViewListener
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public interface ConfirmPaymentViewListener extends ViewListener {
    void renderFormConfirmation(FormConfPaymentData data);

    void renderErrorPaymentMethod(String message);

    void renderErrorSysBank(String message);

    void renderErrorAccountBank(String message);

    void renderErrorAccountName(String message);

    void renderErrorAccountNumber(String message);

    void renderErrorAccountBranch(String message);

    void renderErrorChooseBank(String message);

    void renderErrotDepositorName(String message);

    void renderErrorDepositorPassword(String message);

    void renderErrorPaymentAmount(String message);

    void renderErrorDate(String message);

    void requestFocusError(View view);

    void renderConfirmationSuccess(ConfirmationData data);

    void renderFormEdit(FormEditPaymentData data);

    void renderConfirmationError(String errorMsg);

    void renderConfirmationTimeout(String errorMsg);

    void renderEditTimeout(String message);

    void renderConfirmationNoConnection(String message);

    void renderErrorFormConfirmation(String message);

    void renderNoConnectionFormConfirmation(String message);

    void renderErrorFormEdit(String message);

    void renderNoConnectionFormEdit(String message);
}
