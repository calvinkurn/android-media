package com.tokopedia.seller.menu.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.seller.menu.common.di.module.SellerMenuCommonModule
import com.tokopedia.seller.menu.common.di.scope.SellerMenuCommonScope
import com.tokopedia.seller.menu.common.view.bottomsheet.SettingsFreeShippingBottomSheet
import dagger.Component

@SellerMenuCommonScope
@Component(
    modules = [SellerMenuCommonModule::class],
    dependencies = [BaseAppComponent::class]
)
interface SellerMenuCommonComponent {

    fun inject(freeShippingBottomSheet: SettingsFreeShippingBottomSheet)
}