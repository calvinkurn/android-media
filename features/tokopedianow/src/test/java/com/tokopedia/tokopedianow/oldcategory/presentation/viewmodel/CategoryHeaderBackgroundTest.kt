package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import org.junit.Assert
import org.junit.Test

class CategoryHeaderBackgroundTest: CategoryTestFixtures() {

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
        val translationY = tokoNowCategoryViewModel.getTranslationYHeaderBackground(dy)

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
        tokoNowCategoryViewModel.getTranslationYHeaderBackground(dy)
        tokoNowCategoryViewModel.getTranslationYHeaderBackground(dy)
        tokoNowCategoryViewModel.getTranslationYHeaderBackground(dy)
        val translationY = tokoNowCategoryViewModel.getTranslationYHeaderBackground(dy)

        /**
         * verify the data test
         */
        Assert.assertEquals(expectedResult, translationY)
    }
}
