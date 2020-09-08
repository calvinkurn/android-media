package com.tokopedia.logisticdata.data.autocomplete

sealed class AutoCompleteVisitable

data class SuggestedPlace(
        val mainText: String = "",
        val secondaryText: String = "",
        val placeId: String = "",
        val errorCode: Int = 0) : AutoCompleteVisitable()

data class SavedAddress(
        var addrId: Int = 0,
        var addrName: String = "",
        var address1: String = "",
        var latitude: String = "",
        var longitude: String = ""
) : AutoCompleteVisitable()


data class LoadingType(val id: Int = 0) : AutoCompleteVisitable()
data class NoResultType(val id: Int = 0) : AutoCompleteVisitable()
data class HeaderType(val id: Int = 0) : AutoCompleteVisitable()