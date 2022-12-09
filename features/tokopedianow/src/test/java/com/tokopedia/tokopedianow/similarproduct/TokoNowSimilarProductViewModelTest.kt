package com.tokopedia.tokopedianow.similarproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun getSimilarProducts(){
        val data = ProductRecommendationResponse()
        coEvery {
            getSimilarProductUseCase.execute(userSession.userId.toIntOrZero(), "", mutableMapOf())
        } returns data

        every {
            chooseAddressData.city_id
        } answers{
            "123"
        }
        every {
            chooseAddressData.address_id
        } answers {
            "123"
        }
        every {
            chooseAddressData.district_id
        } answers {
            "321"
        }
        every {
            chooseAddressData.lat
        } answers {
            "123"
        }
        every {
            chooseAddressData.long
        } answers {
            "312"
        }
        every {
            chooseAddressData.postal_code
        } answers {
            "123"
        }
        every {
            chooseAddressData.warehouse_id
        } answers {
            "213"
        }

        coEvery {
            viewModel.appendChooseAddressParams()
        } returns mutableMapOf()

        viewModel.getSimilarProductList("")
        viewModel.similarProductList.verifyValueEquals(data.productRecommendationWidgetSingle?.data?.recommendation)
    }

    @Test
    fun `given recipe list when onViewCreated should map atc quantity to recipe list`() {
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
            SimilarProductUiModel(
                id = "2148241523",
                shopId = "480552",
                shopName = "Raju Karyana Store",
                name = "kaos testing 112",
                quantity = 5,
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
            ),
            SimilarProductUiModel(
                id = "2148241524",
                shopId = "480552",
                shopName = "Bittu Dhaba",
                name = "kaos testing 113",
                quantity = 3,
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
                position = 2
            )
        )

        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.onViewCreated(productList)

        val expectedProductList = listOf(
            SimilarProductUiModel(
                id = "2148241523",
                shopId = "480552",
                shopName = "Raju Karyana Store",
                name = "kaos testing 112",
                quantity = 6,
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
            ),
            SimilarProductUiModel(
                id = "2148241524",
                shopId = "480552",
                shopName = "Bittu Dhaba",
                name = "kaos testing 113",
                quantity = 100,
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
                position = 2
            )
        )

        viewModel.visitableItems.verifyValueEquals(expectedProductList)
    }

    @Test
    fun `given mini cart data not set when onViewCreated should update recipe list`() {
        val productList = listOf(
            SimilarProductUiModel(
                id = "2148241523",
                shopId = "480552",
                shopName = "Raju Karyana Store",
                name = "kaos testing 112",
                quantity = 5,
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
        )

        viewModel.onViewCreated(productList)

        val expectedProductList = listOf(
            SimilarProductUiModel(
                id = "2148241523",
                shopId = "480552",
                shopName = "Raju Karyana Store",
                name = "kaos testing 112",
                quantity = 5,
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
        )

        viewModel.visitableItems
            .verifyValueEquals(expectedProductList)
    }

    @Test
    fun `given layout list throws error when set mini cart data should do nothing`() {
        onGetLayoutItemList_returnNull()

        viewModel.setMiniCartData(MiniCartSimplifiedData())

        viewModel.visitableItems
            .verifyValueEquals(null)
    }

    protected fun onGetLayoutItemList_returnNull() {
        viewModel.mockPrivateField("layoutItemList", null)
    }
}
