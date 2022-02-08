package com.tokopedia.attachvoucher.stub.di

import com.tokopedia.attachvoucher.common.di.FakeBaseAppComponent
import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.di.AttachVoucherScope
import com.tokopedia.attachvoucher.di.AttachVoucherViewModelModule
import com.tokopedia.attachvoucher.test.base.AttachVoucherTest
import dagger.Component

@AttachVoucherScope
@Component(
    modules = [
        AttachVoucherModuleStub::class,
        AttachVoucherViewModelModule::class,
        AttachVoucherUseCaseModuleStub::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface AttachVoucherComponentStub: AttachVoucherComponent {
    fun inject(attachVoucherTest: AttachVoucherTest)
}