package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.tokocash.model.WalletToDepositThanksData;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public interface IWalletToDepositView extends IBaseView {

    void wrappingDataSuccess(long amount);

    void wrappingDataFailed();

}
