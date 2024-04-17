package com.tokopedia.home.beranda.data.newatf

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

    /**
     * Check if the slotting and metadata in dynamic position is the same with the other model.
     */
    fun positionEquals(atfDataList: AtfDataList): Boolean {
        val newList = atfDataList.listAtfData
        return listAtfData.size == newList.size &&
            listAtfData.zip(newList).all { (v1, v2) ->
                v1.atfMetadata == v2.atfMetadata
            }
    }

    /**
     * Populate ATF contents from other model into this, if the ATF list size match.
     * Use case: When getting dynamic position data from remote, we only get the slotting without content.
     * To avoid view layer rendering empty layout, ATF contents are populated into this remote ATF position.
     * @param atfDataList model of ATF list with the contents to be copied
     * @return model of ATF list with original position but content from the passed model.
     */
    fun copyAtfContentsFromCache(atfDataList: AtfDataList): AtfDataList {
        if (atfDataList.listAtfData.size != listAtfData.size) return this
        val newDynamicPosition = listAtfData.zip(atfDataList.listAtfData) { remotePosition, cachedAtf ->
            remotePosition.copy(atfContent = cachedAtf.atfContent, isCache = cachedAtf.isCache)
        }
        return this.copy(listAtfData = newDynamicPosition)
    }

    /**
     * Overwrite ATF contents within the list with updated contents, by using metadata as the predicate.
     * This is used for combining dynamic position with the latest remote data for each ATF component.
     * Important: only overwrite content if the new content is not null. If the new content is null, keep the old content.
     * @param newDataList list of data for each ATF component
     * @return model of ATF list with updated data
     */
    fun updateAtfContents(newDataList: List<AtfData?>): AtfDataList {
        val newList = listAtfData.map { currentData ->
            newDataList.find { it?.atfMetadata == currentData.atfMetadata }?.takeIf {
                it.atfContent != null
            } ?: currentData
        }
        return this.copy(listAtfData = newList)
    }

    /**
     * Check if dynamic position is not empty
     */
    fun isPositionReady(): Boolean {
        return this.listAtfData.isNotEmpty()
    }

    fun isDataError(): Boolean {
        return (this.status == STATUS_ERROR)
    }

    override fun equals(other: Any?): Boolean {
        return if (this.status == STATUS_ERROR) {
            false
        } else {
            super.equals(other)
        }
    }
}
