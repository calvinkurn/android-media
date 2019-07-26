package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.listener.TrackingListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Lukas on 2019-07-15
 */
class RecommendationCarouselItemDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl()
    private fun recommendationCarouselItemDataModelFactory(recommendationItem: RecommendationItem, parentPosition: Int, listener: TrackingListener) = RecommendationCarouselItemDataModel(recommendationItem, parentPosition, listener)

    @Test
    fun test(){
        //given
        val layout = recommendationCarouselItemDataModelFactory(mockk(), 3, mockk()).type(visitor)
        //then
        assertEquals(layout, RecommendationCarouselItemDataModel.LAYOUT)
    }
}
