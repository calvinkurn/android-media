package com.tokopedia.minicart.common.widget.viewmodel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.common.widget.viewmodel.utils.DataProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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

class GetProductBundleRecommendationTest {

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
    fun `WHEN first load mini cart list success THEN flag product bundle recom should be loaded`() {
        runBlocking {
            /**
             * Given
             */
            // mock mini cart list data response
            val mockMiniCartListResponse = DataProvider.provideGetMiniCartListSuccessAllAvailable()
            coEvery { getMiniCartListUseCase.setParams(any()) } just Runs
            coEvery { getMiniCartListUseCase.execute(any(), any()) } answers {
                firstArg<(MiniCartData) -> Unit>().invoke(mockMiniCartListResponse)
            }

            // mock product bundle data response
            val mockProductBundleResponse = DataProvider.provideProductBundleRecommendation()
            coEvery {
                getProductBundleRecomUseCase.execute(
                    productIds = any(),
                    excludeBundleIds = any()
                )
            } returns mockProductBundleResponse

            // mock product bundle ui model
            val mockProductBundleUiModel = DataProvider.provideProductBundleRecommendationData()
            coEvery {
                miniCartListUiModelMapper.mapToProductBundleUiModel(any())
            } returns mockProductBundleUiModel

            //observe value from postValue
            val observer = mockk<Observer<MiniCartListUiModel>>(relaxed = true)
            viewModel.miniCartListBottomSheetUiModel.observeForever(observer)

            /**
             * When
             */
            viewModel.getCartList(isFirstLoad = true)

            /**
             * Then
             */
            // check availability of product bundle recom value
            Assert.assertTrue(viewModel.miniCartListBottomSheetUiModel.value?.visitables?.firstOrNull { it is MiniCartProductBundleRecomUiModel } is MiniCartProductBundleRecomUiModel)

            //remove observer
            viewModel.miniCartListBottomSheetUiModel.removeObserver(observer)
        }
    }

}