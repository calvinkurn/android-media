package com.tokopedia.tokocash.accountsetting.domain;

import com.tokopedia.tokocash.accountsetting.presentation.model.OAuthInfo;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public interface IAccountSettingRepository {

    Observable<OAuthInfo> getOAuthInfo(HashMap<String, String> mapParam);

    Observable<Boolean> unlinkAccountTokoCash(HashMap<String, String> mapParam);
}
