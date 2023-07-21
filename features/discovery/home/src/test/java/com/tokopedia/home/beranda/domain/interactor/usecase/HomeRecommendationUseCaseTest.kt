package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection.NEXT
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection.NO_DIRECTION
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection.PREVIOUS
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.jsonToObject
import com.tokopedia.home_component.visitable.BestSellerDataModel
import com.tokopedia.productcard.ProductCardModel.ProductListType.BEST_SELLER
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import com.tokopedia.home.beranda.data.mapper.BestSellerMapper as BestSellerRevampMapper


@OptIn(ExperimentalCoroutinesApi::class)
class HomeRecommendationUseCaseTest {

    private val bestSellerRevampMapper = BestSellerRevampMapper()
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val homeRecommendationUseCase = HomeRecommendationUseCase(
        getRecommendationUseCase,
        { mockk(relaxed = true) },
        { bestSellerRevampMapper },
    )

    private val bestSellerDataModel = bestSellerRevampMapper.mapChipProductDataModelList(
        recommendationData,
        recommendationFilterList,
        BestSellerDataModel(channelModel = channelModel),
        recommendationFilterList.first(),
    )

    @Test
    fun `click best seller chips will call API and update chips products`() = runTest {
        val selectedChipPosition = 2
        val selectedChip = bestSellerDataModel.chipProductList[selectedChipPosition].chip

        coEvery { getRecommendationUseCase.getData(any()) } returns recommendationData

        val updatedBestSellerDataModel = homeRecommendationUseCase.onHomeBestSellerFilterClick(
            bestSellerDataModel,
            selectedChip,
            NO_DIRECTION,
        )

        val updatedChipProductList = updatedBestSellerDataModel.chipProductList[selectedChipPosition]
        assertTrue(updatedChipProductList.isActivated)
        assertTrue(updatedChipProductList.productModelList.isNotEmpty())

        val recommendationWidget = recommendationData.first()
        assertEquals(recommendationWidget.seeMoreAppLink, updatedChipProductList.seeMoreApplink)

        updatedChipProductList.productModelList.forEachIndexed { index, it ->
            val recommendationItem = recommendationWidget.recommendationItemList[index]
            val expectedProductCardModel = recommendationItem.toProductCardModel(
                productCardListType = BEST_SELLER
            )

            assertEquals(expectedProductCardModel, it.productCardModel)
            assertEquals(recommendationItem.appUrl, it.applink)
            assertEquals(recommendationItem.productId.toString(), it.productId)
            assertEquals(recommendationItem.name, it.name)
            assertEquals(recommendationItem.isTopAds, it.isTopAds)
            assertEquals(recommendationItem.recommendationType, it.recommendationType)
            assertEquals(recommendationItem.priceInt.toLong(), it.price)
            assertEquals(recommendationItem.position, it.position)
            assertEquals(recommendationItem.isFreeOngkirActive, it.isFreeOngkirActive)
            assertEquals(recommendationItem.cartId, it.cartId)
            assertEquals(recommendationItem.categoryBreadcrumbs, it.categoryBreadcrumbs)
            assertEquals(recommendationItem.pageName, it.pageName)
            assertEquals(recommendationItem.header, it.header)
        }

        assertTrue(
            updatedBestSellerDataModel
                .chipProductList
                .filter { it != updatedChipProductList }
                .all { !it.isActivated }
        )
    }

    @Test
    fun `click best seller chips with empty data from BE will just activate the next chip`() = runTest {
        val selectedChipPosition = 2
        val selectedChip = bestSellerDataModel.chipProductList[selectedChipPosition].chip

        coEvery { getRecommendationUseCase.getData(any()) } returns emptyRecommendationData

        val updatedBestSellerDataModel = homeRecommendationUseCase.onHomeBestSellerFilterClick(
            bestSellerDataModel,
            selectedChip,
            NO_DIRECTION,
        )

        val updatedChipProductList = updatedBestSellerDataModel.chipProductList[selectedChipPosition]
        assertFalse(updatedChipProductList.isActivated)
        assertTrue(updatedChipProductList.productModelList.isEmpty())

        val nextSelectedChipPosition = selectedChipPosition + 1
        assertTrue(updatedBestSellerDataModel.chipProductList[nextSelectedChipPosition].isActivated)
    }

