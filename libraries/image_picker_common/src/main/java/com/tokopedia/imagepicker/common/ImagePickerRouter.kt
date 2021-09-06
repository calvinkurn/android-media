package com.tokopedia.imagepicker.common

import android.content.Intent

fun Intent.putImagePickerBuilder(imagePickerBuilder: ImagePickerBuilder, sourcePage: ImagePickerPageSource? = null) {
    putExtra(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder)
    sourcePage?.let {
        putExtra(EXTRA_SOURCE_PAGE, sourcePage.value)
    }
}

fun Intent.putImageEditorBuilder(imageEditorBuilder: ImageEditorBuilder, sourcePage: ImagePickerPageSource? = null) {
    putExtra(EXTRA_IMAGE_EDITOR_BUILDER, imageEditorBuilder)
    sourcePage?.let {
        putExtra(EXTRA_SOURCE_PAGE, sourcePage.value)
    }
}
