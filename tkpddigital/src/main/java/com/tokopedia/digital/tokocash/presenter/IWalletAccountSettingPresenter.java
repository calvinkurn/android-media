package com.tokopedia.digital.tokocash.presenter;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public interface IWalletAccountSettingPresenter {

    void processGetWalletAccountData();

    void processDeleteConnectedUser(String refreshToken,
                                    String identifier, String identifierType);
}
