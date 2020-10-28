package com.tokopedia.sellerhomecommon.common

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created By @ilhamsuaib on 02/09/20
 */

@RunWith(JUnit4::class)
class SellerHomeCommonUtilsTest {

    @Test
    fun `extract urls from given string text correctly`() {
        val givenText = "Bantu kami menjadi lebih baik dengan membagikan pengalamanmu <a href=\"https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU\">di sini\\</a>"

        val expectedUrls = listOf("https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU")

        val actualUrls = SellerHomeCommonUtils.extractUrls(givenText)

        Assert.assertEquals(expectedUrls, actualUrls)
    }

    @Test
    fun `return string empty list from given text without url`() {
        val givenText = "Bantu kami menjadi lebih baik dengan membagikan pengalamanmu"

        val expectedUrls = emptyList<String>()

        val actualUrls = SellerHomeCommonUtils.extractUrls(givenText)

        Assert.assertEquals(expectedUrls, actualUrls)
    }
}