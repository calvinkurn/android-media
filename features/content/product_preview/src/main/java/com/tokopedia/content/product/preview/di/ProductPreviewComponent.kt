package com.tokopedia.content.product.preview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.product.preview.view.activity.ProductPreviewActivity
import dagger.Component

@ProductPreviewScope
@Component(
    modules = [
        ProductPreviewModule::class,
        ProductPreviewBindModule::class,
        ProductPreviewFragmentModule::class,
    ],
    dependencies = [BaseAppComponent::class],
)
interface ProductPreviewComponent {

    fun inject(activity: ProductPreviewActivity)

}
