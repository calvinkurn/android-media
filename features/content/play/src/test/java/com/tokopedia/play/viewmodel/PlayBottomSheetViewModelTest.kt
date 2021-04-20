package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.model.PlayProductTagsModelBuilder
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
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
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private val modelBuilder = ModelBuilder()
    private val productModelBuilder = PlayProductTagsModelBuilder()
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

        coEvery { mockGetProductVariantUseCase.executeOnBackground() } returns mockProductVariantResponse
    }

    @Test
    fun `when get product variant is success, then it should show the un-clicked variant list`() {
        val product = productModelBuilder.buildProductLine()
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
    fun `when add to cart is success, then it should return the the correct feedback`() {

        coEvery { mockPostAddToCartUseCase.executeOnBackground() } returns modelBuilder.buildAddToCartModelResponseSuccess()

        val expectedModel = modelBuilder.buildCartUiModel(
                action = ProductAction.AddToCart,
                product = productModelBuilder.buildProductLine(),
                bottomInsetsType = BottomInsetsType.VariantSheet
        )
        val expectedResult = PlayResult.Success(
                Event(expectedModel)
        )

        playBottomSheetViewModel.addToCart(
                product = productModelBuilder.buildProductLine(),
                action = ProductAction.AddToCart,
                type = BottomInsetsType.VariantSheet
        )

        val actualValue = playBottomSheetViewModel.observableAddToCart.getOrAwaitValue()

        Assertions
                .assertThat(actualValue)
                .isInstanceOf(PlayResult.Success::class.java)

        Assertions
                .assertThat((actualValue as PlayResult.Success).data.peekContent())
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent(), "product")

        Assertions
                .assertThat(actualValue.data.peekContent().product)
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent().product, "impressHolder")
    }

    @Test
    fun `when add to cart is error, then it should return the same error`() {
        coEvery { mockPostAddToCartUseCase.executeOnBackground() } returns modelBuilder.buildAddToCartModelResponseFail()

        val expectedModel = modelBuilder.buildCartUiModel(
                action = ProductAction.AddToCart,
                product = productModelBuilder.buildProductLine(),
                bottomInsetsType = BottomInsetsType.VariantSheet,
                isSuccess = false,
                errorMessage = "error message ",
                cartId = ""
        )
        val expectedResult = PlayResult.Success(
                Event(expectedModel)
        )

        playBottomSheetViewModel.addToCart(
                product = productModelBuilder.buildProductLine(),
                action = ProductAction.AddToCart,
                type = BottomInsetsType.VariantSheet
        )

        val actualValue = playBottomSheetViewModel.observableAddToCart.getOrAwaitValue()

        Assertions
                .assertThat(actualValue)
                .isInstanceOf(PlayResult.Success::class.java)

        Assertions
                .assertThat((actualValue as PlayResult.Success).data.peekContent())
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent(), "product")

        Assertions
                .assertThat(actualValue.data.peekContent().product)
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent().product, "impressHolder")
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

    @Test
    fun `when logged in, should be allowed to open product detail`() {
        val eventProductDetail = InteractionEvent.OpenProductDetail(
                product = modelBuilder.buildProductLineUiModel(),
                position = 0
        )

        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventProductDetail))

        playBottomSheetViewModel.doInteractionEvent(eventProductDetail)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should be allowed to open product detail`() {
        val eventProductDetail = InteractionEvent.OpenProductDetail(
                product = modelBuilder.buildProductLineUiModel(),
                position = 0
        )

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventProductDetail))

        playBottomSheetViewModel.doInteractionEvent(eventProductDetail)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }
}