package com.tokopedia.shop.common.domain.interactor;

import android.content.res.Resources;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by User on 9/8/2017.
 */

public class ToggleFavouriteShopUseCase extends UseCase<Boolean> {

    private static final String SHOP_ID = "SHOP_ID";
    public static final String SHOP_ID_INPUT = "shopID";
    public static final String INPUT = "input";
    private GraphqlUseCase graphqlUseCase;
    private Resources resources;

    @Inject
    public ToggleFavouriteShopUseCase(GraphqlUseCase graphqlUseCase,
                                      Resources resources) {
        this.graphqlUseCase = graphqlUseCase;
        this.resources = resources;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        graphqlUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();
        JsonObject input = new JsonObject();
        input.addProperty(SHOP_ID_INPUT, requestParams.getString(SHOP_ID, ""));
        variables.put(INPUT, input);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_favorite_shop),
                DataFollowShop.class,
                variables, false);

        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams)
                .map(new Func1<GraphqlResponse, Boolean>() {
                    @Override
                    public Boolean call(GraphqlResponse graphqlResponse) {
                        DataFollowShop dataDetailCheckoutPromo = graphqlResponse.getData(DataFollowShop.class);
                        if(dataDetailCheckoutPromo != null && dataDetailCheckoutPromo.getFollowShop() != null) {
                            return dataDetailCheckoutPromo.getFollowShop().isSuccess();
                        }else{
                            throw new RuntimeException();
                        }
                    }
                });
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
