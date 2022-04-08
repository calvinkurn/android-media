package com.tokopedia.media.common.utils

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.picker.common.PickerParam
import javax.inject.Inject

class ParamCacheManager @Inject constructor(
    @ApplicationContext context: Context
) : LocalCacheHandler(context, PREF_NAME_CACHE_PICKER) {

    fun get(): PickerParam {
        val param = getString(KEY_PICKER_PARAM) ?: return PickerParam()
        return Gson().fromJson(param, PickerParam::class.java)
    }

    fun setParam(param: PickerParam) {
        val toJson = Gson().toJson(param)
        putString(KEY_PICKER_PARAM, toJson)
        applyEditor()
    }

    companion object {
        private const val PREF_NAME_CACHE_PICKER = "cache_media_picker"
        private const val KEY_PICKER_PARAM = "key_picker_param"
    }

}