package com.tokopedia.sellerhome.stub.features.home.di

import com.tokopedia.sellerhome.di.component.HomeDashboardComponent
import com.tokopedia.sellerhome.di.module.SellerHomeUseCaseModule
import com.tokopedia.sellerhome.di.module.SellerHomeViewModelModule
import com.tokopedia.sellerhome.di.module.SellerHomeWearModule
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.stub.di.component.BaseAppComponentStub
import dagger.Component

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

@SellerHomeScope
@Component(
    modules = [
        SellerHomeModuleStub::class,
        SellerHomeUseCaseModule::class,
        SellerHomeViewModelModule::class,
        SellerHomeWearModule::class
    ],
    dependencies = [BaseAppComponentStub::class]
)
interface SellerHomeComponentStub : HomeDashboardComponent
