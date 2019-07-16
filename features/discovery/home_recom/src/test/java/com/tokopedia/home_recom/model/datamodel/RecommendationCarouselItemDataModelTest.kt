package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Lukas on 2019-07-15
 */
class RecommendationCarouselItemDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl()
    private fun recommendationCarouselItemDataModelFactory(recommendationItem: RecommendationItem, listener: RecommendationCardView.TrackingListener) = RecommendationCarouselItemDataModel(recommendationItem, listener)

    @Test
    fun test(){
        //given
        val layout = recommendationCarouselItemDataModelFactory(mockk(), mockk()).type(visitor)
        //then
        assertEquals(layout, RecommendationCarouselItemDataModel.LAYOUT)
    }
}
