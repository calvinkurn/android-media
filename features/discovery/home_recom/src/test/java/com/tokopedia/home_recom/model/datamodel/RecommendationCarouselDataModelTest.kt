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
class RecommendationCarouselDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl()
    private fun recommendationCarouselDataModelFactory(title: String, recommendationItems: List<RecommendationItem>, listener: RecommendationCardView.TrackingListener) = RecommendationCarouselDataModel(title, recommendationItems, listener)

    @Test
    fun test(){
        //given
        val layout = recommendationCarouselDataModelFactory("", mockk(), mockk()).type(visitor)
        //then
        assertEquals(layout, RecommendationCarouselDataModel.LAYOUT)
    }
}