    @Test
    fun `click last best seller chips with empty data from BE will do nothing`() = runTest {
        val lastChipIndex = bestSellerDataModel.chipProductList.lastIndex
        val selectedChip = bestSellerDataModel.chipProductList.last().chip

        coEvery { getRecommendationUseCase.getData(any()) } returns emptyRecommendationData

        val updatedBestSellerDataModel = homeRecommendationUseCase.onHomeBestSellerFilterClick(
            bestSellerDataModel,
            selectedChip,
            NO_DIRECTION,
        )

        val updatedChipProductList = updatedBestSellerDataModel.chipProductList[lastChipIndex]
        assertTrue(updatedChipProductList.isActivated)
        assertTrue(updatedChipProductList.productModelList.isEmpty())
    }

    @Test
    fun `click best seller chips with error from BE will just activate the next chip`() = runTest {
        val selectedChipPosition = 2
        val selectedChip = bestSellerDataModel.chipProductList[selectedChipPosition].chip

        coEvery { getRecommendationUseCase.getData(any()) } throws Exception()

        val updatedBestSellerDataModel = homeRecommendationUseCase.onHomeBestSellerFilterClick(
            bestSellerDataModel,
            selectedChip,
            NO_DIRECTION,
        )

        val updatedChipProductList = updatedBestSellerDataModel.chipProductList[selectedChipPosition]
        assertFalse(updatedChipProductList.isActivated)
        assertTrue(updatedChipProductList.productModelList.isEmpty())

        val nextSelectedChipPosition = selectedChipPosition + 1
        assertTrue(updatedBestSellerDataModel.chipProductList[nextSelectedChipPosition].isActivated)
    }

    @Test
    fun `swipe best seller chips to previous chips will show the last page of the group`() = runTest {
        val selectedChipPosition = 2
        val selectedChip = bestSellerDataModel.chipProductList[selectedChipPosition].chip

        coEvery { getRecommendationUseCase.getData(any()) } returns recommendationData

        val updatedBestSellerDataModel = homeRecommendationUseCase.onHomeBestSellerFilterClick(
            bestSellerDataModel,
            selectedChip,
            PREVIOUS,
        )

        assertEquals(
            CarouselPagingModel.LAST_PAGE_IN_GROUP,
            updatedBestSellerDataModel.currentPageInGroup
        )
    }

    @Test
    fun `swipe best seller chips to next chips will show the first page of the group`() = runTest {
        val selectedChipPosition = 2
        val selectedChip = bestSellerDataModel.chipProductList[selectedChipPosition].chip

        coEvery { getRecommendationUseCase.getData(any()) } returns emptyRecommendationData

        val updatedBestSellerDataModel = homeRecommendationUseCase.onHomeBestSellerFilterClick(
            bestSellerDataModel,
            selectedChip,
            NEXT,
        )

        assertEquals(
            CarouselPagingModel.FIRST_PAGE_IN_GROUP,
            updatedBestSellerDataModel.currentPageInGroup
        )
    }

    companion object {
        val channelModel =
            DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel = "bestseller/dynamic_channel_best_seller.json".jsonToObject(),
                verticalPosition = 0,
            )

        val recommendationFilterList =
            "bestseller/recommendation_filter_chips.json"
                .jsonToObject<RecommendationFilterChipsEntity>()
                .recommendationFilterChips
                .data
                .filterChip

        val recommendationData =
            "bestseller/product_recommendation_widget.json"
                .jsonToObject<RecommendationEntity>()
                .productRecommendationWidget
                .data
                .mappingToRecommendationModel()

        val emptyRecommendationData = listOf<RecommendationWidget>()
    }

}
