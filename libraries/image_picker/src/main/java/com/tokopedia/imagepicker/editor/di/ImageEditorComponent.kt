package com.tokopedia.imagepicker.editor.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.imagepicker.editor.di.module.ImageEditorModule
import com.tokopedia.imagepicker.editor.di.scope.ImageEditorScope
import com.tokopedia.imagepicker.editor.main.view.ImageEditPreviewFragment
import dagger.Component

@ImageEditorScope
@Component(modules = [
    ImageEditorModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ImageEditorComponent {
    fun inject(fragment: ImageEditPreviewFragment)
}