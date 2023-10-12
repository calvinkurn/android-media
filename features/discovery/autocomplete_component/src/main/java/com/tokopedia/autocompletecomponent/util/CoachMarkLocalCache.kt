package com.tokopedia.autocompletecomponent.util

interface CoachMarkLocalCache {
    fun shouldShowPlusIconCoachMark() : Boolean
    fun markShowPlusIconCoachMark()
    fun shouldShowAddedKeywordCoachMark() : Boolean
    fun markShowAddedKeywordCoachMark()

    fun shouldShowSuggestionCoachMark() : Boolean

    fun markShowSuggestionCoachMark()
}
