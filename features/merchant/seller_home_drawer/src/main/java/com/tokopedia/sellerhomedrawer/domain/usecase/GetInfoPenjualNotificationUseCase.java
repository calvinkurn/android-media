package com.tokopedia.sellerhomedrawer.domain.usecase;

import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant;
import com.tokopedia.sellerhomedrawer.data.drawernotification.InfoPenjualNotification;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

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
            @Named(SellerHomeParamConstant.GET_INFO_PENJUAL_NOTIFICATION_QUERY) String query,
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
            List<GraphqlError> graphqlErrors = graphqlResponse.getError(InfoPenjualNotification.Response.class);
            if (graphqlErrors != null) {
                if (graphqlErrors.size() > 0) {
                    String errorMessage = graphqlErrors.get(0).getMessage();
                    if (errorMessage != null) {
                        if (!errorMessage.isEmpty())
                            try {
                                throw new MessageErrorException(errorMessage);
                            } catch (MessageErrorException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
            if (response != null) {
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
