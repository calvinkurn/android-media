package com.tokopedia.content.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagBindModule
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagModule
import com.tokopedia.content.common.sample.ContentProductTagSampleActivity
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