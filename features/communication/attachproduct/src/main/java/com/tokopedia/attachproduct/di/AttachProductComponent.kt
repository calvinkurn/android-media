package com.tokopedia.attachproduct.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment
import dagger.Component

@AttachProductScope
@Component(modules = [AttachProductModule::class, AttachProductViewModelModule::class],
    dependencies = [BaseAppComponent::class])
interface AttachProductComponent {

    fun inject(attachProductFragment: AttachProductFragment)
}
