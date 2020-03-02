package com.tokopedia.tkpd.tkpdreputation.network.shop;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;
import com.tokopedia.tkpd.tkpdreputation.network.BaseReputationService;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class ShopService extends BaseReputationService {

    public static final String TAG = ShopService.class.getSimpleName();

    private ShopApi api;
    private Context context;
    private NetworkRouter networkRouter;
    private UserSession userSession;

    public ShopService(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super();
        this.context = context;
        this.networkRouter = networkRouter;
        this.userSession = userSession;
        initApiService();
    }

    @Override
    public Retrofit createRetrofit(Context context, String baseUrl, NetworkRouter networkRouter, UserSession userSession) {
        return super.createRetrofit(context, baseUrl, networkRouter, userSession);
    }

    private void initApiService() {
        Retrofit retrofit = createRetrofit(
                context,
                ReputationBaseURL.URL_SHOP,
                networkRouter,
                userSession);
        api = retrofit.create(ShopApi.class);
    }

    public ShopApi getApi() {
        return api;
    }

}
