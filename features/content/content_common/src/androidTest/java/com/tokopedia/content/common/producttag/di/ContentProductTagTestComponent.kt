package com.tokopedia.content.common.producttag.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.product.picker.ContentProductTagSampleScope
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import dagger.Component

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@com.tokopedia.content.product.picker.ContentProductTagSampleScope
@Component(
    modules = [
        ContentProductTagTestModule::class,
        ContentProductTagTestBindModule::class,
        ContentCreationProductTagBindTestModule::class,
        ContentCreationProductTagTestModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface ContentProductTagTestComponent {

    fun inject(activity: ContentProductTagTestActivity)
}
