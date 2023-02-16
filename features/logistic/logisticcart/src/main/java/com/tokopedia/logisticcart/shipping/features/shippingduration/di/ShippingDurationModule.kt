package com.tokopedia.logisticcart.shipping.features.shippingduration.di

import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationContract
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationPresenter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 07/08/18.
 */
@Module
class ShippingDurationModule {
    @Provides
    @ShippingDurationScope
    fun provideShippingDurationAdapter(): ShippingDurationAdapter {
        return ShippingDurationAdapter()
    }

    @Provides
    @ShippingDurationScope
    fun provideShippingDurationPresenter(
        ratesUseCase: GetRatesUseCase,
        ratesApiUseCase: GetRatesApiUseCase,
        stateConverter: RatesResponseStateConverter
    ): ShippingDurationContract.Presenter {
        return ShippingDurationPresenter(ratesUseCase, ratesApiUseCase, stateConverter)
    }

    @Provides
    @ShippingDurationScope
    fun provideScheduler(): SchedulerProvider {
        return MainScheduler()
    }
}
