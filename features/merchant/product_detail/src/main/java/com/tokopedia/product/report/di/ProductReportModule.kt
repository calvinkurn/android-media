package com.tokopedia.product.report.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.repository.RestRepositoryImpl
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.product.detail.di.ProductDetailUserModule
import com.tokopedia.product.report.constant.ProductReportConstant
import com.tokopedia.product.report.constant.ProductReportConstant.PATH_REPORT
import com.tokopedia.product.report.constant.ProductReportConstant.PATH_REPORT_TYPE
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
            ProductReportConstant.BASE_URL + ProductReportConstant.PATH_PRODUCT_TYPE + ProductReportConstant.PATH_REPORT_TYPE


    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(PATH_REPORT)
    fun provideReportActionURL(): String =
            ProductReportConstant.BASE_URL + ProductReportConstant.PATH_PRODUCT_ACTION + ProductReportConstant.PATH_REPORT

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
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

}