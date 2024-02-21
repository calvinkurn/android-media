package com.tokopedia.autocompletecomponent.unify

/**
 * Created by @milhamj on on 2/20/24.
 **/

data class AutoCompleteAppLogData (
    val enterFrom: String = "",
    val searchEntrance: String = "",
    var imprId: String = "",
    var newSugSessionId: Long = 0
)
