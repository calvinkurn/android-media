package com.tokopedia.mlp.di

import com.tokopedia.mlp.fragment.MerchantLendingFragment
import dagger.Component

@MerchantScope
@Component(modules = [MerchantLendingUseCaseModule::class, ViewModelModule::class])
interface MerchantLendingComponent {
    fun inject(merchantLendingFragment: MerchantLendingFragment)
}
