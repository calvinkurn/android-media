package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.additional_check.domain.usecase.AdditionalCheckUseCase;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.analytics.domain.GetUserAttributesUseCase;
import com.tokopedia.home.account.di.scope.AccountHomeScope;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.presenter.AccountHomePresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 7/20/18.
 */
@Module
@AccountHomeScope
public class AccountHomeModule {
    @Provides
    AccountAnalytics provideAccountAnalytics(@ApplicationContext Context context) {
        return new AccountAnalytics(context);
    }

    @Provides
    AccountHome.Presenter provideAccountHomePresenter(GetUserAttributesUseCase getUserAttributesUseCase, AdditionalCheckUseCase additionalCheckUseCase, AccountAnalytics accountAnalytics) {
        return new AccountHomePresenter(getUserAttributesUseCase, additionalCheckUseCase, accountAnalytics);
    }

    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    public GraphqlRepository provideGraphQlRepository() {
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    public MultiRequestGraphqlUseCase provideMultiRequestGraphql() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }
}
