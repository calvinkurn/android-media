package com.tokopedia.core.drawer2.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.core.drawer2.domain.interactor.GetUserAttributesUseCase;
import com.tokopedia.core.network.apiservices.drawer.DrawerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Herdi_WORK on 27.09.17.
 */

public class CloudAttrDataSource {
    public static final String KEY_TOKOCASH_BALANCE_CACHE = "TOKOCASH_BALANCE_CACHE";

    private Context context;
    private DrawerService drawerService;

    private AnalyticsCacheHandler analyticsCacheHandler;
    private long DURATION_SAVE_TO_CACHE = 60;

    public CloudAttrDataSource(Context context, DrawerService drawerService) {
        this.context = context;
        this.drawerService = drawerService;
        analyticsCacheHandler = new AnalyticsCacheHandler();
    }

    public Observable<UserData> getConsumerUserAttributes(RequestParams requestParams) {
        int userId = requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0);
        return drawerService.getApi()
                .getConsumerDrawerData(String.format(getRequestPayload(), userId))
                .map(new Func1<Response<GraphqlResponse<UserData>>, UserData>() {
                    @Override
                    public UserData call(Response<GraphqlResponse<UserData>> graphqlResponseResponse) {

                        if (graphqlResponseResponse != null) {
                            if (graphqlResponseResponse.isSuccessful()) {
                                return graphqlResponseResponse.body().getData();

                            }
                        }
                        return null;
                    }
                })
                .doOnNext(setToCache());
    }

    public Observable<UserData> getSellerUserAttributes(RequestParams requestParams) {
        return drawerService.getApi()
                .getSellerDrawerData(String.format(getRequestSellerDataPayload(), requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0)))
                .map(new Func1<Response<GraphqlResponse<UserData>>, UserData>() {
                    @Override
                    public UserData call(Response<GraphqlResponse<UserData>> graphqlResponseResponse) {

                        if (graphqlResponseResponse != null) {
                            if (graphqlResponseResponse.isSuccessful()) {
                                return graphqlResponseResponse.body().getData();

                            }
                        }
                        return null;
                    }
                });
    }

    private Action1<UserData> setToCache() {
        return new Action1<UserData>() {
            @Override
            public void call(UserData data) {
                if (data != null) {
                    analyticsCacheHandler.setUserDataGraphQLCache(data);
                }

                //add wallet data in cache
                if (data != null && data.getWallet() != null && data.getWallet().getLinked()) {
                    GlobalCacheManager cacheHandler = new GlobalCacheManager();
                    cacheHandler.save(KEY_TOKOCASH_BALANCE_CACHE,
                            CacheUtil.convertModelToString(data.getWallet(),
                                    new TypeToken<Wallet>() {
                                    }.getType()), DURATION_SAVE_TO_CACHE);
                }
            }
        };
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.consumer_drawer_data_query);
    }

    private String getRequestSellerDataPayload() {
        return loadRawString(context.getResources(), R.raw.seller_drawer_data_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }
}
