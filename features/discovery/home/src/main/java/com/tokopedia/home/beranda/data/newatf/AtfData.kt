package com.tokopedia.home.beranda.data.newatf

import com.google.gson.Gson
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.AtfContent

/**
 * Created by Frenzel
 */
data class AtfData(
    val atfMetadata: AtfMetadata,
    val atfContent: AtfContent? = null,
    val atfStatus: Int = AtfKey.STATUS_LOADING,
    val isCache: Boolean = true,
    val lastUpdate: Long = System.currentTimeMillis(),
) {
    fun getAtfContentAsJson(): String {
        return Gson().toJson(atfContent)
    }
}
