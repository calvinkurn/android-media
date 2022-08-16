package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.mapper.AddToCartBundleDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddToCartBundleTest {

    private lateinit var viewModel: MiniCartViewModel

    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase = mockk()
    private val getProductBundleRecomUseCase: GetProductBundleRecomUseCase = mockk()
    private val addToCartBundleUseCase: AddToCartBundleUseCase = mockk()
    private var miniCartListUiModelMapper: MiniCartListUiModelMapper = spyk()
    private var miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper = spyk()
    private var addToCartBundleDataMapper: AddToCartBundleDataMapper = spyk()
    private val userSession: UserSessionInterface = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartViewModel(
            dispatcher,
            getMiniCartListSimplifiedUseCase,
            getMiniCartListUseCase,
            deleteCartUseCase,
            undoDeleteCartUseCase,
            updateCartUseCase,
            getProductBundleRecomUseCase,
            addToCartBundleUseCase,
            addToCartOccMultiUseCase,
            miniCartListUiModelMapper,
            miniCartChatListUiModelMapper,
            userSession
        )
    }

    @Test
    fun `WHEN add to cart bundle success should return success state from global event`() = runBlocking {
        /**
         * Given
         */
        val shopId = "123421"
        val warehouseId = "222111"
        val bundleId = "131233"
        val bundleName = "product bundle"
        val bundleType = "single_bundling"
        val bundlePosition = 3
        val priceCut = "100000"
        val productQuantity = 1
        val userId = "1221231"

        // mock userid
        coEvery {
            userSession.userId
        } returns userId

        // mock product details param
        val mockProductDetails = DataProvider.provideAddToCartBundleProductDetailParam(
            productDetails = emptyList(),
            quantity = productQuantity,
            shopId = shopId,
            userId = userId
        )
        coEvery {
            miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(any(), any(), any(), any())
        } returns mockProductDetails

        // mock add to cart bundle model
        val response = DataProvider.provideAddToCartBundleResponse(isSuccess = true)
        coEvery {
            addToCartBundleUseCase.setParams(any())
        } just Runs

        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns addToCartBundleDataMapper.mapAddToCartBundleResponse(response)

        /**
         * When
         */
        viewModel.addBundleToCart(
            shopId = shopId,
            warehouseId = warehouseId,
            bundleId = bundleId,
            bundleName = bundleName,
            bundleType = bundleType,
            bundlePosition = bundlePosition,
            priceCut = priceCut,
            productDetails = emptyList(),
            productQuantity = productQuantity
        )

        /**
         * Then
         */
        // check state add to cart
        Assert.assertTrue(viewModel.globalEvent.getOrAwaitValue().state == GlobalEvent.STATE_SUCCESS_ADD_TO_CART_BUNDLE_RECOM_ITEM)
    }

    @Test
    fun `WHEN add to cart bundle success with empty data should return success state from global event`() = runBlocking {
        /**
         * Given
         */
        val shopId = "123421"
        val warehouseId = "222111"
        val bundleId = "131233"
        val bundleName = "product bundle"
        val bundleType = "single_bundling"
        val bundlePosition = 3
        val priceCut = "100000"
        val productQuantity = 1
        val userId = "1221231"

        // mock userid
        coEvery {
            userSession.userId
        } returns userId

        // mock product details param
        val mockProductDetails = DataProvider.provideAddToCartBundleProductDetailParam(
            productDetails = emptyList(),
            quantity = productQuantity,
            shopId = shopId,
            userId = userId
        )
        coEvery {
            miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(any(), any(), any(), any())
        } returns mockProductDetails

        // mock add to cart bundle model
        val response = DataProvider.provideAddToCartBundleResponse(isSuccess = true, isEmptyData = true)
        coEvery {
            addToCartBundleUseCase.setParams(any())
        } just Runs

        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns addToCartBundleDataMapper.mapAddToCartBundleResponse(response)

        /**
         * When
         */
        viewModel.addBundleToCart(
            shopId = shopId,
            warehouseId = warehouseId,
            bundleId = bundleId,
            bundleName = bundleName,
            bundleType = bundleType,
            bundlePosition = bundlePosition,
            priceCut = priceCut,
            productDetails = emptyList(),
            productQuantity = productQuantity
        )

        /**
         * Then
         */
        // check state add to cart
        Assert.assertTrue(viewModel.globalEvent.getOrAwaitValue().state == GlobalEvent.STATE_SUCCESS_ADD_TO_CART_BUNDLE_RECOM_ITEM)
    }


    @Test
    fun `WHEN add to cart bundle not success should return failed state from global event`() = runBlocking {
        /**
         * Given
         */
        val shopId = "123421"
        val warehouseId = "222111"
        val bundleId = "131233"
        val bundleName = "product bundle"
        val bundleType = "single_bundling"
        val bundlePosition = 3
        val priceCut = "100000"
        val productQuantity = 1
        val userId = "1221231"

        // mock userid
        coEvery {
            userSession.userId
        } returns userId

        // mock product details param
        val mockProductDetails = DataProvider.provideAddToCartBundleProductDetailParam(
            productDetails = emptyList(),
            quantity = productQuantity,
            shopId = shopId,
            userId = userId
        )
        coEvery {
            miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(any(), any(), any(), any())
        } returns mockProductDetails

        // mock add to cart bundle model
        val response = DataProvider.provideAddToCartBundleResponse(isSuccess = false)
        coEvery {
            addToCartBundleUseCase.setParams(any())
        } just Runs
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns addToCartBundleDataMapper.mapAddToCartBundleResponse(response)

        /**
         * When
         */
        viewModel.addBundleToCart(
            shopId = shopId,
            warehouseId = warehouseId,
            bundleId = bundleId,
            bundleName = bundleName,
            bundleType = bundleType,
            bundlePosition = bundlePosition,
            priceCut = priceCut,
            productDetails = emptyList(),
            productQuantity = productQuantity
        )

        /**
         * Then
         */
        // check state add to cart
        Assert.assertTrue(viewModel.globalEvent.getOrAwaitValue().state == GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM)
    }

    @Test
    fun `WHEN add to cart bundle not success and not ok should return failed state from global event`() = runBlocking {
        /**
         * Given
         */
        val shopId = "123421"
        val warehouseId = "222111"
        val bundleId = "131233"
        val bundleName = "product bundle"
        val bundleType = "single_bundling"
        val bundlePosition = 3
        val priceCut = "100000"
        val productQuantity = 1
        val userId = "1221231"

        // mock userid
        coEvery {
            userSession.userId
        } returns userId

        // mock product details param
        val mockProductDetails = DataProvider.provideAddToCartBundleProductDetailParam(
            productDetails = emptyList(),
            quantity = productQuantity,
            shopId = shopId,
            userId = userId
        )
        coEvery {
            miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(any(), any(), any(), any())
        } returns mockProductDetails

        // mock add to cart bundle model
        val response = DataProvider.provideAddToCartBundleResponse(isSuccess = false, isOkStatus = false)
        coEvery {
            addToCartBundleUseCase.setParams(any())
        } just Runs
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns addToCartBundleDataMapper.mapAddToCartBundleResponse(response)

        /**
         * When
         */
        viewModel.addBundleToCart(
            shopId = shopId,
            warehouseId = warehouseId,
            bundleId = bundleId,
            bundleName = bundleName,
            bundleType = bundleType,
            bundlePosition = bundlePosition,
            priceCut = priceCut,
            productDetails = emptyList(),
            productQuantity = productQuantity
        )

        /**
         * Then
         */
        // check state add to cart
        Assert.assertTrue(viewModel.globalEvent.getOrAwaitValue().state == GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM)
    }

    @Test
    fun `WHEN failed to fetch add to cart bundle should return failed state from global event`() = runBlocking {
        /**
         * Given
         */
        val shopId = "123421"
        val warehouseId = "222111"
        val bundleId = "131233"
        val bundleName = "product bundle"
        val bundleType = "single_bundling"
        val bundlePosition = 3
        val priceCut = "100000"
        val productQuantity = 1
        val userId = "1221231"

        // mock userid
        coEvery {
            userSession.userId
        } returns userId

        // mock product details param
        val mockProductDetails = DataProvider.provideAddToCartBundleProductDetailParam(
            productDetails = emptyList(),
            quantity = productQuantity,
            shopId = shopId,
            userId = userId
        )
        coEvery {
            miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(any(), any(), any(), any())
        } returns mockProductDetails

        /**
         * When
         */
        viewModel.addBundleToCart(
            shopId = shopId,
            warehouseId = warehouseId,
            bundleId = bundleId,
            bundleName = bundleName,
            bundleType = bundleType,
            bundlePosition = bundlePosition,
            priceCut = priceCut,
            productDetails = emptyList(),
            productQuantity = productQuantity
        )

        /**
         * Then
         */
        // check state add to cart
        Assert.assertTrue(viewModel.globalEvent.getOrAwaitValue().state == GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM)
    }
}