package com.tokopedia.product.report.di

import com.tokopedia.product.detail.data.util.ProductDetailConstant.BASE_REST_URL
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_PRODUCT_ACTION
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_PRODUCT_TYPE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_REPORT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_REPORT_TYPE
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.product.detail.di.ProductDetailUserModule
import com.tokopedia.product.detail.di.ProductRestModule
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@ProductDetailScope
@Module(includes = [ProductDetailUserModule::class, ProductRestModule::class])
class ProductReportModule {
    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(PATH_REPORT_TYPE)
    fun provideReportTypeURL(): String =
            BASE_REST_URL + PATH_PRODUCT_TYPE + PATH_REPORT_TYPE


    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(PATH_REPORT)
    fun provideReportActionURL(): String =
            BASE_REST_URL + PATH_PRODUCT_ACTION + PATH_REPORT

}