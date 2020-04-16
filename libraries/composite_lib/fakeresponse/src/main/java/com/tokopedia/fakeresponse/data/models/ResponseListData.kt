package com.tokopedia.fakeresponse.data.models

data class ResponseListData(
        val id: Int,
        val title: String,
        var isChecked: Boolean,
        val customName: String? = null,
        val responseType: ResponseItemType,
        val updatedAt: Long? = null,
        override var isSelectedForExport: Boolean = false,
        override var isInExportMode: Boolean = false
) : SearchType

enum class ResponseItemType { GQL, REST }

interface SearchType {
    var isInExportMode: Boolean
    var isSelectedForExport: Boolean
}