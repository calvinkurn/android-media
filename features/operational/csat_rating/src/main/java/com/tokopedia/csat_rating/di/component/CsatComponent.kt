package com.tokopedia.csat_rating.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.csat_rating.activity.BottomSheetProvideRatingActivity
import com.tokopedia.csat_rating.di.module.CsatRatingModule
import com.tokopedia.csat_rating.di.module.CsatRatingViewModelModule
import com.tokopedia.csat_rating.di.scope.CsatScope
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import dagger.Component

@CsatScope
@Component(modules = [CsatRatingModule::class, CsatRatingViewModelModule::class],
    dependencies = [BaseAppComponent::class])
interface CsatComponent {
    fun inject(fragmentRating: BaseFragmentProvideRating?)

    fun inject(bottomSheetProvideRatingActivity: BottomSheetProvideRatingActivity?)
}
