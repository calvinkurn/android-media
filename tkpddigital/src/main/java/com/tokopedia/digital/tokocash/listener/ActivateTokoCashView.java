package com.tokopedia.digital.tokocash.listener;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface ActivateTokoCashView {

    void onSuccessLinkWalletToTokoCash();

    void onErrorLinkWalletToTokoCash(String message);

    void showProgressDialog();
}
