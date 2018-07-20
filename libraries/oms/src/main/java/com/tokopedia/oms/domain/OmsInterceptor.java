package com.tokopedia.oms.domain;

import android.content.Context;
import android.util.Log;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;

import okhttp3.Response;

public class OmsInterceptor extends TkpdAuthInterceptor {
    private Context mContext;

    public OmsInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super(context, networkRouter, userSession);
        this.mContext = context;
    }

    public OmsInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
        this.mContext = context;
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        if(response!=null && response.body()!=null) {
            String bodyResponse = response.body().string();
            Log.d("OkHttp", bodyResponse);
            response.body().close();
            ((OmsModuleRouter) mContext).handleOmsPromoError(bodyResponse);
        }else{
            ((OmsModuleRouter) mContext).handleOmsPromoError(null);
        }
    }
}