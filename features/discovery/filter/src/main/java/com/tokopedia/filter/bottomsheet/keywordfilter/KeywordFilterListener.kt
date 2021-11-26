package com.tokopedia.filter.bottomsheet.keywordfilter

internal interface KeywordFilterListener {

    fun scrollToPosition(position: Int)

    fun onChangeKeywordFilter(keywordFilterDataView: KeywordFilterDataView)
}