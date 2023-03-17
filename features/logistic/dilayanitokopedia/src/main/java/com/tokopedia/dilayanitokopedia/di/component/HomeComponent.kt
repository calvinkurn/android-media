package com.tokopedia.dilayanitokopedia.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.dilayanitokopedia.di.module.HomeModule
import com.tokopedia.dilayanitokopedia.di.module.HomeViewModelModule
import com.tokopedia.dilayanitokopedia.di.scope.HomeScope
import com.tokopedia.dilayanitokopedia.ui.home.presentation.fragment.DtHomeFragment
import com.tokopedia.dilayanitokopedia.ui.home.presentation.fragment.DtHomeRecommendationForYouFragment
import dagger.Component

/**
 * Created by irpan on 12/09/22.
 */
@HomeScope
@Component(
    modules = [HomeModule::class, HomeViewModelModule::class], dependencies = [BaseAppComponent::class]
)
interface HomeComponent {
    fun inject(fragmentDtHome: DtHomeFragment)
    fun inject(fragmentDtRecommendationForYou: DtHomeRecommendationForYouFragment)
}
