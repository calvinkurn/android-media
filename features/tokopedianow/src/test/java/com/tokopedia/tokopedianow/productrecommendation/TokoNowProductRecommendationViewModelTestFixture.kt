package com.tokopedia.tokopedianow.productrecommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class TokoNowProductRecommendationViewModelTestFixture {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel: TokoNowProductRecommendationViewModel

    protected val privateFieldProductModels = "productModels"

    protected val privateFieldMiniCartSimplifiedData = "mMiniCartSimplifiedData"

    protected val productModels = mutableListOf<Visitable<*>>(
        ProductCardCompactCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11111",
                name = "product a",
                price = "RP. 10.000",
                minOrder = 1,
                orderQuantity = 2,
                availableStock = 100,
                maxOrder = 6
            ),
            shopId = "122212",
            parentId = "0"
        ),
        ProductCardCompactCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11112",
                name = "product b",
                price = "RP. 30.000",
                minOrder = 1,
                orderQuantity = 0,
                availableStock = 100,
                maxOrder = 3
            ),
            shopId = "122212",
            parentId = "0"
        ),
        ProductCardCompactCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11113",
                name = "product c",
                price = "RP. 20.000",
                minOrder = 2,
                orderQuantity = 1,
                availableStock = 90,
                maxOrder = 2
            ),
            shopId = "122212",
            parentId = "0"
        ),
        ProductCardCompactCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11114",
                name = "product d",
                price = "RP. 60.000",
                minOrder = 2,
                orderQuantity = 3,
                availableStock = 30,
                maxOrder = 6
            ),
            shopId = "122212",
            parentId = "122231443"
        ),
        ProductCardCompactCarouselItemUiModel(
            productCardModel = TokoNowProductCardViewUiModel(
                productId = "11115",
                name = "product e",
                price = "RP. 40.000",
                minOrder = 2,
                orderQuantity = 2,
                availableStock = 70,
                maxOrder = 5
            ),
            shopId = "122212",
            parentId = "122231444"
        ),
        ProductCardCompactCarouselSeeMoreUiModel()
    )

    protected fun onGetRecommendation_thenReturn(response: List<RecommendationWidget>) {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns response
    }

    protected fun onGetRecommendation_thenReturn(error: Throwable) {
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws error
    }

    protected fun onAddToCart_thenReturn(response: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(response)
        }
    }

    protected fun onAddToCart_thenReturn(error: Throwable) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onRemoveItemCart_thenReturn(response: RemoveFromCartData) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(response)
        }
    }

    protected fun onRemoveItemCart_thenReturn(error: Throwable) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onUpdateItemCart_thenReturn(response: UpdateCartV2Data) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(response)
        }
    }

    protected fun onUpdateItemCart_thenReturn(error: Throwable) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun mockProductModels() {
        viewModel.mockPrivateField(
            name = privateFieldProductModels,
            value = productModels
        )
    }

    protected fun mockMiniCartSimplifiedData(productId: String) {
        viewModel.mockPrivateField(
            name = privateFieldMiniCartSimplifiedData,
            value = MiniCartSimplifiedData(
                miniCartItems = mapOf(
                    Pair(
                        MiniCartItemKey(
                            id = productId
                        ),
                        MiniCartItem.MiniCartItemProduct(
                            productId = productId
                        )
                    )
                )
            )
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowProductRecommendationViewModel(
            getRecommendationUseCase = getRecommendationUseCase,
            addToCartUseCase = addToCartUseCase,
            updateCartUseCase = updateCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            userSession = userSession,
            dispatchers = CoroutineTestDispatchers
        )
    }

    @Test
    fun `verify the properties`() {
        Assert.assertEquals(viewModel.isLogin, false)
        Assert.assertEquals(viewModel.userId, "")

        val isLogin = true
        val userId = "1222233"

        every {
            userSession.isLoggedIn
        } answers {
            isLogin
        }

        every {
            userSession.userId
        } answers {
            userId
        }

        Assert.assertEquals(viewModel.isLogin, isLogin)
        Assert.assertEquals(viewModel.userId, userId)
    }

}
