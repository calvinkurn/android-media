package com.tokopedia.logisticaddaddress.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.logisticaddaddress.R
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class GqlQueryModule {

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_DISTRICT_RECOMMENDATION)
    fun provideQueryDiscom(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.district_recommendation)

}
