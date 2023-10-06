package com.tokopedia.product.usecase

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.usecase.GetProductRecommendationUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

@ExperimentalCoroutinesApi
class GetProductRecommendationUseCaseTest {

    @RelaxedMockK
    lateinit var getRecommendationFilterChips: GetRecommendationFilterChips

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val thrown = ExpectedException.none()

    private val useCaseTest by lazy {
        GetProductRecommendationUseCase(
                dispatcher = CoroutineTestDispatchersProvider,
                getRecommendationFilterChips = getRecommendationFilterChips,
                getRecommendationUseCase = getRecommendationUseCase,
                userSessionInterface = userSession
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `on success get recomm without filter`() {
        runTest {
            val mockResponse = RecommendationWidget(
                    tid = "1",
                    recommendationItemList = listOf(RecommendationItem(), RecommendationItem())
            )
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns listOf(mockResponse)

            val mockRequestParams = GetProductRecommendationUseCase.createParams(
                    productId = "123",
                    pageName = "pdp_4",
                    isTokoNow = false,
                    miniCartData = null
            )
            val result = useCaseTest.executeOnBackground(mockRequestParams)

            val slotRecomRequestParams = slot<GetRecommendationRequestParam>()
            coVerify {
                getRecommendationUseCase.getData(capture(slotRecomRequestParams))
            }

            coVerify(inverse = true) {
                getRecommendationFilterChips.executeOnBackground()
                getRecommendationFilterChips.setParams(any())
            }

            //assert request params
            val reqParams = slotRecomRequestParams.captured
            Assert.assertEquals(reqParams.pageNumber, 1)
            Assert.assertEquals(reqParams.pageName, "pdp_4")
            Assert.assertEquals(reqParams.productIds, listOf("123"))
            Assert.assertEquals(reqParams.isTokonow, false)

            Assert.assertEquals(result.recommendationFilterChips.isEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.isNotEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.size, 2)
            Assert.assertEquals(result.pageName, "pdp_4")
        }
    }

    @Test
    fun `on success get recomm with filter`() {
        runTest {
            val mockResponse = RecommendationWidget(
                    tid = "1",
                    recommendationItemList = listOf(
                            RecommendationItem(),
                            RecommendationItem())
            )

            coEvery {
                getRecommendationUseCase.getData(any())
            } returns listOf(mockResponse)

            val mockFilter = RecommendationFilterChipsEntity.FilterAndSort(
                    filterChip = listOf(
                            RecommendationFilterChipsEntity.RecommendationFilterChip(
                                    name = "katun chip",
                                    isActivated = false),
                            RecommendationFilterChipsEntity.RecommendationFilterChip(
                                    name = "kulit chip",
                                    isActivated = false)
                    )
            )

            coEvery {
                getRecommendationFilterChips.executeOnBackground()
            } returns mockFilter

            val mockRequestParams = GetProductRecommendationUseCase.createParams(
                    productId = "123",
                    pageName = "pdp_3",
                    isTokoNow = false,
                    miniCartData = null
            )
            val result = useCaseTest.executeOnBackground(mockRequestParams)

            val slotRecomRequestParams = slot<GetRecommendationRequestParam>()
            coVerify {
                getRecommendationUseCase.getData(capture(slotRecomRequestParams))
                getRecommendationFilterChips.executeOnBackground()
            }

            //assert request params
            val reqParams = slotRecomRequestParams.captured
            Assert.assertEquals(reqParams.pageNumber, 1)
            Assert.assertEquals(reqParams.pageName, "pdp_3")
            Assert.assertEquals(reqParams.productIds, listOf("123"))
            Assert.assertEquals(reqParams.isTokonow, false)

            Assert.assertEquals(result.recommendationFilterChips.isNotEmpty(), true)
            Assert.assertEquals(result.recommendationFilterChips.size, 2)

            Assert.assertEquals(result.recommendationItemList.isNotEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.size, 2)
            Assert.assertEquals(result.pageName, "pdp_3")
        }
    }

    @Test
    fun `on success get recomm without filter tokonow`() {
        runTest {
            val mockResponse = RecommendationWidget(
                    tid = "1",
                    layoutType = "horizontal-atc",
                    recommendationItemList = listOf(RecommendationItem(
                            productId = 1,
                            parentID= 11,
                            quantity = 2,
                            addToCartType = RecommendationItem.AddToCartType.QuantityEditor,
                    ), RecommendationItem(
                            productId = 2,
                            parentID = 22,
                            quantity = 3,
                            addToCartType = RecommendationItem.AddToCartType.QuantityEditor,
                    ))
            )

            coEvery {
                getRecommendationUseCase.getData(any())
            } returns listOf(mockResponse)

            val mockMiniCart = mutableMapOf(
                    "1" to MiniCartItem.MiniCartItemProduct(
                            productId = "1",
                            productParentId = "11",
                            quantity = 10)
            )

            val mockRequestParams = GetProductRecommendationUseCase.createParams(
                    productId = "1",
                    pageName = "pdp_4",
                    isTokoNow = true,
                    miniCartData = mockMiniCart
            )
            val result = useCaseTest.executeOnBackground(mockRequestParams)

            val slotRecomRequestParams = slot<GetRecommendationRequestParam>()
            coVerify {
                getRecommendationUseCase.getData(capture(slotRecomRequestParams))
            }

            coVerify(inverse = true) {
                getRecommendationFilterChips.executeOnBackground()
                getRecommendationFilterChips.setParams(any())
            }

            //assert request params
            val reqParams = slotRecomRequestParams.captured
            Assert.assertEquals(reqParams.pageNumber, 1)
            Assert.assertEquals(reqParams.pageName, "pdp_4")
            Assert.assertEquals(reqParams.productIds, listOf("1"))
            Assert.assertEquals(reqParams.isTokonow, true)

            Assert.assertEquals(result.recommendationFilterChips.isEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.isNotEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.size, 2)
            Assert.assertEquals(result.pageName, "pdp_4")

            val existMiniCartProduct = result.recommendationItemList.firstOrNull {
                it.productId == 1L
            }
            //make sure updated with minicart
            Assert.assertNotNull(existMiniCartProduct)
            Assert.assertEquals(existMiniCartProduct!!.quantity, 10)
            Assert.assertEquals(existMiniCartProduct.currentQuantity, 10)

            val inexsitMiniCartProduct = result.recommendationItemList.firstOrNull {
                it.productId == 2L
            }
            //make sure not update
            Assert.assertNotNull(inexsitMiniCartProduct)
            Assert.assertEquals(inexsitMiniCartProduct!!.quantity, 0)
            Assert.assertEquals(inexsitMiniCartProduct.currentQuantity, 0)
        }
    }

    @Test
    fun `on success get recomm with empty list`() {
        runTest {
            val mockResponse = RecommendationWidget(
                    tid = "1",
                    recommendationItemList = emptyList()
            )
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns listOf(mockResponse)

            coEvery {
                getRecommendationFilterChips.executeOnBackground()
            } throws Throwable()

            val mockRequestParams = GetProductRecommendationUseCase.createParams(
                    productId = "123",
                    pageName = "pdp_3",
                    isTokoNow = false,
                    miniCartData = null
            )

            thrown.expect(MessageErrorException::class.java)
            useCaseTest.executeOnBackground(mockRequestParams)

            coVerify {
                getRecommendationUseCase.getData(any())
                getRecommendationFilterChips.executeOnBackground()
            }
        }
    }

    @Test
    fun `on success get recomm throwable filter`() {
        runTest {
            val mockResponse = RecommendationWidget(
                    tid = "1",
                    recommendationItemList = listOf(RecommendationItem(), RecommendationItem())
            )
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns listOf(mockResponse)

            coEvery {
                getRecommendationFilterChips.executeOnBackground()
            } throws Throwable()

            val mockRequestParams = GetProductRecommendationUseCase.createParams(
                    productId = "123",
                    pageName = "pdp_3",
                    isTokoNow = false,
                    miniCartData = null
            )
            val result = useCaseTest.executeOnBackground(mockRequestParams)

            val slotRecomRequestParams = slot<GetRecommendationRequestParam>()
            coVerify {
                getRecommendationUseCase.getData(capture(slotRecomRequestParams))
                getRecommendationFilterChips.executeOnBackground()
            }

            //assert request params
            val reqParams = slotRecomRequestParams.captured
            Assert.assertEquals(reqParams.pageNumber, 1)
            Assert.assertEquals(reqParams.pageName, "pdp_3")
            Assert.assertEquals(reqParams.productIds, listOf("123"))
            Assert.assertEquals(reqParams.isTokonow, false)

            Assert.assertEquals(result.recommendationFilterChips.isEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.isNotEmpty(), true)
            Assert.assertEquals(result.recommendationItemList.size, 2)
            Assert.assertEquals(result.pageName, "pdp_3")
        }
    }

    @Test
    fun `on error get recomm throwable`() {
        runTest {
            coEvery {
                getRecommendationUseCase.getData(any())
            } throws Throwable()

            coEvery {
                getRecommendationFilterChips.executeOnBackground()
            } throws Throwable()

            val mockRequestParams = GetProductRecommendationUseCase.createParams(
                    productId = "123",
                    pageName = "pdp_3",
                    isTokoNow = false,
                    miniCartData = null
            )
            thrown.expect(MessageErrorException::class.java)
            useCaseTest.executeOnBackground(mockRequestParams)

            coVerify {
                getRecommendationUseCase.getData(any())
                getRecommendationFilterChips.executeOnBackground()
            }
        }
    }
}
