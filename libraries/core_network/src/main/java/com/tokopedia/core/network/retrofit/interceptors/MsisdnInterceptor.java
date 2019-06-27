package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

/**
 * Created by kris on 6/16/17. Tokopedia
 */

@Deprecated
public class MsisdnInterceptor extends TkpdAuthInterceptor {

    public MsisdnInterceptor(String authKey) {
        super(authKey);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        return AuthUtil.generateHeadersWithXUserIdXMsisdn(path, method, authKey, contentTypeHeader,
                userSession.getPhoneNumber());
    }

}
