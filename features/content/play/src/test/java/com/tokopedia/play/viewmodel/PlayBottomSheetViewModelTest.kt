package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.model.PlayProductTagsModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
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

    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()
    private val productModelBuilder = PlayProductTagsModelBuilder()
    private val sectionMockData: ProductSectionUiModel.Section =
        UiModelBuilder.get().buildProductSection(
            productList = listOf(productModelBuilder.buildProductLine()),
            config = UiModelBuilder.get().buildSectionConfig(
                type = ProductSectionType.Active,
                reminderStatus = PlayUpcomingBellStatus.Off,
            ),
            id = ""
        )

    private lateinit var playBottomSheetViewModel: PlayBottomSheetViewModel

    @Before
    fun setUp() {
        playBottomSheetViewModel = PlayBottomSheetViewModel(
                userSession
        )
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
}
