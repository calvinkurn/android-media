package com.tokopedia.tkpd.tkpdreputation.network.uploadimage;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.tkpd.tkpdreputation.network.BaseReputationService;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

public class UploadImageService extends BaseReputationService {

    public static final String TAG = UploadImageService.class.getSimpleName();

    private UploadImageApi api;
    private Context context;
    private NetworkRouter networkRouter;
    private UserSession userSession;

    public UploadImageService(Context context, NetworkRouter networkRouter, UserSession userSession) {
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
                TokopediaUrl.Companion.getInstance().getACCOUNTS(),
                networkRouter,
                userSession);
        api = retrofit.create(UploadImageApi.class);
    }

    public UploadImageApi getApi() {
        return api;
    }

}