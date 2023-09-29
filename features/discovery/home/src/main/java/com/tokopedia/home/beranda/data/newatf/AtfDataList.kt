package com.tokopedia.home.beranda.data.newatf

import android.util.Log
import com.tokopedia.home.constant.AtfKey

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
        if (atfContents.size != listAtfData.size) return this
        val newDynamicPosition = listAtfData.zip(atfContents) { data, atfContent ->
            data.copy(atfContent = atfContent)
        }
        val needToFetchComponents = atfContents.all { it == null }
        return this.copy(
            listAtfData = newDynamicPosition,
            needToFetchComponents = needToFetchComponents
        )
    }

//
//    fun updateMetaData(
//        isCache: Boolean = this.isCache,
//        isLatestData: Boolean = this.isLatestData,
//        listAtfMetadata: List<AtfMetadata> = this.listAtfData.map { it.atfMetadata }
//    ): AtfDataList {
//        return this.copy(
//            isCache = isCache,
//            isLatestData = isLatestData,
//            listAtfData = this.listAtfData.zip(listAtfMetadata) { data, atfMetadata ->
//                data.copy(atfMetadata = atfMetadata)
//            }
//        )
//    }

    fun updateAtfContents(newDataList: List<AtfData?>): AtfDataList {
//        val newList = listAtfData.toMutableList()
//        newAtfData.forEach { data ->
//            data?.let {
//                val index = newList.indexOfFirst { it.atfMetadata == data.atfMetadata }
//                newList[index] = data
//            }
//        }
        Log.d("atfflow", "updateAtfContents: $listAtfData\n$newDataList")

        val newList = listAtfData.map { currentData ->
            newDataList.find { it?.atfMetadata == currentData.atfMetadata }?.takeIf {
                it.atfContent != null
            } ?: currentData
        }
        Log.d("atfflow", "updateAtfContents: result $newList")

        return this.copy(listAtfData = newList)
    }
//
//    fun areNewDataMatches(newAtfData: List<AtfData?>): Boolean {
//        return this.listAtfData.all { current ->
//            newAtfData.any { new ->
//                current.atfMetadata == new?.atfMetadata
//            }
//        }
//    }

    fun isDataReady(): Boolean {
        return (
            this.status == STATUS_SUCCESS && this.listAtfData.isNotEmpty() &&
                this.listAtfData.all { it.atfStatus != AtfKey.STATUS_LOADING }
            )
    }

    fun isDataError(): Boolean {
        return (this.status == STATUS_ERROR)
    }
}
