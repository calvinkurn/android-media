package com.tokopedia.tkpd.tkpdreputation.network.uploadimage;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;
import com.tokopedia.tkpd.tkpdreputation.network.BaseReputationService;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class GenerateHostActService extends BaseReputationService {

    public static final String TAG = GenerateHostActService.class.getSimpleName();

    private GenerateHostActApi api;
    private Context context;
    private NetworkRouter networkRouter;
    private UserSession userSession;

    public GenerateHostActService(Context context, NetworkRouter networkRouter, UserSession userSession) {
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
                ReputationBaseURL.URL_GENERATE_HOST_ACTION,
                networkRouter,
                userSession);
        api = retrofit.create(GenerateHostActApi.class);
    }

    public GenerateHostActApi getApi() {
        return api;
    }

}
