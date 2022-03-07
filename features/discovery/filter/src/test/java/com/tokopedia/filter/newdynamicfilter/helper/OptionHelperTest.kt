package com.tokopedia.filter.newdynamicfilter.helper

import com.tokopedia.filter.common.data.Option
import org.hamcrest.CoreMatchers.hasItems
import org.junit.Assert.assertThat
import org.junit.Test

class OptionHelperTest {

    @Test
    fun `option list to map of key value`() {
        val option1 = Option(key = "fcity", value = "1234", name = "Jakarta")
        val option2 = Option(key = "official", value = true.toString(), name = "Official Store")

        val optionMap = OptionHelper.toMap(listOf(option1, option2))

        assertThat(
            optionMap.toList(),
            hasItems(Pair(option1.key, option1.value), Pair(option2.key, option2.value))
        )
    }

    @Test
    fun `option list to map will combine values with same key using #`() {
        val option1 = Option(key = "fcity", value = "1234", name = "Jakarta")
        val option2 = Option(key = "fcity", value = "2367", name = "Bali")

        val optionMap = OptionHelper.toMap(listOf(option1, option2))

        assertThat(
            optionMap.toList(),
            hasItems(Pair(option1.key,"${option1.value}#${option2.value}"))
        )
    }

    @Test
    fun `option list to map with same value will be combined`() {
        val option1 = Option(key = "fcity", value = "1234", name = "Jakarta")
        val option2 = Option(key = "fcity", value = "1234", name = "DKI Jakarta")

        val optionMap = OptionHelper.toMap(listOf(option1, option2))

        assertThat(
            optionMap.toList(),
            hasItems(Pair(option1.key, option1.value))
        )
    }
}