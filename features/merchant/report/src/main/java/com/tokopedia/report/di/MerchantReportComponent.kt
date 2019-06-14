package com.tokopedia.report.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.report.view.fragment.ProductReportFragment
import com.tokopedia.report.view.fragment.ProductReportSubmitFragment
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@MerchantReportScope
@Component(modules = [MerchantReportModule::class], dependencies = [BaseAppComponent::class])
interface MerchantReportComponent {
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    @ApplicationContext
    fun getContext(): Context
    fun getAbstractionRouter(): AbstractionRouter
    fun retrofitBuilder(): Retrofit.Builder
    fun inject(fragment: ProductReportFragment)
    fun inject(fragment: ProductReportSubmitFragment)
}