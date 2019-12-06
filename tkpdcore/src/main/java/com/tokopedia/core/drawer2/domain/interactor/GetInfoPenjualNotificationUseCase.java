package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.drawer2.data.pojo.InfoPenjualNotification;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.core.drawer2.domain.GqlQueryConstant.GET_INFO_PENJUAL_NOTIFICATION_QUERY;

public class GetInfoPenjualNotificationUseCase extends UseCase<InfoPenjualNotification> {

    private String query;
    private GraphqlUseCase graphqlUseCase;
    private boolean isRefresh = false;
    private static String KEY_TYPE_ID = "typeId";

    public static RequestParams createParams(int typeId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(KEY_TYPE_ID, typeId);
        return requestParams;
    }

    @Inject
    public GetInfoPenjualNotificationUseCase(
            @Named(GET_INFO_PENJUAL_NOTIFICATION_QUERY) String query,
            GraphqlUseCase graphqlUseCase
    ) {
        this.query = query;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<InfoPenjualNotification> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, InfoPenjualNotification.Response.class,requestParams.getParameters());
        graphqlUseCase.clearRequest();
        CacheType cacheType = isRefresh ? CacheType.ALWAYS_CLOUD : CacheType.CACHE_FIRST;
        graphqlUseCase.setCacheStrategy(new GraphqlCacheStrategy.Builder(cacheType)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.val() / 2)
                .setSessionIncluded(true)
                .build()
        );
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(graphqlResponse -> {
            InfoPenjualNotification.Response response = graphqlResponse.getData(InfoPenjualNotification.Response.class);
            if (null != response) {
                return response.getInfoPenjualNotification();
            } else {
                throw new RuntimeException();
            }
        });
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
