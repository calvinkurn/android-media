package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.FeedXTrackViewerResponse
import com.tokopedia.feedcomponent.data.pojo.VisitChannelTracking
import com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.TrackAffiliateViewModel
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.SubmitActionContentResponse
import com.tokopedia.kolcommon.data.SubmitReportContentResponse
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
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
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.lang.reflect.Type

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
    private val mockFavShop: ToggleFavouriteShopUseCase = mockk(relaxed = true)
    private val mockTrackAffiliate: TrackAffiliateClickUseCase = mockk(relaxed = true)
    private val mockFollowKol: FollowKolPostGqlUseCase = mockk(relaxed = true)

    private val gqlFailed = MessageErrorException("ooPs")
    private val mockFailedRx = HashMap<Type, List<GraphqlError>>()

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
                    .assertType<Success<DeletePostViewModel>> { dt ->
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
     * Get First Page
     */

    @Test
    fun `get first page - success without play widget` (){ // with login
        create(dispatcher = testDispatcher, feedXTrackViewerUseCase = mockTrackViewer)
            .use {
            }
    }

    @Test
    fun `get first page - success with play widget` (){

    }

    /**
     * Get Next Page
     */

    /**
     * Follow Kol - havent handle already follow?
     */
    @Test
    fun `follow kol - success` () {
        val expected = FollowKolQuery()

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKol(1, 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                    dt ->
                        dt.data.isSuccess.assertTrue()
                        dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol - success but failed` () {
        val expected = FollowKolQuery() //empty

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKol(1, 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol - error` () {
        every { mockFollowKol.createObservable(any()).toBlocking().single() } throws gqlFailed
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKol(1, 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<Exception> {  }
                }
            }
    }

    /**
     * Unfollow Kol - havent handle already unfollow?
     */
    @Test
    fun `unfollow kol - success` () {
        val expected = FollowKolQuery()

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doUnfollowKol(1, 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                }
            }
    }

    @Test
    fun `unfollow kol - success but failed` () {
        val expected = FollowKolQuery() //empty

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doUnfollowKol(1, 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                }
            }
    }

    @Test
    fun `unfollow kol - error` () {
        every { mockFollowKol.createObservable(any()).toBlocking().single() } throws gqlFailed
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doUnfollowKol(1, 2)
                it.vm.followKolResp.getOrAwaitValue().assertType<Fail> {
                        dt ->
                    dt.throwable.assertType<Exception> {  }
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
    fun `follow kol from rec - success` () {
        val expected = FollowKolQuery()

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKolFromRecommendation(1, 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.isFollow.assertTrue()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol from rec - success but failed` () {
        val expected = FollowKolQuery() //empty

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKolFromRecommendation(1, 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.isFollow.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_FOLLOW)
                }
            }
    }

    @Test
    fun `follow kol from rec - error` () {
        every { mockFollowKol.createObservable(any()).toBlocking().single() } throws gqlFailed
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doFollowKolFromRecommendation(1, 2, 3)
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
    fun `unfollow kol from rec - success` () {
        val expected = FollowKolQuery()

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doUnfollowKolFromRecommendation(1, 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertTrue()
                    dt.data.isFollow.assertTrue()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                }
            }
    }

    @Test
    fun `unfollow kol from rec - success but failed` () {
        val expected = FollowKolQuery() //empty

        val result = HashMap<Type, Any>()
        result[FollowKolQuery::class.java] = expected

        val returnValue = GraphqlResponse(result, mockFailedRx, false)

        every { mockFollowKol.createObservable(any()).toBlocking().single() } returns returnValue
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doUnfollowKolFromRecommendation(1, 2, 3)
                it.vm.followKolRecomResp.getOrAwaitValue().assertType<Success<FollowKolViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertFalse()
                    dt.data.isFollow.assertFalse()
                    dt.data.status.assertEqualTo(FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                }
            }
    }

    @Test
    fun `unfollow kol from rec - error` () {
        every { mockFollowKol.createObservable(any()).toBlocking().single() } throws gqlFailed
        create(dispatcher = testDispatcher, followKolPostGqlUseCase = mockFollowKol)
            .use {
                it.vm.doUnfollowKolFromRecommendation(1, 2, 3)
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
                    .assertType<Success<DeletePostViewModel>> { dt ->
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

    /**
     * ATC
     */

    /**
     * Toggle Fav Shop
     */

    /**
     * Do auto refresh
     */

    /**
     * Add wishlist
     */

    /**
     * TopAds tracker
     */

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
    fun `fav shop - success` () {
        val expected = true
        coEvery { mockFavShop.createObservable(any()).toBlocking().single() } returns expected

        create(dispatcher = testDispatcher, doFavoriteShopUseCase = mockFavShop)
            .use {
                it.vm.doFavoriteShop(Data(), 1)
                it.vm.doFavoriteShopResp.getOrAwaitValue().assertType<Success<FeedPromotedShopViewModel>> {
                    dt ->
                    dt.data.adapterPosition.assertEqualTo(1)
                    dt.data.isSuccess.assertEqualTo(expected)
                    dt.data.isSuccess.assertTrue()
                }
            }
    }

    @Test
    fun `fav shop - failed from gql` () {
        coEvery { mockFavShop.createObservable(any()).toBlocking().single() } throws gqlFailed

        create(dispatcher = testDispatcher, doFavoriteShopUseCase = mockFavShop)
            .use {
                it.vm.doFavoriteShop(Data(), 1)
                it.vm.doFavoriteShopResp.getOrAwaitValue().assertType<Fail> {
                        dt -> dt.throwable.assertEqualTo(gqlFailed)
                        dt.throwable.assertType<MessageErrorException> {  }
                }
            }
    }

    /**
 * track affiliate
 **/

    @Test
    fun `track affiliate - success` (){
        val expected = true
        coEvery { mockTrackAffiliate.createObservable(any()).toBlocking().single() } returns expected

        create(dispatcher = testDispatcher, trackAffiliateClickUseCase = mockTrackAffiliate)
            .use {
                it.vm.doTrackAffiliate("Ini")
                it.vm.trackAffiliateResp.getOrAwaitValue().assertType<Success<TrackAffiliateViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertEqualTo(expected)
                    dt.data.isSuccess.assertTrue()
                }
            }
    }

    @Test
    fun `track affiliate - failed from gql` (){
        val expected = false
        coEvery { mockTrackAffiliate.createObservable(any()).toBlocking().single() } returns expected

        create(dispatcher = testDispatcher, trackAffiliateClickUseCase = mockTrackAffiliate)
            .use {
                it.vm.doTrackAffiliate("")
                it.vm.trackAffiliateResp.getOrAwaitValue().assertType<Success<TrackAffiliateViewModel>> {
                        dt ->
                    dt.data.isSuccess.assertEqualTo(expected)
                    dt.data.isSuccess.assertFalse()
                }
            }
    }

    @Test
    fun `track affiliate - error from gql` (){
        coEvery { mockTrackAffiliate.createObservable(any()).toBlocking().single() } throws gqlFailed

        create(dispatcher = testDispatcher, trackAffiliateClickUseCase = mockTrackAffiliate)
            .use {
                it.vm.doTrackAffiliate("")
                it.vm.trackAffiliateResp.getOrAwaitValue().assertType<Fail> {
                        dt -> dt.throwable.assertEqualTo(gqlFailed)
                }
            }
    }

    /**
     *
     */
}
