package com.tokopedia.people.viewmodel.userprofile.review

import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.review.UserReviewModelBuilder
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.andThen
import com.tokopedia.people.util.assertEmpty
import com.tokopedia.people.util.assertEvent
import com.tokopedia.people.util.assertFalse
import com.tokopedia.people.util.assertTrue
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on June 05, 2023
 */
class UserProfileReviewInteractionViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val userReviewModelBuilder = UserReviewModelBuilder()

    private val mockUserId = "1"
    private val mockOwnUsername = "jonathandarwin"
    private val mockException = commonModelBuilder.buildException()
    private val mockLike = userReviewModelBuilder.buildLikeDislike(isLike = true)
    private val mockUnlike = userReviewModelBuilder.buildLikeDislike(isLike = false)

    private val mockReviewSettingsEnabled = userReviewModelBuilder.buildReviewSetting(isEnabled = true)
    private val mockReviewContentUnlike = userReviewModelBuilder.buildReviewList(isLike = false)
    private val mockReviewContentLike = userReviewModelBuilder.buildReviewList(isLike = true)

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsEnabled
        coEvery { mockRepo.getUserReviewList(any(), any(), any()) } returns mockReviewContentUnlike
    }

    /** Like Dislike */
    @Test
    fun `ClickLikeReview - like review`() {
        coEvery { mockRepo.setLikeStatus(any(), any()) } returns mockLike

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = mockReviewContentUnlike.reviewList[0]))
            } andThen {
                reviewContent.reviewList[0].likeDislike.isLike.assertTrue()
            }
        }
    }

    @Test
    fun `ClickLikeReview - unlike review`() {
        coEvery { mockRepo.setLikeStatus(any(), any()) } returns mockUnlike
        coEvery { mockRepo.getUserReviewList(any(), any(), any()) } returns mockReviewContentLike

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = mockReviewContentLike.reviewList[0]))
            } andThen {
                reviewContent.reviewList[0].likeDislike.isLike.assertFalse()
            }
        }
    }

    @Test
    fun `ClickLikeReview - like and unlike review`() {

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = mockReviewContentUnlike.reviewList[0]))
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = mockReviewContentUnlike.reviewList[0]))
            } andThen {
                reviewContent.reviewList[0].likeDislike.isLike.assertFalse()
                coVerify(exactly = 0) { mockRepo.setLikeStatus(any(), any()) }
            }
        }
    }

    @Test
    fun `ClickLikeReview - like review and error network happens`() {

        coEvery { mockRepo.setLikeStatus(any(), any()) } throws mockException

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = mockReviewContentUnlike.reviewList[0]))
            } andThen {
                reviewContent.reviewList[0].likeDislike.isLike.assertFalse()
            }
        }
    }

    @Test
    fun `ClickLikeReview - like review and error response unexpected`() {

        coEvery { mockRepo.setLikeStatus(any(), any()) } returns mockUnlike

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = mockReviewContentUnlike.reviewList[0]))
            } andThen {
                reviewContent.reviewList[0].likeDislike.isLike.assertFalse()
            }
        }
    }

    @Test
    fun `ClickLikeReview - review not found`() {

        coEvery { mockRepo.setLikeStatus(any(), any()) } returns mockLike

        val randomReview = mockReviewContentUnlike.reviewList[0].copy(feedbackID = "asdfasdf")

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickLikeReview(review = randomReview))
            } andThen {
                reviewContent.equalTo(mockReviewContentUnlike)
            }
        }
    }

    /** Click Review Text See More */
    @Test
    fun `ClickReviewTextSeeMore - expand review text`() {
        val selectedIdx = 0

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickReviewTextSeeMore(review = mockReviewContentUnlike.reviewList[selectedIdx]))
            } andThen {
                reviewContent.reviewList.forEachIndexed { idx, e ->
                    if (idx == selectedIdx) {
                        e.isReviewTextExpanded.assertTrue()
                    } else {
                        e.isReviewTextExpanded.assertFalse()
                    }
                }
            }
        }
    }

    @Test
    fun `ClickReviewTextSeeMore - review is not found`() {
        val randomReview = mockReviewContentUnlike.reviewList[0].copy(feedbackID = "asdfasdf")

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(UserProfileAction.ClickReviewTextSeeMore(review = randomReview))
            } andThen {
                reviewContent.reviewList.forEach { it.isReviewTextExpanded.assertFalse() }
            }
        }
    }

    /** Click Product Info */
    @Test
    fun `ClickProductInfo - should emit event to open pdp page`() {
        val selectedIdx = 0

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordEvent {
                viewModel.submitAction(UserProfileAction.ClickProductInfo(review = mockReviewContentUnlike.reviewList[selectedIdx]))
            } andThen {
                last().assertEvent(UserProfileUiEvent.OpenProductDetailPage(mockReviewContentUnlike.reviewList[selectedIdx].product.productID))
            }
        }
    }

    /** Click Review Media */
    @Test
    fun `ClickReviewMedia - emit event open review media gallery page`() {
        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordEvent {
                viewModel.submitAction(UserProfileAction.ClickReviewMedia(
                    feedbackID = mockReviewContentUnlike.reviewList[0].feedbackID,
                    attachment = mockReviewContentUnlike.reviewList[0].attachments[0],
                ))
            } andThen {
                last().assertEvent(UserProfileUiEvent.OpenReviewMediaGalleryPage(
                    review = mockReviewContentUnlike.reviewList[0],
                    mediaPosition = 1
                ))
            }
        }
    }

    @Test
    fun `ClickReviewMedia - review is not found`() {
        val randomReview = mockReviewContentUnlike.reviewList[0].copy(feedbackID = "asdfasdf")

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordEvent {
                viewModel.submitAction(UserProfileAction.ClickReviewMedia(
                    feedbackID = randomReview.feedbackID,
                    attachment = randomReview.attachments[0],
                ))
            } andThen {
                assertEmpty()
            }
        }
    }
}
