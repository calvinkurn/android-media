package com.tokopedia.loginregister.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.di.modules.FakeShopCreationModule
import com.tokopedia.loginregister.shopcreation.LandingShopCreationCase
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.di.ShopCreationViewModelModule
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

@ActivityScope
@SessionCommonScope
@Component(
    modules = [
        FakeShopCreationModule::class,
        ShopCreationViewModelModule::class,
        SessionModule::class
    ], dependencies = [BaseAppComponent::class]
)
interface ShopCreationComponentStub : ShopCreationComponent {
    fun inject(landingShopCreationCase: LandingShopCreationCase)
}
