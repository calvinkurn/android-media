package com.tokopedia.loginregister.shopcreation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginregister.shopcreation.view.fragment.LandingShopCreationFragment
import com.tokopedia.loginregister.shopcreation.view.fragment.NameShopCreationFragment
import com.tokopedia.loginregister.shopcreation.view.fragment.PhoneShopCreationFragment
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

@ShopCreationScope
@SessionCommonScope
@Component(modules = [
    ShopCreationModule::class,
    ShopCreationViewModelModule::class,
    ShopCreationQueryModule::class,
    SessionModule::class
], dependencies = [BaseAppComponent::class])
interface ShopCreationComponent {
    fun inject(fragment: LandingShopCreationFragment)
    fun inject(fragment: NameShopCreationFragment)
    fun inject(fragment: PhoneShopCreationFragment)
}
