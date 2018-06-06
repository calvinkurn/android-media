package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.model.OAuthInfo;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public interface IAccountSettingInteractor {

    void getOAuthInfo(Subscriber<OAuthInfo> subscriber);

    void unlinkAccountTokoCash(Subscriber<Boolean> subscriber, String refreshToken,
                               String identifier, String identifierType);
}
