package com.tokopedia.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.WishlistMockTimber
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2BulkRemoveAdditionalParams
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TICKER
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import com.tokopedia.wishlistcommon.data.WishlistV2Params
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import timber.log.Timber

@RunWith(JUnit4::class)
class WishlistV2ViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistV2ViewModel: WishlistV2ViewModel
    private var wishlistOneItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistTwoItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistThreeItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistFourItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistFiveItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var emptyWishlistItem = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistV2Response = WishlistV2Response()
    private var wishlistV2ResponseWith1Data = WishlistV2Response(data = WishlistV2Response.Data(wishlistV2 = WishlistV2Response.Data.WishlistV2(totalData = 1)))
    private var recommendationWidget = RecommendationWidget()
    private var wishlistRecommendation = WishlistV2RecommendationDataModel()
    private var listProductCardModel = listOf<ProductCardModel>()
    private var listRecommendationItem = listOf<RecommendationItem>()
    private var topAdsImageViewModel = TopAdsImageViewModel()
    private var tickerState = WishlistV2Response.Data.WishlistV2.TickerState()
    private var timber = WishlistMockTimber()

    @RelaxedMockK
    lateinit var wishlistV2UseCase: WishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    lateinit var getSingleRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    private val deleteWishlistProgressStatusOkErrorEmpty = DeleteWishlistProgressResponse(
        deleteWishlistProgress = DeleteWishlistProgressResponse.DeleteWishlistProgress(status = "OK", errorMessage = emptyList())
    )
    private val deleteWishlistProgressStatusOkErrorNotEmpty = DeleteWishlistProgressResponse(
        deleteWishlistProgress = DeleteWishlistProgressResponse.DeleteWishlistProgress(status = "OK", errorMessage = arrayListOf("error"))
    )
    private val deleteWishlistProgressStatusNotOkErrorEmpty = DeleteWishlistProgressResponse(
        deleteWishlistProgress = DeleteWishlistProgressResponse.DeleteWishlistProgress(status = "ERROR", errorMessage = emptyList())
    )
    private val deleteWishlistProgressStatusNotOkErrorNotEmpty = DeleteWishlistProgressResponse(
        deleteWishlistProgress = DeleteWishlistProgressResponse.DeleteWishlistProgress(status = "ERROR", errorMessage = arrayListOf("error"))
    )
    private val throwable = Fail(Throwable(message = "Error"))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Timber.plant(timber)
        wishlistV2ViewModel = spyk(
            WishlistV2ViewModel(
                dispatcher,
                wishlistV2UseCase,
                deleteWishlistV2UseCase,
                bulkDeleteWishlistV2UseCase,
                deleteWishlistProgressUseCase,
                topAdsImageViewUseCase,
                getSingleRecommendationUseCase,
                atcUseCase
            )
        )

        val primaryButton1 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "ADD_TO_CART")
        val primaryButton2 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "SEE_SIMILAR_PRODUCT")
        val primaryButton3 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "ADD_TO_CART")
        val primaryButton4 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "SEE_SIMILAR_PRODUCT")
        val primaryButton5 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "ADD_TO_CART")

        val listLabelGroup = arrayListOf<WishlistV2Response.Data.WishlistV2.Item.LabelGroupItem>()
        listLabelGroup.add(WishlistV2Response.Data.WishlistV2.Item.LabelGroupItem(title = "test", url = "labelUrl"))

        val listBadge = arrayListOf<WishlistV2Response.Data.WishlistV2.Item.BadgesItem>()
        listBadge.add(WishlistV2Response.Data.WishlistV2.Item.BadgesItem(imageUrl = "badgeUrl", title = "testBadge"))

        tickerState = WishlistV2Response.Data.WishlistV2.TickerState(message = "ticker", type = "announcement")

        val wishlistItem1 = WishlistV2Response.Data.WishlistV2.Item(
            name = "Test1",
            buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton1),
            labelGroup = listLabelGroup,
            badges = listBadge
        )
        val wishlistItem2 = WishlistV2Response.Data.WishlistV2.Item(
            name = "Test2",
            buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton2),
            labelGroup = listLabelGroup,
            badges = listBadge
        )
        val wishlistItem3 = WishlistV2Response.Data.WishlistV2.Item(
            name = "Test3",
            buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton3),
            labelGroup = listLabelGroup,
            badges = listBadge
        )
        val wishlistItem4 = WishlistV2Response.Data.WishlistV2.Item(
            name = "Test4",
            buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton4),
            labelGroup = listLabelGroup,
            badges = listBadge
        )
        val wishlistItem5 = WishlistV2Response.Data.WishlistV2.Item(
            name = "Test5",
            buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton5),
            labelGroup = listLabelGroup,
            badges = listBadge
        )

        wishlistOneItemList = arrayListOf(wishlistItem1)
        wishlistTwoItemList = arrayListOf(wishlistItem1, wishlistItem2)
        wishlistThreeItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3)
        wishlistFourItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3, wishlistItem4)
        wishlistFiveItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3, wishlistItem4, wishlistItem5)
        emptyWishlistItem = emptyList()

        val productCardModel1 = ProductCardModel(
            productName = "product1",
            labelGroupList = listOf(ProductCardModel.LabelGroup()),
            shopBadgeList = listOf(ProductCardModel.ShopBadge()),
            freeOngkir = ProductCardModel.FreeOngkir(),
            labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
            variant = ProductCardModel.Variant(),
            nonVariant = ProductCardModel.NonVariant(),
            hasAddToCartButton = true
        )

        val productCardModel2 = ProductCardModel(
            productName = "product2",
            labelGroupList = listOf(ProductCardModel.LabelGroup()),
            shopBadgeList = listOf(ProductCardModel.ShopBadge()),
            freeOngkir = ProductCardModel.FreeOngkir(),
            labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
            variant = ProductCardModel.Variant(),
            nonVariant = ProductCardModel.NonVariant(),
            hasAddToCartButton = false
        )

        val productCardModel3 = ProductCardModel(
            productName = "product3",
            labelGroupList = listOf(ProductCardModel.LabelGroup()),
            shopBadgeList = listOf(ProductCardModel.ShopBadge()),
            freeOngkir = ProductCardModel.FreeOngkir(),
            labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
            variant = ProductCardModel.Variant(),
            nonVariant = ProductCardModel.NonVariant(),
            hasAddToCartButton = true
        )

        listProductCardModel = listOf(productCardModel1, productCardModel2, productCardModel3)

        val badgesUrl = arrayListOf<String>()
        badgesUrl.add("url")

        val listRecommLabel = arrayListOf<RecommendationLabel>()
        listRecommLabel.add(RecommendationLabel(title = "test", imageUrl = "testUrl"))

        val recommItem1 = RecommendationItem(name = "recomm1", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
        val recommItem2 = RecommendationItem(name = "recomm2", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
        val recommItem3 = RecommendationItem(name = "recomm3", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
        listRecommendationItem = listOf(recommItem1, recommItem2, recommItem3)

        wishlistV2Response = WishlistV2Response(WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(items = wishlistThreeItemList, ticker = tickerState)))
        recommendationWidget = RecommendationWidget(
            tid = "123",
            recommendationItemList = listRecommendationItem,
            recommendationFilterChips = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip()),
            title = "TestRecomm"
        )

        wishlistRecommendation = WishlistV2RecommendationDataModel(listProductCardModel, listRecommendationItem, "TitleRecommendation")

        topAdsImageViewModel = TopAdsImageViewModel(bannerName = "testBanner")
    }

    @Test
    fun loadWishlistV2_shouldReturnSuccess_isAutomaticDeleteTrue_isUsingCollectionFalse() {
        // given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = true,
            isUsingCollection = false
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    @Test
    fun loadWishlistV2_return1Data_shouldReturnSuccess_isAutomaticDeleteTrue_isUsingCollectionFalse() {
        // given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2ResponseWith1Data.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = true,
            isUsingCollection = false
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    @Test
    fun loadWishlistV2_shouldReturnSuccess_isAutomaticDeleteTrue_isUsingCollectionTrue() {
        // given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = true,
            isUsingCollection = true
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    @Test
    fun loadWishlistV2_shouldReturnSuccess_isAutomaticDeleteFalse_isUsingCollectionTrue() {
        // given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = true
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    @Test
    fun loadWishlistV2_shouldReturnSuccess_isAutomaticDeleteFalse_isUsingCollectionFalse() {
        // given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    @Test
    fun loadWishlistV2_shouldReturnSuccess_whenGetTopadsData_isNull() {
        // given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns null

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    // wishlist_v2_failed
    @Test
    fun loadWishlistV2_shouldReturnFail() {
        // given

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } throws Exception()

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } throws Exception()

        // when
        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        // then
        assert(wishlistV2ViewModel.wishlistV2.value is Fail)
    }

    // recommendation_success
    @Test
    fun loadRecommendation_shouldReturnSuccess() {
        // given
        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistV2ViewModel.loadRecommendation(0)

        // then
        assert(wishlistV2ViewModel.wishlistV2Data.value is Success<List<WishlistV2TypeLayoutData>>)
    }

    // recommendation_failed
    @Test
    fun `loadRecommendation_shouldReturnFail`() {
        val throwable = spyk(Throwable())

        // given
        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } throws Exception()

        // when
        wishlistV2ViewModel.loadRecommendation(0)

        // then
        SoftAssertions.assertSoftly {
            timber.lastLogMessage() contentEquals throwable.localizedMessage
        }
    }

    // delete_success
    @Test
    fun deleteWishlist_shouldReturnSuccess() {
        // given
        val deleteResult = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)
        coEvery {
            deleteWishlistV2UseCase.executeOnBackground()
        } returns Success(deleteResult)

        // when
        wishlistV2ViewModel.deleteWishlistV2("", "")

        // then
        assert(wishlistV2ViewModel.deleteWishlistV2Result.value is Success)
        assert((wishlistV2ViewModel.deleteWishlistV2Result.value as Success<DeleteWishlistV2Response.Data.WishlistRemoveV2>).data.success)
    }

    // delete_fail
    @Test
    fun deleteWishlist_shouldReturnFail() {
        // given
        coEvery {
            deleteWishlistV2UseCase.executeOnBackground()
        } returns Fail(Throwable())

        // when
        wishlistV2ViewModel.deleteWishlistV2("", "")

        // then
        assert(wishlistV2ViewModel.deleteWishlistV2Result.value is Fail)
    }

    // bulkDelete_success
    @Test
    fun bulkDeleteWishlist_shouldReturnSuccess() {
        // given
        val bulkDeleteResult = BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2(
            id = "",
            success = true,
            message = "",
            button = BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2.Button("", "", "")
        )
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any(), any(), any(), any())
        } returns Success(bulkDeleteResult)

        // when
        wishlistV2ViewModel.bulkDeleteWishlistV2(listOf(), "", 0, WishlistV2BulkRemoveAdditionalParams())

        // then
        assert(wishlistV2ViewModel.bulkDeleteWishlistV2Result.value is Success)
        assert((wishlistV2ViewModel.bulkDeleteWishlistV2Result.value as Success<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>).data.success)
    }

    // bulkDelete_fail
    @Test
    fun bulkDeleteWishlist_shouldReturnFail() {
        // given
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any(), any(), any(), any())
        } returns Fail(Exception())

        // when
        wishlistV2ViewModel.bulkDeleteWishlistV2(listOf(), "", 0, WishlistV2BulkRemoveAdditionalParams())

        // then
        assert(wishlistV2ViewModel.bulkDeleteWishlistV2Result.value is Fail)
    }

    // atc_success
    @Test
    fun atcWishlist_shouldReturnSuccess() {
        // given
        coEvery {
            atcUseCase.executeOnBackground()
        } returns AddToCartDataModel(
            status = AddToCartDataModel.STATUS_OK,
            data = DataModel(success = 1)
        )

        // when
        wishlistV2ViewModel.doAtc(AddToCartRequestParams())

        // then
        assert(wishlistV2ViewModel.atcResult.value is Success)
        assert((wishlistV2ViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.success == 1)
    }

    // atc_fail
    @Test
    fun atcWishlist_shouldReturnFail() {
        // given
        coEvery {
            atcUseCase.executeOnBackground()
        } throws Exception()

        // when
        wishlistV2ViewModel.doAtc(AddToCartRequestParams())

        // then
        assert(wishlistV2ViewModel.atcResult.value is Fail)
    }

    // mapTicker
    @Test
    fun mapTicker_isNotEmpty() {
        val listItemWishlist = WishlistV2Response.Data(
            WishlistV2Response.Data.WishlistV2(
                totalData = 5,
                items = wishlistFiveItemList,
                page = 1,
                hasNextPage = false,
                ticker = tickerState
            )
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_TICKER))
    }

    // mapToTopads
    @Test
    fun mapToTopads_onExpectedIndex() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 1, hasNextPage = false))

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
    }

    // mapToTopads
    @Test
    fun mapToTopads_onOddPage() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 3, hasNextPage = false))

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
    }

    @Test
    fun mapToTopads_onIndexZero() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 4, items = wishlistFourItemList, page = 1, hasNextPage = false))

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
    }

    @Test
    fun mapToTopads_onPageOneAndHasNextPage() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 1, hasNextPage = true))
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
    }

    @Test
    fun mapToTopads_onOddPageAndHasNextPage() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 3, hasNextPage = true))
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
    }

    @Test
    fun mapToRecommendation_onIndexZero() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 3, items = wishlistThreeItemList, page = 1, hasNextPage = false))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[3].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToRecommendation_onExpectedIndex() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 2, hasNextPage = true))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[5].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToRecommendation_onExpectedIndex_whenItemsSize_smallerThenRecomPosition() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 2, items = wishlistTwoItemList, page = 2, hasNextPage = true))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[2].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[3].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToEmptyState_whenQueryIsNotEmpty() {
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "test", items = emptyList(), page = 1))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(query = "test"),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_NOT_FOUND))
    }

    @Test
    fun mapToEmptyState_whenFilterIsActive() {
        val arrayListSelected = arrayListOf<String>()
        arrayListSelected.add("2")
        arrayListSelected.add("3")
        val paramListSortFilter = arrayListOf(WishlistV2Params.WishlistSortFilterParam(name = "test", selected = arrayListSelected))
        val responseListSortFilter = listOf(WishlistV2Response.Data.WishlistV2.SortFiltersItem(isActive = true))
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "", items = emptyList(), sortFilters = responseListSortFilter, page = 1))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(query = "", sortFilters = paramListSortFilter),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_STATE))
    }

    @Test
    fun mapToEmptyState_whenFilterIsNotActive_andQueryIsEmpty() {
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "", items = emptyList(), page = 1))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_STATE_CAROUSEL))
    }

    @Test
    fun mapToEmptyState_showRecommendation() {
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "test", items = emptyList(), page = 1))

        val recomItem = RecommendationItem(productId = 1L)
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget(recommendationItemList = listOf(recomItem)) }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_NOT_FOUND))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[1].typeLayout.equals(TYPE_RECOMMENDATION_TITLE))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[2].typeLayout.equals(TYPE_RECOMMENDATION_LIST))
    }

    @Test
    fun mapToRecommendation_When_Index_IsMoreThanZero() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 2, totalData = 5, hasNextPage = true, items = wishlistFiveItemList))

        val recomItem = RecommendationItem(productId = 1L)
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget(recommendationItemList = listOf(recomItem)) }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData

        wishlistV2ViewModel.loadWishlistV2(
            WishlistV2Params(),
            "",
            isAutomaticDelete = false,
            isUsingCollection = false
        )

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[5].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun `Execute DeleteWishlistProgress Result Status Ok Error Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusOkErrorEmpty

        // when
        wishlistV2ViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistV2ViewModel.deleteWishlistProgressResult.value is Success)
        assert((wishlistV2ViewModel.deleteWishlistProgressResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistProgress Result Status Ok Error Not Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusOkErrorNotEmpty

        // when
        wishlistV2ViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistV2ViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistProgress Result Status Not Ok Error Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusNotOkErrorEmpty

        // when
        wishlistV2ViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistV2ViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistProgress Result Status Not Ok Error Not Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusNotOkErrorNotEmpty

        // when
        wishlistV2ViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistV2ViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Failed`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } throws throwable.throwable

        // when
        wishlistV2ViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistV2ViewModel.deleteWishlistProgressResult.value is Fail)
    }
}
