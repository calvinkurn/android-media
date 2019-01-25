package com.tokopedia.core.network.apiservices.drawer;

import android.content.Context;

import com.tokopedia.core.network.apiservices.drawer.api.DrawerDataApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 8/31/17.
 */

@Deprecated
public class DrawerService  {

    private Context context;
    private UserSession userSession;
    private NetworkRouter networkRouter;
    private DrawerDataApi api;

    public DrawerService (Context context, UserSession userSession, NetworkRouter networkRouter) {
        this.context = context;
        this.userSession = userSession;
        this.networkRouter = networkRouter;

        initApiService();
    }

    protected void initApiService() {
        Retrofit retrofit1 = CommonNetwork.createRetrofit(context, getBaseUrl(), networkRouter, userSession);
        api = retrofit1.create(DrawerDataApi.class);
    }

    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    public DrawerDataApi getApi() {
        return api;
    }
}
