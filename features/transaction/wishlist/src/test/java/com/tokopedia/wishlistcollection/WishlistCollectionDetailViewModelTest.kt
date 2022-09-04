package com.tokopedia.wishlistcollection

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
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionItemsParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionDetailViewModel
import com.tokopedia.wishlistcommon.data.WishlistV2Params
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import timber.log.Timber

@RunWith(JUnit4::class)
class WishlistCollectionDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistCollectionDetailViewModel: WishlistCollectionDetailViewModel

    private var collectionDetailFiveItemList = listOf<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem>()
    private var collectionDetailFourItemList = listOf<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem>()

    @RelaxedMockK
    lateinit var getWishlistCollectionItemsUseCase: GetWishlistCollectionItemsUseCase

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    lateinit var singleRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteCollectionItemsUseCase: DeleteWishlistCollectionItemsUseCase

    @RelaxedMockK
    lateinit var deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase

    @RelaxedMockK
    lateinit var deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase

    private var getWishlistCollectionItemsResponseDataStatusOk = GetWishlistCollectionItemsResponse(
        getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "")
    )

    private var getWishlistCollectionItemsResponseDataStatusError = GetWishlistCollectionItemsResponse(
        getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "error")
    )

    private var deleteWishlistProgressStatusOkErrorEmpty = DeleteWishlistProgressResponse(DeleteWishlistProgressResponse.DeleteWishlistProgress(
        status = "OK", errorMessage = emptyList()
    ))

    private var deleteWishlistProgressStatusOkErrorNotEmpty = DeleteWishlistProgressResponse(DeleteWishlistProgressResponse.DeleteWishlistProgress(
        status = "OK", errorMessage = arrayListOf("error")
    ))

    private var deleteWishlistProgressStatusNotOkErrorEmpty = DeleteWishlistProgressResponse(DeleteWishlistProgressResponse.DeleteWishlistProgress(
        status = "ERROR", errorMessage = emptyList()
    ))

    private var deleteWishlistProgressStatusNotOkErrorNotEmpty = DeleteWishlistProgressResponse(DeleteWishlistProgressResponse.DeleteWishlistProgress(
        status = "ERROR", errorMessage = arrayListOf("error")
    ))

    private var addWishlistCollectionStatusOkErrorEmpty = AddWishlistCollectionItemsResponse(
        AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(
        status = "OK", errorMessage = emptyList()
    ))

    private var addWishlistCollectionStatusOkErrorNotEmpty = AddWishlistCollectionItemsResponse(AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(
        status = "OK", errorMessage = arrayListOf("error")
    ))

    private var addWishlistCollectionStatusNotOkErrorEmpty = AddWishlistCollectionItemsResponse(AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(
        status = "ERROR", errorMessage = emptyList()
    ))

    private var addWishlistCollectionStatusNotOkErrorNotEmpty = AddWishlistCollectionItemsResponse(AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(
        status = "ERROR", errorMessage = arrayListOf("error")
    ))

    private val throwable = Fail(Throwable(message = "Error"))

    private val getWishlistCollectionItemsParams = GetWishlistCollectionItemsParams()
    private val typeLayout = WishlistV2Consts.TYPE_GRID

    private var topAdsImageViewModel = TopAdsImageViewModel()
    private val productCardModel1 = ProductCardModel(productName = "product1",
        labelGroupList = listOf(ProductCardModel.LabelGroup()),
        shopBadgeList = listOf(ProductCardModel.ShopBadge()),
        freeOngkir = ProductCardModel.FreeOngkir(),
        labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
        variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
        hasAddToCartButton = true)

    private val productCardModel2: ProductCardModel = ProductCardModel(productName = "product2",
        labelGroupList = listOf(ProductCardModel.LabelGroup()),
        shopBadgeList = listOf(ProductCardModel.ShopBadge()),
        freeOngkir = ProductCardModel.FreeOngkir(),
        labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
        variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
        hasAddToCartButton = false)

    private val productCardModel3 = ProductCardModel(productName = "product3",
        labelGroupList = listOf(ProductCardModel.LabelGroup()),
        shopBadgeList = listOf(ProductCardModel.ShopBadge()),
        freeOngkir = ProductCardModel.FreeOngkir(),
        labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
        variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
        hasAddToCartButton = true)

    private val listProductCardModel = listOf(productCardModel1, productCardModel2, productCardModel3)

    private val badgesUrl: List<String> = arrayListOf()

    private val listRecommLabel: List<RecommendationLabel> = arrayListOf()

    val recommItem1 = RecommendationItem(name = "recomm1", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
    val recommItem2 = RecommendationItem(name = "recomm2", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
    val recommItem3 = RecommendationItem(name = "recomm3", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
    private val listRecommendationItem = listOf(recommItem1, recommItem2, recommItem3)
    private val wishlistRecommendation = WishlistV2RecommendationDataModel(listProductCardModel, listRecommendationItem, "TitleRecommendation")
    private val recommendationWidget = RecommendationWidget(tid = "123", recommendationItemList = listRecommendationItem,
    recommendationFilterChips = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip()), title = "TestRecomm")

    private val topadsImage = TopAdsImageViewModel(bannerId = "1")
    private val topadsResult = arrayListOf(topadsImage)

    private var deleteWishlistCollectionItemsStatusOkErrorEmpty = DeleteWishlistCollectionItemsResponse(
        deleteWishlistCollectionItems = DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems(
            status = "OK", errorMessage = emptyList())
    )

    private var deleteWishlistCollectionItemsStatusOkErrorNotEmpty = DeleteWishlistCollectionItemsResponse(
        deleteWishlistCollectionItems = DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems(
            status = "OK", arrayListOf("error"))
    )

    private var deleteWishlistCollectionItemsStatusNotOkErrorEmpty = DeleteWishlistCollectionItemsResponse(
        deleteWishlistCollectionItems = DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems(
            status = "ERROR", errorMessage = emptyList())
    )

    private var deleteWishlistCollectionItemsStatusNotOkErrorNotEmpty = DeleteWishlistCollectionItemsResponse(
        deleteWishlistCollectionItems = DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems(
            status = "ERROR", errorMessage = arrayListOf("error")
    ))

    private val productId = "1"
    private val listProductId: ArrayList<String> = arrayListOf()
    private var deleteWishlistCollectionResponseDataStatusOkErrorEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "OK", errorMessage = emptyList())
    )

    private var deleteWishlistCollectionResponseDataStatusOkErrorNotEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "OK", errorMessage = arrayListOf("error"))
    )

    private var deleteWishlistCollectionResponseDataStatusNotOkErrorEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "ERROR", errorMessage = emptyList())
    )

    private var deleteWishlistCollectionResponseDataStatusNotOkErrorNotEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "ERROR", errorMessage = arrayListOf("error"))
    )

    private var addWishlistParam = AddWishlistCollectionsHostBottomSheetParams()

    private val timber = WishlistMockTimber()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Timber.plant(timber)
        wishlistCollectionDetailViewModel = spyk(
            WishlistCollectionDetailViewModel(
                dispatcher,
                getWishlistCollectionItemsUseCase,
                topAdsImageViewUseCase,
                singleRecommendationUseCase,
                deleteWishlistV2UseCase,
                bulkDeleteWishlistV2UseCase,
                deleteCollectionItemsUseCase,
                deleteWishlistCollectionUseCase,
                deleteWishlistProgressUseCase,
                atcUseCase,
                addWishlistCollectionItemsUseCase
            )
        )
        topAdsImageViewModel = TopAdsImageViewModel(bannerName = "testBanner")

        val primaryButton1 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons.PrimaryButton(action = "ADD_TO_CART")
        val primaryButton2 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons.PrimaryButton(action = "SEE_SIMILAR_PRODUCT")
        val primaryButton3 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons.PrimaryButton(action = "ADD_TO_CART")
        val primaryButton4 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons.PrimaryButton(action = "SEE_SIMILAR_PRODUCT")
        val primaryButton5 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons.PrimaryButton(action = "ADD_TO_CART")

        val listLabelGroup = arrayListOf<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.LabelGroupItem>()
        listLabelGroup.add(GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.LabelGroupItem(title = "test", url = "labelUrl"))

        val listBadge = arrayListOf<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.BadgesItem>()
        listBadge.add(GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.BadgesItem(imageUrl = "badgeUrl", title = "testBadge"))

        val collectionItem1 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem(name = "Test1",
            buttons = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons(primaryButton = primaryButton1), labelGroup = listLabelGroup, badges = listBadge)
        val collectionItem2 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem(name = "Test2",
            buttons = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons(primaryButton = primaryButton2), labelGroup = listLabelGroup, badges = listBadge)
        val collectionItem3 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem(name = "Test3",
            buttons = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons(primaryButton = primaryButton3), labelGroup = listLabelGroup, badges = listBadge)
        val collectionItem4 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem(name = "Test4",
            buttons = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons(primaryButton = primaryButton4), labelGroup = listLabelGroup, badges = listBadge)
        val collectionItem5 = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem(name = "Test5",
            buttons = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.ItemsItem.Buttons(primaryButton = primaryButton5), labelGroup = listLabelGroup, badges = listBadge)
        collectionDetailFiveItemList = arrayListOf(collectionItem1, collectionItem2, collectionItem3, collectionItem4, collectionItem5)
        collectionDetailFourItemList = arrayListOf(collectionItem1, collectionItem2, collectionItem3, collectionItem4)
    }

    @Test
    fun `Execute GetWishlistCollectionItems Success Status OK`() {
        //given
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionItemsResponseDataStatusOk

        coEvery {
            wishlistCollectionDetailViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistCollectionDetailViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        //when
        wishlistCollectionDetailViewModel.getWishlistCollectionItems(getWishlistCollectionItemsParams, typeLayout, false)

        //then
        assert(wishlistCollectionDetailViewModel.collectionItems.value is Success)
        assert((wishlistCollectionDetailViewModel.collectionItems.value as Success).data.getWishlistCollectionItems.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetWishlistCollections Success Status ERROR`() {
        //given
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionItemsResponseDataStatusError

        coEvery {
            wishlistCollectionDetailViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistCollectionDetailViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        //when
        wishlistCollectionDetailViewModel.getWishlistCollectionItems(getWishlistCollectionItemsParams, typeLayout, false)

        //then
        assert(wishlistCollectionDetailViewModel.collectionItems.value is Success)
        assert((wishlistCollectionDetailViewModel.collectionItems.value as Success).data.getWishlistCollectionItems.errorMessage.isNotEmpty())
    }

    @Test
    fun `Execute GetWishlistCollections Failed`() {
        //given
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.getWishlistCollectionItems(getWishlistCollectionItemsParams, typeLayout, false)

        //then
        assert(wishlistCollectionDetailViewModel.collectionItems.value is Fail)
    }

    @Test
    fun `Load Recommendation Success`() {
        //given
        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        //when
        wishlistCollectionDetailViewModel.loadRecommendation(0)

        //then
        assert(wishlistCollectionDetailViewModel.collectionData.value is Success<List<WishlistV2TypeLayoutData>>)
    }

    @Test
    fun `Load Recommendation Failed`() {
        val throwable = spyk(Throwable())

        //given
        coEvery {
            singleRecommendationUseCase.getData(any())
        } throws Exception()

        //when
        wishlistCollectionDetailViewModel.loadRecommendation(0)

        //then
        assertSoftly {
            timber.lastLogMessage() contentEquals throwable.localizedMessage
        }
    }

    @Test
    fun `Delete Wishlist Success`() {
        //given
        val deleteResult = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)
        coEvery {
            deleteWishlistV2UseCase.executeOnBackground()
        } returns Success(deleteResult)

        //when
        wishlistCollectionDetailViewModel.deleteWishlistV2("", "")

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistV2Result.value is Success)
        assert((wishlistCollectionDetailViewModel.deleteWishlistV2Result.value as Success<DeleteWishlistV2Response.Data.WishlistRemoveV2>).data.success)
    }

    @Test
    fun `Delete Wishlist Error`() {
        //given
        coEvery {
            deleteWishlistV2UseCase.executeOnBackground()
        } returns Fail(Throwable())

        //when
        wishlistCollectionDetailViewModel.deleteWishlistV2("", "")

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistV2Result.value is Fail)
    }

    @Test
    fun `Bulk Delete Success`() {
        //given
        val bulkDeleteResult = BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2(id = "",
            success = true, message = "", button = BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2.Button("", "", ""))
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any(), any(), any(), any())
        } returns Success(bulkDeleteResult)


        //when
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(listOf(), "", 0, WishlistV2BulkRemoveAdditionalParams(), "")

        //then
        assert(wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.value is Success)
        assert((wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.value as Success<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>).data.success)
    }

    @Test
    fun `Bulk Delete Failed`() {
        //given
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any(), any(), any(), any())
        } returns Fail(Exception())

        //when
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(listOf(), "", 0, WishlistV2BulkRemoveAdditionalParams(), "")

        //then
        assert(wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Success Status OK Error is Empty`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listProductId)
        } returns deleteWishlistCollectionItemsStatusOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Success)
        assert((wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Success Status OK Error is not Empty`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listProductId)
        } returns deleteWishlistCollectionItemsStatusOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Success Status Not OK Error is Empty`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listProductId)
        } returns deleteWishlistCollectionItemsStatusNotOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Success Status Not OK Error is not Empty`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listProductId)
        } returns deleteWishlistCollectionItemsStatusNotOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Failed`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listProductId)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status OK Error is Empty`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(productId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Success)
        assert((wishlistCollectionDetailViewModel.deleteCollectionResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status OK Error not Empty`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(productId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status Not OK Error Empty`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusNotOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(productId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status Not OK Error not Empty`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusNotOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(productId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Failed`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(productId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status OK And Error is Empty`() {
        //given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.getDeleteWishlistProgress()

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistProgressResult.value is Success)
        assert((wishlistCollectionDetailViewModel.deleteWishlistProgressResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status OK And Error is not Empty`() {
        //given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.getDeleteWishlistProgress()

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status Error And Error is Empty`() {
        //given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusNotOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.getDeleteWishlistProgress()

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status Error And Error is not Empty`() {
        //given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusNotOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.getDeleteWishlistProgress()

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Failed`() {
        //given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.getDeleteWishlistProgress()

        //then
        assert(wishlistCollectionDetailViewModel.deleteWishlistProgressResult.value is Fail)
    }

    // atc_success
    @Test
    fun atcWishlist_shouldReturnSuccess() {
        //given
        coEvery {
            atcUseCase.executeOnBackground()
        } returns AddToCartDataModel(
            status = AddToCartDataModel.STATUS_OK,
            data = DataModel(success = 1)
        )

        //when
        wishlistCollectionDetailViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(wishlistCollectionDetailViewModel.atcResult.value is Success)
        assert((wishlistCollectionDetailViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.success == 1)
    }

    // atc_fail
    @Test
    fun atcWishlist_shouldReturnFail() {
        //given
        coEvery {
            atcUseCase.executeOnBackground()
        } throws Exception()

        //when
        wishlistCollectionDetailViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(wishlistCollectionDetailViewModel.atcResult.value is Fail)
    }

    @Test
    fun `Execute SaveNewWishlistCollection Success Status OK And Error is Empty`() {
        //given
        listProductId.add(productId)
        addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionId = "1", listProductId)
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistParam)
        } returns addWishlistCollectionStatusOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.saveNewWishlistCollection(addWishlistParam)

        //then
        assert(wishlistCollectionDetailViewModel.addWishlistCollectionItem.value is Success)
        assert((wishlistCollectionDetailViewModel.addWishlistCollectionItem.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute SaveNewWishlistCollection Success Status OK And Error is not Empty`() {
        //given
        listProductId.add(productId)
        addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionId = "1", listProductId)
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistParam)
        } returns addWishlistCollectionStatusOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.saveNewWishlistCollection(addWishlistParam)

        //then
        assert(wishlistCollectionDetailViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveNewWishlistCollection Success Status Error And Error is Empty`() {
        //given
        listProductId.add(productId)
        addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionId = "1", listProductId)
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistParam)
        } returns addWishlistCollectionStatusNotOkErrorEmpty

        //when
        wishlistCollectionDetailViewModel.saveNewWishlistCollection(addWishlistParam)

        //then
        assert(wishlistCollectionDetailViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveNewWishlistCollection Success Status Error And Error is not Empty`() {
        //given
        listProductId.add(productId)
        addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionId = "1", listProductId)
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistParam)
        } returns addWishlistCollectionStatusNotOkErrorNotEmpty

        //when
        wishlistCollectionDetailViewModel.saveNewWishlistCollection(addWishlistParam)

        //then
        assert(wishlistCollectionDetailViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveNewWishlistCollection Failed`() {
        //given
        listProductId.add(productId)
        addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionId = "1", listProductId)
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistParam)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.saveNewWishlistCollection(addWishlistParam)

        //then
        assert(wishlistCollectionDetailViewModel.addWishlistCollectionItem.value is Fail)
    }

    // mapToTopads
    @Test
    fun mapToTopads_onExpectedIndex() {
        val getWishlistCollectionFiveItems = GetWishlistCollectionItemsResponse(
            getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "", items = collectionDetailFiveItemList, totalData = 5, page = 1, hasNextPage = false)
        )
        
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { singleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionFiveItems

        wishlistCollectionDetailViewModel.getWishlistCollectionItems(
            GetWishlistCollectionItemsParams(), "",
            isAutomaticDelete = false)

        assert(wishlistCollectionDetailViewModel.collectionData.value is Success)
    }

    // mapToTopads
    @Test
    fun mapToTopads_onOddPage() {
        val getWishlistCollectionFiveItems = GetWishlistCollectionItemsResponse(
            getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "", items = collectionDetailFiveItemList, totalData = 5, page = 1, hasNextPage = false)
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { singleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionFiveItems

        wishlistCollectionDetailViewModel.getWishlistCollectionItems(
            GetWishlistCollectionItemsParams(), "",
            isAutomaticDelete = false)

        assert(wishlistCollectionDetailViewModel.collectionData.value is Success)
    }

    @Test
    fun mapToTopads_onIndexZero() {
        val getWishlistCollectionFourItems = GetWishlistCollectionItemsResponse(
            getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "", items = collectionDetailFourItemList, totalData = 4, page = 1, hasNextPage = false)
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { singleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionFourItems

        wishlistCollectionDetailViewModel.getWishlistCollectionItems(
            GetWishlistCollectionItemsParams(), "",
            isAutomaticDelete = false)

        assert(wishlistCollectionDetailViewModel.collectionData.value is Success)
    }

    @Test
    fun mapToTopads_onPageOneAndHasNextPage() {
        val getWishlistCollectionFiveItems = GetWishlistCollectionItemsResponse(
            getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "", items = collectionDetailFiveItemList, totalData = 5, page = 1, hasNextPage = false)
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { singleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionFiveItems

        wishlistCollectionDetailViewModel.getWishlistCollectionItems(
            GetWishlistCollectionItemsParams(), "",
            isAutomaticDelete = false)

        assert(wishlistCollectionDetailViewModel.collectionData.value is Success)
    }

    @Test
    fun mapToTopads_onOddPageAndHasNextPage() {
        val getWishlistCollectionFiveItems = GetWishlistCollectionItemsResponse(
            getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "", items = collectionDetailFiveItemList, totalData = 5, page = 1, hasNextPage = false)
        )
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { singleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery {
            getWishlistCollectionItemsUseCase(getWishlistCollectionItemsParams)
        } returns getWishlistCollectionFiveItems

        wishlistCollectionDetailViewModel.getWishlistCollectionItems(
            GetWishlistCollectionItemsParams(), "",
            isAutomaticDelete = false)

        assert(wishlistCollectionDetailViewModel.collectionData.value is Success)
    }
}