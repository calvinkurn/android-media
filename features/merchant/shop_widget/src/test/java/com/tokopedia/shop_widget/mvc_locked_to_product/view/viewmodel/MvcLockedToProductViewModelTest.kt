package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.model.MvcLockedToProductAddToCartTracker
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase.MvcLockedToProductUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductUtil
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductRequestUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MvcLockedToProductViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var viewModel: MvcLockedToProductViewModel

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var mvcLockedToProductUseCase: MvcLockedToProductUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK

    lateinit var updateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MvcLockedToProductViewModel(
            userSession,
            mvcLockedToProductUseCase,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            testCoroutineDispatcherProvider
        )
    }

    @Test
    fun `check whether isUserLogin return mocked value`() {
        val mockIsUserLogin = true
        every { userSession.isLoggedIn } returns mockIsUserLogin
        Assert.assertTrue(viewModel.isUserLogin == mockIsUserLogin)
    }

    @Test
    fun `check whether get userId return mocked value`() {
        val mockUserId = "12356"
        every { userSession.userId } returns mockUserId
        Assert.assertTrue(viewModel.userId == mockUserId)
    }

    @Test
    fun `check whether isSellerView return true when match`() {
        val mockShopId = "1234"
        every { userSession.shopId } returns mockShopId
        Assert.assertTrue(viewModel.isSellerView(mockShopId))
    }

    @Test
    fun `check whether mvcLockToProductLiveData value is success for rollence phase 1`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        val mockResponse = getMockMvcLockedToProductResponse()
        mockkObject(MvcLockedToProductUtil)
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } returns mockResponse
        every { MvcLockedToProductUtil.isMvcPhase2() } returns false
        viewModel.getMvcLockedToProductData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == mockResponse.shopPageMVCProductLock.nextPage)
        val mvcLockToProductValue = viewModel.mvcLockToProductLiveData.value
        assert(mvcLockToProductValue is Success)
        val data = (mvcLockToProductValue as Success).data
        val voucherUiModelTitle = data.mvcLockedToProductVoucherUiModel.title
        val voucherResponseTitle = mockResponse.shopPageMVCProductLock.voucher.title
        assert(voucherUiModelTitle == voucherResponseTitle)
        val productModel = data.mvcLockedToProductListGridProductUiModel
            .firstOrNull()?.productCardModel
        assert(productModel?.nonVariant == null)
        assert(productModel?.hasAddToCartButton == false)
    }

    @Test
    fun `check whether mvcLockToProductLiveData value is success for rollence phase 2`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        val mockResponse = getMockMvcLockedToProductResponse()
        mockkObject(MvcLockedToProductUtil)
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } returns mockResponse
        every { MvcLockedToProductUtil.isMvcPhase2() } returns true
        viewModel.getMvcLockedToProductData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == mockResponse.shopPageMVCProductLock.nextPage)
        val mvcLockToProductValue = viewModel.mvcLockToProductLiveData.value
        assert(mvcLockToProductValue is Success)
        val data = (mvcLockToProductValue as Success).data
        val voucherUiModelTitle = data.mvcLockedToProductVoucherUiModel.title
        val voucherResponseTitle = mockResponse.shopPageMVCProductLock.voucher.title
        assert(voucherUiModelTitle == voucherResponseTitle)
        val isProductVariantExists = data.mvcLockedToProductListGridProductUiModel.any {
            it.isVariant
        }
        val isProductNonVariantExists = data.mvcLockedToProductListGridProductUiModel.any {
            it.productCardModel.nonVariant != null
        }
        assert(isProductVariantExists)
        assert(isProductNonVariantExists)
    }

    @Test
    fun `check whether mvcLockToProductLiveData value is fail if exception thrown`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getMvcLockedToProductData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == null)
        val mvcLockToProductValue = viewModel.mvcLockToProductLiveData.value
        assert(mvcLockToProductValue is Fail)
    }

    @Test
    fun `check whether productListData value is success for rollence phase 1`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        val mockResponse = getMockMvcLockedToProductResponse()
        mockkObject(MvcLockedToProductUtil)
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } returns mockResponse
        every { MvcLockedToProductUtil.isMvcPhase2() } returns false
        viewModel.getProductListData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == mockResponse.shopPageMVCProductLock.nextPage)
        val productListData = viewModel.productListDataProduct.value
        assert(productListData is Success)
        val totalProduct = (productListData as Success).data.size
        val mockResponseTotalProduct = mockResponse.shopPageMVCProductLock.productList.data.size
        assert(totalProduct == mockResponseTotalProduct)
        val productModel = productListData.data.firstOrNull()?.productCardModel
        assert(productModel?.nonVariant == null)
        assert(productModel?.hasAddToCartButton == false)
    }

    @Test
    fun `check whether productListData value is success for rollence phase 2`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        val mockResponse = getMockMvcLockedToProductResponse()
        mockkObject(MvcLockedToProductUtil)
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } returns mockResponse
        every { MvcLockedToProductUtil.isMvcPhase2() } returns true
        viewModel.getProductListData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == mockResponse.shopPageMVCProductLock.nextPage)
        val productListData = viewModel.productListDataProduct.value
        assert(productListData is Success)
        val totalProduct = (productListData as Success).data.size
        val mockResponseTotalProduct = mockResponse.shopPageMVCProductLock.productList.data.size
        assert(totalProduct == mockResponseTotalProduct)
        val isProductVariantExists = productListData.data.any {
            it.isVariant
        }
        val isProductNonVariantExists = productListData.data.any {
            it.productCardModel.nonVariant != null
        }
        assert(isProductVariantExists)
        assert(isProductNonVariantExists)
    }

    @Test
    fun `check whether productListData value is fail if exception thrown`() {
        val mockRequestUiModel = getMockMvcLockedToProductRequestUiModel()
        coEvery {
            mvcLockedToProductUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getProductListData(mockRequestUiModel)
        assert(viewModel.nextPageLiveData.value == null)
        val mvcLockToProductValue = viewModel.productListDataProduct.value
        assert(mvcLockToProductValue is Fail)
    }

    @Test
    fun `check setMiniCart value same as mocked data`() {
        val mockMiniCartSimplifiedData = getMockMiniCartSimplifiedData()
        viewModel.setMiniCartData(mockMiniCartSimplifiedData)
        val data = viewModel.miniCartSimplifiedData.value
        assert(data?.miniCartItems?.size == mockMiniCartSimplifiedData.miniCartItems.size)
    }

    @Test
    fun `check miniCartAdd value is success and same as mocked data`() {
        val mockProductId = "10"
        val mockSuccessValue = 1
        val miniCartSimplifiedData = getMockMiniCartSimplifiedData()
        onAddToCartUseCaseThenReturn(
            AddToCartDataModel(
                data = DataModel(
                    success = mockSuccessValue,
                    productId = mockProductId.toLongOrZero()
                )
            )
        )
        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.handleAtcFlow(mockProductId, 1, "123")
        val miniCartAddData = (viewModel.miniCartAdd.value as? Success)
        val atcFlowTracker = viewModel.mvcAddToCartTracker
        assert(atcFlowTracker.value?.atcType == MvcLockedToProductAddToCartTracker.AtcType.ADD)
        assert(miniCartAddData is Success)
        assert(miniCartAddData?.data?.data?.success == 1)
        assert(miniCartAddData?.data?.data?.productId?.toString() == mockProductId)
    }

    @Test
    fun `check miniCartAdd value is error`() {
        val mockProductId = "1"
        val error = Throwable(message = "Error")
        onAddToCartUseCaseThenReturn(error)
        viewModel.handleAtcFlow(mockProductId, 1, "123")
        val miniCartAddData = (viewModel.miniCartAdd.value as? Fail)
        assert(miniCartAddData is Fail)
    }

    @Test
    fun `check miniCartRemove value is success and same as mocked data`() {
        val mockProductId = "1"
        val mockMessage = "Deleted"
        val mockSuccessValue = 1
        val miniCartSimplifiedData = getMockMiniCartSimplifiedData()
        onDeleteCartUseCaseThenReturn(
            RemoveFromCartData(
                data = Data(
                    success = mockSuccessValue,
                    message = listOf(mockMessage)
                )
            )
        )
        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.handleAtcFlow(mockProductId, 0, "123")
        val miniCartRemoveData = (viewModel.miniCartRemove.value as? Success)
        val atcFlowTracker = viewModel.mvcAddToCartTracker
        assert(atcFlowTracker.value?.atcType == MvcLockedToProductAddToCartTracker.AtcType.REMOVE)
        assert(miniCartRemoveData is Success)
        assert(miniCartRemoveData?.data?.first == mockProductId)
        assert(miniCartRemoveData?.data?.second == mockMessage)
    }

    @Test
    fun `check miniCartRemove value is error`() {
        val mockProductId = "1"
        val error = Throwable(message = "Error")
        val miniCartSimplifiedData = getMockMiniCartSimplifiedData()
        onDeleteCartUseCaseThenReturn(error)
        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.handleAtcFlow(mockProductId, 0, "123")
        val miniCartRemoveData = (viewModel.miniCartRemove.value as? Fail)
        assert(miniCartRemoveData is Fail)
    }

    @Test
    fun `check miniCartUpdate value is success and same as mocked data for increase product quantity`() {
        val mockProductId = "1"
        val mockStatusValue = true
        val miniCartSimplifiedData = getMockMiniCartSimplifiedData()
        onUpdateCartUseCaseThenReturn(
            UpdateCartV2Data(
                data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                    status = true
                )
            )
        )
        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.handleAtcFlow(mockProductId, 5, "123")
        val miniCartRemoveData = (viewModel.miniCartUpdate.value as? Success)
        val atcFlowTracker = viewModel.mvcAddToCartTracker
        assert(atcFlowTracker.value?.atcType == MvcLockedToProductAddToCartTracker.AtcType.UPDATE_ADD)
        assert(miniCartRemoveData is Success)
        assert(miniCartRemoveData?.data?.data?.status == mockStatusValue)
    }

    @Test
    fun `check miniCartUpdate value is error`() {
        val mockProductId = "1"
        val error = Throwable(message = "Error")
        val miniCartSimplifiedData = getMockMiniCartSimplifiedData()
        onUpdateCartUseCaseThenReturn(error)
        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.handleAtcFlow(mockProductId, 2, "123")
        val miniCartUpdateData = (viewModel.miniCartUpdate.value as? Fail)
        assert(miniCartUpdateData is Fail)
    }

    @Test
    fun `check miniCartUpdate value is success and same as mocked data for decrease product quantity`() {
        val mockProductId = "1"
        val mockStatusValue = true
        val miniCartSimplifiedData = getMockMiniCartSimplifiedData()
        onUpdateCartUseCaseThenReturn(
            UpdateCartV2Data(
                data = com.tokopedia.cartcommon.data.response.updatecart.Data(
                    status = true
                )
            )
        )
        viewModel.setMiniCartData(miniCartSimplifiedData)
        viewModel.handleAtcFlow(mockProductId, 1, "123")
        val miniCartRemoveData = (viewModel.miniCartUpdate.value as? Success)
        val atcFlowTracker = viewModel.mvcAddToCartTracker
        assert(atcFlowTracker.value?.atcType == MvcLockedToProductAddToCartTracker.AtcType.UPDATE_REMOVE)
        assert(miniCartRemoveData is Success)
        assert(miniCartRemoveData?.data?.data?.status == mockStatusValue)
    }

    private fun getMockMvcLockedToProductRequestUiModel(): MvcLockedToProductRequestUiModel {
        val mockPromoId = "6578"
        val shopId = "1234"
        return MvcLockedToProductRequestUiModel(
            shopId,
            mockPromoId,
            1,
            10,
            MvcLockedToProductSortUiModel(),
            LocalCacheModel()
        )
    }

    private fun getMockMvcLockedToProductResponse(): MvcLockedToProductResponse {
        val mockVoucherTitle = "Title here"
        val mockListProductData = listOf(
            MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data(),
            MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data(
                childIDs = listOf("1234")
            ),
            MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data()
        )
        return MvcLockedToProductResponse(
            MvcLockedToProductResponse.ShopPageMVCProductLock(
                nextPage = 5,
                MvcLockedToProductResponse.ShopPageMVCProductLock.Voucher(
                    title = mockVoucherTitle
                ),
                MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList(
                    data = mockListProductData
                )
            )
        )
    }

    private fun getMockMiniCartSimplifiedData(): MiniCartSimplifiedData {
        return MiniCartSimplifiedData(
            miniCartItems = listOf(
                MiniCartItem(productId = "1", quantity = 3),
                MiniCartItem(productId = "2"),
                MiniCartItem(productId = "3")
            )
        )
    }

    private fun onAddToCartUseCaseThenReturn(model: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(model)
        }
    }

    private fun onAddToCartUseCaseThenReturn(error: Throwable) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    private fun onDeleteCartUseCaseThenReturn(model: RemoveFromCartData) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(model)
        }
    }

    private fun onDeleteCartUseCaseThenReturn(error: Throwable) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    private fun onUpdateCartUseCaseThenReturn(model: UpdateCartV2Data) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(model)
        }
    }

    private fun onUpdateCartUseCaseThenReturn(error: Throwable) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

}