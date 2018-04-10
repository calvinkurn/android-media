package com.tokopedia.core.drawer2.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;
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

    private Context context;
    private DrawerService drawerService;

    private AnalyticsCacheHandler analyticsCacheHandler;

    public CloudAttrDataSource(Context context, DrawerService drawerService) {
        this.context = context;
        this.drawerService = drawerService;
        analyticsCacheHandler = new AnalyticsCacheHandler();
    }

    public Observable<UserDrawerData> getConsumerUserAttributes(RequestParams requestParams) {
        return drawerService.getApi()
                .getConsumerDrawerData(String.format(getRequestPayload(), requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0)))
                .map(new Func1<Response<GraphqlResponse<UserDrawerData>>, UserDrawerData>() {
                    @Override
                    public UserDrawerData call(Response<GraphqlResponse<UserDrawerData>> graphqlResponseResponse) {

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

    public Observable<UserDrawerData> getSellerUserAttributes(RequestParams requestParams) {
        return drawerService.getApi()
                .getSellerDrawerData(String.format(getRequestSellerDataPayload(), requestParams.getInt(GetUserAttributesUseCase.PARAM_USER_ID, 0)))
                .map(new Func1<Response<GraphqlResponse<UserDrawerData>>, UserDrawerData>() {
                    @Override
                    public UserDrawerData call(Response<GraphqlResponse<UserDrawerData>> graphqlResponseResponse) {

                        if (graphqlResponseResponse != null) {
                            if (graphqlResponseResponse.isSuccessful()) {
                                return graphqlResponseResponse.body().getData();

                            }
                        }
                        return null;
                    }
                });
        //.doOnNext(setToCache());
    }

    private Action1<UserDrawerData> setToCache() {
        return new Action1<UserDrawerData>() {
            @Override
            public void call(UserDrawerData data) {
                if (data != null) {
                    analyticsCacheHandler.setUserDataGraphQLCache(data);
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
