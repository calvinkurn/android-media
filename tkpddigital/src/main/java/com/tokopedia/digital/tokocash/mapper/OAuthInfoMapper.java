package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.tokocash.entity.OAuthInfoEntity;
import com.tokopedia.digital.tokocash.model.OAuthInfo;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class OAuthInfoMapper implements Func1<OAuthInfoEntity, OAuthInfo> {

    @Override
    public OAuthInfo call(OAuthInfoEntity oAuthInfoEntity) {
        if (oAuthInfoEntity != null) {
            OAuthInfo oAuthInfo = new OAuthInfo();
            oAuthInfo.setUserId(oAuthInfoEntity.getUser_id());
            oAuthInfo.setName(oAuthInfoEntity.getName());
            oAuthInfo.setEmail(oAuthInfoEntity.getEmail());
            oAuthInfo.setMsisdn(oAuthInfoEntity.getMsisdn());
            return oAuthInfo;
        }
        return null;
    }
}
