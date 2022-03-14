package com.tokopedia.picker.common.intent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
import com.tokopedia.picker.common.PickerParam

object PickerIntent {
    private const val KEY_PICKER_PARAM = "key-picker-param"

    fun intent(context: Context, param: PickerParam.() -> Unit = {}): Intent {
        val pickerParam = PickerParam().apply(param)

        return RouteManager.getIntent(context, INTERNAL_MEDIA_PICKER).apply {
            putExtra(KEY_PICKER_PARAM, pickerParam)
        }
    }

    fun result(intent: Intent?): ArrayList<String> {
        return intent?.getStringArrayListExtra(KEY_PICKER_PARAM)?: arrayListOf()
    }

    fun result(bundle: Bundle?): ArrayList<String> {
        return bundle?.getStringArrayList(KEY_PICKER_PARAM)?: arrayListOf()
    }
}