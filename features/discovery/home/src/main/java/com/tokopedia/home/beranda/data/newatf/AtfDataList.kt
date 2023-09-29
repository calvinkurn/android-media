package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.constant.AtfKey

/**
 * Created by Frenzel
 */
data class AtfDataList(
    val listAtfData: List<AtfData>,
    val isCache: Boolean,
    val needToFetchComponents: Boolean = true,
    val status: Int
) {
    companion object {
        const val STATUS_SUCCESS = 0
        const val STATUS_ERROR = 1
    }

    fun positionEquals(atfDataList: AtfDataList): Boolean {
        val newList = atfDataList.listAtfData
        return listAtfData.size == newList.size &&
            listAtfData.zip(newList).all { (v1, v2) ->
                v1.atfMetadata == v2.atfMetadata
            }
    }

    fun copyAtfContentsFrom(atfDataList: AtfDataList): AtfDataList {
        val atfContents = atfDataList.listAtfData.map { it.atfContent }
        if(atfContents.size != listAtfData.size) return this
        val newDynamicPosition = listAtfData.zip(atfContents) { data, atfContent ->
            data.copy(atfContent = atfContent)
        }
        val needToFetchComponents = atfContents.all { it == null }
        return this.copy(
            listAtfData = newDynamicPosition,
            needToFetchComponents = needToFetchComponents
        )
    }

    fun updateAtfContents(newDataList: List<AtfData?>): AtfDataList {
        val newList = listAtfData.map { currentData ->
            newDataList.find { it?.atfMetadata == currentData.atfMetadata }?.takeIf {
                it.atfContent != null
            } ?: currentData
        }
        return this.copy(listAtfData = newList)
    }

    fun isPositionReady(): Boolean {
        return this.status == STATUS_SUCCESS
            && this.listAtfData.isNotEmpty()
            && !this.isCache
    }

    fun isDataReady(): Boolean {
        return this.status == STATUS_SUCCESS
            && this.listAtfData.isNotEmpty()
            && this.listAtfData.all { it.atfStatus != AtfKey.STATUS_LOADING }

    }
}
