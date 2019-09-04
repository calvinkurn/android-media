package com.tokopedia.affiliate.feature.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardUseCase;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateProductBoughtPresenter;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * @author by yfsx on 13/09/18.
 */
@Module
public class DashboardModule {

    @DashboardScope
    @Provides
    DashboardPresenter provideDashboardPresenter(@ApplicationContext Context context) {
        return new DashboardPresenter(
                new GetDashboardUseCase(context),
                new GetDashboardLoadMoreUseCase(context),
                new CheckAffiliateUseCase(context, new GraphqlUseCase()));
    }

    @DashboardScope
    @Provides
    AffiliateProductBoughtPresenter provideAffiliateProductBoughtPresenter(@ApplicationContext Context context) {
        return new AffiliateProductBoughtPresenter(
                new GetDashboardLoadMoreUseCase(context));
    }

    @DashboardScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @DashboardScope
    @Provides
    GraphqlRepository provideGraphQlRepository(@ApplicationContext Context context) {
        GraphqlClient.init(context);
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    @DashboardScope
    CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }


}