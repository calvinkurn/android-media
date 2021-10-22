package com.tokopedia.filter.bottomsheet.keywordfilter

import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ExistsAsNegative
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ForbiddenCharacter
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.IsOriginalKeyword
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.MaxFiveNegative
import com.tokopedia.filter.testutils.jsonToObject
import com.tokopedia.filter.testutils.shouldBe
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

internal class KeywordFilterTest {

    private var isSuccess: Boolean? = null
    private var error: KeywordFilterError? = null

    private val onSuccess = { isSuccess = true }
    private val onError = { it: KeywordFilterError -> error = it }

    @Test
    fun `addKeyword with empty string should not add into item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("")

        dataView.itemList.isEmpty() shouldBe true
        verifyOnSuccess()
    }

    private fun createKeywordFilterDataViewEmptyOption() =
        KeywordFilterDataView(
            filter = "keywordfilter/keyword-filter-empty.json".jsonToObject(),
        )

    private fun KeywordFilterDataView.addKeyword(keyword: String) {
        addKeyword(keyword, onSuccess, onError)
    }

    private fun verifyOnSuccess() {
        isSuccess shouldBe true
    }

    @Test
    fun `addKeyword with only dash should not add into item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("-")

        dataView.itemList.isEmpty() shouldBe true
        verifyOnSuccess()
    }

    @Test
    fun `addKeyword should add into item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        val newKeywordFilter = "tv"
        dataView.addKeyword(newKeywordFilter)

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe newKeywordFilter
        verifyOnSuccess()
    }

    @Test
    fun `addKeyword should add into item list without prefix dash`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        val newKeywordFilter = "-tv"
        dataView.addKeyword(newKeywordFilter)

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "tv"
        verifyOnSuccess()
    }

    @Test
    fun `addKeyword should add into item list after trimmed`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("- tv")

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "tv"
        verifyOnSuccess()
    }

    @Test
    fun `addKeyword should add into item list without quotes`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("-\"pro max\"")

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "pro max"
        verifyOnSuccess()
    }

    @Test
    fun `addKeyword should add into item list without space prefix and suffix`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword(" - \" pro max \" ")

        dataView.itemList.size shouldBe 1
        dataView.itemList[0].negativeKeyword shouldBe "pro max"
        verifyOnSuccess()
    }

    @Test
    fun `addKeyword more than 5 should be error`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("tv")
        dataView.addKeyword("remote")
        dataView.addKeyword("test")
        dataView.addKeyword("galaxy")
        dataView.addKeyword("testinglagi")
        dataView.addKeyword("testingerror")

        dataView.itemList.size shouldBe 5

        assertThat(error, instanceOf(MaxFiveNegative::class.java))
    }

    @Test
    fun `addKeyword cannot add original keyword`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("samsung")

        dataView.itemList.size shouldBe 0
        assertThat(error, instanceOf(IsOriginalKeyword::class.java))
    }

    @Test
    fun `addKeyword cannot add word contained in original keyword (ignore case)`() {
        val dataView = KeywordFilterDataView(
            filter = "keywordfilter/keyword-filter-multiword-main-keyword.json".jsonToObject(),
        )

        dataView.addKeyword("TV")

        assertThat(error, instanceOf(IsOriginalKeyword::class.java))
        dataView.itemList.size shouldBe 0
    }

    @Test
    fun `addKeyword cannot add same negative keyword`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("tv")
        dataView.addKeyword("tv")

        dataView.itemList.size shouldBe 1
        assertThat(error, instanceOf(ExistsAsNegative::class.java))
    }

    @Test
    fun `addKeyword with non-whitelisted special character`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        val forbiddenInputList = listOf(
            "_te%st",
            "\$te+st",
        )

        forbiddenInputList.forEach { input ->
            dataView.addKeyword(input)

            val reason = "Input keyword is: $input"
            assertThat(
                "$reason assert error type fail.",
                error, instanceOf(ForbiddenCharacter::class.java)
            )
            assertThat(
                "$reason assert size fail.",
                dataView.itemList.size, `is`(0)
            )
        }
    }

    @Test
    fun `removeKeyword should remove from item list`() {
        val dataView = createKeywordFilterDataViewEmptyOption()
        val negativeKeyword = "tv"
        dataView.addKeyword(negativeKeyword)

        dataView.removeKeyword(negativeKeyword)

        dataView.itemList.size shouldBe 0
    }

    @Test
    fun `generateKeyword from keyword filter`() {
        val dataView = KeywordFilterDataView(
            filter = "keywordfilter/keyword-filter.json".jsonToObject(),
        )

        dataView.generateKeyword() shouldBe "samsung -\"tv\""

        dataView.addKeyword("-remote")
        dataView.generateKeyword() shouldBe "samsung -\"tv\" -\"remote\""
    }

    @Test
    fun `generate keyword after adding and removing`() {
        val dataView = createKeywordFilterDataViewEmptyOption()

        dataView.addKeyword("tv")
        dataView.removeKeyword("tv")

        dataView.generateKeyword() shouldBe "samsung"
    }

    @Test
    fun `generate keyword with negative already containing quotes`() {
        val dataView = KeywordFilterDataView(
            filter = "keywordfilter/keyword-filter-quotes-negative-keyword.json".jsonToObject(),
        )

        dataView.addKeyword("galaxy")

        dataView.generateKeyword() shouldBe "samsung -\"tv\" -\"remote\" -\"galaxy\""
    }
}