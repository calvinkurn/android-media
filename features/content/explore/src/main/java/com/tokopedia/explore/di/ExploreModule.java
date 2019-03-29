package com.tokopedia.explore.di;

import com.tokopedia.explore.domain.interactor.GetExploreDataUseCase;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.presenter.ContentExplorePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 23/07/18.
 */

@ExploreScope
@Module
public class ExploreModule {

    @ExploreScope
    @Provides
    ContentExploreContract.Presenter provideContentExplorePresenter(GetExploreDataUseCase
                                                                   getExploreDataUseCase) {
        return new ContentExplorePresenter(getExploreDataUseCase);
    }
}
