package com.tokopedia.tokopedianow.educationalinfo.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.educationalinfo.di.module.EducationalInfoModule
import com.tokopedia.tokopedianow.educationalinfo.di.scope.EducationalInfoScope
import com.tokopedia.tokopedianow.educationalinfo.presentation.fragment.TokoNowEducationalInfoFragment
import dagger.Component

@EducationalInfoScope
@Component(
    modules = [
        EducationalInfoModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface EducationalInfoComponent {
    fun inject(fragment: TokoNowEducationalInfoFragment)
}