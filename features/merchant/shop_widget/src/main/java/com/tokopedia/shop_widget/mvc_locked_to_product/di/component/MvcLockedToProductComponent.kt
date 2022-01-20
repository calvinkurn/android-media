package com.tokopedia.shop_widget.mvc_locked_to_product.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_widget.mvc_locked_to_product.di.module.MvcLockedToProductModule
import com.tokopedia.shop_widget.mvc_locked_to_product.di.scope.MvcLockedToProductScope
import com.tokopedia.shop_widget.mvc_locked_to_product.view.bottomsheet.MvcLockedToProductSortListBottomSheet
import com.tokopedia.shop_widget.mvc_locked_to_product.view.fragment.MvcLockedToProductFragment
import dagger.Component

@MvcLockedToProductScope
@Component(modules = [MvcLockedToProductModule::class], dependencies = [BaseAppComponent::class])
interface MvcLockedToProductComponent {
    fun inject(fragment: MvcLockedToProductFragment)
    fun inject(bottomSheet: MvcLockedToProductSortListBottomSheet)
}