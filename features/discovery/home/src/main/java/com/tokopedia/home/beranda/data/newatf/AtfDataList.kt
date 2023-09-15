package com.tokopedia.home.beranda.data.newatf

data class AtfDataList(
    val listAtfData: List<AtfData>,
    val isCache: Boolean,
    val isDifferentPosition: Boolean = true,
) {
    val shouldFetchAtf = isDifferentPosition

    fun hasSamePosition(newList: List<AtfData>): Boolean {
        return listAtfData.size == newList.size &&
            listAtfData.zip(newList).all { (v1, v2) ->
                v1.atfMetadata == v2.atfMetadata
            }
    }

    fun copyAtfContents(atfContents: List<AtfContent?>): AtfDataList {
        if(atfContents.size != listAtfData.size) return this
        val newDynamicPosition = listAtfData.zip(atfContents) { data, atfContent ->
            data.copy(atfContent = atfContent)
        }
        return this.copy(listAtfData = newDynamicPosition)
    }
}
