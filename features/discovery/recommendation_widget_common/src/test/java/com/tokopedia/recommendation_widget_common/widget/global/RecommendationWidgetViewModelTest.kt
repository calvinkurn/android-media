package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.MainDispatcherRule
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.jsonToObject
import com.tokopedia.recommendation_widget_common.mvvm.ViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.cart.CartService
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetState
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.AddToCartTestObject.addToCartQty
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.AddToCartTestObject.addToCartSuccessData
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.DeleteCartTestObject.TEST_DELETE_CART_PRODUCT_ID
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.DeleteCartTestObject.deleteCartSuccessData
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.MiniCartData.deletedMiniCartData
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.MiniCartData.getMiniCartItemProduct
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.MiniCartData.miniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.UpdateCartTestObject.TEST_UPDATE_CART_PRODUCT_ID
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.UpdateCartTestObject.TEST_UPDATE_CART_QUANTITY
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetViewModelTest.Companion.UpdateCartTestObject.updateCartSuccessData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.tokopedia.cartcommon.data.response.deletecart.Data as DeleteCartData
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetState as State

@OptIn(ExperimentalCoroutinesApi::class)
class RecommendationWidgetViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getRecommendationWidgetUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val getMiniCartUseCase = mockk<GetMiniCartListSimplifiedUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val updateCartUseCase = mockk<UpdateCartUseCase>(relaxed = true)
    private val deleteCartUseCase = mockk<DeleteCartUseCase>(relaxed = true)
    private val cartService = CartService(
        getMiniCartUseCase,
        addToCartUseCase,
        updateCartUseCase,
        deleteCartUseCase,
    )

    private fun ViewModel(state: State = State()): RecommendationWidgetViewModel {
        return RecommendationWidgetViewModel(
            state,
            getRecommendationWidgetUseCase,
            { cartService },
        )
    }

    private val ViewModel<RecommendationWidgetState>.stateValue
        get() = stateFlow.value

    private fun String.jsonToRecommendationWidgetList(): List<RecommendationWidget> =
        this.jsonToObject<RecommendationEntity>()
            .productRecommendationWidget
            .data.mappingToRecommendationModel()

    private val requestParamSlot = slot<GetRecommendationRequestParam>()
    private val requestParam: GetRecommendationRequestParam by lazy { requestParamSlot.captured }

    @Test
    fun `bind model will call use case`() {
        val viewModel = ViewModel()

        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val model = RecommendationWidgetModel(metadata = metadata)

        viewModel.bind(model)

        coVerify { getRecommendationWidgetUseCase.getData(capture(requestParamSlot)) }
        assertEquals(requestParam.pageNumber, model.metadata.pageNumber)
        assertEquals(requestParam.productIds, model.metadata.productIds)
        assertEquals(requestParam.queryParam, model.metadata.queryParam)
        assertEquals(requestParam.pageName, model.metadata.pageName)
        assertEquals(requestParam.categoryIds, model.metadata.categoryIds)
        assertEquals(requestParam.keywords, model.metadata.keyword)
        assertEquals(requestParam.isTokonow, model.metadata.isTokonow)
    }

    @Test
    fun `bind model with use case throws exception will hide the view`() {
        val viewModel = ViewModel()

        coEvery { getRecommendationWidgetUseCase.getData(any()) } throws Exception()

        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val model = RecommendationWidgetModel(metadata = metadata)

        viewModel.bind(model)

        assertEquals(1, viewModel.stateFlow.value.widgetMap.size)
        assertEquals(0, viewModel.stateValue.widgetMap[model.id]!!.size)
    }

    // Test case for our temporary solution
    // RecommendationWidgetModel should not contain RecommendationWidget
    // RecommendationWidget should only exists by calling getRecommendationWidgetUseCase
    @Test
    fun `bind model containing widget data will not call use case`() {
        val viewModel = ViewModel()

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val model = RecommendationWidgetModel(
            metadata = metadata,
            widget = RecommendationWidget(
                layoutType = TYPE_COMPARISON_BPC_WIDGET,
            ),
        )

        viewModel.bind(model)

        coVerify (exactly = 0) { getRecommendationWidgetUseCase.getData(any()) }
        assertEquals(1, viewModel.stateFlow.value.widgetMap.size)
        assertThat(
            viewModel.stateValue.widgetMap[model.id]!!.first(),
            `is`(instanceOf(RecommendationComparisonBpcModel::class.java))
        )
    }

    @Test
    fun `use case return type carousel will render carousel visitable`() {
        val viewModel = ViewModel()

        val recommendationWidgetList = "carousel_hatc.json".jsonToRecommendationWidgetList()
        coEvery { getRecommendationWidgetUseCase.getData(any()) } returns recommendationWidgetList

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)

        viewModel.bind(model)

        assertEquals(1, viewModel.stateValue.widgetMap.size)

        val expectedVisitableList = viewModel.stateValue.widgetMap[model.id]!!
        assertThat(
            expectedVisitableList.first(),
            `is`(instanceOf(RecommendationCarouselModel::class.java))
        )

        val carouselWidget = expectedVisitableList.first() as RecommendationCarouselModel
        assertEquals(carouselWidget.metadata, metadata)
        assertEquals(carouselWidget.trackingModel, trackingModel)
        assertEquals(carouselWidget.widget, recommendationWidgetList.first())
    }

    @Test
    fun `layout name vertical will render carousel vertical`() {
        val viewModel = ViewModel()

        val recommendationWidgetList = "recom_vertical.json".jsonToRecommendationWidgetList()
        coEvery { getRecommendationWidgetUseCase.getData(any()) } returns recommendationWidgetList

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)

        viewModel.bind(model)

        assertEquals(1, viewModel.stateValue.widgetMap.size)

        val expectedVisitableList = viewModel.stateValue.widgetMap[model.id]!!
        assertThat(
            expectedVisitableList.first(),
            `is`(instanceOf(RecommendationVerticalModel::class.java))
        )

        val verticalWidget = expectedVisitableList.first() as RecommendationVerticalModel
        assertEquals(verticalWidget.metadata, metadata)
        assertEquals(verticalWidget.trackingModel, trackingModel)
        assertEquals(verticalWidget.widget, recommendationWidgetList.first())
    }

    @Test
    fun `refresh will clear all data in the states`() {
        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)
        val recommendationWidgetList = "carousel_hatc.json".jsonToRecommendationWidgetList()
        val currentState = RecommendationWidgetState().from(model, recommendationWidgetList)
        val viewModel = ViewModel(currentState)

        viewModel.refresh()

        assertEquals(0, viewModel.stateValue.widgetMap.size)
    }

    @Test
    fun `bind model will not call use case if state has data for that model`() {
        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val model = RecommendationWidgetModel(metadata = metadata)
        val state = RecommendationWidgetState().loading(model)
        val viewModel = ViewModel(state)

        viewModel.bind(model)

        coVerify (exactly = 0) { getRecommendationWidgetUseCase.getData(any()) }
    }

    @Test
    fun `refresh mini cart when bind with MiniCartSource`() {
        every { getMiniCartUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }

        val recommendationWidgetList = "hatc.json".jsonToRecommendationWidgetList()
        coEvery { getRecommendationWidgetUseCase.getData(any()) } returns recommendationWidgetList

        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val miniCart = RecommendationWidgetMiniCart(SHOP_ID_TEST, MiniCartSource.PDPRecommendationWidget)
        val model = RecommendationWidgetModel(metadata = metadata, miniCart = miniCart)
        val viewModel = ViewModel()

        viewModel.bind(model)

        verify { getMiniCartUseCase.execute(any(), any()) }

        viewModel.stateValue.widgetMap.forEach { (_, visitableList) ->
            visitableList.forEach { visitable ->
                if (visitable is RecommendationCarouselModel)
                    visitable.widget.recommendationItemList.forEach { recommendationItem ->
                        val productId = recommendationItem.productId.toString()

                        assertEquals(
                            miniCartSimplifiedData.getMiniCartItemProduct(productId)?.quantity ?: 0,
                            recommendationItem.quantity
                        )
                    }
            }
        }

        assertEquals(miniCartSimplifiedData, viewModel.stateValue.miniCartData)
    }

    @Test
    fun `add to cart non variant success`() {
        every { addToCartUseCase.execute(any(), any()) } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(addToCartSuccessData)
        }
        every { getMiniCartUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)
        val recommendationWidgetList = "hatc.json".jsonToRecommendationWidgetList()
        val currentState = RecommendationWidgetState().from(model, recommendationWidgetList)
        val viewModel = ViewModel(currentState)

        val recommendationItem =
            recommendationWidgetList.first().recommendationItemList.find { it.quantity == 0 }!!
        viewModel.onAddToCartNonVariant(recommendationItem, addToCartQty)

        val expectedATCRequestParams = AddToCartRequestParams(
            recommendationItem.productId.toString(),
            recommendationItem.shopId.toString(),
            addToCartQty,
        )

        verify {
            addToCartUseCase.setParams(expectedATCRequestParams)
            addToCartUseCase.execute(any(), any())
            getMiniCartUseCase.execute(any(), any())
        }

        assertEquals(miniCartSimplifiedData, viewModel.stateValue.miniCartData)
    }

    @Test
    fun `delete cart non variant success`() {
        every { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(deleteCartSuccessData)
        }
        every { getMiniCartUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(deletedMiniCartData)
        }

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)
        val recommendationWidgetList = "hatc.json".jsonToRecommendationWidgetList()
        val currentState = RecommendationWidgetState()
            .from(model, recommendationWidgetList)
            .refreshMiniCart(miniCartSimplifiedData)
        val viewModel = ViewModel(currentState)

        val recommendationItem =
            (currentState.widgetMap[model.id]?.first() as RecommendationCarouselModel)
                .widget
                .recommendationItemList
                .find { it.productId.toString() == TEST_DELETE_CART_PRODUCT_ID }!!

        viewModel.onAddToCartNonVariant(recommendationItem, 0)

        val expectedDeleteCartParams = listOf(
            miniCartSimplifiedData.getMiniCartItemProduct(TEST_DELETE_CART_PRODUCT_ID)!!.cartId
        )
        verify {
            deleteCartUseCase.setParams(expectedDeleteCartParams)
            deleteCartUseCase.execute(any(), any())
            getMiniCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `update cart non variant success`() {
        every { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(updateCartSuccessData)
        }
        every { getMiniCartUseCase.execute(any(), any()) } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(deletedMiniCartData)
        }

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)
        val recommendationWidgetList = "hatc.json".jsonToRecommendationWidgetList()
        val currentState = RecommendationWidgetState()
            .from(model, recommendationWidgetList)
            .refreshMiniCart(miniCartSimplifiedData)
        val viewModel = ViewModel(currentState)

        val recommendationItem =
            (currentState.widgetMap[model.id]?.first() as RecommendationCarouselModel)
                .widget
                .recommendationItemList
                .find { it.productId.toString() == TEST_UPDATE_CART_PRODUCT_ID }!!

        viewModel.onAddToCartNonVariant(recommendationItem, TEST_UPDATE_CART_QUANTITY)

        val miniCartItemProduct =
            miniCartSimplifiedData.getMiniCartItemProduct(TEST_DELETE_CART_PRODUCT_ID)!!
        val expectedUpdateCartParams = listOf(
            UpdateCartRequest(
                cartId = miniCartItemProduct.cartId,
                quantity = TEST_UPDATE_CART_QUANTITY,
                notes = miniCartItemProduct.notes,
            )
        )
        verify {
            updateCartUseCase.setParams(
                updateCartRequestList = expectedUpdateCartParams,
                source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
            )
            updateCartUseCase.execute(any(), any())
            getMiniCartUseCase.execute(any(), any())
        }
    }

    companion object {

        private const val SHOP_ID_TEST = "12345"
        object AddToCartTestObject {
            val addToCartQty
                get() = 10
            val errorMessage
                get() = arrayListOf("Success nih", "1 barang berhasil ditambahkan ke keranjang!")
            val cartId
                get() = "12345"
            val addToCartSuccessData
                get() = AddToCartDataModel(
                    errorMessage = errorMessage,
                    status = AddToCartDataModel.STATUS_OK,
                    data = DataModel(
                        success = 1,
                        cartId = cartId,
                        message = arrayListOf(),
                        quantity = addToCartQty,
                    ),
                )
        }

        object UpdateCartTestObject {
            const val TEST_UPDATE_CART_PRODUCT_ID = "3132258458"
            const val TEST_UPDATE_CART_QUANTITY = 3
            val updateCartSuccessMessage get() = "Success nih"
            val updateCartSuccessData get() = UpdateCartV2Data(
                data = Data(status = true, message = updateCartSuccessMessage)
            )
        }

        object DeleteCartTestObject {
            const val TEST_DELETE_CART_PRODUCT_ID = "3132258458"
            val deleteCartMessage get() = "1 barang telah dihapus."
            val deleteCartSuccessData get() = RemoveFromCartData(
                status = "OK",
                errorMessage = listOf(deleteCartMessage),
                data = DeleteCartData(success = 1, message = listOf(deleteCartMessage))
            )
        }

        object MiniCartData {
            val miniCartItems
                get() = mapOf(
                    MiniCartItemKey("1578003235") to MiniCartItem.MiniCartItemProduct(
                        productId = "1578003235",
                        productParentId = "0",
                        quantity = 10,
                        cartId = "12345",
                    ),

                    MiniCartItemKey("1614387824") to MiniCartItem.MiniCartItemProduct(
                        productId = "1614387824",
                        productParentId = "0",
                        quantity = 3,
                        cartId = "12346",
                    ),

                    MiniCartItemKey("3132258458") to MiniCartItem.MiniCartItemProduct(
                        productId = "3132258458",
                        productParentId = "0",
                        quantity = 5,
                        cartId = "12347",
                    )
                )

            val miniCartSimplifiedData
                get() = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            val updatedMiniCartData
                get() = MiniCartSimplifiedData(
                    miniCartItems = miniCartItems.mapValues {
                        if (it.value.productId == TEST_UPDATE_CART_PRODUCT_ID)
                            it.value.copy(quantity = TEST_UPDATE_CART_QUANTITY)
                        else
                            it.value
                    }
                )

            val deletedMiniCartData
                get() = MiniCartSimplifiedData(
                    miniCartItems = miniCartItems.filter {
                        it.value.productId != TEST_DELETE_CART_PRODUCT_ID
                    }
                )

            fun MiniCartSimplifiedData.getMiniCartItemProduct(productId: String) =
                miniCartItems.getMiniCartItemProduct(productId)
        }
    }
}
