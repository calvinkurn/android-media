package com.tokopedia.layanan_finansial.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.layanan_finansial.view.fragment.LayananFragment
import dagger.Component

@LayananScope
@Component(dependencies = [BaseAppComponent::class], modules = [LayananModule::class,ViewModelModule::class])
public interface LayananComponent {
    fun inject(fragment : LayananFragment)
}