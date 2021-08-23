package com.tokopedia.attachproduct.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.attachproduct.view.activity.NewAttachProductActivity
import com.tokopedia.attachproduct.view.fragment.NewAttachProductFragment
import dagger.Component

@AttachProductScope
@Component(modules = [NewAttachProductModule::class, AttachProductViewModelModule::class],
    dependencies = [BaseAppComponent::class])
interface NewAttachProductComponent {

    fun inject(newAttachProductFragment: NewAttachProductFragment)

    fun inject(newAttachProductActivity: NewAttachProductActivity)
}
