package com.tokopedia.train.common.data.interceptor;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class TrainInterceptor extends TkpdAuthInterceptor {
    private static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";

    public TrainInterceptor(@ApplicationContext Context context,
                            AbstractionRouter abstractionRouter,
                            UserSession userSession) {
        super(context, abstractionRouter, userSession);
        this.maxRetryAttempt = 0;
    }

    @Override
    protected Map<String, String> getHeaderMap(String path,
                                               String strParam,
                                               String method,
                                               String authKey,
                                               String contentTypeHeader) {

        Map<String, String> headerMap = AuthUtil.generateHeadersWithBearer(path,
                strParam,
                method,
                authKey,
                userSession.getUserId(),
                userSession.getAccessToken()
        );
        headerMap.put(HEADER_TKPD_USER_ID, userSession.getUserId());
        return headerMap;
    }
}