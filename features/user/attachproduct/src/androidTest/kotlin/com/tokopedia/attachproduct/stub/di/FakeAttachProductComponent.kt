package com.tokopedia.attachproduct.stub.di

import com.tokopedia.attachproduct.AttachProductTest
import com.tokopedia.attachproduct.di.AttachProductComponent
import com.tokopedia.attachproduct.di.AttachProductScope
import com.tokopedia.attachproduct.di.AttachProductViewModelModule
import dagger.Component

@AttachProductScope
@Component(modules = [FakeAttachProductModule::class, AttachProductViewModelModule::class],
    dependencies = [FakeBaseAppComponent::class])
interface FakeAttachProductComponent : AttachProductComponent {

    fun inject(attachProductTest: AttachProductTest)
}