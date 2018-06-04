package com.tokopedia.digital.product.view.listener;

import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

/**
 * Created by Rizky on 18/05/18.
 */
public interface IETollView {

    void sendCommand(InquiryBalanceModel inquiryBalanceModel);

    void showCardLastBalance(InquiryBalanceModel inquiryBalanceModel);

    void showError(String errorMessage);

    String getStringResource(int stringRes);

}
