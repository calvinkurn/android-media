package com.tokopedia.home.account.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.account.di.module.SellerAccountModule
import com.tokopedia.home.account.di.scope.SellerAccountScope
import com.tokopedia.home.account.presentation.fragment.SellerAccountFragment

import dagger.Component

/**
 * @author by alvinatin on 14/08/18.
 */

@Component(modules = [SellerAccountModule::class], dependencies = [BaseAppComponent::class])
@SellerAccountScope
interface SellerAccountComponent {
    fun inject(fragment: SellerAccountFragment)
}
