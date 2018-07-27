package com.tokopedia.home.account.domain;

import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.data.mapper.AccountMapper;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.QUERY;
import static com.tokopedia.home.account.AccountConstants.VARIABLES;

/**
 * @author okasurya on 7/20/18.
 */
public class GetAccountUseCase extends UseCase<AccountViewModel> {

    private GraphqlUseCase graphqlUseCase;
    private AccountMapper mapper;

    @Inject
    GetAccountUseCase(GraphqlUseCase graphqlUseCase, AccountMapper mapper){
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
    }

    @Override
    public Observable<AccountViewModel> createObservable(RequestParams requestParams) {
        return Observable
                .just(true)
                .flatMap((Func1<Boolean, Observable<GraphqlResponse>>) status -> {
                    String query = requestParams.getString(QUERY, "");
                    Map<String, Object> variables = (Map<String, Object>) requestParams.getObject(VARIABLES);

                    if(!TextUtils.isEmpty(query) && variables != null) {
                        GraphqlRequest request = new GraphqlRequest(query, AccountModel.class, variables);
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(mapper);
    }
}
