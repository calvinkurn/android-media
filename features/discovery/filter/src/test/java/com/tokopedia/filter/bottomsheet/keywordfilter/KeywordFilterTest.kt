package com.tokopedia.filter.bottomsheet.keywordfilter

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.testutils.jsonToObject
import com.tokopedia.filter.testutils.shouldBe
import org.junit.Test

internal class KeywordFilterTest {

    @Test
    fun `get original keyword empty option`() {
        val keywordFilter = "keywordfilter/keyword-filter-empty.json".jsonToObject<Filter>()

        val keyword1 = "samsung"
        KeywordFilterDataView(keywordFilter, keyword1).assertOriginalKeyword(keyword1)

        val keyword2 = "samsung -remote"
        KeywordFilterDataView(keywordFilter, keyword2).assertOriginalKeyword(keyword2)
    }

    private fun KeywordFilterDataView.assertOriginalKeyword(expectedOriginalKeyword: String) {
        originalKeyword shouldBe expectedOriginalKeyword
    }

    @Test
    fun `get original keyword based on options`() {
        val keywordFilter = "keywordfilter/keyword-filter.json".jsonToObject<Filter>()

        val keyword = "samsung -tv"
        val expectedOriginalKeyword = "samsung"
        KeywordFilterDataView(keywordFilter, keyword)
            .assertOriginalKeyword(expectedOriginalKeyword)
    }

    @Test
    fun `addKeyword with empty string should not add into item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("")

        dataView.itemList.isEmpty() shouldBe true
    }

    private fun createKeywordFilterDataViewEmptyOption() =
        KeywordFilterDataView(
            filter = "keywordfilter/keyword-filter-empty.json".jsonToObject(),
            keywordParam = "samsung"
        )

    @Test
    fun `addKeyword with only dash should not add into item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("-")

        dataView.itemList.isEmpty() shouldBe true
    }

    @Test
    fun `addKeyword should add into item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        val newKeywordFilter = "tv"
        dataView.addKeyword(newKeywordFilter)

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe newKeywordFilter
    }

    @Test
    fun `addKeyword should add into item list without prefix dash`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        val newKeywordFilter = "-tv"
        dataView.addKeyword(newKeywordFilter)

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "tv"
    }

    @Test
    fun `addKeyword should add into item list after trimmed`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("- tv")

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "tv"
    }

    @Test
    fun `addKeyword should add into item list without quotes`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("-\"pro max\"")

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "pro max"
    }

    @Test
    fun `addKeyword should add into item list without space prefix and suffix`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword(" - \" pro max \" ")

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "pro max"
    }

    @Test
    fun `generateKeyword from keyword filter`() {
        val keywordParam = "samsung -tv"
        val dataView = KeywordFilterDataView(
            filter = "keywordfilter/keyword-filter.json".jsonToObject(),
            keywordParam = keywordParam
        )

        dataView.generateKeyword() shouldBe keywordParam

        dataView.addKeyword("-remote")
        dataView.generateKeyword() shouldBe "samsung -tv -remote"
    }

    @Test
    fun `generateKeyword with more than one word negative keyword should append quotes`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("pro max")

        dataView.generateKeyword() shouldBe "samsung -\"pro max\""
    }
}