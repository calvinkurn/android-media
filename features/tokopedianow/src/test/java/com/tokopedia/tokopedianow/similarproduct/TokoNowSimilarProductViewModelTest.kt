package com.tokopedia.tokopedianow.similarproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.similarproduct.domain.model.ProductRecommendationResponse
import com.tokopedia.tokopedianow.similarproduct.domain.usecase.GetSimilarProductUseCase
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.tokopedianow.similarproduct.viewmodel.TokoNowSimilarProductViewModel
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class TokoNowSimilarProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var addressData: TokoNowLocalAddress
    private lateinit var userSession: UserSessionInterface
    private lateinit var getSimilarProductUseCase: GetSimilarProductUseCase
    private lateinit var chooseAddressWrapper: ChooseAddressWrapper
    private lateinit var chooseAddressData: LocalCacheModel

    protected lateinit var viewModel: TokoNowSimilarProductViewModel

    @Before
    fun setUp() {
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        getSimilarProductUseCase = mockk(relaxed = true)
        chooseAddressWrapper = mockk(relaxed = true)
        chooseAddressData = mockk(relaxed = true)

        viewModel = TokoNowSimilarProductViewModel(
            getSimilarProductUseCase,
            chooseAddressWrapper,
            userSession,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartUseCase,
            addressData,
            CoroutineTestDispatchers
        )
    }

    @Test
    fun `user id and loggedIn test`(){
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns ""
        assertEquals(true, viewModel.isLoggedIn)
        assertEquals("", viewModel.userId)
    }

    @Test
    fun `get similar products list success`(){
        val recommendationItem = ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem(
            "",
            "",
            ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem.Shop("", "", 0),
            "",
            0,
            0,
            "",
            true,
            "",
            listOf(Any()),
            listOf(ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem.BadgesItem("", "")),
            0,
            "",
            "",
            "",
            0F,
            0,0,"","","",
            0,"","",0,
            listOf(ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem.WholesalePriceItem(0, 0, 0, "")),0
        )

        val response = ProductRecommendationResponse(productRecommendationWidgetSingle = ProductRecommendationResponse.ProductRecommendationWidgetSingle(data = ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data(recommendation = listOf(recommendationItem))))
        coEvery {
            getSimilarProductUseCase.execute(any(), any(), any())
        } returns response

        every {
            chooseAddressWrapper.getChooseAddressData()
        } returns returnLocalCacheModel()

        viewModel.getSimilarProductList("123")
        viewModel.similarProductList.verifyValueEquals(response.productRecommendationWidgetSingle?.data?.recommendation)
        assertEquals("412", viewModel.warehouseId)
    }

    @Test
    fun `empty or null product recommendation widget`(){

        val response = ProductRecommendationResponse(null)
        coEvery {
            getSimilarProductUseCase.execute(any(), any(), any())
        } returns response

        every {
            chooseAddressWrapper.getChooseAddressData()
        } returns returnLocalCacheModel()

        viewModel.getSimilarProductList("123")
        viewModel.similarProductList.verifyValueEquals(emptyList<Visitable<*>>())
    }

    @Test
    fun `empty or null similar products data`(){
        val response = ProductRecommendationResponse(ProductRecommendationResponse.ProductRecommendationWidgetSingle(data = null))
        coEvery {
            getSimilarProductUseCase.execute(any(), any(), any())
        } returns response

        every {
            chooseAddressWrapper.getChooseAddressData()
        } returns returnLocalCacheModel()

        viewModel.getSimilarProductList("123")
        viewModel.similarProductList.verifyValueEquals(emptyList<Visitable<*>>())
    }

    @Test
    fun `error while getting similar products list`(){
        coEvery { getSimilarProductUseCase.execute(any(), any(), any())
        } throws Throwable()
        viewModel.getSimilarProductList(ArgumentMatchers.anyString())
        viewModel.similarProductList.verifyValueEquals(null)
    }

    @Test
    fun `mini cart quantity should map to recommended products list when onViewCreated`() {
        val miniCartItems = mapOf(
            MiniCartItemKey("2148241523") to MiniCartItem.MiniCartItemProduct(
                productId = "2148241523",
                quantity = 6
            ),
            MiniCartItemKey("2148241524") to MiniCartItem.MiniCartItemProduct(
                productId = "2148241524",
                quantity = 100
            )
        )
        val miniCartSimplifiedData = MiniCartSimplifiedData(miniCartItems = miniCartItems)
        val productList = listOf(
            getSimilarProductUiModel("2148241523", 5),
            getSimilarProductUiModel("2148241524", 3)
        )

        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.onViewCreated(productList)

        val expectedProductList = listOf(
            getSimilarProductUiModel("2148241523", 6),
            getSimilarProductUiModel("2148241524", 100)
        )

        viewModel.visitableItems.verifyValueEquals(expectedProductList)
    }

    @Test
    fun `given mini cart data not set when onViewCreated should update similar products list`() {
        val productList = listOf(
            getSimilarProductUiModel("2148241523", 5)
        )

        viewModel.onViewCreated(productList)

        val expectedProductList = listOf(
            getSimilarProductUiModel("2148241523", 5)
        )

        viewModel.visitableItems.verifyValueEquals(expectedProductList)
    }

    @Test
    fun `given layout list throws error when set mini cart data should do nothing`() {
        onGetLayoutItemList_returnNull()

        viewModel.setMiniCartData(MiniCartSimplifiedData())

        viewModel.visitableItems.verifyValueEquals(null)
    }

    private fun onGetLayoutItemList_returnNull() {
        viewModel.mockPrivateField("layoutItemList", null)
    }

    private fun getSimilarProductUiModel(productId: String, quantity: Int): SimilarProductUiModel{
        return SimilarProductUiModel(
            id = productId,
            shopId = "480552",
            shopName = "Raju Karyana Store",
            name = "kaos testing 112",
            quantity = quantity,
            stock = 7,
            minOrder = 1,
            maxOrder = 7,
            priceFmt = "Rp150",
            weight = "500 g",
            imageUrl = "https://ecs7.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
            slashedPrice = "Rp200",
            discountPercentage = "20",
            similarProducts = emptyList(),
            categoryId = "983",
            position = 1
        )
    }

    private fun returnLocalCacheModel() = LocalCacheModel(
        city_id = "123",
        address_id = "112121",
        district_id = "12",
        lat = "123",
        long = "412",
        postal_code = "123",
        warehouse_id = "412"
    )

}
