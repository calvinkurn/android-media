package com.tokopedia.people.viewmodel.userprofile.review

import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.review.UserReviewModelBuilder
import com.tokopedia.people.model.shoprecom.ShopRecomModelBuilder
import com.tokopedia.people.model.userprofile.FollowInfoUiModelBuilder
import com.tokopedia.people.model.userprofile.ProfileUiModelBuilder
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.andThen
import com.tokopedia.people.util.assertEmpty
import com.tokopedia.people.util.assertEvent
import com.tokopedia.people.util.assertFalse
import com.tokopedia.people.util.assertTrue
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.utils.UserProfileSharedPref
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.mapper.UserProfileLikeStatusMapper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on June 05, 2023
 */
class UserProfileReviewTabViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockUserProfileSharedPref: UserProfileSharedPref = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val profileBuilder = ProfileUiModelBuilder()
    private val followInfoBuilder = FollowInfoUiModelBuilder()
    private val shopRecomBuilder = ShopRecomModelBuilder()
    private val userReviewModelBuilder = UserReviewModelBuilder()

    private val mockUserId = "1"
    private val mockOtherUserId = "2"
    private val mockOwnUsername = "jonathandarwin"
    private val mockOtherUsername = "yanglainlain"
    private val mockException = commonModelBuilder.buildException()

    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOtherProfile = profileBuilder.buildProfile(userID = mockOtherUserId)
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockShopRecom = shopRecomBuilder.buildModelIsShown(nextCursor = "")
    private val mockReviewSettingsEnabled = userReviewModelBuilder.buildReviewSetting(isEnabled = true)
    private val mockReviewSettingsDisabled = userReviewModelBuilder.buildReviewSetting(isEnabled = false)
    private val mockReviewContent = userReviewModelBuilder.buildReviewList()
    private val mockReviewContentLastPage = userReviewModelBuilder.buildReviewList(hasNext = false)

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile
        coEvery { mockRepo.getProfile(mockOtherUsername) } returns mockOtherProfile
        coEvery { mockRepo.getFollowInfo(listOf(mockUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecom

        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsEnabled
        coEvery { mockRepo.getUserReviewList(any(), any(), any()) } returns mockReviewContent

        coEvery { mockUserProfileSharedPref.hasBeenShown(UserProfileSharedPref.Key.ReviewOnboarding) } returns true
    }


    @Test
    fun `LoadUserReview - refresh content`() {
        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
                viewModel.submitAction(UserProfileAction.LoadUserReview())
                viewModel.submitAction(UserProfileAction.LoadUserReview())
            } andThen {
                reviewContent.reviewList.size.equalTo(mockReviewContent.reviewList.size * 3)
                reviewContent.reviewList.equalTo(mockReviewContent.reviewList + mockReviewContent.reviewList + mockReviewContent.reviewList)
            }

            recordState {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } andThen {
                reviewSettings.isEnabled.assertTrue()

                reviewContent.isLoading.assertFalse()
                reviewContent.page.equalTo(mockReviewContent.page)
                reviewContent.reviewList.size.equalTo(mockReviewContent.reviewList.size)
                reviewContent.reviewList.equalTo(mockReviewContent.reviewList)
                reviewContent.status.equalTo(mockReviewContent.status)
                reviewContent.hasNext.equalTo(mockReviewContent.hasNext)
            }
        }
    }

    @Test
    fun `LoadUserReview - load next page and exists`() {
        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
                viewModel.submitAction(UserProfileAction.LoadUserReview())
            } andThen {
                reviewSettings.isEnabled.assertTrue()

                reviewContent.isLoading.assertFalse()
                reviewContent.page.equalTo(mockReviewContent.page)
                reviewContent.reviewList.size.equalTo(mockReviewContent.reviewList.size * 2)
                reviewContent.reviewList.equalTo(mockReviewContent.reviewList + mockReviewContent.reviewList)
                reviewContent.status.equalTo(mockReviewContent.status)
                reviewContent.hasNext.equalTo(mockReviewContent.hasNext)
            }
        }
    }

    @Test
    fun `LoadUserReview - load next page on last page`() {

        coEvery { mockRepo.getUserReviewList(any(), any(), any()) } returns mockReviewContentLastPage

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
                viewModel.submitAction(UserProfileAction.LoadUserReview())
            } andThen {
                reviewSettings.isEnabled.assertTrue()

                reviewContent.isLoading.assertFalse()
                reviewContent.page.equalTo(mockReviewContent.page)
                reviewContent.reviewList.size.equalTo(mockReviewContent.reviewList.size)
                reviewContent.reviewList.equalTo(mockReviewContent.reviewList)
                reviewContent.status.equalTo(mockReviewContent.status)
                reviewContent.hasNext.assertFalse()
            }
        }
    }

    @Test
    fun `LoadUserReview - load page and error`() {

        coEvery { mockRepo.getUserReviewList(any(), any(), any()) } throws mockException

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordStateAndEvent {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } andThen { state, events ->
                state.reviewContent.page.equalTo(1)
                state.reviewContent.reviewList.size.equalTo(0)
                state.reviewContent.reviewList.equalTo(emptyList())
                state.reviewContent.status.equalTo(UserReviewUiModel.Status.Error)
                state.reviewContent.hasNext.assertTrue()

                events.last().assertEvent(UserProfileUiEvent.ErrorLoadReview(mockException))
            }
        }
    }

    @Test
    fun `LoadUserReview - load next page and error`() {

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordStateAndEvent {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))

                coEvery { mockRepo.getUserReviewList(any(), any(), any()) } throws mockException
                viewModel.submitAction(UserProfileAction.LoadUserReview())
            } andThen { state, events ->
                state.reviewSettings.isEnabled.assertTrue()

                state.reviewContent.isLoading.assertFalse()
                state.reviewContent.page.equalTo(mockReviewContent.page)
                state.reviewContent.reviewList.size.equalTo(mockReviewContent.reviewList.size)
                state.reviewContent.reviewList.equalTo(mockReviewContent.reviewList)
                state.reviewContent.status.equalTo(UserReviewUiModel.Status.Error)
                state.reviewContent.hasNext.assertTrue()

                events.last().assertEvent(UserProfileUiEvent.ErrorLoadReview(mockException))
            }
        }
    }

    @Test
    fun `LoadUserReview - load page and review setting is hidden`() {

        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsDisabled

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } andThen {
                reviewSettings.isEnabled.assertFalse()

                reviewContent.equalTo(UserReviewUiModel.Empty)
            }
        }
    }

    @Test
    fun `UpdateLikeStatus - success`() {

        val currentLikeDislike = mockReviewContent.reviewList.first().likeDislike

        val expectedIsLike = !currentLikeDislike.isLike
        val expectedLikeStatus = UserProfileLikeStatusMapper.getLikeStatus(expectedIsLike)
        val expectedTotalLike = when (currentLikeDislike.isLike) {
            true -> currentLikeDislike.totalLike - 1
            false -> currentLikeDislike.totalLike + 1
        }

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(
                    UserProfileAction.UpdateLikeStatus(
                        feedbackId = mockReviewContent.reviewList.first().feedbackID,
                        likeStatus = expectedLikeStatus
                    )
                )
            } andThen {
                reviewContent.reviewList.first().likeDislike.totalLike.equalTo(expectedTotalLike)
                reviewContent.reviewList.first().likeDislike.isLike.equalTo(expectedIsLike)
            }
        }
    }

    @Test
    fun `UpdateLikeStatus - review not found`() {

        val currentLikeDislike = mockReviewContent.reviewList.first().likeDislike
        val randomFeedbackId = "kasdjfklajsdf"

        val oldIsLike = currentLikeDislike.isLike
        val oldTotalLike = currentLikeDislike.totalLike
        val expectedLikeStatus = UserProfileLikeStatusMapper.getLikeStatus(!currentLikeDislike.isLike)

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(
                    UserProfileAction.UpdateLikeStatus(
                        feedbackId = randomFeedbackId,
                        likeStatus = expectedLikeStatus
                    )
                )
            } andThen {
                reviewContent.reviewList.first().likeDislike.totalLike.equalTo(oldTotalLike)
                reviewContent.reviewList.first().likeDislike.isLike.equalTo(oldIsLike)
            }
        }
    }

    @Test
    fun `UpdateLikeStatus - review like status is already the same`() {

        val currentLikeDislike = mockReviewContent.reviewList.first().likeDislike

        val expectedIsLike = currentLikeDislike.isLike
        val expectedLikeStatus = UserProfileLikeStatusMapper.getLikeStatus(expectedIsLike)
        val expectedTotalLike = currentLikeDislike.totalLike

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            setup {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
            } recordState {
                viewModel.submitAction(
                    UserProfileAction.UpdateLikeStatus(
                        feedbackId = mockReviewContent.reviewList.first().feedbackID,
                        likeStatus = expectedLikeStatus
                    )
                )
            } andThen {
                reviewContent.reviewList.first().likeDislike.totalLike.equalTo(expectedTotalLike)
                reviewContent.reviewList.first().likeDislike.isLike.equalTo(expectedIsLike)
            }
        }
    }

    @Test
    fun `Public Getter - firstName with 1 words`() {

        val mockName = "Jonathan"
        val expectedFirstName = getFirstName(mockName)

        val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId, name = mockName)
        coEvery { mockRepo.getProfile(any()) } returns mockOwnProfile

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                viewModel.firstName.equalTo(expectedFirstName)
            }
        }
    }

    @Test
    fun `Public Getter - firstName with 2 words`() {

        val mockName = "Jonathan Darwin"
        val expectedFirstName = getFirstName(mockName)

        val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId, name = mockName)
        coEvery { mockRepo.getProfile(any()) } returns mockOwnProfile

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                viewModel.firstName.equalTo(expectedFirstName)
            }
        }
    }

    @Test
    fun `Public Getter - firstName is empty`() {

        val mockName = ""
        val expectedFirstName = getFirstName(mockName)

        val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId, name = mockName)
        coEvery { mockRepo.getProfile(any()) } returns mockOwnProfile

        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
        ).start {
            recordState {
                viewModel.submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                viewModel.firstName.equalTo(expectedFirstName)
            }
        }
    }

    private fun getFirstName(name: String) = name.trim().split(" ").firstOrNull().orEmpty()
}
