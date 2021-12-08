package com.tokopedia.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.DeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter.Companion.LAYOUT_LIST
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.internalSubstitute
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.any
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistV2ViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistV2ViewModel: WishlistV2ViewModel
    private var wishlistItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistV2Response = WishlistV2Response()
    private var recommendationWidget = RecommendationWidget()
    private var wishlistRecommendation = WishlistV2RecommendationDataModel()
    private var listProductCardModel = listOf<ProductCardModel>()
    private var listRecommendationItem = listOf<RecommendationItem>()
    private var topAdsImageViewModel = TopAdsImageViewModel()

    @RelaxedMockK
    lateinit var wishlistV2UseCase: WishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    lateinit var getSingleRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        wishlistV2ViewModel = spyk(WishlistV2ViewModel(dispatcher, wishlistV2UseCase, deleteWishlistV2UseCase,
                bulkDeleteWishlistV2UseCase, topAdsImageViewUseCase, getSingleRecommendationUseCase, atcUseCase))

        val wishlistItem1 = WishlistV2Response.Data.WishlistV2.Item(name = "Test1")
        val wishlistItem2 = WishlistV2Response.Data.WishlistV2.Item(name = "Test2")
        val wishlistItem3 = WishlistV2Response.Data.WishlistV2.Item(name = "Test3")
        wishlistItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3)

        val productCardModel1 = ProductCardModel(productName = "product1",
                labelGroupList = listOf(ProductCardModel.LabelGroup("position1", "title1", "type1", "imageUrl1")),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant())

        val productCardModel2 = ProductCardModel(productName = "product2",
                labelGroupList = listOf(ProductCardModel.LabelGroup("position2", "title2", "type2", "imageUrl2")),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant())

        val productCardModel3 = ProductCardModel(productName = "product3",
                labelGroupList = listOf(ProductCardModel.LabelGroup("position3", "title3", "type3", "imageUrl3")),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant())

        listProductCardModel = listOf(productCardModel1, productCardModel2, productCardModel3)

        val recommItem1 = RecommendationItem(name = "recomm1")
        val recommItem2 = RecommendationItem(name = "recomm2")
        val recommItem3 = RecommendationItem(name = "recomm3")
        listRecommendationItem = listOf(recommItem1, recommItem2, recommItem3)

        wishlistV2Response = WishlistV2Response(WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(items = wishlistItemList)))
        recommendationWidget = RecommendationWidget(tid = "123", recommendationItemList = listRecommendationItem,
                recommendationFilterChips = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip()), title = "TestRecomm")

        wishlistRecommendation = WishlistV2RecommendationDataModel(listProductCardModel, listRecommendationItem, "TitleRecommendation")

        topAdsImageViewModel = TopAdsImageViewModel(bannerName = "testBanner")
    }

    // wishlist_v2_success
    @Test
    fun loadWishlistV2_shouldReturnSuccess() {
        //given

        every {
            wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem)
        } returns listProductCardModel

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.organizeWishlistV2Data(wishlistV2Response.data.wishlistV2, "")
        } returns listOf()

        coEvery {
            wishlistV2ViewModel.mapToEmptyState(wishlistV2Response.data.wishlistV2, any(), any())
        } returns arrayListOf()

        coEvery {
            wishlistV2ViewModel.mapToProductCardList(wishlistItemList, any())
        } returns arrayListOf()

        coEvery {
            wishlistV2ViewModel.mapToRecommendation(any(), any())
        } returns arrayListOf()

        coEvery {
            wishlistV2ViewModel.mapToTopads(any(), any())
        } returns arrayListOf()

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget


        //when
        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        //then
        assert(wishlistV2ViewModel.wishlistV2.value is Success)
    }

    // wishlist_v2_failed
    @Test
    fun loadWishlistV2_shouldReturnFail() {
        //given

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } throws Exception()

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } throws Exception()


        //when
        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        //then
        assert(wishlistV2ViewModel.wishlistV2.value is Fail)
    }

    // recommendation_success
    @Test
    fun loadRecommendation_shouldReturnSuccess() {
        //given
        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget


        //when
        wishlistV2ViewModel.loadRecommendation(0)

        //then
        assert(wishlistV2ViewModel.wishlistV2Data.value is Success<List<WishlistV2TypeLayoutData>>)
    }

    // recommendation_failed
    @Test
    fun loadRecommendation_shouldReturnFail() {
        //given
        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } throws Exception()

        //when
        wishlistV2ViewModel.loadRecommendation(0)

        //then
        assert(wishlistV2ViewModel.wishlistV2Data.value is Fail)
    }

    // delete_success
    @Test
    fun deleteWishlist_shouldReturnSuccess() {
        //given
        val deleteResult = DeleteWishlistV2Response.Data.WishlistRemoveV2(
                button = DeleteWishlistV2Response.Data.WishlistRemoveV2.Button("", "", ""),
                success = true, id = "", message = "")
        coEvery {
            deleteWishlistV2UseCase.executeSuspend(any(), any())
        } returns Success(deleteResult)


        //when
        wishlistV2ViewModel.deleteWishlistV2("", "")

        //then
        assert(wishlistV2ViewModel.deleteWishlistV2Result.value is Success)
        assert((wishlistV2ViewModel.deleteWishlistV2Result.value as Success<DeleteWishlistV2Response.Data.WishlistRemoveV2>).data.success)
    }

    // delete_success
    @Test
    fun deleteWishlist_shouldReturnFail() {
        //given
        coEvery {
            deleteWishlistV2UseCase.executeSuspend(any(), any())
        } returns Fail(Exception())

        //when
        wishlistV2ViewModel.deleteWishlistV2("", "")

        //then
        assert(wishlistV2ViewModel.deleteWishlistV2Result.value is Fail)
    }

    // bulkDelete_success
    @Test
    fun bulkDeleteWishlist_shouldReturnSuccess() {
        //given
        val bulkDeleteResult = BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2(id = "",
                success = true, message = "", button = BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2.Button("", "", ""))
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any())
        } returns Success(bulkDeleteResult)


        //when
        wishlistV2ViewModel.bulkDeleteWishlistV2(listOf(), "")

        //then
        assert(wishlistV2ViewModel.bulkDeleteWishlistV2Result.value is Success)
        assert((wishlistV2ViewModel.bulkDeleteWishlistV2Result.value as Success<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>).data.success)
    }

    // bulkDelete_fail
    @Test
    fun bulkDeleteWishlist_shouldReturnFail() {
        //given
        coEvery {
            bulkDeleteWishlistV2UseCase.executeSuspend(any(), any())
        } returns Fail(Exception())

        //when
        wishlistV2ViewModel.bulkDeleteWishlistV2(listOf(), "")

        //then
        assert(wishlistV2ViewModel.bulkDeleteWishlistV2Result.value is Fail)
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
        wishlistV2ViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(wishlistV2ViewModel.atcResult.value is Success)
        assert((wishlistV2ViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.success == 1)
    }

    // atc_fail
    @Test
    fun atcWishlist_shouldReturnFail() {
        //given
        coEvery {
            atcUseCase.executeOnBackground()
        } throws Exception()

        //when
        wishlistV2ViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(wishlistV2ViewModel.atcResult.value is Fail)
    }

    // convertRecommendationIntoProductDataModel
    @Test
    fun convertRecommendationIntoProductDataModel_returnsNotEmpty() {
        every { wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem) } returns listProductCardModel

        wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listOf())

        Assert.assertEquals("product1", listProductCardModel[0].productName)
    }

    // mapToProductCardList
    @Test
    fun mapToProductCardList_returnsNotEmpty() {
        val listProductCard = arrayListOf<WishlistV2TypeLayoutData>()
        listProductCard.add(WishlistV2TypeLayoutData(typeLayout = "list"))
        every { wishlistV2ViewModel.mapToProductCardList(wishlistItemList, any()) } returns listProductCard

        wishlistV2ViewModel.mapToProductCardList(listOf(), "")

        Assert.assertEquals("list", listProductCard[0].typeLayout)
    }

    // mapToTopads
    /*@Test
    suspend fun mapToTopads_returnsNotEmpty() {
        val listTopadsLayout = arrayListOf<WishlistV2TypeLayoutData>()
        listTopadsLayout.add(WishlistV2TypeLayoutData(typeLayout = TYPE_TOPADS))
        coEvery { wishlistV2ViewModel.getTopAdsData(any()) } returns topAdsImageViewModel
        coEvery { wishlistV2ViewModel.mapToTopads(any(), arrayListOf()) } returns listTopadsLayout

        wishlistV2ViewModel.mapToTopads(0, arrayListOf())

        Assert.assertEquals(TYPE_TOPADS, listTopadsLayout[0].typeLayout)
    }*/

    // mapToTopads
    /*@Test
    suspend fun organizeWishlistV2Data_returnsNotEmpty() {
        val listLayout = arrayListOf<WishlistV2TypeLayoutData>()

        listLayout.add(WishlistV2TypeLayoutData(typeLayout = TYPE_TOPADS))
        listLayout.add(WishlistV2TypeLayoutData(typeLayout = TYPE_RECOMMENDATION_LIST))

        coEvery { wishlistV2ViewModel.getTopAdsData(any()) } returns topAdsImageViewModel
        coEvery { wishlistV2ViewModel.mapToTopads(any(), arrayListOf()) } returns listLayout
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), arrayListOf()) } returns listLayout

        every {
            wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem)
        } returns listProductCardModel

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.organizeWishlistV2Data(wishlistV2Response.data.wishlistV2, "")
        } returns listOf()

        coEvery {
            wishlistV2ViewModel.mapToEmptyState(wishlistV2Response.data.wishlistV2, any(), any())
        } returns arrayListOf()

        coEvery {
            wishlistV2ViewModel.mapToProductCardList(wishlistItemList, any())
        } returns arrayListOf()

        coEvery {
            wishlistV2UseCase.executeSuspend(any())
        } returns wishlistV2Response.data

        coEvery {
            getSingleRecommendationUseCase.getData(any())
        } returns recommendationWidget


        //when
        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")
        wishlistV2ViewModel.organizeWishlistV2Data(wishlistV2Response.data.wishlistV2, "")
        *//*wishlistV2ViewModel.getTopAdsData("")
        wishlistV2ViewModel.mapToTopads(0, arrayListOf())
        wishlistV2ViewModel.mapToRecommendation(0, arrayListOf())*//*

        assert(wishlistV2ViewModel.wishlistV2.value is Success)
        Assert.assertEquals(TYPE_TOPADS, listLayout[0].typeLayout)
    }*/

    // getRecommendationWishlistV2
   /* @Test
    suspend fun getRecommendationWishlistV2_returnsWishlistV2RecommendationDataModel() {
        val recomWishlistV2 = WishlistV2RecommendationDataModel(title = "recomTest")
        coEvery { wishlistV2ViewModel.getRecommendationWishlistV2(any(), arrayListOf(), any()) } returns recomWishlistV2

        wishlistV2ViewModel.getRecommendationWishlistV2(0, arrayListOf(), "")

        coVerify {  wishlistV2ViewModel.getRecommendationWishlistV2(0, arrayListOf(), "")}
        Assert.assertEquals("recomTest", recomWishlistV2.title)
    }*/

}