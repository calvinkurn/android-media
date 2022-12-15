package com.tokopedia.tokopedianow.search.presentation.viewmodel

import org.junit.Assert
import org.junit.Test

class SearchHeaderBackgroundTest: SearchTestFixtures() {

    @Test
    fun `when getTranslationYHeaderBackground should get the opposite of dy`() {
        /**
         * create data test
         */
        val dy = 10
        val expectedResult = -dy.toFloat()

        /**
         * get translation y of header
         */
        val translationY = tokoNowSearchViewModel.getTranslationYHeaderBackground(dy)

        /**
         * verify the data test
         */
        Assert.assertEquals(expectedResult, translationY)
    }

    @Test
    fun `when getTranslationYHeaderBackground should get zero as result`() {
        /**
         * create data test
         */
        val dy = -10
        val expectedResult = 0f

        /**
         * get translation y of header
         */
        tokoNowSearchViewModel.getTranslationYHeaderBackground(dy)
        tokoNowSearchViewModel.getTranslationYHeaderBackground(dy)
        tokoNowSearchViewModel.getTranslationYHeaderBackground(dy)
        val translationY = tokoNowSearchViewModel.getTranslationYHeaderBackground(dy)

        /**
         * verify the data test
         */
        Assert.assertEquals(expectedResult, translationY)
    }
}
