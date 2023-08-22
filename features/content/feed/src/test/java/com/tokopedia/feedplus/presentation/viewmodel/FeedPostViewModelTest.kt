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
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.report_content.model.UserReportSubmissionResponse
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.content.common.usecase.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.UpcomingCampaignResponse
import com.tokopedia.feedcomponent.data.pojo.shopmutation.FollowShop
import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.domain.mapper.ProductMapper
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowModelBase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedData
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedDataVal
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.domain.usecase.FeedCampaignCheckReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedCampaignReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedCommentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowModel
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
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    private val affiliateCookieHelper: AffiliateCookieHelper = mockk()
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase = mockk()
    private val uiEventManager = UiEventManager<FeedPostEvent>()
    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase = mockk()

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
            uiEventManager = uiEventManager,
            feedXGetActivityProductsUseCase = feedXGetActivityProductsUseCase,
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
    fun onClearMerchantVoucher_shouldReturnNull() {
        // when
        viewModel.clearMerchantVoucher()

        // then
        assert(viewModel.merchantVoucherLiveData.value == null)
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
        val dummyData = ShopFollowModel(
            followShop = FollowShop(
                success = true,
                isFollowing = true
            )
        )

        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase(any()) } returns dummyData

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
        viewModel.fetchFeedPosts("", postSource = null)

        // then
        assert(viewModel.feedHome.value is Fail)
        assert((viewModel.feedHome.value as Fail).throwable is MessageErrorException)
        assert((viewModel.feedHome.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithoutRelevantPostInCDP() {
        // given
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()

        // when
        viewModel.fetchFeedPosts("", true, postSource = PostSourceModel("1", null, true))

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == 7)
        assert(data.items[0] is FeedCardImageContentModel)
        assert(data.items[1] is FeedCardVideoContentModel)
        assert(data.items[2] is FeedCardLivePreviewContentModel)
        assert(data.items[3] is FeedCardImageContentModel)
        assert(data.items[4] is FeedCardVideoContentModel)
        assert(data.items[5] is FeedCardLivePreviewContentModel)
        assert(data.items[6] is FeedNoContentModel)
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithoutRelevantPost() {
        // given
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()

        // when
        viewModel.fetchFeedPosts("", postSource = PostSourceModel("1", null, true))

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == 7)
        assert(data.items[0] is FeedCardImageContentModel)
        assert(data.items[1] is FeedCardVideoContentModel)
        assert(data.items[2] is FeedCardLivePreviewContentModel)
        assert(data.items[3] is FeedCardImageContentModel)
        assert(data.items[4] is FeedCardVideoContentModel)
        assert(data.items[5] is FeedCardLivePreviewContentModel)
        assert(data.items[6] is FeedNoContentModel)
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithRelevantPost() {
        // given
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase.createPostDetailParams("1") } returns emptyMap()
        coEvery { feedXHomeUseCase.createParamsWithId("1", any()) } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()

        // when
        viewModel.fetchFeedPosts("", true, PostSourceModel("1", null, false))

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == 14)
        assert(data.items[0] is FeedCardImageContentModel)
        assert(data.items[1] is FeedCardVideoContentModel)
        assert(data.items[2] is FeedCardLivePreviewContentModel)
        assert(data.items[3] is FeedCardImageContentModel)
        assert(data.items[4] is FeedCardVideoContentModel)
        assert(data.items[5] is FeedCardLivePreviewContentModel)
        assert(data.items[6] is FeedNoContentModel)
        assert(data.items[7] is FeedCardImageContentModel)
        assert(data.items[8] is FeedCardVideoContentModel)
        assert(data.items[9] is FeedCardLivePreviewContentModel)
        assert(data.items[10] is FeedCardImageContentModel)
        assert(data.items[11] is FeedCardVideoContentModel)
        assert(data.items[12] is FeedCardLivePreviewContentModel)
        assert(data.items[13] is FeedNoContentModel)
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
        assert(feedData.items[6] is FeedNoContentModel)
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

    @Test
    fun fetchFeedProduct_whenFailed_shouldBeFail() {
        // given
        val activityId = "123456"
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } throws Throwable("Failed")

        // when
        viewModel.fetchFeedProduct(
            activityId,
            emptyList(),
            FeedTaggedProductUiModel.SourceType.Organic
        )

        // then
        assert(viewModel.feedTagProductList.value is Fail)
        val response = viewModel.feedTagProductList.value as Fail
        assert(response.throwable.message == "Failed")
    }

    @Test
    fun fetchFeedProduct_whenSuccess_shouldBeSuccess() {
        // given
        val activityId = "123456"
        val dummyData = getDummyData()
        val productList = dummyData.data.products.map {
            ProductMapper.transform(
                it,
                dummyData.data.campaign,
                FeedTaggedProductUiModel.SourceType.Organic
            )
        }
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData()

        // when
        viewModel.fetchFeedProduct(
            activityId,
            productList,
            FeedTaggedProductUiModel.SourceType.Organic
        )

        // then
        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == getDummyData().data.products.size)
    }

    @Test
    fun fetchFeedProduct_whenSuccessWithoutDefault_shouldBeSuccess() {
        // given
        val activityId = "123456"
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData()

        // when
        viewModel.fetchFeedProduct(
            activityId,
            emptyList(),
            FeedTaggedProductUiModel.SourceType.Organic
        )

        // then
        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == getDummyData().data.products.size)
    }

    /**
     * submit report
     */

    @Test
    fun `when submit report return success`() {
        coEvery { userSession.userId } returns "13232"
        coEvery { userSession.isLoggedIn } returns true

        val response = UserReportSubmissionResponse(
            submissionReport = UserReportSubmissionResponse.Result(
                "success"
            )
        )

        coEvery { postReportUseCase.createParam(any(), any(), any(), any(), any(), any(), any(), any()) } coAnswers { RequestParams() }
        coEvery { postReportUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { postReportUseCase.executeOnBackground() } returns response

        viewModel.selectReport(PlayUserReportReasoningUiModel.Reasoning.Empty)
        viewModel.submitReport("", 0L, FeedCardVideoContentModel.Empty)

        assert(viewModel.isReported.value is Success)
        assert(viewModel.selectedReport == PlayUserReportReasoningUiModel.Reasoning.Empty)
    }

    @Test
    fun `when submit report video return error from be`() {
        coEvery { userSession.isLoggedIn } returns true

        coEvery { postReportUseCase.executeOnBackground() } throws MessageErrorException()

        viewModel.selectReport(PlayUserReportReasoningUiModel.Reasoning.Empty)
        viewModel.submitReport("", 0L, FeedCardVideoContentModel.Empty)

        assert(viewModel.isReported.value is Fail)
        assert(viewModel.selectedReport == PlayUserReportReasoningUiModel.Reasoning.Empty)
    }

    @Test
    fun `when submit report video return error`() {
        coEvery { userSession.isLoggedIn } returns true

        val response = UserReportSubmissionResponse(
            submissionReport = UserReportSubmissionResponse.Result(
                "failed"
            )
        )
        coEvery { postReportUseCase.executeOnBackground() } returns response

        viewModel.selectReport(PlayUserReportReasoningUiModel.Reasoning.Empty)
        viewModel.submitReport("", 0L, FeedCardVideoContentModel.Empty)

        assert(viewModel.isReported.value is Fail)
        assert(viewModel.selectedReport == PlayUserReportReasoningUiModel.Reasoning.Empty)
    }

    /**
     * get list
     */

    @Test
    fun `get reasoning list is success`() {
        val response = UserReportOptions.Response(
            data = listOf(
                UserReportOptions(
                    id = 1,
                    value = "Harga melanggar etika",
                    detail = ""
                ),
                UserReportOptions(
                    id = 11,
                    value = "Melanggar HAM",
                    detail = ""
                ),
                UserReportOptions(
                    id = 12,
                    value = "SARA",
                    detail = ""
                ),
                UserReportOptions(
                    id = 9,
                    value = "Melanggar etik",
                    detail = ""
                )
            )
        )

        coEvery { getReportListUseCase.executeOnBackground() } returns response
        viewModel.getReport()

        assert(viewModel.userReportList is Success)
        assert((viewModel.userReportList as Success<List<PlayUserReportReasoningUiModel>>).data.isNotEmpty())
    }

    @Test
    fun `get reasoning list is error`() {
        coEvery { getReportListUseCase.executeOnBackground() } throws MessageErrorException()
        viewModel.getReport()

        assert(viewModel.userReportList is Fail)
    }

    private fun getDummyData(): FeedXGQLResponse = FeedXGQLResponse(
        data = FeedXGetActivityProductsResponse(
            products = listOf(
                FeedXProduct(shopID = "09876", id = "1"),
                FeedXProduct(id = "2"),
                FeedXProduct(id = "3"),
                FeedXProduct(id = "4"),
                FeedXProduct(id = "5")
            ),
            isFollowed = true,
            contentType = "content type",
            campaign = FeedXCampaign(),
            nextCursor = "",
            hasVoucher = false
        )
    )

    private fun provideDefaultFeedPostMockData() {
        coEvery { feedXHomeUseCase.createParams(any(), any(), any(), any()) } returns emptyMap()
        coEvery { feedXHomeUseCase.createPostDetailParams("1") } returns emptyMap()
        coEvery { feedXHomeUseCase(any()) } returns getDummyFeedModel()
        viewModel.fetchFeedPosts("")
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
            FeedNoContentModel(0, "", "", "")
        ),
        pagination = FeedPaginationModel(
            "",
            true,
            10
        )
    )
}
