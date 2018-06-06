package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.digital.tokocash.model.ParamsActionHistory;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public interface IWalletToDepositPresenter {
    void processMoveToSaldo(String url, ParamsActionHistory paramsActionHistory);
}
