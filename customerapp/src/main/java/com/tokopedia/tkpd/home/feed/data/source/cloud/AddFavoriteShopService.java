package com.tokopedia.tkpd.home.feed.data.source.cloud;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.R;
import com.tokopedia.topads.sdk.base.GlobalConstant;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author madi on 4/27/17.
 */

public class AddFavoriteShopService extends IntentService {

    public static final String EXTRAS_SESSION_ID = "EXTRAS_SESSION_ID";
    public static final String EXTRAS_SHOP_ID = "EXTRAS_SHOP_ID";
    public static final String EXTRAS_AD_KEY = "EXTRAS_AD_KEY";

    private static final String SERVICE_NAME = "AddFavoriteShopService";
    private static final String TAG = "AddFavoriteShopService";
    private static final String KEY_SRC = "src";
    private static final String DEFAULT_VALUE_SRC = "fav_shop";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private ServiceV4 serviceV4;
    private Subscription subscription = Subscriptions.empty();
    private Context context;

    public AddFavoriteShopService() {
        super(SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        serviceV4 = buildRetrofit().create(ServiceV4.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            String sessionId = intent.hasExtra(EXTRAS_SESSION_ID)
                    ? intent.getStringExtra(EXTRAS_SESSION_ID) : "";

            String shopId
                    = intent.hasExtra(EXTRAS_SHOP_ID) ? intent.getStringExtra(EXTRAS_SHOP_ID) : "";
            String adKey
                    = intent.hasExtra(EXTRAS_AD_KEY) ? intent.getStringExtra(EXTRAS_AD_KEY) : "";

            TKPDMapParam<String, String> param = new TKPDMapParam<>();
            param.put(GlobalConstant.KEY_USER_ID, sessionId);
            param.put(GlobalConstant.KEY_SHOP_ID, shopId);
            param.put(GlobalConstant.KEY_AD_KEY, adKey);
            param.put(KEY_SRC, DEFAULT_VALUE_SRC);
            AddFavoriteShop(AuthUtil.generateParamsNetwork(context, param));
        }
    }

    private void AddFavoriteShop(TKPDMapParam<String, String> params) {
        this.subscription = serviceV4.postFavoriteShop(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AddFavoriteShopSubscriber());
    }

    private Retrofit buildRetrofit() {
        OkHttpClient okHttpClient = OkHttpFactory.create().buildClientDefaultAuth();
        TkpdResponseConverter tkpdResponseConverter = new TkpdResponseConverter();
        GeneratedHostConverter generatedHostConverter = new GeneratedHostConverter();
        StringResponseConverter stringResponseConverter = new StringResponseConverter();

        return getWsV4Retrofit(okHttpClient,
                getRetrofitBuilder(
                        generatedHostConverter,
                        tkpdResponseConverter,
                        stringResponseConverter,
                        buildGson()));
    }

    private Retrofit getWsV4Retrofit(OkHttpClient okHttpClient,
                                     Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN).client(okHttpClient).build();
    }

    public Retrofit.Builder getRetrofitBuilder(GeneratedHostConverter generatedHostConverter,
                                               TkpdResponseConverter tkpdResponseConverter,
                                               StringResponseConverter stringResponseConverter,
                                               Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(generatedHostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public Gson buildGson() {
        return new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    private class AddFavoriteShopSubscriber extends Subscriber<Response<String>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            showMessage(context.getString(R.string.msg_fav_shop_failed));
            subscription.unsubscribe();
        }

        @Override
        public void onNext(Response<String> response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                FavoriteSendData data
                        = buildGson().fromJson(response.body(), FavoriteSendData.class);

                if (data != null && data.getResult() != null) {
                    showMessage(context.getString(R.string.msg_fav_shop_success));
                    subscription.unsubscribe();
                } else {
                    showMessage(context.getString(R.string.msg_fav_shop_failed));
                    subscription.unsubscribe();
                }
            }
        }

        private void showMessage(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
