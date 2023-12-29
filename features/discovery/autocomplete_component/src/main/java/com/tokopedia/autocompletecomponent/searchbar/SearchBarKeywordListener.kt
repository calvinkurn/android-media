package com.tokopedia.autocompletecomponent.searchbar

import android.view.View

interface SearchBarKeywordListener {
    fun onKeywordRemoved(searchBarKeyword: SearchBarKeyword)
    fun onKeywordSelected(searchBarKeyword: SearchBarKeyword)
    fun showAddedKeywordCoachMark(view: View)
}
