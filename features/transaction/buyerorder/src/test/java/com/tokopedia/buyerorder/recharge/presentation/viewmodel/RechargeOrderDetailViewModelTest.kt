package com.tokopedia.buyerorder.recharge.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.data.response.DigitalPaymentInfoMessage
import com.tokopedia.buyerorder.recharge.domain.RechargeEmoneyVoidUseCase
import com.tokopedia.buyerorder.recharge.domain.RechargeOrderDetailUseCase
import com.tokopedia.buyerorder.recharge.presentation.model.*
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.home_component_header.model.ChannelHeader
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
    private val emoneyVoidUseCase: RechargeEmoneyVoidUseCase = mockk()
    private val getRecommendationUseCaseCoroutine: GetRecommendationUseCase = mockk()
    private val bestSellerMapper: BestSellerMapper = mockk()
    private val recommendationUseCase: DigitalRecommendationUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = RechargeOrderDetailViewModel(
            orderDetailUseCase,
            emoneyVoidUseCase,
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
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
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
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
        } coAnswers {
            Success(emptyList())
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
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
        } coAnswers {
            Success(
                listOf(
                    "dg_order_detail_dgu",
                    "pg_order_detail_dgu"
                )
            )
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
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
        } coAnswers {
            Success(
                listOf(
                    "pg_order_detail_dgu",
                    "dg_order_detail_dgu"
                )
            )
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
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
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
    fun getDigitalRecommendationPositionData_whenValueSuccess_shouldReturnListOfString() {
        // given
        val expectedRecommendationList = listOf(
            "dg_order_detail_dgu",
            "pg_order_detail_dgu"
        )
        coEvery {
            orderDetailUseCase.execute(any())
        } coAnswers {
            Fail(Throwable())
        }
        coEvery {
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
        } coAnswers {
            Success(expectedRecommendationList)
        }

        // when
        viewModel.fetchData(RechargeOrderDetailRequest("", ""))
        val actualResult = viewModel.getRecommendationWidgetPositionData()

        // then
        assertEquals(expectedRecommendationList, actualResult)
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
        coEvery {
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
        } coAnswers {
            Success(
                listOf(
                    "dg_order_detail_dgu",
                    "pg_order_detail_dgu"
                )
            )
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
                additionalTicker = null,
                paymentInfoMessage = RechargePaymentInfoMessage(
                    "Belum termasuk biaya layanan, jasa aplikasi dan biaya lainnya.",
                    "Lihat SK",
                    "tokopedia://mybills",
                )
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
        coEvery {
            recommendationUseCase.getRecommendationPosition(any(), any(), any())
        } coAnswers {
            Success(
                listOf(
                    "dg_order_detail_dgu",
                    "pg_order_detail_dgu"
                )
            )
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
            seeMoreAppLink = "",
            channelHeader = ChannelHeader()
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

    @Test
    fun onResetOrderDetailData() {
        // when
        viewModel.resetOrderDetailData()

        // then
        assert(viewModel.orderDetailData.value == null)
    }
}
