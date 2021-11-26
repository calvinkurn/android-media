package com.tokopedia.applink.imagepicker

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

object DeeplinkMapperImagePicker {

    fun getImagePickerV2Deeplink(deeplink: String): String {
        val regexExp = "${ApplinkConst.IMAGE_PICKER_V2}/?".toRegex()
        return deeplink.replace(regexExp, ApplinkConstInternalGlobal.IMAGE_PICKER_V2)
    }
}