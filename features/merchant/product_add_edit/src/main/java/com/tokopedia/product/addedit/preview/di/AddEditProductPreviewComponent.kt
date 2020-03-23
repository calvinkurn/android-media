package com.tokopedia.product.addedit.preview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.di.MediaUploaderModule
import com.tokopedia.product.addedit.common.di.ProductAddUseCaseModule
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductUploadService
import dagger.Component

@AddEditProductPreviewScope
@Component(modules = [MediaUploaderModule::class, ProductAddUseCaseModule::class, AddEditProductPreviewModule::class],
        dependencies = [BaseAppComponent::class])
interface AddEditProductPreviewComponent {
    fun inject(fragment: AddEditProductPreviewFragment)
    fun inject(service: AddEditProductUploadService)
}