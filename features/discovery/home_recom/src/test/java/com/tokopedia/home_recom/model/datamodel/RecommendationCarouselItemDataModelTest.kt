package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Lukas on 2019-07-15
 */
class RecommendationCarouselItemDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl(mockk(), mockk(), mockk(), mockk())
    private fun recommendationCarouselItemDataModelFactory(recommendationItem: RecommendationItem, parentPosition: Int, listener: RecommendationListener) = RecommendationCarouselItemDataModel(recommendationItem, parentPosition)

    @Test
    fun test(){
        //given
        val layout = recommendationCarouselItemDataModelFactory(mockk(), 3, mockk()).type(visitor)
        //then
        assertEquals(layout, RecommendationCarouselItemDataModel.LAYOUT)
    }
}
