package com.tokopedia.vouchercreation.common.di.module

import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoring
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringInterface
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringType
import dagger.Module
import dagger.Provides
import javax.inject.Named

@VoucherCreationScope
@Module
class VoucherCreationPerformanceMonitoringModule {

    @Named("list")
    @VoucherCreationScope
    @Provides
    fun provideVoucherListPerformanceMonitoring(): MvcPerformanceMonitoringInterface =
            MvcPerformanceMonitoring(MvcPerformanceMonitoringType.List)

    @Named("create")
    @VoucherCreationScope
    @Provides
    fun provideVoucherCreatePerformanceMonitoring(): MvcPerformanceMonitoringInterface =
            MvcPerformanceMonitoring(MvcPerformanceMonitoringType.Create)

    @Named("detail")
    @VoucherCreationScope
    @Provides
    fun provideVoucherDetailPerformanceMonitoring(): MvcPerformanceMonitoringInterface =
            MvcPerformanceMonitoring(MvcPerformanceMonitoringType.Detail)
}