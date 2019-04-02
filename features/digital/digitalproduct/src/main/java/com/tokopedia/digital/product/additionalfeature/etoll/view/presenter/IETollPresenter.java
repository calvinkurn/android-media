package com.tokopedia.digital.product.additionalfeature.etoll.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital.product.view.listener.IETollView;

/**
 * Created by Rizky on 18/05/18.
 */
public interface IETollPresenter extends CustomerPresenter<IETollView> {

    void inquiryBalance(int issuerId, String responseCardAttribute, String responseCardInfo,
                        String responseCardUID, String responseCardLastBalance);

    void sendCommand(String payload, int id, int issuerId);
}
