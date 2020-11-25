package com.tokopedia.deals.common.listener

/**
 * @author by jessica on 15/06/20
 */

interface SearchBarActionListener{
    fun onClickSearchBar()
    fun afterSearchBarTextChanged(text: String)
}