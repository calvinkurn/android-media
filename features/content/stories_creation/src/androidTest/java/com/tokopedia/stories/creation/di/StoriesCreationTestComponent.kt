package com.tokopedia.stories.creation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.content.common.di.ContentFragmentFactoryModule
import com.tokopedia.content.product.picker.seller.di.ProductPickerBindModule
import com.tokopedia.content.product.picker.seller.di.ProductPickerFragmentModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on October 24, 2023
 */
@Component(
    modules = [
        StoriesCreationTestModule::class,
        StoriesCreationBindTestModule::class,
        StoriesCreationViewModelModule::class,
        ProductPickerFragmentModule::class,
        ProductPickerBindModule::class,
        ContentFragmentFactoryModule::class,
        ContentCoachMarkSharedPrefModule::class,
    ],
    dependencies = [
        BaseAppComponent::class,
    ]
)
@StoriesCreationTestScope
interface StoriesCreationTestComponent : StoriesCreationComponent
