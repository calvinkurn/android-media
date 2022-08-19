package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionItemsParams
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionDetailViewModel
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistCollectionDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistCollectionDetailViewModel: WishlistCollectionDetailViewModel

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

    private var getWishlistCollectionItemsResponseDataStatusOk = GetWishlistCollectionItemsResponse(
        getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "")
    )

    private var getWishlistCollectionItemsResponseDataStatusError = GetWishlistCollectionItemsResponse(
        getWishlistCollectionItems = GetWishlistCollectionItemsResponse.GetWishlistCollectionItems(errorMessage = "error")
    )

    private val throwable = Fail(Throwable(message = "Error"))

    private val getWishlistCollectionItemsParams = GetWishlistCollectionItemsParams()
    private val typeLayout = WishlistV2Consts.TYPE_GRID

    private val topAdsImageViewModel = TopAdsImageViewModel(bannerName = "testBanner")
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

    private val listDeleteProductId: List<String> = arrayListOf()
    private var deleteWishlistCollectionItemsResponseDataStatusOk = DeleteWishlistCollectionItemsResponse(
        deleteWishlistCollectionItems = DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems(status = "OK")
    )
    private var deleteWishlistCollectionItemsResponseDataStatusError = DeleteWishlistCollectionItemsResponse(
        deleteWishlistCollectionItems = DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems(status = "ERROR")
    )

    private val deletedProductId = "1"
    private var deleteWishlistCollectionResponseDataStatusOk = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "OK")
    )
    private var deleteWishlistCollectionResponseDataStatusError = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "ERROR")
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        wishlistCollectionDetailViewModel = spyk(
            WishlistCollectionDetailViewModel(
                dispatcher,
                getWishlistCollectionItemsUseCase,
                topAdsImageViewUseCase,
                singleRecommendationUseCase,
                deleteWishlistV2UseCase,
                bulkDeleteWishlistV2UseCase,
                deleteCollectionItemsUseCase,
                deleteWishlistCollectionUseCase
            )
        )
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
        //given
        coEvery {
            singleRecommendationUseCase.getData(any())
        } throws Exception()

        //when
        wishlistCollectionDetailViewModel.loadRecommendation(0)

        //then
        assert(wishlistCollectionDetailViewModel.collectionData.value is Fail)
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
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any(), any(), any())
        } returns Success(bulkDeleteResult)


        //when
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(listOf(), "", 0)

        //then
        assert(wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.value is Success)
        assert((wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.value as Success<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>).data.success)
    }

    @Test
    fun `Bulk Delete Failed`() {
        //given
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any(), any(), any())
        } returns Fail(Exception())

        //when
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(listOf(), "", 0)

        //then
        assert(wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Success Status OK`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listDeleteProductId)
        } returns deleteWishlistCollectionItemsResponseDataStatusOk

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listDeleteProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Success)
        assert((wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Success Status ERROR`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listDeleteProductId)
        } returns deleteWishlistCollectionItemsResponseDataStatusError

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listDeleteProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollectionItems Failed`() {
        //given
        coEvery {
            deleteCollectionItemsUseCase(listDeleteProductId)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listDeleteProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionItemsResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status OK`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(deletedProductId)
        } returns deleteWishlistCollectionResponseDataStatusOk

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(deletedProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Success)
        assert((wishlistCollectionDetailViewModel.deleteCollectionResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status ERROR`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(deletedProductId)
        } returns deleteWishlistCollectionResponseDataStatusError

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(deletedProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Failed`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(deletedProductId)
        } throws throwable.throwable

        //when
        wishlistCollectionDetailViewModel.deleteWishlistCollection(deletedProductId)

        //then
        assert(wishlistCollectionDetailViewModel.deleteCollectionResult.value is Fail)
    }
}