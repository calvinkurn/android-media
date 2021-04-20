package com.tokopedia.review.feature.inbox.buyerreview.network.tome;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.review.feature.inbox.buyerreview.network.BaseReputationService;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class TomeService extends BaseReputationService {

    public static final String TAG = TomeService.class.getSimpleName();

    private TomeApi api;
    private Context context;
    private NetworkRouter networkRouter;
    private UserSession userSession;

    public TomeService(Context context, NetworkRouter networkRouter, UserSession userSession) {
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
                TokopediaUrl.Companion.getInstance().getTOME(),
                networkRouter,
                userSession);
        api = retrofit.create(TomeApi.class);
    }

    public TomeApi getApi() {
        return api;
    }

}
