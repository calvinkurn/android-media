package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.model.PlayProductTagsModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.model.GetProductVariantResponse
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
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()
    private val productModelBuilder = PlayProductTagsModelBuilder()
    private val mockProductVariantResponse: GetProductVariantResponse = modelBuilder.buildProductVariant()
    private val sectionMockData: ProductSectionUiModel.Section = UiModelBuilder.get().buildProductSection(
                                productList = listOf(productModelBuilder.buildProductLine()),
                                config = UiModelBuilder.get().buildSectionConfig(type = ProductSectionType.Active, reminderStatus = PlayUpcomingBellStatus.Off(0L)),
                                id = "")

    private lateinit var playBottomSheetViewModel: PlayBottomSheetViewModel

    @Before
    fun setUp() {
        playBottomSheetViewModel = PlayBottomSheetViewModel(
                mockGetProductVariantUseCase,
                userSession,
                dispatchers,
                mockRepo
        )

        coEvery { mockGetProductVariantUseCase.executeOnBackground() } returns mockProductVariantResponse
    }

    @Test
    fun `when add to cart is success, then it should return the the correct feedback`() {

        coEvery { mockRepo.addItemToCart(any(), any(), any(), any(), any()) } returns modelBuilder.buildAddToCartModelResponseSuccess()

        val expectedModel = modelBuilder.buildCartUiModel(
            action = ProductAction.AddToCart,
            product = productModelBuilder.buildProductLine(),
            bottomInsetsType = BottomInsetsType.VariantSheet,
        )
        val expectedResult = PlayResult.Success(
                Event(expectedModel)
        )

        playBottomSheetViewModel.addToCart(
                product = productModelBuilder.buildProductLine(),
                action = ProductAction.AddToCart,
                type = BottomInsetsType.VariantSheet,
                sectionInfo = sectionMockData
        )

        val actualValue = playBottomSheetViewModel.observableAddToCart.getOrAwaitValue()

        Assertions
                .assertThat(actualValue)
                .isInstanceOf(PlayResult.Success::class.java)

        Assertions
                .assertThat((actualValue as PlayResult.Success).data.first.peekContent())
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent(), "product", "errorMessage")

        Assertions
                .assertThat(actualValue.data.first.peekContent().product)
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent().product, "impressHolder")
    }

    @Test
    fun `when add to cart is error, then it should return the same error`() {
        coEvery { mockRepo.addItemToCart(any(), any(), any(), any(), any()) } returns modelBuilder.buildAddToCartModelResponseFail()

        val expectedModel = modelBuilder.buildCartUiModel(
                action = ProductAction.AddToCart,
                product = productModelBuilder.buildProductLine(),
                bottomInsetsType = BottomInsetsType.VariantSheet,
                isSuccess = false,
                errorMessage = IllegalStateException("error message "),
                cartId = ""
        )
        val expectedResult = PlayResult.Success(
                Event(expectedModel)
        )

        playBottomSheetViewModel.addToCart(
                product = productModelBuilder.buildProductLine(),
                action = ProductAction.AddToCart,
                type = BottomInsetsType.VariantSheet,
                sectionInfo = sectionMockData
        )

        val actualValue = playBottomSheetViewModel.observableAddToCart.getOrAwaitValue()

        Assertions
                .assertThat(actualValue)
                .isInstanceOf(PlayResult.Success::class.java)

        Assertions
                .assertThat((actualValue as PlayResult.Success).data.first.peekContent())
                .isEqualToIgnoringGivenFields(expectedResult.data.peekContent(), "product", "errorMessage")

        Assertions
            .assertThat(actualValue.data.first.peekContent().product)
            .isEqualToIgnoringGivenFields(expectedResult.data.peekContent().product, "impressHolder")
    }

    @Test
    fun `when logged in, should be allowed to do product action`() {
        val eventProductAction = InteractionEvent.DoActionProduct(
                product = modelBuilder.buildProductLineUiModel(),
                action = ProductAction.Buy,
                type = BottomInsetsType.VariantSheet,
                sectionInfo = sectionMockData
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
                type = BottomInsetsType.VariantSheet,
                sectionInfo = sectionMockData
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
                position = 0,
                sectionInfo = sectionMockData
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
                position = 0,
                sectionInfo = sectionMockData
        )

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventProductDetail))

        playBottomSheetViewModel.doInteractionEvent(eventProductDetail)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
            .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when logged in, should be allowed to do user report action`() {
        val eventUserReport = InteractionEvent.OpenUserReport
        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventUserReport))

        playBottomSheetViewModel.doInteractionEvent(eventUserReport)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
            .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should not be allowed to do user report action`() {
        val eventUserReport = InteractionEvent.OpenUserReport

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.NeedLoggedIn(eventUserReport))

        playBottomSheetViewModel.doInteractionEvent(eventUserReport)

        Assertions.assertThat(playBottomSheetViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
            .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when submit user report return success`(){
        coEvery { mockRepo.submitReport(any(), any(), any(), any(), any(), any()) } returns true

        val expectedResult = PlayResult.Success(
            Event(true)
        )

        playBottomSheetViewModel.submitUserReport(
            1L, "http://", 3L, 2, 5000L, "OKOKOKOKOK"
        )

        val actualValue = playBottomSheetViewModel.observableUserReportSubmission.getOrAwaitValue()

        Assertions
            .assertThat(actualValue)
            .isInstanceOf(PlayResult.Success::class.java)

        Assertions
            .assertThat((actualValue as PlayResult.Success).data.peekContent())
            .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when submit user report return failed`(){
        coEvery { mockRepo.submitReport(any(), any(), any(), any(), any(), any()) } returns false

        playBottomSheetViewModel.submitUserReport(
            1L, "http://", 3L, 2, 5000L, "OKOKOKOKOK"
        )

        val actualValue = playBottomSheetViewModel.observableUserReportSubmission.getOrAwaitValue()

        Assertions
            .assertThat(actualValue)
            .isInstanceOf(PlayResult.Failure::class.java)
    }

    @Test
    fun `when get reasoning list is success`(){
        val expectedResult = modelBuilder.buildUserReportList().reasoningList

        coEvery { mockRepo.getReasoningList() } returns expectedResult

        playBottomSheetViewModel.getUserReportList()

        val actualValue = playBottomSheetViewModel.observableUserReportReasoning.getOrAwaitValue()

        Assertions
            .assertThat((actualValue as PlayResult.Success).data.reasoningList)
            .isEqualTo(expectedResult)
    }
}