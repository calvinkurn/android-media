package com.tokopedia.product.detail.imagepreview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPDPActivity
import dagger.Component

@ImagePreviewPDPScope
@Component(
    modules = [
        ImagePreviewPDPModule::class,
        ImagePreviewPDPWishlistModule::class,
        ImagePreviewPDPViewModelModule::class
    ],
    dependencies = [
        BaseAppComponent::class
    ]
)
interface ImagePreviewPDPComponent {
    fun inject(activity: ImagePreviewPDPActivity)
}