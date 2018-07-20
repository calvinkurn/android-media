package com.tokopedia.home.account.data;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.AccountViewModel;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountMapper implements Func1<GraphqlResponse, AccountViewModel> {
    @Inject
    AccountMapper() {}

    @Override
    public AccountViewModel call(GraphqlResponse graphqlResponse) {
        graphqlResponse.getData(AccountModel.class);
        return null;
    }
}
