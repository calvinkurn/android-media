package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.report_content.model.UserReportSubmissionResponse
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feed.common.comment.model.CountComment
import com.tokopedia.feed.common.comment.usecase.GetCountCommentsUseCase
import com.tokopedia.feed.component.product.FeedProductPaging
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
import com.tokopedia.feedcomponent.people.model.ProfileDoUnFollowModelBase
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedGetChannelStatusEntity
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.domain.FeedRepository
import com.tokopedia.feedplus.domain.usecase.FeedCampaignCheckReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedCampaignReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedGetChannelStatusUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXRecomWidgetUseCase
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
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
import com.tokopedia.feedplus.presentation.model.FeedProductActionModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedViewModel
import com.tokopedia.feedplus.presentation.model.PostSourceModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.feedplus.presentation.model.type.FeedContentType
import com.tokopedia.feedplus.presentation.tooltip.FeedTooltipManager
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
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.CpmShop
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
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
    val coroutineTestRule = CoroutineTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    private val repository: FeedRepository = mockk()
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
    private val trackReportViewerUseCase: BroadcasterReportTrackViewerUseCase =
        mockk(relaxed = true)
    private val getReportListUseCase: GetUserReportListUseCase = mockk()
    private val postReportUseCase: PostUserReportUseCase = mockk()
    private val feedXRecomWidgetUseCase: FeedXRecomWidgetUseCase = mockk()
    private val affiliateCookieHelper: AffiliateCookieHelper = mockk()
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase = mockk()
    private val uiEventManager = UiEventManager<FeedPostEvent>()
    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase = mockk()
    private val feedGetChannelStatusUseCase: FeedGetChannelStatusUseCase = mockk()
    private val tooltipManager: FeedTooltipManager = mockk()

    private lateinit var viewModel: FeedPostViewModel

    private val playChannelId get() = (getDummyFeedModel().items[1] as FeedCardVideoContentModel).playChannelId

    @Before
    fun setUp() {
        every { userSession.isLoggedIn } returns false

        viewModel = FeedPostViewModel(
            repository = repository,
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
            trackReportTrackViewerUseCase = trackReportViewerUseCase,
            submitReportUseCase = submitReportUseCase,
            getReportUseCase = getReportListUseCase,
            postReportUseCase = postReportUseCase,
            feedXRecomWidgetUseCase = feedXRecomWidgetUseCase,
            uiEventManager = uiEventManager,
            feedXGetActivityProductsUseCase = feedXGetActivityProductsUseCase,
            feedGetChannelStatusUseCase = feedGetChannelStatusUseCase,
            dispatchers = testDispatcher,
            tooltipManager = tooltipManager,
        )
    }

    @Test
    fun getScrollPosition_onInitial_shouldBeNull() {
        assert(viewModel.getScrollPosition() == null)
    }

    @Test
    fun getScrollPosition_whenChanged_shouldBeChanged() {
        coEvery { tooltipManager.isShowTooltip(any()) } returns false

        // given
        val position = 1

        // when
        viewModel.saveScrollPosition(position)

        // then
        assert(viewModel.getScrollPosition() == position)
    }

    @Test
    fun shouldFetchInitialPost_whenNotLoggedInAndFirstTime_shouldReturnTrue() {
        assert(viewModel.shouldFetchInitialPost())
    }

    @Test
    fun shouldFetchInitialPost_whenNotLoggedInAndAlreadyHaveData_shouldReturnTrue() {
        coEvery { userSession.isLoggedIn } returns true
        provideDefaultFeedPostMockData()
        assert(!viewModel.shouldFetchInitialPost())
    }

    @Test
    fun shouldFetchInitialPost_whenLoggedInAndFirstTime_shouldReturnTrue() {
        coEvery { userSession.isLoggedIn } returns true

        assert(viewModel.shouldFetchInitialPost())
    }

    @Test
    fun shouldFetchInitialPost_whenLoggedInAndAlreadyHaveData_shouldReturnTrue() {
        coEvery { userSession.isLoggedIn } returns false andThen true
        provideDefaultFeedPostMockData()

        assert(viewModel.shouldFetchInitialPost())
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

        val dummyData = ContentTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = ContentTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = ContentTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = ContentTaggedProductUiModel.Campaign(
                ContentTaggedProductUiModel.CampaignType.NoCampaign,
                ContentTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = ContentTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            ContentTaggedProductUiModel.Stock.Available
        )

        // when
        viewModel.suspendAddProductToCart(dummyData, FeedProductActionModel.Source.BottomSheet)
        viewModel.processSuspendedAddProductToCart()

        // then
        assert(viewModel.observeAddProductToCart.value is Fail)
        assert((viewModel.observeAddProductToCart.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onAtc_whenFailed() {
        // given
        val dummyData = ContentTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = ContentTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = ContentTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = ContentTaggedProductUiModel.Campaign(
                ContentTaggedProductUiModel.CampaignType.NoCampaign,
                ContentTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = ContentTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            ContentTaggedProductUiModel.Stock.Available
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
        viewModel.addProductToCart(dummyData, FeedProductActionModel.Source.BottomSheet)

        // then
        assert(viewModel.observeAddProductToCart.value is Fail)
        assert((viewModel.observeAddProductToCart.value as Fail).throwable is ResponseErrorException)
        assert((viewModel.observeAddProductToCart.value as Fail).throwable.message == "Error Message")
    }

    @Test
    fun onAtc_whenSuccess() {
        // given
        val dummyData = ContentTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = ContentTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = ContentTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = ContentTaggedProductUiModel.Campaign(
                ContentTaggedProductUiModel.CampaignType.NoCampaign,
                ContentTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = ContentTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            ContentTaggedProductUiModel.Stock.Available
        )

        val cartId = "123"
        val dummyAtcResult = AddToCartDataModel(
            errorMessage = arrayListOf(),
            status = "OK",
            data = DataModel(message = arrayListOf(), cartId = cartId),
            errorReporter = ErrorReporterModel(),
            responseJson = ""
        )

        val dummySuccess = FeedProductActionModel(
            cartId = cartId,
            product = dummyData,
            source = FeedProductActionModel.Source.BottomSheet
        )

        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } returns dummyAtcResult
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        // when
        viewModel.addProductToCart(dummyData, FeedProductActionModel.Source.BottomSheet)

        // then
        assert(viewModel.observeAddProductToCart.value is Success)
        assert((viewModel.observeAddProductToCart.value as Success).data == dummySuccess)
    }

    @Test
    fun onSuspendBuy() {
        // given
        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } throws MessageErrorException("Failed")
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        val dummyData = ContentTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = ContentTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = ContentTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = ContentTaggedProductUiModel.Campaign(
                ContentTaggedProductUiModel.CampaignType.NoCampaign,
                ContentTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = ContentTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            ContentTaggedProductUiModel.Stock.Available
        )

        // when
        viewModel.suspendBuyProduct(dummyData, FeedProductActionModel.Source.BottomSheet)
        viewModel.processSuspendedBuyProduct()

        // then
        assert(viewModel.observeBuyProduct.value is Fail)
        assert((viewModel.observeBuyProduct.value as Fail).throwable.message == "Failed")
    }

    @Test
    fun onBuyProduct_whenFailed() {
        // given
        val dummyData = ContentTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = ContentTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = ContentTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = ContentTaggedProductUiModel.Campaign(
                ContentTaggedProductUiModel.CampaignType.NoCampaign,
                ContentTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = ContentTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            ContentTaggedProductUiModel.Stock.Available
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
        viewModel.buyProduct(dummyData, FeedProductActionModel.Source.BottomSheet)

        // then
        assert(viewModel.observeBuyProduct.value is Fail)
        assert((viewModel.observeBuyProduct.value as Fail).throwable is ResponseErrorException)
        assert((viewModel.observeBuyProduct.value as Fail).throwable.message == "Error Message")
    }

    @Test
    fun onBuyProduct_whenSuccess() {
        // given
        val dummyData = ContentTaggedProductUiModel(
            id = "dummyId",
            parentID = "123",
            showGlobalVariant = false,
            shop = ContentTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            title = "dummy title",
            imageUrl = "dummy image url",
            price = ContentTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            appLink = "dummy applink",
            campaign = ContentTaggedProductUiModel.Campaign(
                ContentTaggedProductUiModel.CampaignType.NoCampaign,
                ContentTaggedProductUiModel.CampaignStatus.Unknown,
                false
            ),
            affiliate = ContentTaggedProductUiModel.Affiliate(
                "xxx",
                "play"
            ),
            ContentTaggedProductUiModel.Stock.Available
        )

        val cartId = "123"
        val dummyAtcResult = AddToCartDataModel(
            errorMessage = arrayListOf(),
            status = "OK",
            data = DataModel(message = arrayListOf(), cartId = cartId),
            errorReporter = ErrorReporterModel(),
            responseJson = ""
        )

        val dummySuccess = FeedProductActionModel(
            cartId = cartId,
            product = dummyData,
            source = FeedProductActionModel.Source.BottomSheet
        )

        coEvery { userSession.userId } returns "1"
        coEvery { atcUseCase.setParams(any()) } coAnswers {}
        coEvery { atcUseCase.executeOnBackground() } returns dummyAtcResult
        coEvery { affiliateCookieHelper.initCookie(any(), any(), any()) } coAnswers {}

        // when
        viewModel.buyProduct(dummyData, FeedProductActionModel.Source.BottomSheet)

        // then
        assert(viewModel.observeBuyProduct.value is Success)
        assert((viewModel.observeBuyProduct.value as Success).data == dummySuccess)
    }

    @Test
    fun onFetchFeedPosts_whenFailed() {
        // given
        coEvery {
            repository.getPost(
                any(),
                any(),
                any(),
                any()
            )
        } throws MessageErrorException("Failed")

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
        val dummyData = getDummyFeedModel()
        coEvery { repository.getPost(any(), any(), any(), any()) } returns dummyData

        // when
        viewModel.fetchFeedPosts(
            "",
            true,
            postSource = PostSourceModel("1", FeedBaseFragment.TAB_TYPE_CDP)
        )

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)
        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == dummyData.items.size)
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
    fun onFetchFeedPosts_whenSuccessWithoutRelevantPost() {
        // given
        coEvery { repository.getPost(any(), any(), any(), any()) } returns getDummyFeedModel()

        // when
        viewModel.fetchFeedPosts(
            "",
            postSource = PostSourceModel("1", FeedBaseFragment.TAB_TYPE_CDP)
        )

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
        assert(viewModel.hasNext)
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithRelevantPost() {
        // given
        coEvery { repository.getPost(any(), any(), any(), any()) } returns getDummyFeedModel()
        coEvery { repository.getRelevantPosts(any()) } returns getDummyFeedModel()

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

        assert(viewModel.determinePostDataEligibilityForOnboarding(isFollowingTab = false))
        assert(viewModel.determinePostDataEligibilityForOnboarding(isFollowingTab = true))
    }

    @Test
    fun onFetchFeedPosts_whenSuccessWithOnlyFollowRecomWidget() {
        // given
        coEvery {
            repository.getPost(
                any(),
                any(),
                any(),
                any()
            )
        } returns getDummyFollowRecommendationWidgetOnlyModel()
        coEvery { feedXRecomWidgetUseCase(any()) } returns getDummyProfileRecommendationList()

        // when
        viewModel.fetchFeedPosts(
            "",
            postSource = PostSourceModel("1", FeedBaseFragment.TAB_TYPE_CDP)
        )

        // then
        assert(!viewModel.shouldShowNoMoreContent)
        assert(viewModel.feedHome.value is Success)

        val data = (viewModel.feedHome.value as Success).data
        assert(data.items.size == 1)
        assert(data.items[0] is FeedFollowRecommendationModel)

        assert(viewModel.determinePostDataEligibilityForOnboarding(isFollowingTab = false))
        assert(!viewModel.determinePostDataEligibilityForOnboarding(isFollowingTab = true))
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
    fun onTrackChannelProduct() {
        runTest(testDispatcher.coroutineDispatcher) {
            coEvery { trackReportViewerUseCase.executeOnBackground() } returns true
            // when
            viewModel.trackPerformance(
                playChannelId,
                listOf("2", "4"),
                BroadcasterReportTrackViewerUseCase.Companion.Event.ProductChanges
            )

            coVerify { trackReportViewerUseCase.executeOnBackground() }
        }

    }

    @Test
    fun onTrackVisitChannel() {
        runTest(testDispatcher.coroutineDispatcher) {
            coEvery { trackReportViewerUseCase.executeOnBackground() } returns true

            viewModel.trackPerformance(
                playChannelId,
                emptyList(),
                BroadcasterReportTrackViewerUseCase.Companion.Event.Visit
            )

            coVerify { trackReportViewerUseCase.executeOnBackground() }
        }

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

        val throwable = Throwable("Failed")
        coEvery { feedXGetActivityProductsUseCase(any()) } throws throwable
        val expected = FeedProductPaging(state = ResultState.Fail(throwable), products = emptyList(), cursor = "")

        // then
        val result = recordProduct {
            // when
            viewModel.fetchFeedProduct(
                activityId,
                emptyList(),
                ContentTaggedProductUiModel.SourceType.Organic,
                "video"
            )
        }
        assert(result.state == expected.state)
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
                ContentTaggedProductUiModel.SourceType.Organic
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
        val result = recordProduct {
            viewModel.fetchFeedProduct(
                activityId,
                productList,
                ContentTaggedProductUiModel.SourceType.Organic,
                "video"
            )
        }

        // then
        assert(result.state is ResultState.Success)
        assert(result.products.size == getDummyData().data.products.size)
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
        val result = recordProduct {
            viewModel.fetchFeedProduct(
                activityId,
                emptyList(),
                ContentTaggedProductUiModel.SourceType.Organic,
                "video"
            )
        }

        // then
        assert(result.state is ResultState.Success)
        assert(result.products.size == getDummyData().data.products.size)
    }

    @Test
    fun fetchFeedProduct_hasNextPage() {
        // given
        val activityId = "123456"
        val cursor = "abKG"
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData(cursor = cursor)

        // when
        val result = recordProduct {
            viewModel.fetchFeedProduct(
                activityId,
                emptyList(),
                ContentTaggedProductUiModel.SourceType.Organic,
                "video"
            )
        }

        // then
        println("hello $result")
        assert(result.state is ResultState.Success)
        assert(result.products.size == getDummyData().data.products.size)
        assert(result.hasNextPage)
        assert(result.cursor == cursor)
    }

    @Test
    fun fetchFeedProduct_nextPage() {
        // given
        val activityId = "123456"
        val cursor = "kOjn"
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData(cursor = cursor)

        val expected = buildList {
            add(ContentTaggedProductUiModel(
                id = "99129",
                parentID = "123",
                showGlobalVariant = false,
                shop = ContentTaggedProductUiModel.Shop(
                    "dummy-shop-id",
                    "dummy Name"
                ),
                title = "dummy title",
                imageUrl = "dummy image url",
                price = ContentTaggedProductUiModel.NormalPrice(
                    "Rp1.000.000",
                    1000000.0
                ),
                appLink = "dummy applink",
                campaign = ContentTaggedProductUiModel.Campaign(
                    ContentTaggedProductUiModel.CampaignType.NoCampaign,
                    ContentTaggedProductUiModel.CampaignStatus.Unknown,
                    false
                ),
                affiliate = ContentTaggedProductUiModel.Affiliate(
                    "xxx",
                    "play"
                ),
                ContentTaggedProductUiModel.Stock.Available
            ))
        }

        // when
        val result = recordProduct {
            viewModel.fetchFeedProduct(
                activityId,
                expected,
                ContentTaggedProductUiModel.SourceType.Organic,
                "video"
            )
        }

        println("hello 2 $result")

        // then
        assert(result.state is ResultState.Success)
        assert(result.products.size == getDummyData().data.products.size + expected.size)
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

        coEvery {
            postReportUseCase.createParam(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } coAnswers { RequestParams() }
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

    private fun getDummyData(cursor: String = ""): FeedXGQLResponse = FeedXGQLResponse(
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
            nextCursor = cursor,
            hasVoucher = false
        )
    )

    /** Follow Recommendation */
    @Test
    fun onInitialFetchFollowRecommendation_Success() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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
        assert(viewModel.followRecommendationResult.value is Fail)
    }

    @Test
    fun onFetchFollowRecommendation_NoMorePage_LoadMore() {
        // prepare
        val mockFollowRecommendationDataWithoutCursor =
            getDummyProfileRecommendationList(cursor = "")

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // test
        coEvery { shopFollowUseCase(any()) } returns mockFollowed
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // double test
        coEvery { shopFollowUseCase(any()) } returns mockNotFollowed
        viewModel.doUnfollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // test
        coEvery { shopFollowUseCase(any()) } returns mockShopFollowNotSuccess
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // follow first
        coEvery { shopFollowUseCase(any()) } returns mockShopFollowSuccess
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)

        // test
        coEvery { shopFollowUseCase(any()) } returns mockShopUnfollowNotSuccess
        viewModel.doUnfollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData
        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase.createParams(any(), any()) } returns mapOf()

        provideMockForFollowRecommendation()

        // test
        coEvery { shopFollowUseCase(any()) } throws mockException
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        coEvery { userFollowUseCase.executeOnBackground(any()) } returns getDummyUserFollowResponseData()
        coEvery { userUnfollowUseCase.executeOnBackground(any()) } returns getDummyUserUnfollowResponseData()

        provideMockForFollowRecommendation()

        // test
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // double test
        viewModel.doUnfollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        coEvery { userFollowUseCase.executeOnBackground(any()) } returns getDummyUserFollowResponseData()
        coEvery { userUnfollowUseCase.executeOnBackground(any()) } returns getDummyUserUnfollowResponseData(
            isError = true
        )

        provideMockForFollowRecommendation()

        // follow first
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // test
        viewModel.doUnfollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        coEvery { userFollowUseCase.executeOnBackground(any()) } returns getDummyUserFollowResponseData()
        coEvery { userUnfollowUseCase.executeOnBackground(any()) } throws mockException

        provideMockForFollowRecommendation()

        // follow first
        viewModel.doFollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data[selectedProfileIndex].isFollowed)
        assert(viewModel.followResult.value is Success)

        // test
        viewModel.doUnfollowProfileRecommendation(
            selectedProfile.id,
            selectedProfile.encryptedId,
            selectedProfile.isShop
        )

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

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
        coEvery { feedXRecomWidgetUseCase(any()) } returns mockFollowRecommendationData

        provideMockForFollowRecommendation()

        // test
        viewModel.removeProfileRecommendation(selectedRemovedProfile)

        // verify
        assert(viewModel.feedHome.value is Success)

        val followRecomModel = (viewModel.feedHome.value as Success).data.items[6]

        assert(followRecomModel is FeedFollowRecommendationModel)
        assert((followRecomModel as FeedFollowRecommendationModel).data.size == mockFollowRecommendationData.data.size - 1)
        assert(followRecomModel.data.indexOf(selectedRemovedProfile) == -1)
    }

    @Test
    fun RemoveProfileRecommendation_NotFound() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedRemovedProfile = mockFollowRecommendationData.data[1].copy(
            id = "asdfasdf",
            encryptedId = "asdfasdf"
        )

        coEvery {
            feedXRecomWidgetUseCase.createFeedFollowRecomParams(
                any(),
                any()
            )
        } returns mapOf()
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
        assert(followRecomModel.data.size == mockFollowRecommendationData.data.size)
    }

    @Test
    fun RemoveProfileRecommendation_NoRecommendation_TryToRemove() {
        // prepare
        val mockFollowRecommendationData = getDummyProfileRecommendationList()
        val selectedRemovedProfile = mockFollowRecommendationData.data[1]

        coEvery {
            repository.getPost(
                any(),
                any(),
                any(),
                any()
            )
        } throws MessageErrorException("Failed")
        viewModel.fetchFeedPosts("", true, null)

        // test
        viewModel.removeProfileRecommendation(selectedRemovedProfile)

        // verify
        assert(viewModel.feedHome.value is Fail)
    }

    @Test
    fun onUpdateChannelStatus_shouldUpdateData() {
        // given
        provideDefaultFeedPostMockData()
        coEvery { feedGetChannelStatusUseCase(any()) } returns FeedGetChannelStatusEntity(
            playGetChannelsStatus = FeedGetChannelStatusEntity.Data(
                data = listOf(
                    FeedGetChannelStatusEntity.ChannelStatus(
                        channelId = "123",
                        status = "freeze"
                    )
                )
            )
        )

        // when
        viewModel.updateChannelStatus("123")

        // then
        val dataItems = (viewModel.feedHome.value as Success).data.items
        dataItems.forEach {
            when {
                it is FeedCardLivePreviewContentModel && it.playChannelId == "123" -> assert(!it.isLive)
            }
        }
    }

    /** Tooltip */
    @Test
    fun saveScrollPosition_showTooltipByPosition() {
        coEvery { tooltipManager.showTooltipEvent() } returns Unit
        coEvery { tooltipManager.isShowTooltip(any()) } answers { arg<Int>(0) == 4 }

        repeat(5) {
            viewModel.saveScrollPosition(it)

            coVerify(exactly = if (it == 4) 1 else 0) { tooltipManager.showTooltipEvent() }
        }
    }

    private fun provideDefaultFeedPostMockData() {
        coEvery { repository.getPost(any(), any(), any(), any()) } returns getDummyFeedModel()
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
        FeedAuthorModel("", AuthorType.User, "", "", "", "", "")

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
                "",
                false,
                "",
                FeedContentType.TopAds
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
                "1",
                FeedContentType.PlayShortVideo
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
                "123",
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
                "",
                false,
                "",
                FeedContentType.ProductHighlight
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
                "",
                FeedContentType.PlayChannel
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

    private fun getDummyFollowRecommendationWidgetOnlyModel() = FeedModel(
        items = listOf(
            FeedFollowRecommendationModel.Empty.copy(id = "follow-recommendation")
        ),
        pagination = FeedPaginationModel(
            "",
            true,
            10
        )
    )

    private fun getDummyProfileRecommendationList(
        profileSize: Int = 5,
        cursor: String = "asdf"
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
                    applink = "applink",
                    isFollowed = false
                )
            },
            cursor = cursor,
            status = FeedFollowRecommendationModel.Status.Success
        )
    }

    private fun getDummyShopFollowResponseData(
        success: Boolean = true,
        isFollowing: Boolean = true
    ): ShopFollowModel {
        return ShopFollowModel(
            followShop = FollowShop(
                success = success,
                isFollowing = isFollowing
            )
        )
    }

    private fun getDummyUserFollowResponseData(
        isError: Boolean = false
    ) = ProfileDoFollowModelBase(
        profileFollowers = ProfileDoFollowedData(
            messages = if (isError) listOf("Error") else emptyList(),
            errorCode = if (isError) "123" else "",
            data = ProfileDoFollowedDataVal(
                userIdSource = "",
                userIdTarget = "",
                relation = "",
                isSuccess = ""
            )
        )
    )

    private fun getDummyUserUnfollowResponseData(
        isError: Boolean = false
    ) = ProfileDoUnFollowModelBase(
        profileFollowers = ProfileDoFollowedData(
            messages = if (isError) listOf("Error") else emptyList(),
            errorCode = if (isError) "123" else "",
            data = ProfileDoFollowedDataVal(
                userIdSource = "",
                userIdTarget = "",
                relation = "",
                isSuccess = ""
            )
        )
    )

    private fun recordProduct(fn: suspend () -> Unit): FeedProductPaging {
        val scope = CoroutineScope(testDispatcher.coroutineDispatcher)
        lateinit var products: FeedProductPaging
        scope.launch {
            viewModel.feedTagProductList.collect {
                products = it
            }
        }
        testDispatcher.coroutineDispatcher.runBlockingTest { fn() }
        testDispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return products
    }
}
