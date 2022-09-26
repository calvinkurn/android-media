package com.tokopedia.wishlistcommon.domain;

import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlistcommon.data.response.ProductIsWishlistedResponse;

import java.util.List;

import rx.Observable;

public class GetProductIsWishlistedUseCase extends UseCase<Boolean> {
    private String rawQuery;
    private GraphqlUseCase gqlUseCase;
    private static final String ARG_PRODUCT_ID = "productID";

    public GetProductIsWishlistedUseCase(String rawQuery, GraphqlUseCase gqlUseCase){
        this.rawQuery = rawQuery;
        this.gqlUseCase = gqlUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        gqlUseCase.clearRequest();
        GraphqlRequest gqlRequest = new GraphqlRequest(rawQuery, ProductIsWishlistedResponse.class,
                requestParams.getParameters());
        gqlUseCase.addRequest(gqlRequest);
        return gqlUseCase.createObservable(RequestParams.EMPTY)
                .map(graphqlResponse -> {
                    List<GraphqlError> errors = graphqlResponse.getError(ProductIsWishlistedResponse.class);
                    if (errors != null && !errors.isEmpty()){
                        return false;
                    } else {
                        ProductIsWishlistedResponse resp = graphqlResponse.getData(ProductIsWishlistedResponse.class);
                        if (resp == null) return false;
                        else return resp.isWishlisted();
                    }
                });
    }

    @Override
    public void unsubscribe() {
        gqlUseCase.unsubscribe();
        super.unsubscribe();
    }

    public static RequestParams createParams(String productId){
        RequestParams params = RequestParams.create();
        params.putString(ARG_PRODUCT_ID, productId);
        return params;
    }
}
