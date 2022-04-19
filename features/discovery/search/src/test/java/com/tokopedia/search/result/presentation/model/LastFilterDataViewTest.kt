package com.tokopedia.search.result.presentation.model

import com.tokopedia.filter.common.data.SavedOption
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class LastFilterDataViewTest {

    @Test
    fun `sortFilterParamString() will return string with format key=value from SavedOptions`() {
        val savedOption1 = SavedOption(key = "fcity", value = "1234")
        val lastFilterDataView = LastFilterDataView(listOf(savedOption1))

        val sortFilterParamString = lastFilterDataView.sortFilterParamsString()

        assertThat(
            sortFilterParamString,
            `is`("${savedOption1.key}=${savedOption1.value}")
        )
    }

    @Test
    fun `sortFilterParamString() will separate SavedOption key value with &`() {
        val savedOption1 = SavedOption(key = "fcity", value = "1234")
        val savedOption2 = SavedOption(key = "official", value = true.toString())
        val lastFilterDataView = LastFilterDataView(listOf(savedOption1, savedOption2))

        val sortFilterParamString = lastFilterDataView.sortFilterParamsString()

        assertThat(
            sortFilterParamString,
            `is`("${savedOption1.key}=${savedOption1.value}" +
                "&" +
                "${savedOption2.key}=${savedOption2.value}")
        )
    }

    @Test
    fun `sortFilterParamString() for SavedOptions of same key will combine value with #`() {
        val savedOption1 = SavedOption(key = "fcity", value = "1234")
        val savedOption2 = SavedOption(key = "fcity", value = "5678")
        val lastFilterDataView = LastFilterDataView(listOf(savedOption1, savedOption2))

        val sortFilterParamString = lastFilterDataView.sortFilterParamsString()

        assertThat(
            sortFilterParamString,
            `is`("${savedOption1.key}=${savedOption1.value}%23${savedOption2.value}")
        )
    }
}