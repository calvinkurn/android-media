package com.tokopedia.csat_rating.di.component

import com.tokopedia.csat_rating.di.general.CsatComponentCommon
import com.tokopedia.csat_rating.di.module.CsatRatingModule
import com.tokopedia.csat_rating.di.scope.CsatScope
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import dagger.Component

@CsatScope
@Component(modules = [CsatRatingModule::class], dependencies = [CsatComponentCommon::class])
interface CsatComponent {
    fun inject(fragmentRating: BaseFragmentProvideRating?)

}