package com.tokopedia.stories.creation.di.product

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.content.product.picker.seller.di.ProductPickerFragmentModule
import com.tokopedia.stories.creation.container.ProductPickerTestActivity
import dagger.Component

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
@Component(
    modules = [
        ProductPickerFragmentModule::class,
        ProductPickerTestModule::class,
        ProductPickerBindTestModule::class,
        ContentCoachMarkSharedPrefModule::class,
    ],
    dependencies = [
        BaseAppComponent::class
    ]
)
@ProductPickerTestScope
interface ProductPickerTestComponent {

    fun inject(activity: ProductPickerTestActivity)
}
