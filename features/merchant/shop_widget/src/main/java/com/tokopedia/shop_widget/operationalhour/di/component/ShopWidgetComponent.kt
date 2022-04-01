package com.tokopedia.shop_widget.operationalhour.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_widget.operationalhour.di.module.ShopWidgetModule
import com.tokopedia.shop_widget.operationalhour.di.scope.ShopWidgetScope
import com.tokopedia.shop_widget.operationalhour.view.bottomsheet.ShopOperationalHoursListBottomSheet
import dagger.Component

@ShopWidgetScope
@Component(modules = [ShopWidgetModule::class], dependencies = [BaseAppComponent::class])
interface ShopWidgetComponent {
    fun inject(bottomSheet: ShopOperationalHoursListBottomSheet)
}