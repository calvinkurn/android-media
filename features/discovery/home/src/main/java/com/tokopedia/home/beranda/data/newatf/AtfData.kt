package com.tokopedia.home.beranda.data.newatf

import com.google.gson.Gson
import com.tokopedia.home.constant.AtfKey

data class AtfData(
    val atfMetadata: AtfMetadata,
    val atfContent: AtfContent? = null,
    val atfStatus: Int = AtfKey.STATUS_LOADING,
    val isCache: Boolean = true,
) {
    fun getAtfContentAsJson(): String {
        return Gson().toJson(atfContent)
    }
}
