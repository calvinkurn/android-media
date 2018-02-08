package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public interface IWalletAccountSettingPresenter {

    void processGetWalletAccountData();

    void processDeleteConnectedUser(ProfileModel profileModel, String refreshToken,
                                    String identifier, String identifierType);
}
