package com.tokopedia.filter.bottomsheet.keywordfilter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ExistsAsNegative
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.IsOriginalKeyword
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.MaxFiveNegative
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.removeFirst

internal class KeywordFilterDataView(
    val filter: Filter = Filter(),
    val originalKeyword: String = "",
): Visitable<SortFilterBottomSheetTypeFactory> {

    companion object {
        const val KEYWORD_FILTER_SEPARATOR = " -"
        const val MAX_NEGATIVE_KEYWORD = 5
    }

    sealed class KeywordFilterError {
        object MaxFiveNegative: KeywordFilterError()
        object IsOriginalKeyword: KeywordFilterError()
        object ExistsAsNegative: KeywordFilterError()
    }

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?) =
        typeFactory?.type(this) ?: 0

    private val mutableItemList = filter.options
        .map(Option::name)
        .map(::KeywordFilterItemDataView)
        .toMutableList()

    val itemList: List<KeywordFilterItemDataView> = mutableItemList

    fun addKeyword(
        keyword: String,
        onSuccess: () -> Unit,
        onError: (KeywordFilterError) -> Unit,
    ) {
        val sanitizeKeyword = keyword.sanitize()

        if (sanitizeKeyword.isEmpty()) {
            onSuccess()
            return
        }

        when {
            isMaximum() -> onError(MaxFiveNegative)
            isContainedInOriginalKeyword(sanitizeKeyword) -> onError(IsOriginalKeyword)
            isExistsAsNegativeKeyword(sanitizeKeyword) -> onError(ExistsAsNegative)
            else -> {
                mutableItemList.add(KeywordFilterItemDataView(sanitizeKeyword))
                onSuccess()
            }
        }
    }

    private fun String.sanitize() =
        this.trim()
            .removePrefix("-").trim()
            .removePrefix("\"").trim()
            .removeSuffix("\"").trim()

    private fun isMaximum() = itemList.size == MAX_NEGATIVE_KEYWORD

    private fun isContainedInOriginalKeyword(sanitizeKeyword: String) =
        originalKeyword
            .split(" ")
            .contains(sanitizeKeyword)

    private fun isExistsAsNegativeKeyword(sanitizeKeyword: String) =
        itemList.map { it.negativeKeyword }.contains(sanitizeKeyword)

    fun generateKeyword() =
        "$originalKeyword ${generateNegativeKeyword()}".trim()

    private fun generateNegativeKeyword() =
        itemList
            .map(KeywordFilterItemDataView::negativeKeyword)
            .joinToString(separator = " ", transform = ::modifyNegative)

    private fun modifyNegative(keyword: String) = "-\"$keyword\""

    fun removeKeyword(negativeKeyword: String) {
        mutableItemList.removeFirst { it.negativeKeyword == negativeKeyword }
    }
}
