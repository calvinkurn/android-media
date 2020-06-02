package com.tokopedia.brizzi.di

import com.tokopedia.common_electronic_money.di.NfcCheckBalanceComponent
import com.tokopedia.brizzi.fragment.BrizziCheckBalanceFragment
import dagger.Component

@DigitalBrizziScope
@Component(modules = [DigitalBrizziModule::class, DigitalBrizziViewModelModule::class],
        dependencies = [NfcCheckBalanceComponent::class])
interface DigitalBrizziComponent {

    fun inject(brizziCheckBalanceFragment: BrizziCheckBalanceFragment)

}