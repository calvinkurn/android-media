package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 02/04/20.
 */
class PlayBottomSheetViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetProductVariantUseCase: GetProductVariantUseCase = mockk(relaxed = true)
    private val mockPostAddToCartUseCase: PostAddToCartUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val modelBuilder = ModelBuilder()
    private val mockProductVariantResponse: ProductDetailVariantCommonResponse = modelBuilder.buildProductVariant()

    private lateinit var playBottomSheetViewModel: PlayBottomSheetViewModel

    @Before
    fun setUp() {
        playBottomSheetViewModel = PlayBottomSheetViewModel(
                mockGetProductVariantUseCase,
                mockPostAddToCartUseCase,
                userSession,
                dispatchers
        )

        coEvery { mockGetProductVariantUseCase.executeOnBackground() } returns modelBuilder.buildProductVariant()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `when get product variant is success, then it should show the un-clicked variant list`() {
        val product = PlayUiMapper.mapItemProduct(modelBuilder.buildProduct())
        val action = ProductAction.AddToCart
        val selectedVariants = VariantCommonMapper.mapVariantIdentifierToHashMap(mockProductVariantResponse.data)
        val categoryVariants = VariantCommonMapper.processVariant(mockProductVariantResponse.data,
                mapOfSelectedVariant = selectedVariants)
        val expectedModel = modelBuilder.buildVariantSheetUiModel(
                product = product,
                action = action,
                parentVariant = mockProductVariantResponse.data,
                mapOfSelectedVariants = selectedVariants,
                listOfVariantCategory = categoryVariants.orEmpty(),
                stockWording = null
        )
        val expectedResult = PlayResult.Success(expectedModel)

        playBottomSheetViewModel.getProductVariant(product, action)

        Assertions
                .assertThat(playBottomSheetViewModel.observableProductVariant.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `test add to cart success`() {

        coEvery { mockPostAddToCartUseCase.executeOnBackground() } returns modelBuilder.buildAddToCartModelResponseSuccess()

        val expectedModel = modelBuilder.buildCartUiModel(
                action = ProductAction.AddToCart,
                product = PlayUiMapper.mapItemProduct(modelBuilder.buildProduct()),
                bottomInsetsType = BottomInsetsType.VariantSheet
        )

        playBottomSheetViewModel.addToCart(
                product = PlayUiMapper.mapItemProduct(modelBuilder.buildProduct()),
                action = ProductAction.AddToCart,
                type = BottomInsetsType.VariantSheet
        )

        Assertions
                .assertThat(playBottomSheetViewModel.observableAddToCart.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test add to cart fail`() {
        coEvery { mockPostAddToCartUseCase.executeOnBackground() } returns modelBuilder.buildAddToCartModelResponseFail()

        val expectedModel = modelBuilder.buildCartUiModel(
                action = ProductAction.AddToCart,
                product = PlayUiMapper.mapItemProduct(modelBuilder.buildProduct()),
                bottomInsetsType = BottomInsetsType.VariantSheet,
                isSuccess = false,
                errorMessage = "error message ",
                cartId = ""
        )

        playBottomSheetViewModel.addToCart(
                product = PlayUiMapper.mapItemProduct(modelBuilder.buildProduct()),
                action = ProductAction.AddToCart,
                type = BottomInsetsType.VariantSheet
        )

        Assertions
                .assertThat(playBottomSheetViewModel.observableAddToCart.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `when logged in, should be allowed to do product action`() {
        val eventProductAction = InteractionEvent.DoActionProduct(
                product = modelBuilder.buildProductLineUiModel(),
                action = ProductAction.Buy,
                type = BottomInsetsType.VariantSheet
        )

        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventProductAction))

        playBottomSheetViewModel.doInteractionEvent(eventProductAction)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should not be allowed to do product action`() {
        val eventProductAction = InteractionEvent.DoActionProduct(
                product = modelBuilder.buildProductLineUiModel(),
                action = ProductAction.Buy,
                type = BottomInsetsType.VariantSheet
        )

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.NeedLoggedIn(eventProductAction))

        playBottomSheetViewModel.doInteractionEvent(eventProductAction)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }
}