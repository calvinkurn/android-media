package com.tokopedia.picker.common.cache

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import javax.inject.Inject

interface PickerCacheManager {
    fun get(): PickerParam
    fun set(param: PickerParam): PickerParam
    fun disposeSubPicker()
}

class PickerParamCacheManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) : LocalCacheHandler(context, PREF_CACHE_PICKER), PickerCacheManager {

    private var param: PickerParam? = null

    override fun get(): PickerParam {
        if (param != null) return param!!

        val parentSource = getString(KEY_SOURCE)
        val subSource = getString(KEY_SUB_SOURCE).orEmpty()

        param = if (subSource.isEmpty()) {
            gson.fromJson(getString(parentSource), PickerParam::class.java)
        } else {
            gson.fromJson(getString(subSource), PickerParam::class.java)
        }

        return param!!
    }

    override fun set(param: PickerParam): PickerParam {
        val source = if (param.subPageSource() != PageSource.Unknown) {
            param.subPageSourceName()
        } else {
            param.pageSourceName()
        }

        putString(KEY_SOURCE, param.pageSourceName())
        putString(KEY_SUB_SOURCE, param.subPageSourceName())
        putString(source, gson.toJson(param))

        applyEditor()

        return param
    }

    override fun disposeSubPicker() {
        param = null

        val subSource = getString(KEY_SUB_SOURCE).orEmpty()
        if (subSource.isNotEmpty()) {
            remove(KEY_SUB_SOURCE)
            remove(subSource)
        }

        applyEditor()
    }

    companion object {
        private const val PREF_CACHE_PICKER = "cache_media_picker"

        private const val KEY_SOURCE = "key_source"
        private const val KEY_SUB_SOURCE = "key_sub_source"
    }
}
