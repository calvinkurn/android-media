package com.rahullohra.fakeresponse.data.models

data class ResponseListData(
    val id: Int,
    val title: String,
    var isChecked: Boolean,
    val customName: String? = null,
    val responseType: ResponseItemType,
    var isSelectedForExport:Boolean = false,
    var isInExportMode:Boolean = false
):SearchType

enum class ResponseItemType { GQL, REST }

interface SearchType