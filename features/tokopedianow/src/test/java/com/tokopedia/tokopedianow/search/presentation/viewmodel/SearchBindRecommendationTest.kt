package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.BindRecommendationCarouselTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.core.Is.`is` as shouldBe

class SearchBindRecommendationTest: SearchTestFixtures(), BindRecommendationCarouselTestHelper.Callback {

    private val searchModel = "search/emptyproduct/empty-product.json".jsonToObject<SearchModel>()

    private lateinit var bindRecommendationTestHelper: BindRecommendationCarouselTestHelper

    override fun setUp() {
        super.setUp()

        bindRecommendationTestHelper = BindRecommendationCarouselTestHelper(
                tokoNowSearchViewModel,
                getRecommendationUseCase,
                getMiniCartListSimplifiedUseCase,
                this,
        )
    }

    override fun `Given view will show recommendation carousel`() {
        `Given get search first page use case will be successful`(searchModel)
        `Given view already created`()
    }

    override fun `Then assert get recommendation request`(
            getRecommendationRequestParam: GetRecommendationRequestParam,
    ) {
        assertThat(getRecommendationRequestParam.keywords, shouldBe(listOf(defaultKeyword)))
    }

    @Test
    fun `bind recommendation success`() {
        bindRecommendationTestHelper.`bind recommendation success`()
    }

    @Test
    fun `bind recommendation fail`() {
        bindRecommendationTestHelper.`bind recommendation fail`()
    }

    @Test
    fun `should not load recommendation again after success`() {
        bindRecommendationTestHelper.`should not load recommendation again after success`()
    }

    @Test
    fun `bind recommendation success with empty product list should remove recom widget`() {
        bindRecommendationTestHelper.
            `bind recommendation success with empty product list should remove recom widget`()
    }

    @Test
    fun `bind recommendation with quantity from mini cart`() {
        bindRecommendationTestHelper.`bind recommendation with quantity from mini cart`()
    }

    @Test
    fun `update recommendation quantity on update mini cart`() {
        bindRecommendationTestHelper.`update recommendation quantity on update mini cart`()
    }
}