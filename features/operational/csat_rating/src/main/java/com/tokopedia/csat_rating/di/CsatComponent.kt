package com.tokopedia.csat_rating.di

import com.tokopedia.csat_rating.ProvideRatingContract
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating

import dagger.Component


@Component(modules = [CsatModule::class])
interface CsatComponent {

    val provideRatingPresenter: ProvideRatingContract.ProvideRatingPresenter
}
