package com.tokopedia.review.feature.inbox.buyerreview.network;

import android.content.Context;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class ReputationService extends BaseReputationService {

    public static final String TAG = ReputationService.class.getSimpleName();

    private ReputationApi api;
    private Context context;
    private NetworkRouter networkRouter;
    private UserSession userSession;

    public ReputationService(Context context, NetworkRouter networkRouter, UserSession userSession) {
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
                ReputationBaseURL.URL_REPUTATION,
                networkRouter,
                userSession);
        api = retrofit.create(ReputationApi.class);
    }

    public ReputationApi getApi() {
        return api;
    }

}
