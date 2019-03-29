package com.tokopedia.affiliate.feature.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardUseCase;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

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
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}