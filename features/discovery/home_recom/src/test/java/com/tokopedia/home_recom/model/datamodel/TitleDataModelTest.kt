package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Lukas on 2019-07-15
 */
@RunWith(JUnit4::class)
class TitleDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl(mockk(), mockk(), mockk(), mockk())
    private fun titleDataModelFactory(title: String) = TitleDataModel(title, "", "")

    @Test
    fun test(){
        //given
        val layout = titleDataModelFactory("").type(visitor)
        //then
        assertEquals(layout, TitleDataModel.LAYOUT)
    }
}