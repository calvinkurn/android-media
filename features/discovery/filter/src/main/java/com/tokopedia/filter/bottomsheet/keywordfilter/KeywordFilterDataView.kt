package com.tokopedia.filter.bottomsheet.keywordfilter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ExistsAsNegative
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ForbiddenCharacter
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.IsOriginalKeyword
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.MaxFiveNegative
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.removeFirst

internal class KeywordFilterDataView(
    val filter: Filter = Filter(),
): Visitable<SortFilterBottomSheetTypeFactory> {

    companion object {
        const val MAX_NEGATIVE_KEYWORD = 5

        /**
         * Regex Pattern to only allow one or more of these characters:
         * 1. Alphanumeric characters
         * 2. Whitelisted special characters: - . , / + % & [space]
        */
        const val WHITELISTED_CHARACTER_REGEX_PATTERN = "^[a-zA-Z0-9-.,/+%& ]+\$"
    }

    sealed class KeywordFilterError {
        object MaxFiveNegative: KeywordFilterError()
        object IsOriginalKeyword: KeywordFilterError()
        object ExistsAsNegative: KeywordFilterError()
        object ForbiddenCharacter: KeywordFilterError()
    }

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?) =
        typeFactory?.type(this) ?: 0

    private val mutableItemList = filter.options
        .filter { it.key == Option.KEY_NEGATIVE_KEYWORD }
        .map(Option::name)
        .map(::KeywordFilterItemDataView)
        .toMutableList()

    private val originalKeyword = filter.options
        .find { it.key == Option.KEY_MAIN_KEYWORD }
        ?.name
        ?: ""

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
            isContainForbiddenCharacters(sanitizeKeyword) -> onError(ForbiddenCharacter)
            else -> {
                mutableItemList.add(KeywordFilterItemDataView(sanitizeKeyword))
                onSuccess()
            }
        }
    }

    private fun String.sanitize() =
        this.trim()
            .removePrefix("-").trim()
            .removeSurrounding("\"").trim()

    private fun isMaximum() = itemList.size == MAX_NEGATIVE_KEYWORD

    private fun isContainedInOriginalKeyword(sanitizeKeyword: String) =
        originalKeyword
            .split(" ")
            .any { it.equals(sanitizeKeyword, ignoreCase = true) }

    private fun isExistsAsNegativeKeyword(sanitizeKeyword: String) =
        itemList.map { it.negativeKeyword }.contains(sanitizeKeyword)

    private fun isContainForbiddenCharacters(sanitizeKeyword: String) =
        !sanitizeKeyword.matches(Regex(WHITELISTED_CHARACTER_REGEX_PATTERN))

    fun generateKeyword() =
        "$originalKeyword ${generateNegativeKeyword()}".trim()

    private fun generateNegativeKeyword() =
        itemList
            .map(KeywordFilterItemDataView::negativeKeyword)
            .joinToString(separator = " ", transform = ::modifyNegative)

    private fun modifyNegative(keyword: String) =
        "-\"${keyword.removeSurrounding("\"")}\""

    fun removeKeyword(negativeKeyword: String) {
        mutableItemList.removeFirst { it.negativeKeyword == negativeKeyword }
    }
}
