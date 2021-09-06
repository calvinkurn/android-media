package com.tokopedia.filter.bottomsheet.keywordfilter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option

internal class KeywordFilterDataView(
    val filter: Filter = Filter(),
    keywordParam: String = "",
): Visitable<SortFilterBottomSheetTypeFactory> {

    companion object {
        const val KEYWORD_FILTER_SEPARATOR = " -"
    }

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?) = 0

    private val mutableItemList = filter.options
        .map(Option::name)
        .map(::KeywordFilterItemDataView)
        .toMutableList()

    val itemList: List<KeywordFilterItemDataView> = mutableItemList
    val originalKeyword = keywordParam
        .split(KEYWORD_FILTER_SEPARATOR)
        .filter(::isNotKeywordFilter)
        .joinToString(separator = KEYWORD_FILTER_SEPARATOR)

    private fun isNotKeywordFilter(keyword: String) =
        !itemList.map { it.negativeKeyword }.contains(keyword)

    fun addKeyword(keyword: String) {
        val sanitizeKeyword = keyword.sanitize()

        if (sanitizeKeyword.isEmpty()) return

        mutableItemList.add(KeywordFilterItemDataView(sanitizeKeyword))
    }

    private fun String.sanitize() =
        this.trim()
            .removePrefix("-").trim()
            .removePrefix("\"").trim()
            .removeSuffix("\"").trim()

    fun generateKeyword() = originalKeyword + itemList
        .map(KeywordFilterItemDataView::negativeKeyword)
        .joinToString(
            prefix = KEYWORD_FILTER_SEPARATOR,
            separator = KEYWORD_FILTER_SEPARATOR,
            transform = ::modifyNegative,
        )

    private fun modifyNegative(keyword: String) =
        if (keyword.contains(" ")) "\"$keyword\""
        else keyword
}
