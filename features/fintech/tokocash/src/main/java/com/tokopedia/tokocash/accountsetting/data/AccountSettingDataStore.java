package com.tokopedia.tokocash.accountsetting.data;

import com.tokopedia.tokocash.accountsetting.data.entity.OAuthInfoEntity;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public interface AccountSettingDataStore {

    Observable<OAuthInfoEntity> getOAuthInfo();

    Observable<Boolean> unlinkAccountTokoCash(HashMap<String, String> mapParam);
}
