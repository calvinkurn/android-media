package com.tokopedia.layanan_finansial.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@LayananScope
@Component(dependencies = [BaseAppComponent::class], modules = [LayananModule::class])
interface LayananComponent {
}