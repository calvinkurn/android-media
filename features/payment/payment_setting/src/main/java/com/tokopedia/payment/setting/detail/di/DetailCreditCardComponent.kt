package com.tokopedia.payment.setting.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.detail.view.fragment.DetailCreditCardFragment
import dagger.Component

@DetailCreditCardScope
@Component(modules = [DetailCreditCardModule::class,
    GqlQueryModule::class], dependencies = [BaseAppComponent::class])
interface DetailCreditCardComponent {
    fun inject(detailCreditCardFragment: DetailCreditCardFragment)
}