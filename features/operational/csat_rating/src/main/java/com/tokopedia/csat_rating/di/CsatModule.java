package com.tokopedia.csat_rating.di;

import com.tokopedia.csat_rating.ProvideRatingContract;
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class CsatModule {
    @Provides
    ProvideRatingContract.ProvideRatingPresenter provideRatingPresenter() {
        return new BaseProvideRatingFragmentPresenter();
    }
}
