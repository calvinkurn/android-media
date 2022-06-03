package com.tokopedia.search.utils

import com.tokopedia.filter.common.data.Option
import org.junit.Test

internal class TrackingUtilsTest {
    @Test
    fun `Generate Event Label with Empty Active Options`() {
        val optionList = listOf<Option>()

        assert(optionList.toDropdownQuickFilterEventLabel().isEmpty())
    }

    @Test
    fun `Generate Event Label with One Active Option`() {
        val optionList = listOf(
            Option(key = "cashbackm", value = "true", inputState = "true")
        )

        val expectedLabel = "cashbackm=true"

        assert(optionList.toDropdownQuickFilterEventLabel() == expectedLabel)
    }

    @Test
    fun `Generate Event Label with Multiple Active Option`() {
        val optionList = listOf(
            Option(key = "is_discount", value = "true", inputState = "true"),
            Option(key = "cashbackm", value = "true", inputState = "true"),
            Option(key = "cashbackm", value = "true", inputState = "false")
        )

        val expectedLabel = "cashbackm=true&is_discount=true"

        assert(optionList.toDropdownQuickFilterEventLabel() == expectedLabel)
    }

    @Test
    fun `Generate Event Label with Multiple Active Option and Association`() {
        val optionList = listOf(
            Option(key = "is_discount", value = "true", inputState = "false"),
            Option(key = "cashbackm", value = "true", inputState = "true"),
            Option(key = "fcity", value = "1", inputState = "true"),
            Option(key = "fcity", value = "2", inputState = "false"),
            Option(key = "fcity", value = "3", inputState = "true"),
        )

        val expectedLabel = "cashbackm=true&fcity=1,3"

        assert(optionList.toDropdownQuickFilterEventLabel() == expectedLabel)
    }
}