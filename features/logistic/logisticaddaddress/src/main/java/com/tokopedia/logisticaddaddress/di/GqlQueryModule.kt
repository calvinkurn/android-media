package com.tokopedia.logisticaddaddress.di

import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class GqlQueryModule {

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_DISTRICT_RECOMMENDATION)
    fun provideQueryDiscom(): String =
            KeroLogisticQuery.district_recommendation

}
