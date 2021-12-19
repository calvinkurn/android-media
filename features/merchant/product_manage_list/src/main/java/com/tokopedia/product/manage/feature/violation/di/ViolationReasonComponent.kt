package com.tokopedia.product.manage.feature.violation.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.violation.view.bottomsheet.ViolationReasonBottomSheet
import dagger.Component

@ViolationReasonScope
@Component(dependencies = [ProductManageComponent::class])
interface ViolationReasonComponent {
    fun inject(bottomSheet: ViolationReasonBottomSheet)
}