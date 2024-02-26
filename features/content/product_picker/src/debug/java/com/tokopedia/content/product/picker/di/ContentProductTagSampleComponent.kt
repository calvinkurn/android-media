package com.tokopedia.content.product.picker.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.product.picker.ugc.di.module.ContentCreationProductTagBindModule
import com.tokopedia.content.product.picker.ugc.di.module.ContentCreationProductTagModule
import com.tokopedia.content.product.picker.sample.ContentProductTagSampleActivity
import dagger.Component

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
@ContentProductTagSampleScope
@Component(
    modules = [
        ContentProductTagSampleModule::class,
        ContentProductTagSampleBindModule::class,
        ContentCreationProductTagBindModule::class,
        ContentCreationProductTagModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface ContentProductTagSampleComponent {

    fun inject(activity: ContentProductTagSampleActivity)
}
