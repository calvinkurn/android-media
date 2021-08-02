package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel.Companion.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.slot
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class BindRecommendationCarouselTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        private val callback: Callback,
) {

    private val recommendationEntity = "recom/recom-carousel.json".jsonToObject<RecommendationEntity>()
    private val recommendationWidgetList = recommendationEntity.mapToRecommendationWidgetList()
    private val getRecommendationParamsSlot = slot<GetRecommendationRequestParam>()

    private fun RecommendationEntity.mapToRecommendationWidgetList() =
            productRecommendationWidget
                    .data
                    .mappingToRecommendationModel()

    fun `bind recommendation success`() {
        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will be successful`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert mandatory get recommendation request`(
                getRecommendationParamsSlot.captured,
                recomWidget,
        )
        callback.`Then assert get recommendation request`(getRecommendationParamsSlot.captured)
        `Then assert recommendation data view is updated to ready with recom widget`(recomWidget)
        `Then assert updated visitable list`(recomWidgetPosition)
    }

    private fun `Given get recommendation will be successful`() {
        coEvery {
            getRecommendationUseCase.getData(capture(getRecommendationParamsSlot))
        } returns recommendationWidgetList
    }

    private fun List<Visitable<*>>.findRecomWidget(): RecommendationCarouselDataView =
            find { it is RecommendationCarouselDataView } as? RecommendationCarouselDataView
                    ?: throw Throwable("Cannot find recom widget")

    private fun `When bind recommendation carousel`(
            recomWidget: RecommendationCarouselDataView,
            recomWidgetPosition: Int,
    ) {
        baseViewModel.onBindRecommendationCarousel(recomWidget, recomWidgetPosition)
    }

    private fun `Then assert mandatory get recommendation request`(
            getRecommendationRequestParam: GetRecommendationRequestParam,
            recommendationCarouselDataView: RecommendationCarouselDataView,
    ) {
        assertTokonowRecommendationCarouselRequestParams(
                getRecommendationRequestParam,
                recommendationCarouselDataView,
        )
    }

    private fun `Then assert recommendation data view is updated to ready with recom widget`(
            recomWidget: RecommendationCarouselDataView
    ) {
        val recommendationWidget = recommendationWidgetList.first()

        assertThat(recomWidget.carouselData.state, shouldBe(STATE_READY))
        assertThat(recomWidget.carouselData.recommendationData, shouldBe(recommendationWidget))
    }

    private fun `Then assert updated visitable list`(recomWidgetPosition: Int) {
        val updatedIndex = baseViewModel.updatedVisitableIndicesLiveData.value!!

        assertThat(updatedIndex, shouldBe(listOf(recomWidgetPosition)))
    }

    fun `bind recommendation fail`() {
        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will fail`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert remmendation widget data view state is updated to fail`(recomWidget)
        `Then assert updated visitable list`(recomWidgetPosition)
    }

    private fun `Given get recommendation will fail`() {
        coEvery {
            getRecommendationUseCase.getData(capture(getRecommendationParamsSlot))
        } throws Throwable("Get recommendation failed")
    }

    private fun `Then assert remmendation widget data view state is updated to fail`(
            recomWidget: RecommendationCarouselDataView
    ) {
        assertThat(recomWidget.carouselData.state, shouldBe(STATE_FAILED))
    }

    fun `should not load recommendation again after success`() {
        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will be successful`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `Given recommendation carousel already success`(recomWidget, recomWidgetPosition)

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then verify get recommendation only called once`()
    }

    private fun `Given recommendation carousel already success`(
            recomWidget: RecommendationCarouselDataView,
            recomWidgetPosition: Int,
    ) {
        baseViewModel.onBindRecommendationCarousel(recomWidget, recomWidgetPosition)
    }

    private fun `Then verify get recommendation only called once`() {
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
    }

    fun `bind recommendation with quantity from mini cart`() {
        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData

        callback.`Given view will show recommendation carousel`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)
        `Given get recommendation will be successful`()
        `Given view is resumed`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert quantity is updated from mini cart`(miniCartSimplifiedData)
    }

    private fun `Given get mini cart simplified use case will be successful`(
            miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        every {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }
    }

    private fun `Given view is resumed`() {
        baseViewModel.onViewResumed()
    }

    private fun `Then assert quantity is updated from mini cart`(
            miniCartSimplifiedData: MiniCartSimplifiedData,
    ) {
        val recommendationItems = recommendationWidgetList.first().recommendationItemList
        val miniCartItems = miniCartSimplifiedData.miniCartItems

        `Then assert product item non variant quantity`(miniCartItems, recommendationItems)
        `Then assert product item variant quantity`(miniCartItems, recommendationItems)
    }

    private fun `Then assert product item non variant quantity`(
            miniCartItems: List<MiniCartItem>,
            productItems: List<RecommendationItem>,
    ) {
        val miniCartItemsNonVariant = miniCartItems.filter {
            it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID
        }

        miniCartItemsNonVariant.forEach { miniCartItem ->
            val productItemIndexed = productItems.withIndex().find {
                it.value.productId.toString() == miniCartItem.productId
            }!!
            val productItem = productItemIndexed.value
            val reason = createInvalidNonVariantQtyReason(miniCartItem)
            assertThat(reason, productItem.quantity, shouldBe(miniCartItem.quantity))
        }
    }

    private fun createInvalidNonVariantQtyReason(miniCartItem: MiniCartItem) =
            "Product \"${miniCartItem.productId}\" non variant quantity is invalid."

    private fun `Then assert product item variant quantity`(
            miniCartItems: List<MiniCartItem>,
            productItems: List<RecommendationItem>,
    ) {
        val miniCartItemsVariant = miniCartItems.filter {
            it.productParentId != NO_VARIANT_PARENT_PRODUCT_ID
        }
        val miniCartItemsVariantGroup = miniCartItemsVariant.groupBy { it.productParentId }

        miniCartItemsVariantGroup.forEach { miniCartItemGroup ->
            val totalQuantity = miniCartItemGroup.value.sumBy { it.quantity }
            val productItemIndexed = productItems.withIndex()
                    .find { it.value.parentID.toString() == miniCartItemGroup.key }!!
            val productItem = productItemIndexed.value
            val parentProductId = productItem.parentID.toString()
            val productId = productItem.productId.toString()

            val reason = createInvalidVariantQtyReason(productId, parentProductId)
            assertThat(reason, productItem.quantity, shouldBe(totalQuantity))
        }
    }

    private fun createInvalidVariantQtyReason(productId: String, parentProductId: String) =
            "Product \"$productId\" with parent \"$parentProductId\" variant quantity is invalid"

    fun `update recommendation quantity on update mini cart`() {
        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData

        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will be successful`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `Given recommendation carousel already success`(recomWidget, recomWidgetPosition)

        `When view update cart items`(miniCartSimplifiedData)

        `Then assert quantity is updated from mini cart`(miniCartSimplifiedData)
        `Then assert updated visitable indices has recom position`(recomWidgetPosition)
    }

    private fun `When view update cart items`(miniCartSimplifiedData: MiniCartSimplifiedData) {
        baseViewModel.onViewUpdateCartItems(miniCartSimplifiedData)
    }

    private fun `Then assert updated visitable indices has recom position`(
            recomWidgetPosition: Int
    ) {
        val updatedVisitableIndices = baseViewModel.updatedVisitableIndicesLiveData.value!!
        val hasRecomWidgetInUpdatedVisitableIndices =
                updatedVisitableIndices.contains(recomWidgetPosition)

        assertThat(hasRecomWidgetInUpdatedVisitableIndices, shouldBe(true))
    }

    interface Callback {
        fun `Given view will show recommendation carousel`()
        fun `Then assert get recommendation request`(
                getRecommendationRequestParam: GetRecommendationRequestParam
        )
    }
}