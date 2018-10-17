package com.tokopedia.affiliate.feature.explore.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.presenter.ExplorePresenter;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

/**
 * @author by yfsx on 03/10/18.
 */
@Module
public class ExploreModule {

    @ExploreScope
    @Provides
    ExploreContract.Presenter provideExplorePresenter(ExplorePresenter explorePresenter) {
        return explorePresenter;
    }

    @ExploreScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
