package com.tokopedia.picker.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER

object MediaPicker {

    /**
     * Intent builder for Picker module.
     * @param: context
     * @param: [PickerParam]
     *
     * this intent builder have built-in appLink for media-picker.
     * you can simply passing a context and the [PickerParam] itself.
     *
     * how-to:
     *
     * PickerIntent.intent(context) {
     *    withEditor(true)
     *    singleSelectionMode()
     *    pageType(PageType.CAMERA)
     * }
     *
     * and you can use a normal startActivityForIntent to run it.
     */
    fun intent(
        context: Context,
        param: PickerParam.() -> Unit = {}
    ): Intent {
        return intent(
            context = context,
            appLink = INTERNAL_MEDIA_PICKER,
            param = param
        )
    }

    /**
     * The intent builder with a custom appLink for Picker module.
     * @param: context
     * @param: appLink
     * @param: [PickerParam]
     *
     * this intent builder is similar like [MediaPicker.intent] above.
     * The differentiate is you can put a custom appLink for picker module
     * on the parameter.
     *
     * for now, the picker module have ability to choose what page want to render first.
     * we have 2 type of [PageType], [PageType.CAMERA] and [PageType.GALLERY]
     *
     * as the user of page owner, you can choose what page want to render first
     * following this appLink:
     * `tokopedia-android-internal://global/media-picker?start=(0/1)`
     *
     * start index:
     * 0 = camera page
     * 1 = gallery page
     *
     */
    fun intent(
        context: Context,
        appLink: String = INTERNAL_MEDIA_PICKER,
        param: PickerParam.() -> Unit = {}
    ): Intent {
        return RouteManager.getIntent(context, appLink).apply {
            putExtra(
                EXTRA_PICKER_PARAM,
                PickerParam().apply(param)
            )
        }
    }

    fun intentWithGalleryFirst(
        context: Context,
        param: PickerParam.() -> Unit = {}
    ): Intent {
        return intent(
            context = context,
            appLink = "${INTERNAL_MEDIA_PICKER}?start=1",
            param = param
        )
    }

    /**
     * For extracting the data from picker module, you can use this [MediaPicker.result].
     * @param: data
     *
     * the data type of picker module is [PickerResult]. A [PickerResult] contains 2 entities,
     * 1. originalPaths, the original files from camera or gallery.
     * 2. compressedImages, the image compressed files
     *
     * Note:
     * `originalPaths` is a proper file path to upload.
     * `compressedImages` will hit the save-to-gallery on device.
     */
    fun result(data: Intent?): PickerResult {
        return data?.getParcelableExtra(EXTRA_RESULT_PICKER) ?: PickerResult()
    }

}
