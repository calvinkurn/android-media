package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.repository.RestRepositoryImpl
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.product.detail.constant.Constant
import com.tokopedia.product.detail.constant.Constant.PATH_REPORT_TYPE
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@ProductDetailScope
@Module(includes = [ProductDetailUserModule::class])
class ProductReportModule {
    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(PATH_REPORT_TYPE)
    fun provideReportTypeURL(): String =
            Constant.BASE_URL + Constant.PATH_PRODUCT_TYPE + Constant.PATH_REPORT_TYPE

    @ProductDetailScope
    @Provides
    fun provideReportRestRepository(interceptors: MutableList<Interceptor>,
                                    @ApplicationContext context: Context): RestRepository =
            RestRepositoryImpl(interceptors, context)

    @ProductDetailScope
    @Provides
    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor) =
        mutableListOf(tkpdAuthInterceptor, loggingInterceptor, commonErrorResponseInterceptor)

    @ProductDetailScope
    @Provides
    fun provideAuthInterceptors(@ApplicationContext context: Context,
                                userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }

    @ProductDetailScope
    @Provides
    fun provideErrorInterceptors(@ApplicationContext context: Context): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

}