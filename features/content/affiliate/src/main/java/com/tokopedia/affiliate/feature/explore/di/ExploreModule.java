package com.tokopedia.affiliate.feature.explore.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.explore.domain.usecase.ExploreUseCase;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.presenter.ExplorePresenter;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by yfsx on 03/10/18.
 */
@Module
public class ExploreModule {

    private Context mContext;

    public ExploreModule(Context context) {
        this.mContext = context;
    }

    @ExploreScope
    @Provides
    ExploreContract.Presenter provideExplorePresenter(ExplorePresenter explorePresenter) {
        return explorePresenter;
    }

    @ExploreScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ExploreScope
    @Provides
    ExploreUseCase provideExploreUseCase(GraphqlUseCase graphQlUseCase) {
        return new ExploreUseCase(mContext, graphQlUseCase);
    }
}
