package com.tokopedia.imagepicker.common

import android.content.Intent

fun Intent.putImagePickerBuilder(imagePickerBuilder: ImagePickerBuilder) {
    putExtra(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder)
}

fun Intent.putImageEditorBuilder(imageEditorBuilder: ImageEditorBuilder) {
    putExtra(EXTRA_IMAGE_EDITOR_BUILDER, imageEditorBuilder)
}