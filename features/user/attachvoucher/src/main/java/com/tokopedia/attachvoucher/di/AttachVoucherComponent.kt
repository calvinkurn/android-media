package com.tokopedia.attachvoucher.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.attachvoucher.view.fragment.AttachVoucherFragment
import dagger.Component

@AttachVoucherScope
@Component(
        modules = [AttachVoucherModule::class, AttachVoucherViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface AttachVoucherComponent {
    fun inject(fragment: AttachVoucherFragment)
}