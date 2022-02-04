package com.tokopedia.media.preview.di

import com.tokopedia.media.preview.di.module.PickerPreviewModule
import com.tokopedia.media.preview.di.scope.PreviewScope
import com.tokopedia.media.preview.ui.fragment.PickerPreviewFragment
import dagger.Component

@PreviewScope
@Component(modules = [
    PickerPreviewModule::class
])
interface PreviewComponent {
    fun inject(fragment: PickerPreviewFragment)
}