package com.tokopedia.sellerappwidget.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.model.GetOrderResponse
import com.tokopedia.sellerappwidget.data.model.OrderFilterSomModel
import com.tokopedia.sellerappwidget.data.model.OrderProductModel
import com.tokopedia.sellerappwidget.domain.mapper.OrderMapper
import com.tokopedia.sellerappwidget.utils.TestHelper
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import com.tokopedia.sellerappwidget.view.model.OrderProductUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.model.SellerOrderStatusUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 21/12/20
 */

class GetOrderUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_order_list_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: OrderMapper

    private lateinit var getOrderUseCase: GetOrderUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getOrderUseCase = GetOrderUseCase(gqlRepository, mapper)
    }

    @Test
    fun `should success when get order list`() = runBlocking {
        getOrderUseCase.params = GetOrderUseCase.createParams(anyString(), anyString(), anyInt(), anyInt())

        val successResponse = TestHelper.createSuccessResponse<GetOrderResponse>(SUCCESS_RESPONSE)
        val data = successResponse.getData<GetOrderResponse>(GetOrderResponse::class.java)
        val uiModel: OrderUiModel = getMappedUiModel(data)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        coEvery {
            mapper.mapRemoteModelToUiModel(any())
        } returns uiModel

        val actualResult: OrderUiModel = getOrderUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        coVerify {
            mapper.mapRemoteModelToUiModel(any())
        }

        assertEquals(getOrderUiModel(), actualResult)
    }

    @Test
    fun `when failed get chat list then throw RuntimeException`() = runBlocking {
        getOrderUseCase.params = GetOrderUseCase.createParams(anyString(), anyString(), anyInt(), anyInt())

        val errorResponse = TestHelper.createErrorResponse<GetOrderResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val actualResult = getOrderUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertNull(actualResult)
    }

    private fun getMappedUiModel(data: GetOrderResponse): OrderUiModel {
        return OrderUiModel(
                orders = data.orderList.list.map {
                    OrderItemUiModel(
                            orderId = it.orderId,
                            deadLineText = it.deadLineText,
                            statusId = it.statusId,
                            product = getFirstProduct(it.orderProducts.getOrNull(0)),
                            productCount = it.orderProducts.size
                    )
                },
                sellerOrderStatus = SellerOrderStatusUiModel(
                        newOrder = getOrderAmount(data.orderFilterSom, Const.OrderStatusStr.NEW_ORDER),
                        readyToShip = getOrderAmount(data.orderFilterSom, Const.OrderStatusStr.READY_TO_SHIP)
                )
        )
    }

    private fun getOrderAmount(orderFilterSom: OrderFilterSomModel?, orderStatus: String): Int {
        return orderFilterSom?.statusList
                ?.firstOrNull {
                    it.key == orderStatus
                }
                ?.orderAmount.orZero()
    }

    private fun getFirstProduct(product: OrderProductModel?): OrderProductUiModel? {
        product?.let {
            return OrderProductUiModel(
                    productId = it.productId,
                    productName = it.productName,
                    picture = it.picture
            )
        }
        return null
    }

    private fun getOrderUiModel(): OrderUiModel {
        return OrderUiModel(
                orders = listOf(OrderItemUiModel(
                        orderId = "665019822",
                        deadLineText = "5 Hari 23 Jam",
                        statusId = 400,
                        product = OrderProductUiModel(
                                productId = "199047330",
                                productName = "Product 48",
                                picture = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2016/12/17/12299749/12299749_a943a8f6-af0e-4507-8bbd-399f397a081a_300_300.jpg"
                        ),
                        productCount = 1 //number of orders
                )),
                sellerOrderStatus = SellerOrderStatusUiModel(
                        newOrder = 64,
                        readyToShip = 218
                )
        )
    }
}