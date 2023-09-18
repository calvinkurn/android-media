package com.tokopedia.sellerorder.detail

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.usecase.*
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-05-11.
 */

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SomDetailViewModelTest : SomOrderBaseViewModelTest<SomDetailViewModel>() {

    private var listProducts = listOf<SomDetailOrder.GetSomDetail.Details.Product>()
    private var listReasonReject = listOf(SomReasonRejectData.Data.SomRejectReason())

    @RelaxedMockK
    lateinit var somGetOrderDetailUseCase: SomGetOrderDetailWithResolutionUseCase

    @RelaxedMockK
    lateinit var somReasonRejectUseCase: SomReasonRejectUseCase

    @RelaxedMockK
    lateinit var somSetDeliveredUseCase: SomSetDeliveredUseCase

    @RelaxedMockK
    lateinit var authorizeSomDetailAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeChatReplyAccessUseCase: AuthorizeAccessUseCase

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = SomDetailViewModel(
            somAcceptOrderUseCase,
            somRejectOrderUseCase,
            somEditRefNumUseCase,
            somRejectCancelOrderUseCase,
            somValidateOrderUseCase,
            userSessionInterface,
            coroutineTestRule.dispatchers,
            somGetOrderDetailUseCase,
            somReasonRejectUseCase,
            somSetDeliveredUseCase,
            authorizeSomDetailAccessUseCase,
            authorizeChatReplyAccessUseCase
        )

        val product1 = SomDetailOrder.GetSomDetail.Details.Product("123")
        listProducts = arrayListOf(product1).toMutableList()

        val reasonReject1 = SomReasonRejectData.Data.SomRejectReason(1)
        listReasonReject = arrayListOf(reasonReject1).toMutableList()
    }

    // order_detail
    @Test
    fun getOrderDetail_shouldCancelOldProcess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(getSomDetail = SomDetailOrder.GetSomDetail("123")))

        // when
        viewModel.loadDetailOrder("")
        viewModel.loadDetailOrder("")

        // then
        assert(viewModel.orderDetailResult.value is Success)
        assert((viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.orderId == "123")
    }

    @Test
    fun `given the bmgm data when loadDetailOrder then should return success`() {
        // given
        val orderId = "123"
        val bmgmDetailsResponse =
            SomDetailOrder.GetSomDetail.Details(
                bmgmIcon = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2023/2/8/60274de2-2dbc-48b4-b0cb-4f626792df2A.jpg",
                bmgms = listOf(
                    SomDetailOrder.GetSomDetail.Bmgm(
                        bmgmTierName = "offers - Beli2DiskonDiskon30%",
                        id = "1:3:0",
                        priceBeforeBenefitFormatted = "Rp400.000",
                        orderDetail = listOf(
                            SomDetailOrder.GetSomDetail.Bmgm.OrderDetail(
                                id = "2150865420",
                                productName = "Power Bank Original - Pink",
                                thumbnail = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2023/2/8/60274de2-2dbc-48b4-b0cb-4f626792df2b.jpg",
                                price = 75000.00,
                                priceText = "Rp 75.000",
                                quantity = 2
                            ),
                            SomDetailOrder.GetSomDetail.Bmgm.OrderDetail(
                                id = "2150865421",
                                productName = "Power Bank Original - Blue",
                                thumbnail = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2023/2/8/60274de2-2dbc-48b4-b0cb-4f626792df2b.jpg",
                                price = 75000.00,
                                priceText = "Rp 75.000",
                                quantity = 2
                            )
                        )
                    )
                )
            )

        coEvery {
            somGetOrderDetailUseCase.execute(orderId)
        } returns Success(
            GetSomDetailResponse(
                getSomDetail = SomDetailOrder.GetSomDetail(
                    details = bmgmDetailsResponse
                )
            )
        )

        // when
        viewModel.loadDetailOrder(orderId)

        // then
        coVerify {
            somGetOrderDetailUseCase.execute(orderId)
        }

        val detailsActual =
            (viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail!!.details

        assert(viewModel.orderDetailResult.value is Success)
        assertEquals(bmgmDetailsResponse.bmgmIcon, detailsActual.bmgmIcon)
        assertEquals(
            bmgmDetailsResponse.bmgms!!.first().bmgmTierName,
            detailsActual.bmgms!!.first().bmgmTierName
        )
        assertEquals(
            bmgmDetailsResponse.bmgms!!.first().priceBeforeBenefitFormatted,
            detailsActual.bmgms!!.first().priceBeforeBenefitFormatted
        )

        bmgmDetailsResponse.bmgms!!.first().orderDetail.forEachIndexed { index, product ->
            val productActual = detailsActual.bmgms!!.first().orderDetail[index]

            assertEquals(product.id, productActual.id)
            assertEquals(product.productName, productActual.productName)
            assertTrue(product.price == productActual.price)
            assertEquals(product.priceText, productActual.priceText)
            assertEquals(product.thumbnail, productActual.thumbnail)
            assertEquals(product.note, productActual.note)
        }
    }

    @Test
    fun `given the bmgm null when loadDetailOrder then should return success`() {
        // given
        val orderId = "123"
        val bmgmDetailsResponse =
            SomDetailOrder.GetSomDetail.Details(
                bmgmIcon = "null",
                bmgms = null
            )

        coEvery {
            somGetOrderDetailUseCase.execute(orderId)
        } returns Success(
            GetSomDetailResponse(
                getSomDetail = SomDetailOrder.GetSomDetail(
                    details = bmgmDetailsResponse
                )
            )
        )

        // when
        viewModel.loadDetailOrder(orderId)

        // then
        coVerify {
            somGetOrderDetailUseCase.execute(orderId)
        }

        val detailsActual =
            (viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail!!.details

        assert(viewModel.orderDetailResult.value is Success)
        assertEquals(bmgmDetailsResponse.bmgmIcon, detailsActual.bmgmIcon)
        assertEquals(
            bmgmDetailsResponse.bmgms,
            detailsActual.bmgms
        )
    }

    @Test
    fun getOrderDetail_shouldReturnSuccess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(getSomDetail = SomDetailOrder.GetSomDetail("123")))

        // when
        viewModel.loadDetailOrder("")

        // then
        assert(viewModel.orderDetailResult.value is Success)
        assert((viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.orderId == "123")
    }

    @Test
    fun getOrderDetail_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Fail(Throwable())

        // when
        viewModel.loadDetailOrder("")

        // then
        assert(viewModel.orderDetailResult.value is Fail)
    }

    @Test
    fun getOrderDetail_ifThrowableThrown_shouldFail() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } throws Throwable()

        // when
        viewModel.loadDetailOrder("")

        // then
        assert(viewModel.orderDetailResult.value is Fail)
    }

    @Test
    fun getOrderDetail_shouldNotReturnEmpty() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(
            GetSomDetailResponse(
                getSomDetail = SomDetailOrder.GetSomDetail(
                    details = SomDetailOrder.GetSomDetail.Details(
                        nonBundle = listProducts
                    )
                )
            )
        )

        // when
        viewModel.loadDetailOrder("")

        // then
        assert(viewModel.orderDetailResult.value is Success)
        assert(
            (viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.getProductList()
                ?.isNotEmpty()
                ?: false
        )
    }

    // reason_reject
    @Test
    fun getReasonReject_shouldReturnSuccess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somReasonRejectUseCase.execute(any())
        } returns Success(SomReasonRejectData.Data(listSomRejectReason = listReasonReject))

        // when
        viewModel.getRejectReasons()

        // then
        assert(viewModel.rejectReasonResult.value is Success)
        assert((viewModel.rejectReasonResult.value as Success<SomReasonRejectData.Data>).data.listSomRejectReason.first().reasonCode == 1)
    }

    @Test
    fun getReasonReject_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        coEvery {
            somReasonRejectUseCase.execute(any())
        } returns Fail(Throwable())

        // when
        viewModel.getRejectReasons()

        // then
        assert(viewModel.rejectReasonResult.value is Fail)
    }

    @Test
    fun getReasonReject_ifThrowableThrown_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        coEvery {
            somReasonRejectUseCase.execute(any())
        } throws Throwable()

        // when
        viewModel.getRejectReasons()

        // then
        assert(viewModel.rejectReasonResult.value is Fail)
    }

    @Test
    fun getReasonReject_msgShouldNotReturnEmpty() = coroutineTestRule.runTest {
        // given
        coEvery {
            somReasonRejectUseCase.execute(any())
        } returns Success(SomReasonRejectData.Data(listSomRejectReason = listReasonReject))

        // when
        viewModel.getRejectReasons()

        // then
        assert(viewModel.rejectReasonResult.value is Success)
        assert((viewModel.rejectReasonResult.value as Success<SomReasonRejectData.Data>).data.listSomRejectReason.isNotEmpty())
    }

    // set_delivered
    @Test
    fun setDelivered_shouldReturnSuccess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any())
        } returns Success(SetDeliveredResponse(SetDelivered(success = 1)))

        // when
        viewModel.setDelivered("", "")

        // then
        assert(viewModel.setDelivered.value is Success)
        assert((viewModel.setDelivered.value as Success<SetDeliveredResponse>).data.setDelivered.success == 1)
    }

    @Test
    fun setDelivered_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any())
        } returns Fail(Throwable())

        // when
        viewModel.setDelivered("", "")

        // then
        assert(viewModel.setDelivered.value is Fail)
    }

    @Test
    fun setDelivered_ifThrowableThrown_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any())
        } throws Throwable()

        // when
        viewModel.setDelivered("", "")

        // then
        assert(viewModel.setDelivered.value is Fail)
    }

    @Test
    fun setDelivered_msgShouldNotReturnEmpty() = coroutineTestRule.runTest {
        // given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any())
        } returns Success(SetDeliveredResponse(SetDelivered(message = listMsg)))

        // when
        viewModel.setDelivered("", "")

        // then
        assert(viewModel.setDelivered.value is Success)
        assert((viewModel.setDelivered.value as Success<SetDeliveredResponse>).data.setDelivered.message.first() == "msg1")
    }

    @Test
    fun checkAdminAccess_ifShopOwner_shouldReturnTruePair() = coroutineTestRule.runTest {
        coEvery {
            userSessionInterface.isShopOwner
        } returns true

        viewModel.getAdminPermission()

        assertDetailChatEligibilityEquals(true to true)
    }

    @Test
    fun checkAdminAccess_ifShopAdmin_shouldSuccess() = coroutineTestRule.runTest {
        val isSomDetailRole = true
        val isReplyChatRole = true
        onAuthorizeSomDetailAccessSuccess_thenReturn(isSomDetailRole)
        onAuthorizeReplyChatAccessSuccess_thenReturn(isReplyChatRole)
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        assertDetailChatEligibilityEquals(isSomDetailRole to isReplyChatRole)
    }

    @Test
    fun checkAdminAccess_ifShopAdmin_shouldFail() = coroutineTestRule.runTest {
        onAuthorizeReplyChatAccessFail_thenThrow(ResponseErrorException())
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        assert(viewModel.somDetailChatEligibility.value is Fail)
    }

    private fun onAuthorizeSomDetailAccessSuccess_thenReturn(isEligible: Boolean) =
        coroutineTestRule.runTest {
            coEvery {
                authorizeSomDetailAccessUseCase.execute(any())
            } returns isEligible
        }

    private fun onAuthorizeReplyChatAccessSuccess_thenReturn(isEligible: Boolean) =
        coroutineTestRule.runTest {
            coEvery {
                authorizeChatReplyAccessUseCase.execute(any())
            } returns isEligible
        }

    private fun onAuthorizeSomDetailAccessFail_thenThrow(throwable: Throwable) =
        coroutineTestRule.runTest {
            coEvery {
                authorizeSomDetailAccessUseCase.execute(any())
            } throws throwable
        }

    private fun onAuthorizeReplyChatAccessFail_thenThrow(throwable: Throwable) =
        coroutineTestRule.runTest {
            coEvery {
                authorizeChatReplyAccessUseCase.execute(any())
            } throws throwable
        }

    private fun assertDetailChatEligibilityEquals(pairs: Pair<Boolean, Boolean>) =
        coroutineTestRule.runTest {
            Assert.assertEquals(pairs, (viewModel.somDetailChatEligibility.value as? Success)?.data)
        }
}
