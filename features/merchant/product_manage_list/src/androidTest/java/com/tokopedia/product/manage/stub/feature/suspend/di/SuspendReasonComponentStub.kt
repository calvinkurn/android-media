package com.tokopedia.product.manage.stub.feature.suspend.di

import com.tokopedia.product.manage.feature.suspend.di.SuspendReasonComponent
import com.tokopedia.product.manage.feature.suspend.di.SuspendReasonScope
import com.tokopedia.product.manage.stub.common.di.component.ProductManageComponentStub
import com.tokopedia.product.manage.stub.feature.suspend.view.SuspendReasonBottomSheetStub
import dagger.Component

@SuspendReasonScope
@Component(dependencies = [ProductManageComponentStub::class])
interface SuspendReasonComponentStub : SuspendReasonComponent {
    fun inject(bottomSheet: SuspendReasonBottomSheetStub)
}
