package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.common.comment.usecase.GetCountCommentsUseCase
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.data.pojo.shopmutation.FollowShop
import com.tokopedia.feedcomponent.data.pojo.shopmutation.ShopFollowModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowModelBase
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedData
import com.tokopedia.feedcomponent.people.model.ProfileDoFollowedDataVal
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedplus.domain.usecase.FeedCampaignCheckReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedCampaignReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedCommentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.feedplus.presentation.model.FeedPaginationModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedViewModel
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
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

    private lateinit var viewModel: FeedPostViewModel

    @Before
    fun setUp() {
        viewModel = FeedPostViewModel(
            feedXHomeUseCase,
            atcUseCase,
            likeContentUseCase,
            deletePostUseCase,
            userSession,
            shopFollowUseCase,
            userFollowUseCase,
            campaignReminderUseCase,
            checkCampaignReminderUseCase,
            topAdsHeadlineUseCase,
            mvcSummaryUseCase,
            topAdsAddressHelper,
            getCountCommentsUseCase,
            testDispatcher
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
        val dummyData = ShopFollowModel(
            followShop = FollowShop(
                success = true,
                isFollowing = true
            )
        )

        coEvery { shopFollowUseCase.createParams(any()) } returns mapOf()
        coEvery { shopFollowUseCase(any()) } returns dummyData

        // when
        viewModel.doFollow("", "", true)

        // then
        assert(viewModel.followResult.value is Success)
        assert((viewModel.followResult.value as Success).data == "toko")
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

        val dummyData = FeedTaggedProductUiModel(
            "dummyId",
            FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            "dummy title",
            "dummy image url",
            FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            "dummy applink",
            FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            )
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
            "dummyId",
            FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            "dummy title",
            "dummy image url",
            FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            "dummy applink",
            FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            )
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
            "dummyId",
            FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            "dummy title",
            "dummy image url",
            FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            "dummy applink",
            FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            )
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

        val dummyData = FeedTaggedProductUiModel(
            "dummyId",
            FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            "dummy title",
            "dummy image url",
            FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            "dummy applink",
            FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            )
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
            "dummyId",
            FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            "dummy title",
            "dummy image url",
            FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            "dummy applink",
            FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            )
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
            "dummyId",
            FeedTaggedProductUiModel.Shop(
                "dummy-shop-id",
                "dummy Name"
            ),
            "dummy title",
            "dummy image url",
            FeedTaggedProductUiModel.NormalPrice(
                "Rp1.000.000",
                1000000.0
            ),
            "dummy applink",
            FeedTaggedProductUiModel.Campaign(
                FeedTaggedProductUiModel.CampaignType.NoCampaign,
                FeedTaggedProductUiModel.CampaignStatus.Unknown,
                false
            )
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
        viewModel.fetchFeedPosts("", true, "")

        // then
        assert(viewModel.feedHome.value is Fail)
        assert((viewModel.feedHome.value as Fail).throwable is MessageErrorException)
        assert((viewModel.feedHome.value as Fail).throwable.message == "Failed")
    }

    private fun getDummyFeedModel() = FeedModel(
        items = listOf(
            FeedCardImageContentModel(
                "activity id",
                "FeedProductHighlight",
                "Dummy Type",
                FeedAuthorModel(),
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
                FeedShareModel("", FeedAuthorModel(), "", "", ""),
                FeedFollowModel(),
                true,
                true,
                true,
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
                "activity id",
                "FeedPlay",
                "Dummy Type",
                FeedAuthorModel(),
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
                FeedShareModel("", FeedAuthorModel(), "", "", ""),
                FeedFollowModel(),
                true,
                true,
                true,
                emptyList(),
                "",
                ""
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
