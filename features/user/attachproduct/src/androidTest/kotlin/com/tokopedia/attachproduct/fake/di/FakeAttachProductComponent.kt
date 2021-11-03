package com.tokopedia.attachproduct.fake.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.attachproduct.AttachProductTest
import com.tokopedia.attachproduct.di.AttachProductComponent
import com.tokopedia.attachproduct.di.AttachProductScope
import com.tokopedia.attachproduct.di.AttachProductViewModelModule
import com.tokopedia.attachproduct.fake.view.fragment.AttachProductTestFragment
import dagger.Component

@AttachProductScope
@Component(modules = [FakeAttachProductModule::class, AttachProductViewModelModule::class],
    dependencies = [FakeBaseAppComponent::class])
interface FakeAttachProductComponent : AttachProductComponent {

    fun inject(attachProductTest: AttachProductTest)
}