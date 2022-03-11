package com.tokopedia.picker.common

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.picker.common.types.PickerModeType
import com.tokopedia.picker.common.types.PickerPageType
import com.tokopedia.picker.common.types.PickerSelectionType

class PickerCacheManager constructor(
    context: Context
) : LocalCacheHandler(context, PREF_NAME_CACHE_PICKER) {

    fun getParam(): PickerParam {
        val param = getString(KEY_PICKER_PARAM) ?: return PickerParam()
        return Gson().fromJson(param, PickerParam::class.java)
    }

    fun setParam(
        @PickerPageType page: Int,
        @PickerModeType mode: Int,
        @PickerSelectionType selection: Int
    ) {
        val isOnlyVideo = mode == PickerModeType.VIDEO_ONLY
        val isIncludeVideo = mode == PickerModeType.COMMON
        val isMultipleSelection = selection == PickerSelectionType.MULTIPLE

        val result = PickerParam(
            pageType = page,
            isOnlyVideo = isOnlyVideo,
            isIncludeVideo = isIncludeVideo,
            isMultipleSelection = isMultipleSelection,
        )

        val toJson = Gson().toJson(result)

        putString(KEY_PICKER_PARAM, toJson)
        applyEditor()
    }

    companion object {
        private const val PREF_NAME_CACHE_PICKER = "cache_media_picker"
        private const val KEY_PICKER_PARAM = "key_picker_param"
    }

}