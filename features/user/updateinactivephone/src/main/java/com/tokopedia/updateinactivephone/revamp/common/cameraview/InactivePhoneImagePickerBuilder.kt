package com.tokopedia.updateinactivephone.revamp.common.cameraview

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef

class InactivePhoneImagePickerBuilder constructor(
        title: String,
        @ImagePickerTabTypeDef imagePickerTabTypeDef: IntArray
) : ImagePickerBuilder(title, imagePickerTabTypeDef, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB, DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true, null, null)