package com.tokopedia.buyerorder.recharge.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.domain.RechargeOrderDetailUseCase
import com.tokopedia.buyerorder.recharge.presentation.model.*
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationTrackingModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationType
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by furqan on 08/11/2021
 */
@RunWith(JUnit4::class)
class RechargeOrderDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: RechargeOrderDetailViewModel

    private val orderDetailUseCase: RechargeOrderDetailUseCase = mockk()
    private val getRecommendationUseCaseCoroutine: GetRecommendationUseCase = mockk()
    private val bestSellerMapper: BestSellerMapper = mockk()
    private val recommendationUseCase: DigitalRecommendationUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = RechargeOrderDetailViewModel(
                orderDetailUseCase,
                getRecommendationUseCaseCoroutine,
                bestSellerMapper,
                recommendationUseCase,
                dispatcher
        )
    }

    @Test
    fun getDigitalRecommendationPosition_whenValueNull_shouldReturnZero() {
        // given

        // when
        val digitalRecommendationPosition = viewModel.getDigitalRecommendationPosition()

        // then
        assertEquals(digitalRecommendationPosition, 0)
    }

    @Test
    fun getDigitalRecommendationPosition_whenValueFail_shouldReturnZero() {
        // given
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Fail(Throwable())
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val digitalRecommendationPosition = viewModel.getDigitalRecommendationPosition()

        // then
        assertEquals(digitalRecommendationPosition, 0)
    }

    @Test
    fun getDigitalRecommendationPosition_whenValueSuccessButEmpty_shouldReturnZero() {
        // given
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Success(DigitalRecommendationModel("", "", emptyList()))
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val digitalRecommendationPosition = viewModel.getDigitalRecommendationPosition()

        // then
        assertEquals(digitalRecommendationPosition, 0)
    }

    @Test
    fun getDigitalRecommendationPosition_whenValueSuccessInFirstPosition_shouldReturnOne() {
        // given
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Success(DigitalRecommendationModel(
                    "",
                    "",
                    listOf(
                            DigitalRecommendationItemModel(
                                    categoryName = "dg_order_detail",
                                    iconUrl = "",
                                    productName = "",
                                    applink = "",
                                    tracking = DigitalRecommendationTrackingModel(),
                                    type = DigitalRecommendationType.CATEGORY,
                                    price = "",
                                    beforePrice = "",
                                    discountTag = ""
                            ),
                            DigitalRecommendationItemModel(
                                    categoryName = "pg_order_detail",
                                    iconUrl = "",
                                    productName = "",
                                    applink = "",
                                    tracking = DigitalRecommendationTrackingModel(),
                                    type = DigitalRecommendationType.CATEGORY,
                                    price = "",
                                    beforePrice = "",
                                    discountTag = ""
                            ),
                    )
            ))
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val digitalRecommendationPosition = viewModel.getDigitalRecommendationPosition()

        // then
        assertEquals(digitalRecommendationPosition, 1)
    }

    @Test
    fun getDigitalRecommendationPosition_whenValueSuccessInSecondPosition_shouldReturnTwo() {
        // given
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Success(DigitalRecommendationModel(
                    "",
                    "",
                    listOf(
                            DigitalRecommendationItemModel(
                                    categoryName = "pg_order_detail",
                                    iconUrl = "",
                                    productName = "",
                                    applink = "",
                                    tracking = DigitalRecommendationTrackingModel(),
                                    type = DigitalRecommendationType.CATEGORY,
                                    price = "",
                                    beforePrice = "",
                                    discountTag = ""
                            ),
                            DigitalRecommendationItemModel(
                                    categoryName = "dg_order_detail",
                                    iconUrl = "",
                                    productName = "",
                                    applink = "",
                                    tracking = DigitalRecommendationTrackingModel(),
                                    type = DigitalRecommendationType.CATEGORY,
                                    price = "",
                                    beforePrice = "",
                                    discountTag = ""
                            ),
                    )
            ))
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val digitalRecommendationPosition = viewModel.getDigitalRecommendationPosition()

        // then
        assertEquals(digitalRecommendationPosition, 2)
    }

    @Test
    fun getDigitalRecommendationPositionData_whenValueNull_shouldReturnNull() {
        // given

        // when
        val digitalRecommendationWidgetData = viewModel.getRecommendationWidgetPositionData()

        // then
        assertEquals(digitalRecommendationWidgetData, null)
    }

    @Test
    fun getDigitalRecommendationPositionData_whenValueFail_shouldReturnNull() {
        // given
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Fail(Throwable())
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val digitalRecommendationWidgetData = viewModel.getRecommendationWidgetPositionData()

        // then
        assertEquals(digitalRecommendationWidgetData, null)
    }

    @Test
    fun getDigitalRecommendationPositionData_whenValueSuccess_shouldReturnRecommendationWidgetModel() {
        // given
        val recommendationWidgetData = DigitalRecommendationModel(
                "",
                "",
                listOf(
                        DigitalRecommendationItemModel(
                                categoryName = "dg_order_detail",
                                iconUrl = "",
                                productName = "",
                                applink = "",
                                tracking = DigitalRecommendationTrackingModel(),
                                type = DigitalRecommendationType.CATEGORY,
                                price = "",
                                beforePrice = "",
                                discountTag = ""
                        ),
                        DigitalRecommendationItemModel(
                                categoryName = "pg_order_detail",
                                iconUrl = "",
                                productName = "",
                                applink = "",
                                tracking = DigitalRecommendationTrackingModel(),
                                type = DigitalRecommendationType.CATEGORY,
                                price = "",
                                beforePrice = "",
                                discountTag = ""
                        ),
                )
        )
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Success(recommendationWidgetData)
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val digitalRecommendationWidgetData = viewModel.getRecommendationWidgetPositionData()

        // then
        assertEquals(digitalRecommendationWidgetData, recommendationWidgetData)
    }

    @Test
    fun getOrderDetailResultData_whenOrderDetailNull_shouldReturnNull() {
        // given

        // when
        val orderDetailData = viewModel.getOrderDetailResultData()

        // then
        assertEquals(orderDetailData, null)
    }

    @Test
    fun getOrderDetailResultData_whenOrderDetailFail_shouldReturnNull() {
        // given
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Fail(Throwable())
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val orderDetailData = viewModel.getOrderDetailResultData()

        // then
        assertEquals(orderDetailData, null)
    }

    @Test
    fun getOrderDetailResultData_whenOrderDetailSuccess_shouldReturnData() {
        // given
        val orderDetailResponse = RechargeOrderDetailModel(
                topSectionModel = RechargeOrderDetailTopSectionModel(
                        labelStatusColor = "",
                        textStatusColor = "",
                        textStatus = "",
                        tickerData = RechargeOrderDetailTickerModel(
                                title = "",
                                text = "",
                                urlDetail = "",
                                type = 0
                        ),
                        invoiceRefNum = "",
                        invoiceUrl = "",
                        titleData = emptyList()
                ),
                detailsSection = RechargeOrderDetailSectionModel(
                        detailList = emptyList()
                ),
                paymentSectionModel = RechargeOrderDetailPaymentModel(
                        paymentMethod = RechargeOrderDetailSimpleModel(
                                label = "",
                                detail = "",
                                isTitleBold = false,
                                isDetailBold = false,
                                alignment = RechargeSimpleAlignment.RIGHT
                        ),
                        paymentDetails = emptyList(),
                        totalPriceLabel = "",
                        totalPrice = "",
                        additionalTicker = null
                ),
                helpUrl = "",
                actionButtonList = RechargeOrderDetailActionButtonListModel(
                        actionButtons = emptyList()
                )
        )
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Success(orderDetailResponse)
        }
        coEvery {
            recommendationUseCase.execute(any(), any(), any())
        } coAnswers {
            Fail(Throwable())
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val orderDetailData = viewModel.getOrderDetailResultData()

        // then
        assertEquals(orderDetailData, orderDetailResponse)
    }

    @Test
    fun getTopAdsData_whenResponseNull_shouldReturnNull() {
        // given

        // when
        val topAdsData = viewModel.getTopAdsData()

        // then
        assertEquals(topAdsData, null)
    }

    @Test
    fun getTopAdsData_whenResponseFail_shouldReturnNull() {
        // given
        coEvery {
            getRecommendationUseCaseCoroutine.getData(any())
        } coAnswers {
            throw Throwable("Failed")
        }

        // when
        viewModel.fetchTopAdsData()
        val topAdsData = viewModel.getTopAdsData()

        // then
        assertEquals(topAdsData, null)
    }

    @Test
    fun getTopAdsData_whenResponseSuccess_shouldReturnData() {
        // given
        val bestSellerDataModel = BestSellerDataModel(
                title = "",
                subtitle = "",
                height = 0,
                pageName = "",
                productCardModelList = emptyList(),
                recommendationItemList = emptyList(),
                filterChip = emptyList(),
                seeMoreAppLink = ""
        )
        coEvery {
            getRecommendationUseCaseCoroutine.getData(any())
        } returns
                listOf(
                        RecommendationWidget(
                                title = "",
                                subtitle = "",
                                pageName = "",
                                recommendationFilterChips = emptyList(),
                                seeMoreAppLink = "",
                                recommendationItemList = listOf(
                                        RecommendationItem()
                                )
                        )
                )
        coEvery {
            bestSellerMapper.mappingRecommendationWidget(any())
        } returns bestSellerDataModel

        // when
        viewModel.fetchTopAdsData()
        val topAdsData = viewModel.getTopAdsData()

        // then
        assertEquals(topAdsData, bestSellerDataModel)
    }

}