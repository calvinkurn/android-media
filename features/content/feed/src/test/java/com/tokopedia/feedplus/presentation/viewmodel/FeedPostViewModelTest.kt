package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.content.common.comment.model.CountComment
import com.tokopedia.content.common.comment.usecase.GetCountCommentsUseCase
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.model.TrackVisitChannelResponse
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.content.common.usecase.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.data.pojo.UpcomingCampaignResponse
import com.tokopedia.feedcomponent.data.pojo.shopmutation.FollowShop
import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowModelBase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedData
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedDataVal
import com.tokopedia.feedcomponent.people.model.ProfileDoUnFollowModelBase
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.domain.usecase.FeedCampaignCheckReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedCampaignReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXRecomWidgetUseCase
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedCommentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowModel
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedPaginationModel
import com.tokopedia.feedplus.presentation.model.FeedPostEvent
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedViewModel
import com.tokopedia.feedplus.presentation.model.PostSourceModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonType
import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction
import com.tokopedia.kolcommon.data.SubmitActionContentResponse
import com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Created By : Muhammad Furqan on 22/05/23
 */
class FeedPostViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    private val feedXHomeUseCase: FeedXHomeUseCase = mockk()
    private val atcUseCase: AddToCartUseCase = mockk()
    private val likeContentUseCase: SubmitLikeContentUseCase = mockk()
    private val deletePostUseCase: SubmitActionContentUseCase = mockk()
    private val userSession: UserSessionInterface = mockk()
    private val shopFollowUseCase: ShopFollowUseCase = mockk()
    private val userFollowUseCase: ProfileFollowUseCase = mockk()
    private val userUnfollowUseCase: ProfileUnfollowedUseCase = mockk()
    private val campaignReminderUseCase: FeedCampaignReminderUseCase = mockk()
    private val checkCampaignReminderUseCase: FeedCampaignCheckReminderUseCase = mockk()
    private val topAdsHeadlineUseCase: GetTopAdsHeadlineUseCase = mockk()
    private val mvcSummaryUseCase: MVCSummaryUseCase = mockk()
    private val topAdsAddressHelper: TopAdsAddressHelper = mockk()
    private val getCountCommentsUseCase: GetCountCommentsUseCase = mockk()
    private val trackVisitChannelUseCase: TrackVisitChannelBroadcasterUseCase = mockk()
    private val trackReportViewerUseCase: BroadcasterReportTrackViewerUseCase = mockk()
    private val getReportListUseCase: GetUserReportListUseCase = mockk()
    private val postReportUseCase: PostUserReportUseCase = mockk()
    private val feedXRecomWidgetUseCase: FeedXRecomWidgetUseCase = mockk()
    private val affiliateCookieHelper: AffiliateCookieHelper = mockk()
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase = mockk()
    private val uiEventManager = UiEventManager<FeedPostEvent>()

    private lateinit var viewModel: FeedPostViewModel

    @Before
    fun setUp() {
        viewModel = FeedPostViewModel(
            feedXHomeUseCase = feedXHomeUseCase,
            addToCartUseCase = atcUseCase,
            likeContentUseCase = likeContentUseCase,
            deletePostUseCase = deletePostUseCase,
            userSession = userSession,
            shopFollowUseCase = shopFollowUseCase,
            userFollowUseCase = userFollowUseCase,
            userUnfollowUseCase = userUnfollowUseCase,
            setCampaignReminderUseCase = campaignReminderUseCase,
            checkCampaignReminderUseCase = checkCampaignReminderUseCase,
            topAdsHeadlineUseCase = topAdsHeadlineUseCase,
            mvcSummaryUseCase = mvcSummaryUseCase,
            topAdsAddressHelper = topAdsAddressHelper,
            getCountCommentsUseCase = getCountCommentsUseCase,
            affiliateCookieHelper = affiliateCookieHelper,
            trackVisitChannelUseCase = trackVisitChannelUseCase,
            trackReportTrackViewerUseCase = trackReportViewerUseCase,
            submitReportUseCase = submitReportUseCase,
            getReportUseCase = getReportListUseCase,
            postReportUseCase = postReportUseCase,
            feedXRecomWidgetUseCase = feedXRecomWidgetUseCase,
            uiEventManager = uiEventManager,
            dispatchers = testDispatcher
        )
    }

    @Test
    fun onGetMerchantVoucher_whenFailed_shouldReturnFail() {
        // given
        coEvery { mvcSummaryUseCase.getQueryParams(any()) } returns hashMapOf()
        coEvery { mvcSummaryUseCase.getResponse(any()) } throws MessageErrorException("Failed")

        // when
        viewModel.getMerchantVoucher("")

        // then
        assert(viewModel.merchantVoucherLiveData.value is Fail)
        assert((viewModel.merchantVoucherLiveData.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onGetMerchantVoucher_whenSuccessButNull_shouldReturnFail() {
        // given
        coEvery { mvcSummaryUseCase.getQueryParams(any()) } returns hashMapOf()
        coEvery { mvcSummaryUseCase.getResponse(any()) } returns TokopointsCatalogMVCSummaryResponse(
            data = null
        )

        // when
        viewModel.getMerchantVoucher("")

        // then
        assert(viewModel.merchantVoucherLiveData.value is Fail)
        assert((viewModel.merchantVoucherLiveData.value as Fail).throwable is IllegalStateException)
    }

    @Test
    fun onGetMerchantVoucher_whenSuccessButCodeStatusError_shouldReturnFail() {
        // given
        coEvery { mvcSummaryUseCase.getQueryParams(any()) } returns hashMapOf()
        coEvery { mvcSummaryUseCase.getResponse(any()) } returns TokopointsCatalogMVCSummaryResponse(
            data = TokopointsCatalogMVCSummary(
                resultStatus = ResultStatus("403", listOf("Not Authorized"), "", ""),
                isShown = true,
                counterTotal = 0,
                animatedInfoList = emptyList()
            )
        )

        // when
        viewModel.getMerchantVoucher("")

        // then
        assert(viewModel.merchantVoucherLiveData.value is Fail)
        assert((viewModel.merchantVoucherLiveData.value as Fail).throwable is ResponseErrorException)
        assert((viewModel.merchantVoucherLiveData.value as Fail).throwable.message == "Not Authorized")
    }

    @Test
    fun onGetMerchantVoucher_whenSuccess_shouldReturnSuccess() {
        // given
        val dummyData = TokopointsCatalogMVCSummaryResponse(
            data = TokopointsCatalogMVCSummary(
                resultStatus = ResultStatus("200", emptyList(), "", ""),
                isShown = true,
                counterTotal = 0,
                animatedInfoList = emptyList()
            )
        )
        coEvery { mvcSummaryUseCase.getQueryParams(any()) } returns hashMapOf()
        coEvery { mvcSummaryUseCase.getResponse(any()) } returns dummyData

        // when
        viewModel.getMerchantVoucher("")

        // then
        assert(viewModel.merchantVoucherLiveData.value is Success)
        assert((viewModel.merchantVoucherLiveData.value as Success).data == dummyData.data)
    }

    @Test
    fun onSuspendFollow_shouldUpdateValue() {
        // given
        coEvery { userFollowUseCase.executeOnBackground(any()) } throws MessageErrorException("Failed")

        val id = "dummy-id"
        val encryptedId = "dummy-encrypted-id"
        val isShop = false

        // when
        viewModel.suspendFollow(id, encryptedId, isShop)
        viewModel.processSuspendedFollow()

        // then
        assert(viewModel.followResult.value is Fail)
        assert((viewModel.followResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.followResult.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onDoFollowShop_whenSuccess() {
        // given
        provideDefaultFeedPostMockData()

        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase(any()) } returns getDummyShopFollowResponseData()

        // when
        viewModel.doFollow("authorId", "", true)

        // then
        assert(viewModel.followResult.value is Success)
        assert((viewModel.followResult.value as Success).data == "toko")

        val feedDataItems = (viewModel.feedHome.value as Success).data.items
        assert((feedDataItems[0] as FeedCardImageContentModel).followers.isFollowed)
        assert((feedDataItems[1] as FeedCardVideoContentModel).followers.isFollowed)
    }

    @Test
    fun onDoFollowUser_whenFailed() {
        // given
        coEvery { userFollowUseCase.executeOnBackground(any()) } returns ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                errorCode = "403",
                messages = listOf("Error Message"),
                data = ProfileDoFollowedDataVal("", "", "", "")
            )
        )

        // when
        viewModel.doFollow("", "", false)

        // then
        assert(viewModel.followResult.value is Fail)
        assert((viewModel.followResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.followResult.value as Fail).throwable.message == "Error Message")
    }

    @Test
    fun onDoFollowUser_whenSuccess() {
        // given
        coEvery { userFollowUseCase.executeOnBackground(any()) } returns ProfileDoFollowModelBase(
            profileFollowers = ProfileDoFollowedData(
                errorCode = "",
                messages = emptyList(),
                data = ProfileDoFollowedDataVal("", "", "", "")
            )
        )

        // when
        viewModel.doFollow("1", "123", false)

        // then
        assert(viewModel.followResult.value is Success)
        assert((viewModel.followResult.value as Success).data == "akun")
    }

    @Test
    fun onSuspendLike_shouldUpdateValue() {
        // given
        coEvery { likeContentUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { likeContentUseCase.executeOnBackground() } throws MessageErrorException("Failed")

        val id = "dummy-id"
        val row = 1

        // when
        viewModel.suspendLikeContent(id, row)
        viewModel.processSuspendedLike()

        // then
        assert(viewModel.getLikeKolResp.value is FeedResult.Failure)
        assert((viewModel.getLikeKolResp.value as FeedResult.Failure).error is MessageErrorException)
        assert((viewModel.getLikeKolResp.value as FeedResult.Failure).error.message == "Failed")
    }

    @Test
    fun onSuspendAddToCart() {
        // given
        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } throws MessageErrorException("Failed")
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        val dummyData = FeedTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = FeedTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            FeedTaggedProductUiModel.Stock.Available
        )

        // when
        viewModel.suspendAddProductToCart(dummyData)
        viewModel.processSuspendedAddProductToCart()

        // then
        assert(viewModel.observeAddProductToCart.value is Fail)
        assert((viewModel.observeAddProductToCart.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onAtc_whenFailed() {
        // given
        val dummyData = FeedTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = FeedTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            FeedTaggedProductUiModel.Stock.Available
        )

        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } returns AddToCartDataModel(
            errorMessage = arrayListOf(),
            status = "NOT OK",
            data = DataModel(message = arrayListOf("Error Message")),
            errorReporter = ErrorReporterModel(),
            responseJson = ""
        )
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        // when
        viewModel.addProductToCart(dummyData)

        // then
        assert(viewModel.observeAddProductToCart.value is Fail)
        assert((viewModel.observeAddProductToCart.value as Fail).throwable is ResponseErrorException)
        assert((viewModel.observeAddProductToCart.value as Fail).throwable.message == "Error Message")
    }

    @Test
    fun onAtc_whenSuccess() {
        // given
        val dummyData = FeedTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = FeedTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            FeedTaggedProductUiModel.Stock.Available
        )
        val dummyAtcResult = AddToCartDataModel(
            errorMessage = arrayListOf(),
            status = "OK",
            data = DataModel(message = arrayListOf()),
            errorReporter = ErrorReporterModel(),
            responseJson = ""
        )

        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } returns dummyAtcResult
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        // when
        viewModel.addProductToCart(dummyData)

        // then
        assert(viewModel.observeAddProductToCart.value is Success)
        assert((viewModel.observeAddProductToCart.value as Success).data == dummyAtcResult)
    }

    @Test
    fun onSuspendBuy() {
        // given
        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } throws MessageErrorException("Failed")
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        val dummyData = FeedTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = FeedTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            FeedTaggedProductUiModel.Stock.Available
        )

        // when
        viewModel.suspendBuyProduct(dummyData)
        viewModel.processSuspendedBuyProduct()

        // then
        assert(viewModel.observeBuyProduct.value is Fail)
        assert((viewModel.observeBuyProduct.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onBuyProduct_whenFailed() {
        // given
        val dummyData = FeedTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = FeedTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            FeedTaggedProductUiModel.Stock.Available
        )

        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}
        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } returns AddToCartDataModel(
            errorMessage = arrayListOf(),
            status = "NOT OK",
            data = DataModel(message = arrayListOf("Error Message")),
            errorReporter = ErrorReporterModel(),
            responseJson = ""
        )

        // when
        viewModel.buyProduct(dummyData)

        // then
        assert(viewModel.observeBuyProduct.value is Fail)
        assert((viewModel.observeBuyProduct.value as Fail).throwable is ResponseErrorException)
        assert((viewModel.observeBuyProduct.value as Fail).throwable.message == "Error Message")
    }

    @Test
    fun onBuyProduct_whenSuccess() {
        // given
        val dummyData = FeedTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = FeedTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            FeedTaggedProductUiModel.Stock.Available
        )
        val dummyAtcResult = AddToCartDataModel(
            errorMessage = arrayListOf(),
            status = "OK",
            data = DataModel(message = arrayListOf()),
            errorReporter = ErrorReporterModel(),
            responseJson = ""
        )

        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } returns dummyAtcResult
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        // when
        viewModel.buyProduct(dummyData)

        // then
        assert(viewModel.observeBuyProduct.value is Success)
        assert((viewModel.observeBuyProduct.value as Success).data == dummyAtcResult)
    }

    @Test
    fun onFetchFeedPosts_whenFailed() {
        // given
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase.createPostDetailParams("1") } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } throws MessageErrorException("Failed")

        // when
        viewModel.fetchFeedPosts("", true, null)

        // then
        assert(viewModel.feedHome.value is Fail)
        assert((viewModel.feedHome.value as Fail).throwable is MessageErrorException)
        assert((viewModel.feedHome.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithoutRelevantPost() {
        // given
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()

        // when
        viewModel.fetchFeedPosts("")

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == 8)
        assert(data.items[0] is FeedCardImageContentModel)
        assert(data.items[1] is FeedCardVideoContentModel)
        assert(data.items[2] is FeedCardLivePreviewContentModel)
        assert(data.items[3] is FeedCardImageContentModel)
        assert(data.items[4] is FeedCardVideoContentModel)
        assert(data.items[5] is FeedCardLivePreviewContentModel)
        assert(data.items[6] is FeedFollowRecommendationModel)
        assert(data.items[7] is FeedNoContentModel)
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithRelevantPost() {
        // given
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase.createPostDetailParams("1") } returns emptyMap()
        coEvery { feedXHomeUseCase.createParamsWithId("1", any()) } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()

        // when
        viewModel.fetchFeedPosts("", true, PostSourceModel("1", null))

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == 16)
        assert(data.items[0] is FeedCardImageContentModel)
        assert(data.items[1] is FeedCardVideoContentModel)
        assert(data.items[2] is FeedCardLivePreviewContentModel)
        assert(data.items[3] is FeedCardImageContentModel)
        assert(data.items[4] is FeedCardVideoContentModel)
        assert(data.items[5] is FeedCardLivePreviewContentModel)
        assert(data.items[6] is FeedFollowRecommendationModel)
        assert(data.items[7] is FeedNoContentModel)
        assert(data.items[8] is FeedCardImageContentModel)
        assert(data.items[9] is FeedCardVideoContentModel)
        assert(data.items[10] is FeedCardLivePreviewContentModel)
        assert(data.items[11] is FeedCardImageContentModel)
        assert(data.items[12] is FeedCardVideoContentModel)
        assert(data.items[13] is FeedCardLivePreviewContentModel)
        assert(data.items[14] is FeedFollowRecommendationModel)
        assert(data.items[15] is FeedNoContentModel)
    }

    @Test
    fun onDeletePost_whenFailed_shouldBeFail() {
        // given
        coEvery { deletePostUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { deletePostUseCase.executeOnBackground() } throws MessageErrorException("Failed")

        // when
        viewModel.doDeletePost("", 1)

        // then
        assert(viewModel.deletePostResult.value is Fail)
        assert((viewModel.deletePostResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.deletePostResult.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onDeletePost_whenNotSuccess_shouldBeFail() {
        // given
        coEvery { deletePostUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { deletePostUseCase.executeOnBackground() } returns SubmitActionContentResponse(
            content = SubmitActionContentResponse.FeedContentSubmit(
                success = 0
            )
        )

        // when
        viewModel.doDeletePost("", 1)

        // then
        assert(viewModel.deletePostResult.value is Fail)
        assert((viewModel.deletePostResult.value as Fail).throwable is MessageErrorException)
    }

    @Test
    fun onDeletePost_whenSuccess_shouldBeSuccess() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { deletePostUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { deletePostUseCase.executeOnBackground() } returns SubmitActionContentResponse(
            content = SubmitActionContentResponse.FeedContentSubmit(
                success = 1
            )
        )

        // when
        viewModel.doDeletePost("image 1 id", 1)

        // then
        assert(viewModel.deletePostResult.value is Success)
        assert((viewModel.deletePostResult.value as Success).data == 1)
        assert((viewModel.feedHome.value as Success).data.items.size == getDummyFeedModel().items.size - 1)
    }

    @Test
    fun onSetReminder_whenFailed_shouldBeFail() {
        // given
        coEvery { campaignReminderUseCase.createParams(1, true) } returns mapOf()
        coEvery { campaignReminderUseCase(any()) } throws MessageErrorException("Failed")

        // when
        viewModel.setUnsetReminder(1, true, FeedCampaignRibbonType.ASGC_GENERAL)

        // then
        assert(viewModel.reminderResult.value is Fail)
        assert((viewModel.reminderResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.reminderResult.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onSetReminder_whenNotSuccess_shouldBeFail() {
        // given
        coEvery { campaignReminderUseCase.createParams(1, true) } returns mapOf()
        coEvery { campaignReminderUseCase(any()) } returns UpcomingCampaignResponse()

        // when
        viewModel.setUnsetReminder(1, true, FeedCampaignRibbonType.ASGC_GENERAL)

        // then
        assert(viewModel.reminderResult.value is Fail)
        assert((viewModel.reminderResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.reminderResult.value as Fail).throwable.message == "")
    }

    @Test
    fun onSetReminder_whenSuccessWithErrorMessage_shouldBeFail() {
        // given
        coEvery { campaignReminderUseCase.createParams(1, true) } returns mapOf()
        coEvery { campaignReminderUseCase(any()) } returns UpcomingCampaignResponse(
            success = true,
            errorMessage = "Failed",
            isAvailable = true
        )

        // when
        viewModel.setUnsetReminder(1, true, FeedCampaignRibbonType.ASGC_GENERAL)

        // then
        assert(viewModel.reminderResult.value is Fail)
        assert((viewModel.reminderResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.reminderResult.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onSetReminder_whenNotSuccessWithErrorMessage_shouldBeFail() {
        // given
        coEvery { campaignReminderUseCase.createParams(1, true) } returns mapOf()
        coEvery { campaignReminderUseCase(any()) } returns UpcomingCampaignResponse(
            success = false,
            errorMessage = "Failed"
        )

        // when
        viewModel.setUnsetReminder(1, true, FeedCampaignRibbonType.ASGC_GENERAL)

        // then
        assert(viewModel.reminderResult.value is Fail)
        assert((viewModel.reminderResult.value as Fail).throwable is MessageErrorException)
        assert((viewModel.reminderResult.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onSetReminder_whenSuccessWithoutErrorMessage_shouldBeSuccess() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { campaignReminderUseCase.createParams(1, true) } returns mapOf()
        coEvery { campaignReminderUseCase(any()) } returns UpcomingCampaignResponse(success = true)

        // when
        viewModel.setUnsetReminder(1, true, FeedCampaignRibbonType.ASGC_GENERAL)

        // then
        assert(viewModel.reminderResult.value is Success)
        val data = (viewModel.reminderResult.value as Success).data
        assert(data.isSetReminder)
        assert(data.reminderType == FeedCampaignRibbonType.ASGC_GENERAL)

        assert(viewModel.feedHome.value is Success)
        val feedData = (viewModel.feedHome.value as Success).data
        assert(feedData.items.size == getDummyFeedModel().items.size)
        assert(feedData.items[0] is FeedCardImageContentModel)
        assert((feedData.items[0] as FeedCardImageContentModel).campaign.isReminderActive)
        assert(feedData.items[1] is FeedCardVideoContentModel)
        assert((feedData.items[1] as FeedCardVideoContentModel).campaign.isReminderActive)
        assert(feedData.items[2] is FeedCardLivePreviewContentModel)
        assert(!(feedData.items[2] as FeedCardLivePreviewContentModel).campaign.isReminderActive)
        assert(feedData.items[3] is FeedCardImageContentModel)
        assert(!(feedData.items[3] as FeedCardImageContentModel).campaign.isReminderActive)
        assert(feedData.items[4] is FeedCardVideoContentModel)
        assert(!(feedData.items[4] as FeedCardVideoContentModel).campaign.isReminderActive)
        assert(feedData.items[5] is FeedCardLivePreviewContentModel)
        assert(!(feedData.items[5] as FeedCardLivePreviewContentModel).campaign.isReminderActive)
        assert(feedData.items[6] is FeedFollowRecommendationModel)
        assert(feedData.items[7] is FeedNoContentModel)
    }

    @Test
    fun onCheckIsFollowing_whenFeedHomeNull_shouldBeFalse() {
        // when
        val isFollowing = viewModel.isFollowing("1")

        // then
        assert(!isFollowing)
    }

    @Test
    fun onCheckIsFollowing_whenFeedHomeFailed_shouldBeFalse() {
        // given
        viewModel.fetchFeedPosts("foryou")

        // when
        val isFollowing = viewModel.isFollowing("1")

        // then
        assert(!isFollowing)
    }

    @Test
    fun onCheckIsFollowing_whenFeedHomeSuccess_shouldBeBasedOnResponse() {
        // given
        provideDefaultFeedPostMockData()

        // when
        val isFollowingImage1 = viewModel.isFollowing("image 1 id")
        val isFollowingVideo1 = viewModel.isFollowing("video 1 id")
        val isFollowingLive1 = viewModel.isFollowing("live 1 id")
        val isFollowingImage2 = viewModel.isFollowing("image 2 id")
        val isFollowingVideo2 = viewModel.isFollowing("video 2 id")
        val isFollowingLive2 = viewModel.isFollowing("live 2 id")
        val isFollowingRandom = viewModel.isFollowing("random")

        // then
        assert(isFollowingImage1)
        assert(isFollowingVideo1)
        assert(isFollowingLive1)
        assert(!isFollowingImage2)
        assert(!isFollowingVideo2)
        assert(!isFollowingLive2)
        assert(!isFollowingRandom)
    }

    @Test
    fun onFetchTopAds_whenSuccess_shouldChangeTopAdsValue() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { userSession.userId } returns ""
        coEvery { topAdsAddressHelper.getAddressData() } returns mapOf()
        coEvery { topAdsHeadlineUseCase.setParams(any(), any()) } coAnswers {}
        coEvery { topAdsHeadlineUseCase.executeOnBackground() } returns TopAdsHeadlineResponse(
            displayAds = CpmModel(
                data = mutableListOf(
                    CpmData(
                        id = "",
                        adRefKey = "",
                        redirect = "",
                        adClickUrl = "",
                        cpm = Cpm(
                            cpmShop = CpmShop(
                                domain = "domain.com",
                                id = "shopId"
                            )
                        ),
                        applinks = ""
                    )
                )
            )
        )

        // when
        viewModel.fetchTopAdsData()

        // then
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == getDummyFeedModel().items.size)
        val topAds = data.items[0] as FeedCardImageContentModel
        assert(topAds.author.id == "shopId")
        assert(topAds.author.type == AuthorType.Shop)
        assert(topAds.author.name == "")
        assert(topAds.isFetched)
        assert(!topAds.followers.isFollowed)
    }

    @Test
    fun onFetchTopAds_whenSuccessButNoData_shouldRemoveTopAds() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { userSession.userId } returns ""
        coEvery { topAdsAddressHelper.getAddressData() } returns mapOf()
        coEvery { topAdsHeadlineUseCase.setParams(any(), any()) } coAnswers {}
        coEvery { topAdsHeadlineUseCase.executeOnBackground() } returns TopAdsHeadlineResponse(
            displayAds = CpmModel(data = mutableListOf())
        )

        // when
        viewModel.fetchTopAdsData()

        // then
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == getDummyFeedModel().items.size - 1)
        assert(data.items[0] is FeedCardVideoContentModel)
    }

    @Test
    fun onLikeContent_whenSuccessWithError_shouldFail() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { likeContentUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { likeContentUseCase.executeOnBackground() } returns LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                error = "Failed"
            )
        )

        // when
        viewModel.likeContent("image 1 id", 0)

        // then
        assert(viewModel.getLikeKolResp.value is FeedResult.Failure)
        assert((viewModel.getLikeKolResp.value as FeedResult.Failure).error is MessageErrorException)
        assert((viewModel.getLikeKolResp.value as FeedResult.Failure).error.message == "Failed")
    }

    @Test
    fun onLikeContent_whenNotSuccess_shouldFail() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { likeContentUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { likeContentUseCase.executeOnBackground() } returns LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                data = LikeKolPostData.LikeKolPostSuccessData(
                    success = 0
                )
            )
        )

        // when
        viewModel.likeContent("image 1 id", 0)

        // then
        assert(viewModel.getLikeKolResp.value is FeedResult.Failure)
        assert((viewModel.getLikeKolResp.value as FeedResult.Failure).error is CustomUiMessageThrowable)
        assert(((viewModel.getLikeKolResp.value as FeedResult.Failure).error as CustomUiMessageThrowable).errorMessageId == R.string.feed_like_error_message)
    }

    @Test
    fun onLikeContent_whenSuccess_shouldSuccess() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { likeContentUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { likeContentUseCase.executeOnBackground() } returns LikeKolPostData(
            doLikeKolPost = LikeKolPostData.DoLikeKolPost(
                data = LikeKolPostData.LikeKolPostSuccessData(
                    success = 1
                )
            )
        )

        // when
        viewModel.likeContent("image 1 id", 0)

        // then
        assert(viewModel.getLikeKolResp.value is FeedResult.Success)
        assert((viewModel.getLikeKolResp.value as FeedResult.Success).data.contentId == "image 1 id")
        assert((viewModel.getLikeKolResp.value as FeedResult.Success).data.action == FeedLikeAction.Like)
        assert((viewModel.getLikeKolResp.value as FeedResult.Success).data.rowNumber == 0)
    }

    @Test
    fun onUpdateCommentsCount_whenSuccess_shouldUpdateData() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { getCountCommentsUseCase(any()) } returns CountComment(
            parent = CountComment.Parent(
                child = CountComment.Child(
                    data = listOf(
                        CountComment.Data(
                            contentId = "image 1 id"
                        ),
                        CountComment.Data(
                            contentId = "video 1 id"
                        )
                    )
                ),
                error = ""
            )
        )

        // when
        viewModel.updateCommentsCount("image 1 id", false)
        viewModel.updateCommentsCount("video 1 id", false)
        viewModel.updateCommentsCount("video 2 id", true)

        // then
        assert((viewModel.feedHome.value as Success).data.items.size == getDummyFeedModel().items.size)
    }

    @Test
    fun onTrackChannelPerformance() {
        coEvery { trackReportViewerUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { trackReportViewerUseCase.executeOnBackground() } returns true

        // when
        viewModel.trackChannelPerformance(getDummyFeedModel().items[1] as FeedCardVideoContentModel)
    }

    @Test
    fun onTrackVisiChannel() {
        coEvery { trackVisitChannelUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { trackVisitChannelUseCase.executeOnBackground() } returns TrackVisitChannelResponse.Response(
            TrackVisitChannelResponse()
        )

        viewModel.trackVisitChannel(getDummyFeedModel().items[1] as FeedCardVideoContentModel)
    }

    @Test
    fun onReportContent_whenFailUsecase_shouldChangeValueToFail() {
        // given
        coEvery { submitReportUseCase(any()) } throws MessageErrorException("Failed to fetch")

        // when
        viewModel.reportContent(FeedComplaintSubmitReportUseCase.Param("", "", "", ""))

        // then
        val response = viewModel.reportResponse.value
        assert(response is Fail)
        assert((response as Fail).throwable is MessageErrorException)
        assert(response.throwable.message == "Failed to fetch")
    }

    @Test
    fun onReportContent_whenNotSuccess_shouldChangeValueToFail() {
        // given
        val dummyResponse = FeedComplaintSubmitReportResponse(
            data = FeedComplaintSubmitReportResponse.FeedComplaintSubmitReport(
                success = false
            )
        )
        coEvery { submitReportUseCase(any()) } returns dummyResponse

        // when
        viewModel.reportContent(FeedComplaintSubmitReportUseCase.Param("", "", "", ""))

        // then
        val response = viewModel.reportResponse.value
        assert(response is Fail)
        assert((response as Fail).throwable is MessageErrorException)
        assert(response.throwable.message == "Error in Reporting")
    }

    @Test
    fun onReportContent_whenSuccess_shouldChangeValueToSuccess() {
        // given
        val dummyResponse = FeedComplaintSubmitReportResponse(
            data = FeedComplaintSubmitReportResponse.FeedComplaintSubmitReport(
                success = true
            )
        )
        coEvery { submitReportUseCase(any()) } returns dummyResponse

        // when
        viewModel.reportContent(FeedComplaintSubmitReportUseCase.Param("", "", "", ""))

        // then
        val response = viewModel.reportResponse.value
        assert(response is Success)
        assert((response as Success).data == dummyResponse)
    }

    /** Follow Recommendation */
    @Test
    fun onInitialFetchFollowRecommendation_Success() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        // test
        provideMockForFollowRecommendation()

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel) == mockFollowRecommendationData)
    }

    @Test
    fun onInitialFetchBulkFollowRecommendation_Error() {
        // prepare
        val mockFollowRecommendationData = Exception("Network Error")

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } throws mockFollowRecommendationData

        // test
        provideMockForFollowRecommendation()

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).isError)
    }

    @Test
    fun onFetchFollowRecommendation_LoadMore_Success() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        viewModel.fetchFollowRecommendation(6)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data == mockFollowRecommendationData.data + mockFollowRecommendationData.data)
    }

    @Test
    fun onFetchFollowRecommendation_LoadMore_WidgetNotFound() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        viewModel.fetchFollowRecommendation(5)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data == mockFollowRecommendationData.data)
    }

    @Test
    fun onFetchFollowRecommendation_InitialFetchError_RetrySuccess() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val mockInitialFollowRecommendationData = Exception()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } throws mockInitialFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        viewModel.fetchFollowRecommendation(6)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert(followRecomModel == mockFollowRecommendationData)
    }

    @Test
    fun onFetchFollowRecommendation_InitialFetchError_RetryError() {
        // prepare
        val mockInitialFollowRecommendationData = Exception()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } throws mockInitialFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        viewModel.fetchFollowRecommendation(6)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).isError)
        assert(followRecomModel.data.isEmpty())
    }

    @Test
    fun onFetchFollowRecommendation_LoadMore_Error() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val mockErrorFollowRecommendationData = Exception()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        coEvery { feedXRecomWidgetUseCase(any()) } throws mockErrorFollowRecommendationData
        viewModel.fetchFollowRecommendation(6)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).status == FeedFollowRecommendationModel.Status.Success)
        assert(followRecomModel.data == mockFollowRecommendationData.data)
    }

    @Test
    fun onFetchFollowRecommendation_NoMorePage_LoadMore() {
        // prepare
        val mockFollowRecommendationDataWithoutCursor = getDummyProfileRecommendationList(cursor = "")

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationDataWithoutCursor

        provideMockForFollowRecommendation()

        // test
        viewModel.fetchFollowRecommendation(6)
        viewModel.fetchFollowRecommendation(6)
        viewModel.fetchFollowRecommendation(6)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert(followRecomModel == mockFollowRecommendationDataWithoutCursor)
    }

    /** Follow UnFollow Profile Recommendation */
    @Test
    fun onFollowProfileRecommendation_Shop_Success() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 0
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        val mockFollowed = getDummyShopFollowResponseData(isFollowing = true)
        val mockNotFollowed = getDummyShopFollowResponseData(isFollowing = false)

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // test
        coEvery { shopFollowUseCase(any()) } returns mockFollowed
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // double test
        coEvery { shopFollowUseCase(any()) } returns mockNotFollowed
        viewModel.doUnfollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel2 = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel2 is FeedFollowRecommendationModel)
        assert((followRecomModel2 as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed.not())
        assert(viewModel.unfollowResult.value is Success)
    }

    @Test
    fun onFollowProfileRecommendation_Shop_NotSuccess() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 0
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        val mockShopFollowNotSuccess = getDummyShopFollowResponseData(success = false)

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // test
        coEvery { shopFollowUseCase(any()) } returns mockShopFollowNotSuccess
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert(
            (followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed == mockFollowRecommendationData.data[selectedProfileIndex].isFollowed
        )
        assert(viewModel.followResult.value is Fail)
    }

    @Test
    fun onUnFollowProfileRecommendation_Shop_NotSuccess() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 0
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        val mockShopFollowSuccess = getDummyShopFollowResponseData()
        val mockShopUnfollowNotSuccess = getDummyShopFollowResponseData(success = false)

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // follow first
        coEvery { shopFollowUseCase(any()) } returns mockShopFollowSuccess
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)

        // test
        coEvery { shopFollowUseCase(any()) } returns mockShopUnfollowNotSuccess
        viewModel.doUnfollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel2 = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel2 is FeedFollowRecommendationModel)
        assert((followRecomModel2 as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.unfollowResult.value is Fail)
    }

    @Test
    fun onFollowProfileRecommendation_Shop_Error() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 0
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        val mockException = Exception()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // test
        coEvery { shopFollowUseCase(any()) } throws mockException
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert(
            (followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed == mockFollowRecommendationData.data[selectedProfileIndex].isFollowed
        )
        assert(viewModel.followResult.value is Fail)
    }

    @Test
    fun onFollowProfileRecommendation_Ugc_Success() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 1
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        coEvery { userFollowUseCase.executeOnBackground(any()) } returns getDummyUserFollowResponseData()
        coEvery { userUnfollowUseCase.executeOnBackground(any()) } returns getDummyUserUnfollowResponseData()

        provideMockForFollowRecommendation()

        // test
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // double test
        viewModel.doUnfollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel2 = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel2 is FeedFollowRecommendationModel)
        assert((followRecomModel2 as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed.not())
        assert(viewModel.unfollowResult.value is Success)
    }

    @Test
    fun onUnFollowProfileRecommendation_Ugc_NotSuccess() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 1
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        coEvery { userFollowUseCase.executeOnBackground(any()) } returns getDummyUserFollowResponseData()
        coEvery { userUnfollowUseCase.executeOnBackground(any()) } returns getDummyUserUnfollowResponseData(isError = true)

        provideMockForFollowRecommendation()

        // follow first
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // test
        viewModel.doUnfollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel2 = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel2 is FeedFollowRecommendationModel)
        assert((followRecomModel2 as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.unfollowResult.value is Fail)
    }

    @Test
    fun onUnFollowProfileRecommendation_Ugc_Error() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedProfileIndex = 1
        val selectedProfile = mockFollowRecommendationData.data[selectedProfileIndex]

        val mockException = Exception()

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        coEvery { userFollowUseCase.executeOnBackground(any()) } returns getDummyUserFollowResponseData()
        coEvery { userUnfollowUseCase.executeOnBackground(any()) } throws mockException

        provideMockForFollowRecommendation()

        // follow first
        viewModel.doFollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // test
        viewModel.doUnfollowProfileRecommendation(selectedProfile.id, selectedProfile.encryptedId, selectedProfile.isShop)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel2 = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel2 is FeedFollowRecommendationModel)
        assert((followRecomModel2 as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.unfollowResult.value is Fail)
    }

    /** Remove Profile Recommendation */
    @Test
    fun RemoveProfileRecommendation_Success() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedRemovedProfile = mockFollowRecommendationData.data[2]

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        viewModel.removeProfileRecommendation(selectedRemovedProfile)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data.size == mockFollowRecommendationData.data.size-1)
        assert((followRecomModel as FeedFollowRecommendationModel).data.indexOf(selectedRemovedProfile) == -1)
    }

    @Test
    fun RemoveProfileRecommendation_NotFound() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedRemovedProfile = mockFollowRecommendationData.data[1].copy(
            id = "asdfasdf",
            encryptedId = "asdfasdf",
        )

        coEvery { feedXRecomWidgetUseCase.createFeedFollowRecomParams(any(), any()) } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        viewModel.removeProfileRecommendation(selectedRemovedProfile)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        println("JOE LOG ${(followRecomModel as FeedFollowRecommendationModel).data.size}")
        println("JOE LOG ${mockFollowRecommendationData.data.size}")
        assert((followRecomModel as FeedFollowRecommendationModel).data.size == mockFollowRecommendationData.data.size)
    }

    @Test
    fun RemoveProfileRecommendation_NoRecommendation_TryToRemove() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedRemovedProfile = mockFollowRecommendationData.data[1]

        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase.createPostDetailParams("1") } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } throws MessageErrorException("Failed")

        viewModel.fetchFeedPosts("", true, null)

        // test
        viewModel.removeProfileRecommendation(selectedRemovedProfile)

        // verify
        assert(viewModel.feedHome.value is Fail)
    }

    private fun provideDefaultFeedPostMockData() {
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase.createPostDetailParams("1") } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()
        viewModel.fetchFeedPosts("")
    }

    private fun provideMockForFollowRecommendation() {
        coEvery { userSession.userId } returns ""
        coEvery { topAdsAddressHelper.getAddressData() } returns mapOf()
        coEvery { topAdsHeadlineUseCase.setParams(any(), any()) } coAnswers {}
        coEvery { topAdsHeadlineUseCase.executeOnBackground() } returns TopAdsHeadlineResponse(
            displayAds = CpmModel(
                data = mutableListOf(
                    CpmData(
                        id = "",
                        adRefKey = "",
                        redirect = "",
                        adClickUrl = "",
                        cpm = Cpm(
                            cpmShop = CpmShop(
                                domain = "domain.com",
                                id = "shopId"
                            )
                        ),
                        applinks = ""
                    )
                )
            )
        )

        provideDefaultFeedPostMockData()
    }

    private fun getAuthorModelDefault() =
        FeedAuthorModel("", AuthorType.User, "", "", "", "", "", false)

    private fun getDummyFeedModel() = FeedModel(
        items = listOf(
            FeedCardImageContentModel(
                "image 1 id",
                FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER,
                FeedXCard.TYPE_FEED_TOP_ADS,
                getAuthorModelDefault().copy(id = "authorId"),
                "Title",
                "Subtitle",
                "Caption",
                FeedCardCtaModel(),
                "",
                "",
                "",
                "",
                "",
                emptyList(),
                0,
                FeedCardCampaignModel(id = "1"),
                false,
                emptyList(),
                "",
                "",
                FeedViewModel(),
                FeedLikeModel(),
                FeedCommentModel(),
                FeedShareModel("", getAuthorModelDefault(), "", "", ""),
                FeedFollowModel(isFollowed = true),
                emptyList(),
                emptyList(),
                "",
                0,
                "0",
                "",
                "",
                false,
                ""
            ),
            FeedCardVideoContentModel(
                "video 1 id",
                "FeedPlay",
                "Dummy Type",
                getAuthorModelDefault().copy(id = "authorId"),
                "Title",
                "Subtitle",
                "Caption",
                FeedCardCtaModel(),
                "",
                "",
                "",
                FeedCardCampaignModel(id = "1"),
                false,
                emptyList(),
                0,
                emptyList(),
                "",
                "",
                FeedViewModel(),
                FeedLikeModel(),
                FeedCommentModel(),
                FeedShareModel("", getAuthorModelDefault(), "", "", ""),
                FeedFollowModel(isFollowed = true),
                emptyList(),
                emptyList(),
                "",
                "1"
            ),
            FeedCardLivePreviewContentModel(
                "live 1 id",
                "",
                "",
                getAuthorModelDefault(),
                "",
                "",
                "",
                "",
                emptyList(),
                "",
                "",
                "",
                FeedFollowModel(isFollowed = true),
                emptyList(),
                false,
                FeedCardCampaignModel(id = "1"),
                emptyList()
            ),
            FeedCardImageContentModel(
                "image 2 id",
                "FeedProductHighlight",
                "Dummy Type",
                getAuthorModelDefault(),
                "Title",
                "Subtitle",
                "Caption",
                FeedCardCtaModel(),
                "",
                "",
                "",
                "",
                "",
                emptyList(),
                0,
                FeedCardCampaignModel(),
                false,
                emptyList(),
                "",
                "",
                FeedViewModel(),
                FeedLikeModel(),
                FeedCommentModel(),
                FeedShareModel("", getAuthorModelDefault(), "", "", ""),
                FeedFollowModel(isFollowed = false),
                emptyList(),
                emptyList(),
                "",
                0,
                "0",
                "",
                "",
                false,
                ""
            ),
            FeedCardVideoContentModel(
                "video 2 id",
                "FeedPlay",
                "Dummy Type",
                getAuthorModelDefault(),
                "Title",
                "Subtitle",
                "Caption",
                FeedCardCtaModel(),
                "",
                "",
                "",
                FeedCardCampaignModel(),
                false,
                emptyList(),
                0,
                emptyList(),
                "",
                "",
                FeedViewModel(),
                FeedLikeModel(),
                FeedCommentModel(),
                FeedShareModel("", getAuthorModelDefault(), "", "", ""),
                FeedFollowModel(isFollowed = false),
                emptyList(),
                emptyList(),
                "",
                ""
            ),
            FeedCardLivePreviewContentModel(
                "live 2 id",
                "",
                "",
                getAuthorModelDefault(),
                "",
                "",
                "",
                "",
                emptyList(),
                "",
                "",
                "",
                FeedFollowModel(isFollowed = false),
                emptyList(),
                false,
                FeedCardCampaignModel(),
                emptyList()
            ),
            FeedFollowRecommendationModel.Empty.copy(id = "follow-recommendation"),
            FeedNoContentModel(0, "", "", "")
        ),
        pagination = FeedPaginationModel(
            "",
            true,
            10
        )
    )

    private fun getDummyProfileRecommendationList(
        profileSize: Int = 5,
        cursor: String = "asdf",
    ): FeedFollowRecommendationModel {
        return FeedFollowRecommendationModel(
            id = "follow-recommendation",
            title = "Kayaknya joe bakal suka kreator ini~",
            description = "Yuk, follow mereka buat dapetin update & konten yang pas banget buat joe!",
            data = List(profileSize) {
                FeedFollowRecommendationModel.Profile(
                    id = it.toString(),
                    encryptedId = it.toString(),
                    name = "Profile $it",
                    badge = "",
                    type = if (it % 2 == 0) FeedFollowRecommendationModel.ProfileType.Seller else FeedFollowRecommendationModel.ProfileType.Ugc,
                    thumbnailUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg?b=UaM%25G%23Rjn4WYVBx%5DjFWX%3D~t6bbWB0PkWkqoL",
                    imageUrl = "https://images.tokopedia.net/img/seller_no_logo_1.png",
                    videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                    isFollowed = false,
                )
            },
            cursor = cursor,
            status = FeedFollowRecommendationModel.Status.Success,
        )
    }

    private fun getDummyShopFollowResponseData(
        success: Boolean = true,
        isFollowing: Boolean = true,
    ): ShopFollowModel {
        return ShopFollowModel(
            followShop = FollowShop(
                success = success,
                isFollowing = isFollowing,
            )
        )
    }

    private fun getDummyUserFollowResponseData(
        isError: Boolean = false,
    ) = ProfileDoFollowModelBase(
        profileFollowers = ProfileDoFollowedData(
            messages = if (isError) listOf("Error") else emptyList(),
            errorCode = if (isError) "123" else "",
            data = ProfileDoFollowedDataVal(
                userIdSource = "",
                userIdTarget = "",
                relation = "",
                isSuccess = "",
            )
        )
    )

    private fun getDummyUserUnfollowResponseData(
        isError: Boolean = false,
    ) = ProfileDoUnFollowModelBase(
        profileFollowers = ProfileDoFollowedData(
            messages = if (isError) listOf("Error") else emptyList(),
            errorCode = if (isError) "123" else "",
            data = ProfileDoFollowedDataVal(
                userIdSource = "",
                userIdTarget = "",
                relation = "",
                isSuccess = "",
            )
        )
    )
}
