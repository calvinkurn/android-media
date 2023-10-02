package com.tokopedia.filter.common.data

interface IOption {

    val key: String
    val value: String
    val name: String
    var inputState: String
    val isNew: Boolean
    val iconUrl: String
    val hexColor: String
    val inputType: String
    val description: String
}
