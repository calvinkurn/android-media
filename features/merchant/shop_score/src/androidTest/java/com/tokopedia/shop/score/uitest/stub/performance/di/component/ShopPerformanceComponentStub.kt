package com.tokopedia.shop.score.uitest.stub.performance.di.component

import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.uitest.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.performance.di.module.ShopPerformanceModuleStub
import dagger.Component

@ShopPerformanceScope
@Component(
    modules = [ShopPerformanceModuleStub::class],
    dependencies = [BaseAppComponentStub::class]
)
interface ShopPerformanceComponentStub : ShopPerformanceComponent
