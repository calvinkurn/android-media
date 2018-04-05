package com.tokopedia.gamification.network;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.Request;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GamificationAuthInterceptor extends TkpdAuthInterceptor {

    public static final String PARAM_TKPD_USER_ID = "Tkpd-UserId";
    @Inject
    public GamificationAuthInterceptor(@ApplicationContext Context context,
                                       AbstractionRouter abstractionRouter,
                                       UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {
        Map<String, String> headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
        String userId = userSession.getUserId();
        headerMap.remove(PARAM_TKPD_USER_ID);
        if (!TextUtils.isEmpty(userId)) {
            headerMap.put(PARAM_TKPD_USER_ID, userId);
        }
        return headerMap;
    }
}
