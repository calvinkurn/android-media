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
    val style: AtfStyle = AtfStyle()
) {

    fun getAtfContentAsJson(): String {
        return Gson().toJson(atfContent)
    }

    data class AtfStyle(
        val isBleeding: Boolean = false,
        val heightRatio: Int = 0,
        val widthRatio: Int = 0,
        val gradientColor: List<String> = emptyList(),
    )
}
