package com.tokopedia.digital.product.view.listener;

import com.tokopedia.digital.product.view.model.InquiryBalanceModel;

/**
 * Created by Rizky on 18/05/18.
 */
public interface IEMoneyView {

    void sendCommand(InquiryBalanceModel inquiryBalanceModel);

    void showCardLastBalance(InquiryBalanceModel inquiryBalanceModel);

    void renderLocalCardInfo();

    void showCardLastBalanceWithError(InquiryBalanceModel inquiryBalanceModel, String errorMessage);

}
