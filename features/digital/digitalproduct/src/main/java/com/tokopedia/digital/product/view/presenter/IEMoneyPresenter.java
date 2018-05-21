package com.tokopedia.digital.product.view.presenter;

/**
 * Created by Rizky on 18/05/18.
 */
public interface IEMoneyPresenter {

    void inquiryBalance(int issuerId, String responseCardAttribute, String responseCardInfo,
                        String responseCardUID, String responseCardLastBalance);

    void sendCommand();

}
