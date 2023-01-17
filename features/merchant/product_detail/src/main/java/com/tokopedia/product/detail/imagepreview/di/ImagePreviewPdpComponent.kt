package com.tokopedia.product.detail.imagepreview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPdpActivity
import dagger.Component

@ImagePreviewPdpScope
@Component(
    modules = [
        ImagePreviewPdpModule::class,
        ImagePreviewPdpViewModelModule::class
    ],
    dependencies = [
        BaseAppComponent::class
    ]
)
interface ImagePreviewPdpComponent {
    fun inject(activity: ImagePreviewPdpActivity)
}