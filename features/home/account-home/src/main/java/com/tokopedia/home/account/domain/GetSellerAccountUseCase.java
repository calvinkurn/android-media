package com.tokopedia.home.account.domain;

import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.data.mapper.SellerAccountMapper;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.data.model.ShopInfoLocation;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.navigation_common.model.SaldoModel;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.QUERY;
import static com.tokopedia.home.account.AccountConstants.SALDO_QUERY;
import static com.tokopedia.home.account.AccountConstants.SHOP_LOCATION_QUERY;
import static com.tokopedia.home.account.AccountConstants.TOPADS_QUERY;
import static com.tokopedia.home.account.AccountConstants.VARIABLES;

/**
 * @author by alvinatin on 10/08/18.
 */

public class GetSellerAccountUseCase extends UseCase<SellerViewModel> {

    private GraphqlUseCase graphqlUseCase;
    private SellerAccountMapper mapper;

    @Inject
    public GetSellerAccountUseCase(GraphqlUseCase graphqlUseCase, SellerAccountMapper mapper) {
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
    }

    @Override
    public Observable<SellerViewModel> createObservable(RequestParams requestParams) {
        return Observable
                .just(requestParams)
                .flatMap((Func1<RequestParams, Observable<GraphqlResponse>>) requestParam -> {
                    String query = requestParam.getString(QUERY, "");
                    String topadsQuery = requestParam.getString(TOPADS_QUERY, "");
                    String saldoQuery = requestParam.getString(SALDO_QUERY, "");
                    Map<String, Object> variables = (Map<String, Object>) requestParam.getObject(VARIABLES);
                    int[] shopIds = (int[])variables.get("shop_ids");
                    String shopId = (shopIds != null && shopIds.length > 0) ? shopIds[0]+"" : "0";

                    if(!TextUtils.isEmpty(query) && variables != null) {
                        GraphqlRequest request = new GraphqlRequest(query, AccountModel.class, variables, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);

                        graphqlUseCase.addRequest(TopAdsGetShopDepositGraphQLUseCase.createGraphqlRequest(topadsQuery,
                                TopAdsGetShopDepositGraphQLUseCase.createRequestParams(topadsQuery, shopId)));
                        GraphqlRequest saldoGraphql = new GraphqlRequest(saldoQuery, SaldoModel.class);
                        graphqlUseCase.addRequest(saldoGraphql);

                        String shopLocationQuery = requestParam.getString(SHOP_LOCATION_QUERY, "");
                        graphqlUseCase.addRequest(createGraphqlRequest(
                                shopLocationQuery,
                                creteShopLocationRequestParam(shopId))
                        );

                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(mapper);
    }

    private RequestParams creteShopLocationRequestParam(String shopId) {
        String SHOP_ID = "shopID";

        RequestParams param = RequestParams.create();
        int ShopIdInt = 0;
        if (shopId == "0") {
            ShopIdInt = Integer.parseInt(shopId);
        }
        param.putInt(SHOP_ID, ShopIdInt);
        return param;
    }

    private GraphqlRequest createGraphqlRequest(String query, RequestParams requestParams) {
        return new GraphqlRequest(
                query,
                ShopInfoLocation.class,
                requestParams.getParameters(),
                false);
    }
}
