package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.tokocash.model.OAuthInfo;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public interface IWalletAccountSettingView extends IBaseView {

    void renderWalletOAuthInfoData(OAuthInfo oAuthInfo);

    void renderErrorGetWalletAccountSettingData(String message);

    void renderSuccessUnlinkAccount();

    void renderSuccessUnlinkToHome();

    void renderErrorUnlinkAccount(String message);

    void disableSwipeRefresh();

    void enableSwipeRefresh();
}
