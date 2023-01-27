package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.createpost.common.domain.entity.SubmitPostData
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.data.pojo.FeedXTrackViewerResponse
import com.tokopedia.feedcomponent.data.pojo.PostUpcomingCampaign
import com.tokopedia.feedcomponent.data.pojo.UpcomingCampaignResponse
import com.tokopedia.feedcomponent.data.pojo.VisitChannelTracking
import com.tokopedia.feedcomponent.data.pojo.shopmutation.FollowShop
import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.PostUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.feedcomponent.people.model.*
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.shoprecom.model.FeedXRecomWidget
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomItem
import com.tokopedia.feedcomponent.shoprecom.model.UserShopRecomModel
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedAsgcCampaignResponseModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedWidgetData
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopModel
import com.tokopedia.kolcommon.data.SubmitActionContentResponse
import com.tokopedia.kolcommon.data.SubmitReportContentResponse
import com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.tokopedia.feedplus.helper.assertEqualTo
import com.tokopedia.tokopedia.feedplus.helper.assertFalse
import com.tokopedia.tokopedia.feedplus.helper.assertTrue
import com.tokopedia.tokopedia.feedplus.helper.assertType
import com.tokopedia.tokopedia.feedplus.robot.create
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 23/09/22
 */
@ExperimentalCoroutinesApi
class FeedViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val mockReport: SubmitReportContentUseCase = mockk(relaxed = true)
    private val mockTrackChannel: FeedBroadcastTrackerUseCase = mockk(relaxed = true)
    private val mockTrackViewer: FeedXTrackViewerUseCase = mockk(relaxed = true)
    private val mockLike: SubmitLikeContentUseCase = mockk(relaxed = true)
    private val mockDelete: SubmitActionContentUseCase = mockk(relaxed = true)
    private val mockTopAdsTracker: SendTopAdsUseCase = mockk(relaxed = true)
    private val mockFollowShop: ShopFollowUseCase = mockk(relaxed = true)
    private val mockFollowKol: ProfileFollowUseCase = mockk(relaxed = true)
    private val mockUnFollowKol: ProfileUnfollowedUseCase = mockk(relaxed = true)
    private val mockDynamicFeed: GetDynamicFeedNewUseCase = mockk(relaxed = true)
    private val mockShopRecom: ShopRecomUseCase = mockk(relaxed = true)
    private val mockAtc: AddToCartUseCase = mockk(relaxed = true)
    private val mockPlayWidget: PlayWidgetTools = mockk(relaxed = true)
    private val mockPostReminderCampaign: PostUpcomingCampaignReminderUseCase = mockk(relaxed = true)

    private val gqlFailed = MessageErrorException("ooPs")

    /**
     * Send Report
     */
    @Test
    fun `send report - success`() {
        val expected = SubmitReportContentResponse(
            content = SubmitReportContentResponse.FeedReportSubmit(
                data = SubmitReportContentResponse.FeedReportData(success = 1)
            )
        )

        coEvery { mockReport.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, sendReportUseCase = mockReport)
            .use {
                it.vm.sendReport(1, "1", "", "")
                it.vm.reportResponse.getOrAwaitValue()
                    .assertType<Success<DeletePostModel>> { dt ->
                        dt.data.errorMessage.assertEqualTo(expected.content.errorMessage)
                        dt.data.isSuccess.assertTrue()
                    }
            }
    }

    @Test
    fun `send report - failed from response`() {
        val expected = SubmitReportContentResponse(
            content = SubmitReportContentResponse.FeedReportSubmit(
                data = SubmitReportContentResponse.FeedReportData(success = 200),
                errorMessage = "OOps"
            )
        )

        coEvery { mockReport.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, sendReportUseCase = mockReport)
            .use {
                it.vm.sendReport(1, "1", "", "")
                it.vm.reportResponse.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.message.assertEqualTo(expected.content.errorMessage)
                }
            }
    }

    @Test
    fun `send report - failed throw from gql`() {
        coEvery { mockReport.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, sendReportUseCase = mockReport)
            .use {
                it.vm.sendReport(1, "1", "", "")
                it.vm.reportResponse.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     * Track Visit Channel
     */

    @Test
    fun `track visit channel - success`() {
        val expected =
            VisitChannelTracking.Response(reportVisitChannelTracking = VisitChannelTracking(success = true))

        coEvery { mockTrackChannel.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, trackVisitChannelBroadcasterUseCase = mockTrackChannel)
            .use {
                it.vm.trackVisitChannel("", 1)
                it.vm.viewTrackResponse.getOrAwaitValue().assertType<Success<ViewsKolModel>> { dt ->
                    dt.data.isSuccess.assertTrue()
                }
            }
    }

    @Test
    fun `track visit channel - failed throw from gql`() {
        coEvery { mockTrackChannel.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, trackVisitChannelBroadcasterUseCase = mockTrackChannel)
            .use {
                it.vm.trackVisitChannel("", 1)
                it.vm.viewTrackResponse.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     * Track Long Video
     */

    @Test
    fun `track long video - success`() {
        val expected = FeedXTrackViewerResponse.Response(
            feedXTrackViewerResponse = FeedXTrackViewerResponse(success = true)
        )

        coEvery { mockTrackViewer.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, feedXTrackViewerUseCase = mockTrackViewer)
            .use {
                it.vm.trackLongVideoView("", 1)
                it.vm.longVideoViewTrackResponse.getOrAwaitValue()
                    .assertType<Success<ViewsKolModel>> { dt ->
                        dt.data.isSuccess.assertTrue()
                    }
            }
    }

    @Test
    fun `track long video - failed throw from gql`() {
        coEvery { mockTrackViewer.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, feedXTrackViewerUseCase = mockTrackViewer)
            .use {
                it.vm.trackLongVideoView("", 1)
                it.vm.longVideoViewTrackResponse.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     * Follow Kol - havent handle already follow?
     */
    @Test
    fun `follow kol - success`() {
        val expected = ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "200", relation = ""),
                messages = emptyList(), errorCode = ""
            )
        )

        coEvery { mockFollowKol.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, doFollowUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKol("1", 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol - success but failed`() {
        val expected = ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "200", relation = ""),
                messages = listOf("Error aja"), errorCode = "404"
            )
        )

        coEvery { mockFollowKol.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, doFollowUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKol("1", 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol - error`() {
        coEvery { mockFollowKol.executeOnBackground(any()) } throws gqlFailed
        create(dispatcher = testDispatcher, doFollowUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKol("1", 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<Exception> { }
                }
            }
    }

    /**
     * Unfollow Kol - havent handle already unfollow?
     */
    @Test
    fun `unfollow kol - success`() {
        val expected = ProfileDoUnFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "1", relation = ""),
                messages = listOf("Yeay, success"), errorCode = ""
            )
        )

        coEvery { mockUnFollowKol.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, doUnfollowUseCase = mockUnFollowKol)
            .use {
                it.vm.doUnfollowKol("1", 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.isFollow.assertFalse()
                    dt.data.status.assertEqualTo(0)
                }
            }
    }

    @Test
    fun `unfollow kol - success but failed`() {
        val expected = ProfileDoUnFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "200", relation = ""),
                messages = listOf("Error Aja"), errorCode = "404"
            )
        )

        coEvery { mockUnFollowKol.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, doUnfollowUseCase = mockUnFollowKol)
            .use {
                it.vm.doUnfollowKol("1", 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                }
            }
    }

    @Test
    fun `unfollow kol - error`() {
        val expected = MessageErrorException("Error Aja")

        coEvery { mockUnFollowKol.executeOnBackground(any()) } throws expected

        create(dispatcher = testDispatcher, doUnfollowUseCase = mockUnFollowKol)
            .use {
                it.vm.doUnfollowKol("1", 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<Exception> { }
                }
            }
    }

    /**
     * Like KOL
     */

    @Test
    fun `like kol - success`() {
        val expected = LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                data = LikeKolPostData.LikeKolPostSuccessData(success = 1)
            )
        )

        coEvery { mockLike.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, likeKolPostUseCase = mockLike)
            .use {
                it.vm.doLikeKol(1, 1)
                it.vm.likeKolResp.getOrAwaitValue()
                    .assertType<Success<LikeKolViewModel>> { dt ->
                        dt.data.isSuccess.assertEqualTo(expected.doLikeKolPost.data.success == SubmitLikeContentUseCase.SUCCESS)
                        dt.data.isSuccess.assertTrue()
                    }
            }
    }

    @Test
    fun `like kol - failed from response`() {
        val expected = LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                data = LikeKolPostData.LikeKolPostSuccessData(success = 200)
            )
        )

        coEvery { mockLike.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, likeKolPostUseCase = mockLike)
            .use {
                it.vm.doLikeKol(1, 1)
                it.vm.likeKolResp.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertType<CustomUiMessageThrowable> { msg ->
                        msg.errorMessageId.assertEqualTo(R.string.feed_like_error_message)
                    }
                }
            }
    }

    @Test
    fun `like kol - failed throw from gql`() {
        coEvery { mockLike.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, likeKolPostUseCase = mockLike)
            .use {
                it.vm.doLikeKol(1, 1)
                it.vm.likeKolResp.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     * UnLike KOL
     */

    @Test
    fun `Unlike kol - success`() {
        val expected = LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                data = LikeKolPostData.LikeKolPostSuccessData(success = 1)
            )
        )

        coEvery { mockLike.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, likeKolPostUseCase = mockLike)
            .use {
                it.vm.doUnlikeKol(1, 1)
                it.vm.likeKolResp.getOrAwaitValue()
                    .assertType<Success<LikeKolViewModel>> { dt ->
                        dt.data.isSuccess.assertEqualTo(expected.doLikeKolPost.data.success == SubmitLikeContentUseCase.SUCCESS)
                        dt.data.isSuccess.assertTrue()
                    }
            }
    }

    @Test
    fun `Unlike kol - failed from response`() {
        val expected = LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                data = LikeKolPostData.LikeKolPostSuccessData(success = 200)
            )
        )

        coEvery { mockLike.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, likeKolPostUseCase = mockLike)
            .use {
                it.vm.doUnlikeKol(1, 1)
                it.vm.likeKolResp.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertType<MessageErrorException> { }
                }
            }
    }

    @Test
    fun `Unlike kol - failed throw from gql`() {
        coEvery { mockLike.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, likeKolPostUseCase = mockLike)
            .use {
                it.vm.doUnlikeKol(1, 1)
                it.vm.likeKolResp.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     * follow Kol  from rec- havent handle already unfollow?
     */
    @Test
    fun `follow kol from rec - success`() {
        val expected = ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "200", relation = ""),
                messages = emptyList(), errorCode = ""
            )
        )

        coEvery { mockFollowKol.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, doFollowUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKolFromRecommendation("1", 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.isFollow.assertTrue()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol from rec - success but failed`() {
        val expected = ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "", relation = ""),
                messages = listOf("Error aja"), errorCode = "404"
            )
        )

        coEvery { mockFollowKol.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, doFollowUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKolFromRecommendation("1", 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.isFollow.assertTrue()
                    dt.data.status.assertEqualTo(1)
                }
            }
    }

    @Test
    fun `follow kol from rec - error`() {
        coEvery { mockFollowKol.executeOnBackground(any()) } throws gqlFailed
        create(dispatcher = testDispatcher, doFollowUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKolFromRecommendation("1", 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<Exception> {
                        it.assertEqualTo(gqlFailed)
                    }
                }
            }
    }

    /**
     * unfollow Kol  from rec- havent handle already unfollow?
     */
    @Test
    fun `unfollow kol from rec - success`() {
        val expected = ProfileDoUnFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "1", relation = ""),
                messages = listOf("Yeay, success"), errorCode = ""
            )
        )

        coEvery { mockUnFollowKol.executeOnBackground(any()) } returns expected
        create(dispatcher = testDispatcher, doUnfollowUseCase = mockUnFollowKol)
            .use {
                it.vm.doUnfollowKolFromRecommendation("1", 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.isFollow.assertFalse()
                    dt.data.status.assertEqualTo(0)
                }
            }
    }

    @Test
    fun `unfollow kol from rec - success but failed`() {
        val expected = ProfileDoUnFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                data =
                ProfileDoFollowedDataVal(userIdSource = "", userIdTarget = "", isSuccess = "200", relation = ""),
                messages = listOf("Error aja"), errorCode = "404"
            )
        )

        coEvery { mockUnFollowKol.executeOnBackground(any()) } returns expected
        create(dispatcher = testDispatcher, doUnfollowUseCase = mockUnFollowKol)
            .use {
                it.vm.doUnfollowKolFromRecommendation("1", 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.isFollow.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                }
            }
    }

    @Test
    fun `unfollow kol from rec - error`() {
        coEvery { mockUnFollowKol.executeOnBackground(any()) } throws gqlFailed
        create(dispatcher = testDispatcher, doUnfollowUseCase = mockUnFollowKol)
            .use {
                it.vm.doUnfollowKolFromRecommendation("1", 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<Exception> {
                        it.assertEqualTo(gqlFailed)
                    }
                }
            }
    }

    /**
     * Delete Post
     */

    @Test
    fun `delete post - success`() {
        val expected = SubmitActionContentResponse(
            content = SubmitActionContentResponse.FeedContentSubmit(success = 1)
        )

        coEvery { mockDelete.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, deletePostUseCase = mockDelete)
            .use {
                it.vm.doDeletePost("1", 1)
                it.vm.deletePostResp.getOrAwaitValue()
                    .assertType<Success<DeletePostModel>> { dt ->
                        dt.data.isSuccess.assertEqualTo(expected.content.success == SubmitPostData.SUCCESS)
                        dt.data.isSuccess.assertTrue()
                    }
            }
    }

    @Test
    fun `delete post - failed from response`() {
        val expected = SubmitActionContentResponse(
            content = SubmitActionContentResponse.FeedContentSubmit(success = 200)
        )

        coEvery { mockDelete.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, deletePostUseCase = mockDelete)
            .use {
                it.vm.doDeletePost("", 1)
                it.vm.deletePostResp.getOrAwaitValue()
                    .assertType<Fail> { dt ->
                        dt.throwable.assertType<MessageErrorException> { }
                    }
            }
    }

    @Test
    fun `delete post - failed throw from gql`() {
        coEvery { mockDelete.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, deletePostUseCase = mockDelete)
            .use {
                it.vm.doDeletePost("", 1)
                it.vm.deletePostResp.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    @Test
    fun `top ads - when topads is clicked hit tracker hitClick`() {
        create(dispatcher = testDispatcher, sendTopAdsUseCase = mockTopAdsTracker)
            .use {
                it.vm.doTopAdsTracker(
                    url = "",
                    shopId = "",
                    shopName = "",
                    imageUrl = "",
                    isClick = true
                )
                verify { mockTopAdsTracker.hitClick(any(), any(), any(), any()) }
            }
    }

    @Test
    fun `top ads - when topads is not clicked hit tracker impression`() {
        create(dispatcher = testDispatcher, sendTopAdsUseCase = mockTopAdsTracker)
            .use {
                it.vm.doTopAdsTracker(
                    url = "",
                    shopId = "",
                    shopName = "",
                    imageUrl = "",
                    isClick = false
                )
                verify { mockTopAdsTracker.hitImpressions(any(), any(), any(), any()) }
            }
    }

    /**
     * favorite shop
     */

    @Test
    fun `fav shop - success`() {
        val expected = ShopFollowModel(followShop = FollowShop(success = true))
        coEvery { mockFollowShop.executeOnBackground(any()) } returns expected

        create(dispatcher = testDispatcher, shopFollowUseCase = mockFollowShop)
            .use {
                it.vm.doFavoriteShop(Data(), 1)
                it.vm.doFavoriteShopResp.getOrAwaitValue().assertType<Success<FeedPromotedShopModel>> {
                        dt ->
                    dt.data.adapterPosition.assertEqualTo(1)
                    dt.data.isSuccess.assertEqualTo(expected.followShop.success)
                    dt.data.isSuccess.assertTrue()
                }
            }
    }

    @Test
    fun `fav shop - failed from gql`() {
        coEvery { mockFollowShop.executeOnBackground(any()) } throws gqlFailed

        create(dispatcher = testDispatcher, shopFollowUseCase = mockFollowShop)
            .use {
                it.vm.doFavoriteShop(Data(), 1)
                it.vm.doFavoriteShopResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                    dt.throwable.assertType<MessageErrorException> { }
                }
            }
    }

    /**
     * Fetch latest widget
     */

    @Test
    fun `fetch latest widget success not empty`() {
        val expected = FeedXData(feedXHome = FeedXHome(items = listOf(FeedXCard(), FeedXCard()), mods = emptyList(), pagination = FeedXPaginationInfo(cursor = "", hasNext = false, totalData = 0)))
        coEvery { mockDynamicFeed.executeForCDP(any(), any(), any()) } returns expected

        create(dispatcher = testDispatcher, getDynamicFeedNewUseCase = mockDynamicFeed)
            .use {
                it.vm.fetchLatestFeedPostWidgetData("1", 1)
                it.vm.feedWidgetLatestData.getOrAwaitValue().assertType<Success<FeedWidgetData>> {
                        dt ->
                    dt.data.feedXCard.assertEqualTo(expected.feedXHome.items.first())
                }
            }
    }

    @Test
    fun `fetch latest widget success  empty`() {
        val expected = FeedXData(feedXHome = FeedXHome(items = emptyList(), mods = emptyList(), pagination = FeedXPaginationInfo(cursor = "", hasNext = false, totalData = 0)))
        coEvery { mockDynamicFeed.executeForCDP(any(), any(), any()) } returns expected

        create(dispatcher = testDispatcher, getDynamicFeedNewUseCase = mockDynamicFeed)
            .use {
                it.vm.fetchLatestFeedPostWidgetData("1", 1)
                it.vm.feedWidgetLatestData.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<CustomUiMessageThrowable> { it.errorMessageId.assertEqualTo(R.string.feed_result_empty) }
                }
            }
    }

    @Test
    fun `fetch latest widget failed`() {
        coEvery { mockDynamicFeed.executeForCDP(any(), any(), any()) } throws gqlFailed

        create(dispatcher = testDispatcher, getDynamicFeedNewUseCase = mockDynamicFeed)
            .use {
                it.vm.fetchLatestFeedPostWidgetData("1", 1)
                it.vm.feedWidgetLatestData.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<CustomUiMessageThrowable> { it.errorMessageId.assertEqualTo(R.string.feed_result_empty) }
                }
            }
    }

    /**
     * shop recom
     */

    @Test
    fun `shop recom is shown`() {
        val expected = UserShopRecomModel(feedXRecomWidget = FeedXRecomWidget(isShown = true, items = listOf(ShopRecomItem())))
        coEvery { mockShopRecom.executeOnBackground(any(), any(), any()) } returns expected

        create(dispatcher = testDispatcher, shopRecomUseCase = mockShopRecom)
            .use {
                it.vm.getShopRecomWidget("")
                it.vm.shopRecom.value.shopRecomUiModel.isShown.assertTrue()
                it.vm.shopRecom.value.shopRecomUiModel.items.isNotEmpty().assertTrue()
            }
    }

    @Test
    fun `shop recom is not shown`() {
        val expected = UserShopRecomModel(feedXRecomWidget = FeedXRecomWidget(isShown = false, items = listOf(ShopRecomItem())))
        coEvery { mockShopRecom.executeOnBackground(any(), any(), any()) } returns expected

        create(dispatcher = testDispatcher, shopRecomUseCase = mockShopRecom)
            .use {
                it.vm.getShopRecomWidget("")
                it.vm.shopRecom.value.shopRecomUiModel.isShown.assertFalse()
            }
    }

    @Test
    fun `shop recom is failed`() {
        coEvery { mockShopRecom.executeOnBackground(any(), any(), any()) } throws gqlFailed
        create(dispatcher = testDispatcher, shopRecomUseCase = mockShopRecom)
            .use {
                it.vm.getShopRecomWidget("1")
                it.vm.shopRecom.value.onError.assertEqualTo(gqlFailed.message)
            }
    }

    /**
     * atc
     */

    @Test
    fun `atc gql return not success`() {
        val expected = AddToCartDataModel(status = "NOT FOUND", data = DataModel(success = 0), errorMessage = arrayListOf("Error"))
        coEvery { mockAtc.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, atcUseCase = mockAtc)
            .use {
                it.vm.addtoCartProduct(FeedXProduct(), "1", "1", true, "")
                it.vm.atcResp.getOrAwaitValue().assertType<Fail> {}
            }
    }

    /**
     * auto refresh
     */

    @Test
    fun `auto refresh play widget - success`() {
        val expected = PlayWidget()

        coEvery { mockPlayWidget.mapWidgetToModel(any(), any(), any()) } returns PlayWidgetState()
        coEvery { mockPlayWidget.getWidgetFromNetwork(any(), testDispatcher.io) } returns expected

        create(dispatcher = testDispatcher, playWidgetTools = mockPlayWidget)
            .use {
                it.vm.doAutoRefreshPlayWidget()
                it.vm.playWidgetModel.getOrAwaitValue().assertType<Success<CarouselPlayCardModel>> {}
            }
    }

    @Test
    fun `auto refresh play widget - failed`() {
        coEvery { mockPlayWidget.getWidgetFromNetwork(any(), any()) } throws gqlFailed

        create(dispatcher = testDispatcher, playWidgetTools = mockPlayWidget)
            .use {
                it.vm.doAutoRefreshPlayWidget()
                it.vm.playWidgetModel.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     * set - unset reminder
     */

    @Test
    fun `set unset reminder - succes from gql`() {
        val expected = PostUpcomingCampaign(response = UpcomingCampaignResponse(success = true))
        coEvery { mockPostReminderCampaign.executeOnBackground() } returns expected
        create(dispatcher = testDispatcher, postUpcomingCampaignReminderUseCase = mockPostReminderCampaign)
            .use {
                it.vm.setUnsetReminder(FeedXCampaign(id = "123"), 1)
                it.vm.asgcReminderButtonStatus.getOrAwaitValue().assertType<Success<FeedAsgcCampaignResponseModel>> {
                    it.data.reminderStatus
                }
            }
    }

    @Test
    fun `set unset reminder - error from gql`() {
        val expected = PostUpcomingCampaign(response = UpcomingCampaignResponse(errorMessage = "Error Aja", success = false))
        coEvery { mockPostReminderCampaign.executeOnBackground() } returns expected
        create(dispatcher = testDispatcher, postUpcomingCampaignReminderUseCase = mockPostReminderCampaign)
            .use {
                it.vm.setUnsetReminder(FeedXCampaign(id = "123"), 1)
                it.vm.asgcReminderButtonStatus.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.message.assertEqualTo(expected.response.errorMessage)
                }
            }
    }

    @Test
    fun `set unset reminder - failed`() {
        coEvery { mockPostReminderCampaign.executeOnBackground() } throws gqlFailed

        create(dispatcher = testDispatcher, postUpcomingCampaignReminderUseCase = mockPostReminderCampaign)
            .use {
                it.vm.setUnsetReminder(FeedXCampaign(id = "123"), 1)
                it.vm.asgcReminderButtonStatus.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }
}
