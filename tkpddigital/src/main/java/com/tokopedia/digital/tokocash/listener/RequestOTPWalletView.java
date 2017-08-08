package com.tokopedia.digital.tokocash.listener;

/**
 * Created by nabillasabbaha on 7/25/17.
 */

public interface RequestOTPWalletView {

    void onSuccessRequestOtpWallet();

    void onSuccessLinkWalletToTokoCash();

    void onErrorOTPWallet(String message);

    void showProgressDialog();
}
