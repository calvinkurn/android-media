package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryPageMapper.mapToShowcaseProductCard
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.util.MiniCartMapper
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel.Companion.BATCH_SHOWCASE_TOTAL
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class TokoNowCategoryMainViewModelTestFixture {

    /**
     * private variable section
     */
    private lateinit var localAddress: TokoNowLocalAddress

    private val privateFieldLocalAddress = "localAddressData"

    private val categoryProductResponse1 = "category/ace-search-product-1-aneka-sayuran.json".jsonToObject<AceSearchProductModel>()
    private val categoryProductResponse2 = "category/ace-search-product-2-bawang.json".jsonToObject<AceSearchProductModel>()
    private val categoryProductResponse3 = "category/ace-search-product-3-buah-buahan.json".jsonToObject<AceSearchProductModel>()
    private val categoryProductResponse4 = "category/ace-search-product-4-jamur.json".jsonToObject<AceSearchProductModel>()
    private val categoryProductResponse5 = "category/ace-search-product-5-paket-sayur.json".jsonToObject<AceSearchProductModel>()
    private val categoryProductResponse6 = "category/ace-search-product-6-rempah.json".jsonToObject<AceSearchProductModel>()
    private val categoryProductResponse7 = "category/ace-search-product-7-tahu-tempe.json".jsonToObject<AceSearchProductModel>()
    private val miniCartDataResponse = "category/get-minicart.json".jsonToObject<MiniCartGqlResponse>()

    /**
     * protected variable section
     */

    protected lateinit var viewModel: TokoNowCategoryMainViewModel
    protected lateinit var addressData: LocalCacheModel

    protected val categoryIdL1: String = "123"
    protected val warehouseId: String = "345"
    protected val shopId: String = "11122"
    protected val navToolbarHeight: Int = 100

    protected val categoryDetailResponse = "category/category-detail.json".jsonToObject<CategoryDetailResponse>()
    protected val targetedTickerResponse = "category/targeted-ticker.json".jsonToObject<GetTargetedTickerResponse>()
    protected val addToCartGqlResponse = "category/add-to-cart-product.json".jsonToObject<AddToCartGqlResponse>()
    protected val updateProductInCartResponse = "category/update-product-in-cart.json".jsonToObject<UpdateCartGqlResponse>()
    protected val removeProductFromCartResponse = "category/remove-product-from-cart.json".jsonToObject<RemoveFromCartData>()

    protected val categoryProductResponseMap = mapOf(
        "4859" to categoryProductResponse1,
        "4826" to categoryProductResponse2,
        "4860" to categoryProductResponse3,
        "4863" to categoryProductResponse4,
        "4865" to categoryProductResponse5,
        "4948" to categoryProductResponse6,
        "4864" to categoryProductResponse7
    )

    /**
     * lateinit variable section
     */

    @RelaxedMockK
    lateinit var getCategoryDetailUseCase: GetCategoryDetailUseCase
    @RelaxedMockK
    lateinit var getCategoryProductUseCase: GetCategoryProductUseCase
    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase
    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase
    @RelaxedMockK
    lateinit var deleteCartUseCase:DeleteCartUseCase
    @RelaxedMockK
    lateinit var affiliateService: NowAffiliateService
    @RelaxedMockK
    lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase

    /**
     * variable with annotation section
     */

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        localAddress = TokoNowLocalAddress(mockk(relaxed = true))
        setAddressData(
            warehouseId = warehouseId,
            shopId = shopId
        )

        MockKAnnotations.init(this)

        viewModel = TokoNowCategoryMainViewModel(
            getCategoryDetailUseCase = getCategoryDetailUseCase,
            getCategoryProductUseCase = getCategoryProductUseCase,
            categoryIdL1 = categoryIdL1,
            addressData = localAddress,
            userSession = userSession,
            getMiniCartUseCase = getMiniCartUseCase,
            addToCartUseCase = addToCartUseCase,
            updateCartUseCase = updateCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            affiliateService = affiliateService,
            getTargetedTickerUseCase = getTargetedTickerUseCase,
            dispatchers = CoroutineTestDispatchersProvider
        )
    }

    /**
     * protected thenReturns & thenThrows functions section
     */

    protected fun setAddressData(
        warehouseId: String,
        shopId: String
    ) {
        addressData = LocalCacheModel(
            warehouse_id = warehouseId,
            shop_id = shopId
        )
        localAddress.mockPrivateField(
            name = privateFieldLocalAddress,
            value = LocalCacheModel(
                warehouse_id = warehouseId,
                shop_id = shopId
            )
        )
    }

    protected fun onUserSession_thenReturns(
        isLoggedIn: Boolean,
        userId: String,
        deviceId: String
    ) {
        every { userSession.isLoggedIn } returns isLoggedIn
        every { userSession.userId } returns userId
        every { userSession.deviceId } returns deviceId
    }

    protected fun onCategoryDetail_thenReturns() {
        coEvery {
            getCategoryDetailUseCase.execute(
                categoryIdL1 = categoryIdL1,
                warehouseId = warehouseId
            )
        } returns categoryDetailResponse
    }

    protected fun onCategoryDetail_thenThrows() {
        coEvery {
            getCategoryDetailUseCase.execute(
                categoryIdL1 = categoryIdL1,
                warehouseId = warehouseId
            )
        } throws Exception()
    }

    protected fun onTargetedTicker_thenReturns() {
        coEvery {
            getTargetedTickerUseCase.execute(
                warehouseId = warehouseId,
                page = GetTargetedTickerUseCase.CATEGORY_PAGE
            )
        } returns targetedTickerResponse
    }

    protected fun onTargetedTicker_thenThrows() {
        coEvery {
            getTargetedTickerUseCase.execute(
                warehouseId = warehouseId,
                page = GetTargetedTickerUseCase.CATEGORY_PAGE
            )
        } throws Exception()
    }

    protected fun onCategoryProduct_thenReturns(uniqueId: String) {
        categoryProductResponseMap.forEach { (categoryIdL2, categoryProductResponse) ->
            coEvery {
                getCategoryProductUseCase.execute(
                    addressData,
                    uniqueId,
                    categoryIdL2
                )
            } returns categoryProductResponse
        }
    }

    protected fun onCategoryProduct_thenThrows(
        uniqueId: String,
        expectedCategoryIdL2Failed: String
    ) {
        categoryProductResponseMap.forEach { (categoryIdL2, categoryProductResponse) ->
            if (expectedCategoryIdL2Failed == categoryIdL2) {
                coEvery {
                    getCategoryProductUseCase.execute(
                        addressData,
                        uniqueId,
                        categoryIdL2
                    )
                } throws Exception()
            } else {
                coEvery {
                    getCategoryProductUseCase.execute(
                        addressData,
                        uniqueId,
                        categoryIdL2
                    )
                } returns categoryProductResponse
            }
        }
    }

    protected fun onAddToCart_thenReturns(addToCartDataModel: AddToCartDataModel) {
        coEvery {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(addToCartDataModel)
        }
    }

    protected fun onAddToCart_thenThrows(exception: Exception) {
        coEvery {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Exception) -> Unit>().invoke(exception)
        }
    }

    protected fun onUpdateProductInCart_thenReturns() {
        coEvery {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(updateProductInCartResponse.updateCartData)
        }
    }

    protected fun onUpdateProductInCart_thenThrows(exception: Exception) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Exception) -> Unit>().invoke(exception)
        }
    }

    protected fun onRemoveProductFromCart_thenReturns() {
        coEvery {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(removeProductFromCartResponse)
        }
    }

    protected fun onRemoveProductFromCart_thenThrows(exception: Exception) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Exception) -> Unit>().invoke(exception)
        }
    }


    protected fun onGetMiniCart_thenReturns() {
        coEvery {
            getMiniCartUseCase.executeOnBackground()
        } returns MiniCartMapper.mapMiniCartSimplifiedData(miniCartDataResponse.miniCart)
    }

    /**
     * protected verification function section
     */

    protected fun verifyCategoryDetail() {
        coVerify {
            getCategoryDetailUseCase.execute(
                categoryIdL1 = categoryIdL1,
                warehouseId = warehouseId
            )
        }
    }

    protected fun verifyTargetedTicker() {
        coVerify {
            getTargetedTickerUseCase.execute(
                warehouseId = warehouseId,
                page = GetTargetedTickerUseCase.CATEGORY_PAGE
            )
        }
    }

    /**
     * protected other function section
     */

    protected fun getUniqueId(
        isLoggedIn: Boolean,
        userId: String,
        deviceId: String
    ) = if (isLoggedIn) AuthHelper.getMD5Hash(userId) else AuthHelper.getMD5Hash(deviceId)

    protected fun mapShowcaseProduct(
        expectedCategoryIdL2Failed: String = String.EMPTY,
        hasAdded: Boolean,
        categoryNavigationList: MutableList<CategoryNavigationItemUiModel>,
        resultList: MutableList<Visitable<*>>,
        hasBlockedAddToCart: Boolean
    ) {
        categoryNavigationList.take(BATCH_SHOWCASE_TOTAL).forEach { itemUiModel ->
            categoryProductResponseMap[itemUiModel.id]?.apply {
                if (this.searchProduct.data.productList.isEmpty()) {
                    resultList.remove(itemUiModel)
                    return@forEach
                }

                if (hasAdded) {
                    if (expectedCategoryIdL2Failed != itemUiModel.id) {
                        resultList.add(
                            mapToShowcaseProductCard(
                                categoryIdL2 = itemUiModel.id,
                                title = itemUiModel.title,
                                state = TokoNowLayoutState.SHOW,
                                seeAllAppLink = itemUiModel.appLink,
                                miniCartData = null,
                                hasBlockedAddToCart = hasBlockedAddToCart
                            )
                        )
                        categoryNavigationList.remove(itemUiModel)
                    }
                } else {
                    resultList.add(
                        AceSearchProductModel().mapToShowcaseProductCard(
                            categoryIdL2 = itemUiModel.id,
                            title = String.EMPTY,
                            state = TokoNowLayoutState.LOADING,
                            seeAllAppLink = String.EMPTY,
                            miniCartData = null,
                            hasBlockedAddToCart = hasBlockedAddToCart
                        )
                    )
                }
            }
        }
    }

}
