package com.tokopedia.product.addedit.preview.di

import com.tokopedia.mediauploader.di.MediaUploaderModule
import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import com.tokopedia.product.addedit.preview.presentation.service.AddProductUploadService
import com.tokopedia.product.addedit.preview.presentation.service.BaseProductUploadService
import dagger.Component

@AddEditProductPreviewScope
@Component(modules = [MediaUploaderModule::class, ProductAddUseCaseModule::class, AddEditProductPreviewModule::class],
        dependencies = [AddEditProductComponent::class])
interface AddEditProductPreviewComponent {
    fun inject(fragment: AddEditProductPreviewFragment)
    fun inject(baseService: BaseProductUploadService)
}