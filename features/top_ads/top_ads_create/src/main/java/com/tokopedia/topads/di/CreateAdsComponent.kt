package com.tokopedia.topads.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.view.fragment.*
import dagger.Component

/**
 * Author errysuprayogi on 08,November,2019
 */

@CreateAdsScope
@Component(modules = [CreateAdsModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CreateAdsComponent {

    fun inject(productAdsListFragment: ProductAdsListFragment)
    fun inject(keywordAdsListFragment: KeywordAdsListFragment)
    fun inject(budgetingAdsFragment: BudgetingAdsFragment)
    fun inject(summaryAdsFragment: SummaryAdsFragment)
    fun inject(adCreationChooserFragment: AdCreationChooserFragment)
    fun inject(creationOnboardingFragScreen: CreationOnboardingFragScreen)
    fun inject(creationOnboardingFragScreen: CreationOnboardingFragScreen2)
    fun inject(creationOnboardingFragScreen: CreationOnboardingFragScreen3)


}