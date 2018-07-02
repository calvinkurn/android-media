package com.tokopedia.nps.data.net;

import android.content.Context;

import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

/**
 * Created by meta on 28/06/18.
 */
public class NpsService {

    private NetworkRouter networkRouter;
    private UserSession userSession;
    private NpsApi api;
    private Context context;

    public NpsService(NetworkRouter networkRouter,
                      UserSession userSession,
                      Context context) {
        this.networkRouter = networkRouter;
        this.userSession = userSession;
        this.context = context;

        initApiService();
    }

    protected void initApiService() {
        Retrofit retrofit = CommonNetwork.createRetrofit(context, getBaseUrl(),
                networkRouter, userSession);
        api = retrofit.create(NpsApi.class);
    }

    protected String getBaseUrl() {
        return TkpdBaseURL.ContactUs.WEB_BASE;
    }

    public NpsApi getApi() {
        return api;
    }
}
