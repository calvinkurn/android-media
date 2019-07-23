package com.tokopedia.csat_rating.di;

import com.tokopedia.csat_rating.ProvideRatingContract;

import dagger.Component;


@Component(modules = CsatModule.class)
public interface CsatComponent {

    ProvideRatingContract.ProvideRatingPresenter getProvideRatingPresenter();
}
