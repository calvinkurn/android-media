package com.tokopedia.imagepicker.common

import android.content.Intent

/**
 *  ..
 *  startActivityForResult (ImagePickerIntent)
 * }
 *
 * onActivityResult (data:Intent) {
 *    result = ImagePickerResultExtractor.extract(data)
 * }
 */
object ImagePickerResultExtractor {
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun extract (intent: Intent?): ImagePickerResult {
        if (intent == null) {
            return ImagePickerResult()
        }
        return ImagePickerResult(
            imageUrlOrPathList = intent.getStringArrayListExtra(PICKER_RESULT_PATHS) ?: mutableListOf(),
            originalImageUrl = intent.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE) ?: mutableListOf(),
            isEditted = (intent.getSerializableExtra(RESULT_IS_EDITTED) as? MutableList<Boolean>) ?: mutableListOf(),
            imagesFedIntoPicker = intent.getStringArrayListExtra(RESULT_IMAGES_FED_INTO_IMAGE_PICKER) ?: mutableListOf()
        )
    }
}