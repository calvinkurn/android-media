package com.tokopedia.creation.common.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.creation.common.domain.ContentCreationConfigUseCase
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigAuthorModel
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationMediaModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Muhammad Furqan on 23/10/23
 */
class ContentCreationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val contentCreationConfigUseCase: ContentCreationConfigUseCase = mockk()
    private val testDispatcher = coroutineTestRule.dispatchers

    private lateinit var viewModel: ContentCreationViewModel

    @Before
    fun setUp() {
        viewModel = ContentCreationViewModel(contentCreationConfigUseCase, testDispatcher)
    }

    @Test
    fun `on selectCreationItem when success should change selectedCreationType value`() {
        // given
        val creationItem = getCreationItemList()[0]

        // when
        viewModel.selectCreationItem(creationItem)

        // then
        val data = viewModel.selectedCreationType.value
        assert(data == creationItem)
    }

    @Test
    fun `on fetchConfig when no data from outside should fetch from usecase and if success should change creation config value to success`() {
        // given
        val creationConfigModel = getCreationConfigModelData()
        coEvery { contentCreationConfigUseCase(Unit) } returns creationConfigModel

        // when
        viewModel.fetchConfig()

        // then
        val creationData = viewModel.creationConfig.value
        assert(creationData is Success)
        val successCreationData = creationData as Success
        assert(successCreationData.data == creationConfigModel)
    }

    @Test
    fun `on fetchConfig when no data from outside should fetch from usecase and fail should change creation config value to fail`() {
        // given
        coEvery { contentCreationConfigUseCase(Unit) } throws Throwable("Failed")

        // when
        viewModel.fetchConfig()

        // then
        val creationData = viewModel.creationConfig.value
        assert(creationData is Fail)
        val successCreationData = creationData as Fail
        assert(successCreationData.throwable.message == "Failed")
    }

    @Test
    fun `on fetchConfig when there is data from outside should change creation config value to success with outside data`() {
        // given
        val creationConfigModel = getCreationConfigModelData()

        // when
        viewModel.fetchConfig(creationConfigModel)

        // then
        val creationData = viewModel.creationConfig.value
        assert(creationData is Success)
        val successCreationData = creationData as Success
        assert(successCreationData.data == creationConfigModel)
    }

    @Test
    fun `on getPerformanceDashboardApplink when creation config success should return link from config`() {
        // given
        val creationConfigModel = getCreationConfigModelData()
        viewModel.fetchConfig(creationConfigModel)

        // when
        val applink = viewModel.getPerformanceDashboardApplink()

        // then
        assert(applink == creationConfigModel.statisticApplink)
    }

    @Test
    fun `on getPerformanceDashboardApplink when creation config not success should return static performance link`() {
        // given
coEvery { contentCreationConfigUseCase(Unit) } throws Throwable("Failed")
        viewModel.fetchConfig()

        // when
        val applink = viewModel.getPerformanceDashboardApplink()

        // then
        assert(applink == ApplinkConstInternalContent.PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK)
    }

    private fun getCreationConfigModelData() = ContentCreationConfigModel(
        isActive = true,
        statisticApplink = "tokopedia://test-applink/statistic",
        authors = listOf(
            ContentCreationConfigAuthorModel(
                id = "author-id",
                name = "Author Name",
                type = "shop",
                image = "http://...",
                hasUsername = true,
                hasAcceptTnc = true,
            )
        ),
        creationItems = getCreationItemList(),
    )

    private fun getCreationItemList() = listOf(
        ContentCreationItemModel(
            isActive = true,
            type = ContentCreationTypeEnum.LIVE,
            title = "Live",
            applink = "tokopedia://test-applink/live",
            media = ContentCreationMediaModel(
                type = "image",
                id = "123",
                coverUrl = "https://...",
                mediaUrl = "https://...",
            ),
            descriptionApplink = "tokopedia://test-applink/description",
            drawableIconId = 0,
            authorType = ContentCreationAuthorEnum.SHOP,
        ),
        ContentCreationItemModel(
            isActive = true,
            type = ContentCreationTypeEnum.STORY,
            title = "Live",
            applink = "tokopedia://test-applink/story",
            media = ContentCreationMediaModel(
                type = "video",
                id = "123",
                coverUrl = "https://...",
                mediaUrl = "https://...",
            ),
            descriptionApplink = "tokopedia://test-applink/description",
            drawableIconId = 0,
            authorType = ContentCreationAuthorEnum.SHOP,
        ),
        ContentCreationItemModel(
            isActive = false,
            type = ContentCreationTypeEnum.STORY,
            title = "Live",
            applink = "tokopedia://test-applink/story",
            media = ContentCreationMediaModel(
                type = "video",
                id = "123",
                coverUrl = "https://...",
                mediaUrl = "https://...",
            ),
            descriptionApplink = "tokopedia://test-applink/description",
            drawableIconId = 0,
            authorType = ContentCreationAuthorEnum.SHOP,
        )
    )
}
