package com.tokopedia.preview.di

import com.tokopedia.preview.di.module.PickerPreviewModule
import com.tokopedia.preview.di.scope.PreviewScope
import com.tokopedia.preview.ui.fragment.PickerPreviewFragment
import dagger.Component

@PreviewScope
@Component(modules = [
    PickerPreviewModule::class
])
interface PreviewComponent {
    fun inject(fragment: PickerPreviewFragment)
}