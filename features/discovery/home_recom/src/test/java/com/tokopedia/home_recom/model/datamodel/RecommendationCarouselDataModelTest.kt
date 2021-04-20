package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Lukas on 2019-07-15
 */
class RecommendationCarouselDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl(mockk(), mockk(), mockk(), mockk())
    private fun recommendationCarouselDataModelFactory(title: String, recommendationItems: List<RecommendationCarouselItemDataModel>, listener: RecommendationListener) = RecommendationCarouselDataModel(title, "", recommendationItems)

    @Test
    fun test(){
        //given
        val layout = recommendationCarouselDataModelFactory("", mockk(), mockk()).type(visitor)
        //then
        assertEquals(layout, RecommendationCarouselDataModel.LAYOUT)
    }
}