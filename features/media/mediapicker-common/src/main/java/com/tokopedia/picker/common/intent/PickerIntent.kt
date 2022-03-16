package com.tokopedia.picker.common.intent

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
import com.tokopedia.picker.common.PickerParam

const val EXTRA_PICKER_PARAM = "key-picker-param"
const val RESULT_PICKER = "result-picker"

object PickerIntent {

    fun intent(context: Context, param: PickerParam.() -> Unit = {}): Intent {
        val pickerParam = PickerParam().apply(param)

        return RouteManager.getIntent(context, INTERNAL_MEDIA_PICKER).apply {
            putExtra(EXTRA_PICKER_PARAM, pickerParam)
        }
    }

    fun get(intent: Intent?): PickerParam {
        return intent?.getParcelableExtra(EXTRA_PICKER_PARAM)?: PickerParam()
    }

}