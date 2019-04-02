package com.tokopedia.tokocash.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class TokoCashAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public TokoCashAuthInterceptor(@ApplicationContext Context context,
                                   AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + userSession.getAccessToken());
        headerMap.put("X-Device", "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        return headerMap;
    }
}