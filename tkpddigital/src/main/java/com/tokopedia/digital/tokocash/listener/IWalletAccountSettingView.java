package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.tokocash.model.AccountTokoCash;
import com.tokopedia.digital.tokocash.model.OAuthInfo;

import java.util.List;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public interface IWalletAccountSettingView extends IBaseView {

    void renderWalletOAuthInfoData(OAuthInfo oAuthInfo);

    void renderAccountTokoCashList(List<AccountTokoCash> accountTokoCashList);

    void renderErrorGetWalletAccountSettingData(String message);

    void disableSwipeRefresh();

    void enableSwipeRefresh();
}
