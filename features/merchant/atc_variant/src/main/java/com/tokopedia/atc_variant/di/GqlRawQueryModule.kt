package com.tokopedia.atc_variant.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_variant.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@NormalCheckoutScope
@Module
class GqlRawQueryModule {

    @NormalCheckoutScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_INFO)
    fun provideRawProductInfo(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_info)

    @NormalCheckoutScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_VARIANT)
    fun provideRawVariant(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_variant)

    @NormalCheckoutScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_MULTI_ORIGIN)
    fun provideRawMultiOrigin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_nearest_warehouse)

    @NormalCheckoutScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_INSURANCE_RECOMMENDATION)
    fun provideRawInsuranceRecommendation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_insurance_recommendation)

}