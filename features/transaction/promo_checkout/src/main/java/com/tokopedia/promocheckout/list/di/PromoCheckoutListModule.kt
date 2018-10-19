package com.tokopedia.promocheckout.list.di

import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailScope
import com.tokopedia.promocheckout.list.PromoCheckoutListPresenter
import dagger.Module
import dagger.Provides

@Module
class PromoCheckoutListModule {

    @PromoCheckoutDetailScope
    @Provides
    fun providePresenter() : PromoCheckoutListPresenter {
        return PromoCheckoutListPresenter()
    }
}
