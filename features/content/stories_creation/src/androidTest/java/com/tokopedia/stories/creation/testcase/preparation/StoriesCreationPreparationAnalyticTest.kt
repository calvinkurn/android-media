package com.tokopedia.stories.creation.testcase.preparation

import android.graphics.Bitmap
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.creation.common.presentation.utils.ContentCreationRemoteConfigManager
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.stories.creation.builder.StoriesCreationModelBuilder
import com.tokopedia.stories.creation.di.DaggerStoriesCreationTestComponent
import com.tokopedia.stories.creation.di.StoriesCreationInjector
import com.tokopedia.stories.creation.di.StoriesCreationTestModule
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.view.activity.StoriesCreationActivity
import com.tokopedia.stories.creation.view.factory.StoriesMediaFactory
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 24, 2023
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class StoriesCreationPreparationAnalyticTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<StoriesCreationActivity>()

    @get:Rule
    val cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** Helper */
    private val robot = StoriesCreationPreparationRobot(targetContext, composeTestRule, cassavaTestRule)

    /** Builder */
    private val storiesCreationModelBuilder = StoriesCreationModelBuilder()

    /** Mock Class */
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockRepository: StoriesCreationRepository = mockk(relaxed = true)
    private val mockCreationUploader: CreationUploader = mockk(relaxed = true)
    private val mockVideoSnapshotHelper: VideoSnapshotHelper = mockk(relaxed = true)
    private val mockContentCreationRemoteConfig: ContentCreationRemoteConfigManager = mockk(relaxed = true)

    /** Mock Data */
    private val mockAccountList = storiesCreationModelBuilder.buildAccountList()
    private val mockAccount = mockAccountList[0]
    private val mockStoriesInfo = storiesCreationModelBuilder.buildStoriesInfo()

    init {
        mockkObject(StoriesMediaFactory)
        coEvery { StoriesMediaFactory.get() } returns StoriesMedia("asdf", ContentMediaType.Image)
        coEvery { mockRepository.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepository.getStoryPreparationInfo(any()) } returns mockStoriesInfo
        coEvery { mockVideoSnapshotHelper.snapVideoBitmap(any(), any()) } returns Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        coEvery { mockContentCreationRemoteConfig.isShowingCreation() } returns true

        StoriesCreationInjector.set(
            DaggerStoriesCreationTestComponent
                .builder()
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .storiesCreationTestModule(
                    StoriesCreationTestModule(
                        context = targetContext,
                        mockUserSession = mockUserSession,
                        mockRepository = mockRepository,
                        mockCreationUploader = mockCreationUploader,
                        mockContentCreationRemoteConfig = mockContentCreationRemoteConfig,
                        mockVideoSnapshotHelper = mockVideoSnapshotHelper,
                    )
                )
                .build()
        )
    }

    @Test
    fun `testAnalytic_storiesCreation_preparation`() {

        robot
            .performDelay()
            .verifyOpenScreen("/play broadcast story - ${mockAccount.id} - seller - review page post creation - ${mockStoriesInfo.storiesId}")

            .clickAddProduct()
            .performDelay()
            .verifyAction("click - tambah product post creation page")

            .back()
            .performDelay()
            .clickUpload()
            .verifyAction("click - upload")
    }
}
