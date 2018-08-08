package com.tokopedia.mitratoppers.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;

import java.util.Map;

import javax.inject.Inject;


public class MitraToppersAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public MitraToppersAuthInterceptor(@ApplicationContext Context context,
                               AbstractionRouter abstractionRouter,
                               UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {
        String pathExcludeBaseUrl = path.replaceFirst(MitraToppersBaseURL.PATH_MITRA_TOPPERS, "");
        return super.getHeaderMap(pathExcludeBaseUrl, strParam, method, authKey, contentTypeHeader);
    }

}
