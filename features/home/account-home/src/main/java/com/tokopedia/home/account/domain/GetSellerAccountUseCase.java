package com.tokopedia.home.account.domain;

import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.data.mapper.SellerAccountMapper;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.QUERY;
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
                    Map<String, Object> variables = (Map<String, Object>) requestParam.getObject(VARIABLES);

                    if(!TextUtils.isEmpty(query) && variables != null) {
                        GraphqlRequest request = new GraphqlRequest(query, AccountModel.class, variables);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(mapper);
    }
}
