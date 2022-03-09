package com.tokopedia.media.common.intent

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.common.PickerParam

object PickerIntent : BaseIntent<MediaUiModel> {

    override val appLink: String
        get() = INTERNAL_MEDIA_PICKER

    override val keyName: String
        get() = "media-picker"

    fun intent(context: Context, param: PickerParam.() -> Unit = {}) {
        val pickerParam = PickerParam().apply(param)
        val intent = RouteManager.getIntent(context, appLink)
        intent.putExtra(keyName, pickerParam)
        context.startActivity(intent)
    }

}