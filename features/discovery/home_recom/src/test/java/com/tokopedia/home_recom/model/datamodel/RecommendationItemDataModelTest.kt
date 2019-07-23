package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.listener.TrackingListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Lukas on 2019-07-15
 */
class RecommendationItemDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl()
    private fun recommendationItemDataModelFactory(recommendationItem: RecommendationItem, listener: TrackingListener) = RecommendationItemDataModel(recommendationItem, listener)

    @Test
    fun test(){
        //given
        val layout = recommendationItemDataModelFactory(mockk(), mockk()).type(visitor)
        //then
        assertEquals(layout, RecommendationItemDataModel.LAYOUT)
    }
}