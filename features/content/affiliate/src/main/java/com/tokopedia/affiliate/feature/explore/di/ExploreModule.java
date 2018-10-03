package com.tokopedia.affiliate.feature.explore.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.explore.view.presenter.ExplorePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by yfsx on 03/10/18.
 */
@Module
public class ExploreModule {

    @ExploreScope
    @Provides
    ExplorePresenter provideExplorePresenter(@ApplicationContext Context context) {
        return new ExplorePresenter();
    }
}
