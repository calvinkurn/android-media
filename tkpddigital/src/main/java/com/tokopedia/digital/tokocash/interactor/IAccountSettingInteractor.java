package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.model.AccountTokoCash;
import com.tokopedia.digital.tokocash.model.OAuthInfo;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public interface IAccountSettingInteractor {

    void getOAuthInfo(Subscriber<OAuthInfo> subscriber);

    void getLinkedAccountList(Subscriber<List<AccountTokoCash>> subscriber);

    void unlinkAccountTokoCash(Subscriber<Boolean> isSuccess, String userId);
}
