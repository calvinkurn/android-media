package com.tokopedia.imagepicker_insta.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.imagepicker_insta.di.module.DispatcherModule
import com.tokopedia.imagepicker_insta.di.module.FragmentFactoryModule
import com.tokopedia.imagepicker_insta.di.module.ImagePickerModule
import com.tokopedia.imagepicker_insta.fragment.FeedVideoDepreciationBottomSheet
import com.tokopedia.imagepicker_insta.fragment.ImagePickerInstaMainFragment
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import dagger.Component
import javax.inject.Scope

@ImagePickerScope
@Component(
    modules = [
        DispatcherModule::class,
        ImagePickerModule::class,
        UGCOnboardingModule::class,
        FragmentFactoryModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ImagePickerComponent {
    fun inject(viewModel: PickerViewModel)

    fun inject(fragment: ImagePickerInstaMainFragment)

    fun inject(bottomSheet: FeedVideoDepreciationBottomSheet)
}

@Scope
@Retention
annotation class ImagePickerScope
