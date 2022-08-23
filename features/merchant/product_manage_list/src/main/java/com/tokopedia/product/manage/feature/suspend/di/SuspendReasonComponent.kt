package com.tokopedia.product.manage.feature.suspend.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.suspend.view.bottomsheet.SuspendReasonBottomSheet
import dagger.Component

@SuspendReasonScope
@Component(dependencies = [ProductManageComponent::class])
interface SuspendReasonComponent {
    fun inject(bottomSheet: SuspendReasonBottomSheet)
}