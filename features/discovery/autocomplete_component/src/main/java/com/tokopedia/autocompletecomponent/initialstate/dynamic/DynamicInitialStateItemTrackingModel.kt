package com.tokopedia.autocompletecomponent.initialstate.dynamic

data class DynamicInitialStateItemTrackingModel(
        var userId: String = "",
        var title: String = "",
        var type: String = "",
        var list: List<Any> = listOf()
)