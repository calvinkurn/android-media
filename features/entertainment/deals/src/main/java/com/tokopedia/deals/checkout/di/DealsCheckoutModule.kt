package com.tokopedia.deals.checkout.di

import com.google.gson.Gson
import com.tokopedia.deals.checkout.ui.mapper.DealsCheckoutMapper
import dagger.Module
import dagger.Provides

@Module
class DealsCheckoutModule {


    @Provides
    @DealsCheckoutScope
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @DealsCheckoutScope
    fun provideCheckoutMapper(gson: Gson): DealsCheckoutMapper {
        return DealsCheckoutMapper(gson)
    }
}
