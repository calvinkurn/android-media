package com.tokopedia.media.common.intent

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.common.PickerParam

object PickerIntent : BaseIntent<MediaUiModel> {

    override val appLink: String
        get() = INTERNAL_MEDIA_PICKER

    override val keyName: String
        get() = "media-picker"

    // TODO create intent for multiple use-cases

    /**
     * how to use:
     *
     * PickerIntent.singleSelection(context) {
     *    ...
     *    maxMediaSize(5)
     *    maxVideoSize(2)
     *    withEditor()
     *    ...
     * }
     */
    fun singleSelection(context: Context, param: PickerParam.() -> Unit) {
        val pickerParam = PickerParam().apply(param)

        // TODO
    }

}