package com.tokopedia.affiliate.feature.explore.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.common.domain.usecase.CheckQuotaUseCase;
import com.tokopedia.affiliate.feature.explore.domain.usecase.ExploreUseCase;
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
    ExplorePresenter provideExplorePresenter(@ApplicationContext Context context,
                                             CheckQuotaUseCase checkQuotaUseCase) {
        return new ExplorePresenter(new ExploreUseCase(context), checkQuotaUseCase);
    }
}
