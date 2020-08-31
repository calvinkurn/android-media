package com.tokopedia.tkpd.tkpdreputation.network.shop;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;
import com.tokopedia.tkpd.tkpdreputation.network.BaseReputationService;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class ReputationActService extends BaseReputationService {

    public static final String TAG = ReputationService.class.getSimpleName();

    private ReputationActApi api;
    private Context context;
    private NetworkRouter networkRouter;
    private UserSession userSession;

    public ReputationActService(Context context, NetworkRouter networkRouter, UserSession userSession) {
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
                ReputationBaseURL.URL_REPUTATION_ACTION,
                networkRouter,
                userSession);
        api = retrofit.create(ReputationActApi.class);
    }

    public ReputationActApi getApi() {
        return api;
    }

}
