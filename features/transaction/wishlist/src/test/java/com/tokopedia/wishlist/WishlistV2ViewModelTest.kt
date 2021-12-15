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
import com.tokopedia.wishlist.data.model.WishlistV2EmptyStateData
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.DeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
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
    private var wishlistEmptyItem = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistOneItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistTwoItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistThreeItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistFourItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistFiveItemList = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var emptyWishlistItem = listOf<WishlistV2Response.Data.WishlistV2.Item>()
    private var wishlistV2Response = WishlistV2Response()
    private var recommendationWidget = RecommendationWidget()
    private var wishlistRecommendation = WishlistV2RecommendationDataModel()
    private var listProductCardModel = listOf<ProductCardModel>()
    private var emptyListWishlistV2TypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2TypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2OneItemLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2TwoItemLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FourItemsOnly = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FourItemsTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FiveItemsOnly = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FiveItemsTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FiveItemsHasNextPageTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var listWishlistV2WithRecommendationTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var emptyWishlistNotFoundV2TypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var emptyWishlistEmptyStateV2TypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var emptyWishlistCarouselV2TypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
    private var emptyWishlistRecommendationTypeLayoutData = arrayListOf<WishlistV2TypeLayoutData>()
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

        val primaryButton1 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "ADD_TO_CART")
        val primaryButton2 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "SEE_SIMILAR_PRODUCT")
        val primaryButton3 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "ADD_TO_CART")
        val primaryButton4 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "SEE_SIMILAR_PRODUCT")
        val primaryButton5 = WishlistV2Response.Data.WishlistV2.Item.Buttons.PrimaryButton(action = "ADD_TO_CART")

        val wishlistItem1 = WishlistV2Response.Data.WishlistV2.Item(name = "Test1",
                buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton1))
        val wishlistItem2 = WishlistV2Response.Data.WishlistV2.Item(name = "Test2",
                buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton2))
        val wishlistItem3 = WishlistV2Response.Data.WishlistV2.Item(name = "Test3",
                buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton3))
        val wishlistItem4 = WishlistV2Response.Data.WishlistV2.Item(name = "Test4",
                buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton4))
        val wishlistItem5 = WishlistV2Response.Data.WishlistV2.Item(name = "Test5",
                buttons = WishlistV2Response.Data.WishlistV2.Item.Buttons(primaryButton = primaryButton5))

        wishlistOneItemList = arrayListOf(wishlistItem1)
        wishlistTwoItemList = arrayListOf(wishlistItem1, wishlistItem2)
        wishlistThreeItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3)
        wishlistFourItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3, wishlistItem4)
        wishlistFiveItemList = arrayListOf(wishlistItem1, wishlistItem2, wishlistItem3, wishlistItem4, wishlistItem5)
        emptyWishlistItem = emptyList()

        val productCardModel1 = ProductCardModel(productName = "product1",
                labelGroupList = listOf(ProductCardModel.LabelGroup()),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
                hasAddToCartButton = true)

        val productCardModel2 = ProductCardModel(productName = "product2",
                labelGroupList = listOf(ProductCardModel.LabelGroup()),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
                hasAddToCartButton = false)

        val productCardModel3 = ProductCardModel(productName = "product3",
                labelGroupList = listOf(ProductCardModel.LabelGroup()),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
                hasAddToCartButton = true)

        val productCardModel4 = ProductCardModel(productName = "product4",
                labelGroupList = listOf(ProductCardModel.LabelGroup()),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
                hasAddToCartButton = true)

        val productCardModel5 = ProductCardModel(productName = "product5",
                labelGroupList = listOf(ProductCardModel.LabelGroup()),
                shopBadgeList = listOf(ProductCardModel.ShopBadge()),
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupVariantList = listOf(ProductCardModel.LabelGroupVariant()),
                variant = ProductCardModel.Variant(), nonVariant = ProductCardModel.NonVariant(),
                hasAddToCartButton = true)

        listProductCardModel = listOf(productCardModel1, productCardModel2, productCardModel3)

        val recommItem1 = RecommendationItem(name = "recomm1")
        val recommItem2 = RecommendationItem(name = "recomm2")
        val recommItem3 = RecommendationItem(name = "recomm3")
        listRecommendationItem = listOf(recommItem1, recommItem2, recommItem3)

        wishlistV2Response = WishlistV2Response(WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(items = wishlistThreeItemList)))
        recommendationWidget = RecommendationWidget(tid = "123", recommendationItemList = listRecommendationItem,
                recommendationFilterChips = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip()), title = "TestRecomm")

        wishlistRecommendation = WishlistV2RecommendationDataModel(listProductCardModel, listRecommendationItem, "TitleRecommendation")

        val typeTitleRecommendation = WishlistV2TypeLayoutData("", TYPE_RECOMMENDATION_TITLE)
        val typeRecommendationList = WishlistV2TypeLayoutData("", TYPE_RECOMMENDATION_LIST)
        val typeRecommendationCarousel = WishlistV2TypeLayoutData("", TYPE_RECOMMENDATION_CAROUSEL)

        topAdsImageViewModel = TopAdsImageViewModel(bannerName = "testBanner")

        val topadsLayout = WishlistV2TypeLayoutData(topAdsImageViewModel, TYPE_TOPADS)
        val itemLayout1 = WishlistV2TypeLayoutData(productCardModel1, TYPE_LIST)
        val itemLayout2 = WishlistV2TypeLayoutData(productCardModel2, TYPE_LIST)
        val itemLayout3 = WishlistV2TypeLayoutData(productCardModel3, TYPE_LIST)
        val itemLayout4 = WishlistV2TypeLayoutData(productCardModel4, TYPE_LIST)
        val itemLayout5 = WishlistV2TypeLayoutData(productCardModel5, TYPE_LIST)
        listWishlistV2TypeLayoutData = arrayListOf(itemLayout1, itemLayout2, itemLayout3)
        listWishlistV2OneItemLayoutData = arrayListOf(itemLayout1)
        listWishlistV2TwoItemLayoutData = arrayListOf(itemLayout1, itemLayout2)
        listWishlistV2FourItemsOnly = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4)
        listWishlistV2FourItemsTypeLayoutData = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4, topadsLayout, typeTitleRecommendation, typeRecommendationCarousel)
        listWishlistV2FiveItemsOnly = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4, topadsLayout, itemLayout5)
        listWishlistV2FiveItemsTypeLayoutData = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4, topadsLayout, itemLayout5, typeTitleRecommendation, typeRecommendationCarousel)
        listWishlistV2FiveItemsHasNextPageTypeLayoutData = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4, topadsLayout, itemLayout5)
        listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4, typeTitleRecommendation, typeRecommendationCarousel)
        listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData = arrayListOf(itemLayout1, itemLayout2, itemLayout3, itemLayout4, topadsLayout, itemLayout5)

        val typeDataEmptyNotFound = WishlistV2TypeLayoutData("test", TYPE_EMPTY_NOT_FOUND)
        emptyWishlistNotFoundV2TypeLayoutData = arrayListOf(typeDataEmptyNotFound, typeTitleRecommendation, typeRecommendationList)

        val typeDataEmptyState = WishlistV2TypeLayoutData(WishlistV2EmptyStateData(), TYPE_EMPTY_STATE)
        emptyWishlistEmptyStateV2TypeLayoutData = arrayListOf(typeDataEmptyState, typeTitleRecommendation, typeRecommendationList)

        val typeEmptyStateCarousel = WishlistV2TypeLayoutData("", TYPE_EMPTY_STATE_CAROUSEL)
        emptyWishlistCarouselV2TypeLayoutData = arrayListOf(typeEmptyStateCarousel, typeTitleRecommendation, typeRecommendationList)

        emptyWishlistRecommendationTypeLayoutData = arrayListOf(typeDataEmptyNotFound, typeTitleRecommendation, typeRecommendationList)

        listWishlistV2WithRecommendationTypeLayoutData = arrayListOf(itemLayout1, itemLayout2, typeTitleRecommendation, typeRecommendationList)
    }

    // wishlist_v2_success
    @Test
    fun loadWishlistV2_shouldReturnSuccess() {
        //given

        coEvery {
            wishlistV2ViewModel.getRecommendationWishlistV2(any(), listOf(), any())
        } returns wishlistRecommendation

        coEvery {
            wishlistV2ViewModel.getTopAdsData()
        } returns topAdsImageViewModel

        coEvery {
            wishlistV2ViewModel.organizeWishlistV2Data(wishlistV2Response.data.wishlistV2, "")
        } returns listOf()

        coEvery {
            wishlistV2ViewModel.mapToEmptyState(wishlistV2Response.data.wishlistV2, any(), any())
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

    // convertRecommendationIntoProductDataModel returns not empty
    @Test
    fun convertRecommendationIntoProductDataModel_returnsNotEmpty() {
        every { wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem) } returns listProductCardModel

        wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem)

        Assert.assertEquals("product1", listProductCardModel[0].productName)
    }

    /*@Test
    fun convertRecommendationIntoProductDataModel_returnsExpectedShopBadge() {
        every { wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem) } returns listProductCardModel

        wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem)

        Assert.assertEquals("shopBadgeImgUrl1", listProductCardModel[0].shopBadgeList[0].imageUrl)
        Assert.assertEquals("shopBadgeImgUrl2", listProductCardModel[1].shopBadgeList[0].imageUrl)
        Assert.assertEquals("shopBadgeImgUrl3", listProductCardModel[2].shopBadgeList[0].imageUrl)
    }

    @Test
    fun convertRecommendationIntoProductDataModel_returnsExpectedLabelGroup() {
        every { wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem) } returns listProductCardModel

        wishlistV2ViewModel.convertRecommendationIntoProductDataModel(listRecommendationItem)

        Assert.assertEquals("titleLabel1", listProductCardModel[0].labelGroupList[0].title)
        Assert.assertEquals("titleLabel2", listProductCardModel[1].labelGroupList[0].title)
        Assert.assertEquals("titleLabel3", listProductCardModel[2].labelGroupList[0].title)
    }*/

    // mapToProductCardList
    @Test
    fun mapToProductCardList_returnWithAppropriateButton() {
        wishlistV2ViewModel.mapToProductCardList(wishlistThreeItemList, "")

        val data1HasAddToCartButton = (listWishlistV2TypeLayoutData[0].dataObject as ProductCardModel).hasAddToCartButton
        val data2HasAddToCartButton = (listWishlistV2TypeLayoutData[1].dataObject as ProductCardModel).hasAddToCartButton
        val data3HasAddToCartButton = (listWishlistV2TypeLayoutData[2].dataObject as ProductCardModel).hasAddToCartButton

        Assert.assertEquals(data1HasAddToCartButton, listWishlistV2TypeLayoutData[0].dataObject is ProductCardModel)
        Assert.assertEquals(!data2HasAddToCartButton, listWishlistV2TypeLayoutData[1].dataObject is ProductCardModel)
        Assert.assertEquals(data3HasAddToCartButton, listWishlistV2TypeLayoutData[2].dataObject is ProductCardModel)
    }

    // mapToTopads
    @Test
    fun mapToTopads_onExpectedIndex() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 1, hasNextPage = false))

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_TOPADS))
    }

    @Test
    fun mapToTopads_onIndexZero() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 4, items = wishlistFourItemList, page = 1, hasNextPage = false))

        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_TOPADS))
    }

    @Test
    fun mapToRecommendation_onIndexZero() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 3, items = wishlistThreeItemList, page = 1, hasNextPage = false))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[3].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToRecommendation_onExpectedIndex() {
        val listItemWishlist = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(totalData = 5, items = wishlistFiveItemList, page = 2, hasNextPage = true))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns listItemWishlist

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[5].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToEmptyState_whenQueryIsNotEmpty() {
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "test", items = emptyList(), page = 1))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(query = "test"), "")

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

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(query = "", sortFilters = paramListSortFilter), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_STATE))
    }

    @Test
    fun mapToEmptyState_whenFilterIsNotActive_andQueryIsEmpty() {
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "", items = emptyList(), page = 1))

        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget() }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_STATE_CAROUSEL))
    }

    @Test
    fun mapToEmptyState_showRecommendation() {
        val emptyList = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(query = "test", items = emptyList(), page = 1))

        val recomItem = RecommendationItem(productId = 1L)
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget(recommendationItemList = listOf(recomItem)) }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns emptyList

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[0].typeLayout.equals(TYPE_EMPTY_NOT_FOUND))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[1].typeLayout.equals(TYPE_RECOMMENDATION_TITLE))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[2].typeLayout.equals(TYPE_RECOMMENDATION_LIST))
    }

    @Test
    fun mapToList_whenOnlyHaveOnePage() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 1, hasNextPage = false))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(any(), any()) } returns listWishlistV2OneItemLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")
        Assert.assertTrue(listWishlistV2TypeLayoutData[0].typeLayout.equals(TYPE_LIST))
    }

    @Test
    fun mapToList_whenTotalData_smallerThanRecommPosition() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 1, totalData = 2, hasNextPage = false))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(wishlistTwoItemList, TYPE_LIST) } returns listWishlistV2TwoItemLayoutData
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), any()) } returns listWishlistV2WithRecommendationTypeLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        Assert.assertTrue(listWishlistV2WithRecommendationTypeLayoutData[0].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2WithRecommendationTypeLayoutData[1].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2WithRecommendationTypeLayoutData[2].typeLayout.equals(TYPE_RECOMMENDATION_TITLE))
        Assert.assertTrue(listWishlistV2WithRecommendationTypeLayoutData[3].typeLayout.equals(TYPE_RECOMMENDATION_LIST))
    }

    @Test
    fun mapToList_whenTotalData_isSameAsRecommPosition() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 1, totalData = 4, hasNextPage = false))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(wishlistFourItemList, TYPE_LIST) } returns listWishlistV2FourItemsOnly
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), any()) } returns listWishlistV2FourItemsTypeLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[0].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[1].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[2].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[3].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[4].typeLayout.equals(TYPE_TOPADS))
        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[5].typeLayout.equals(TYPE_RECOMMENDATION_TITLE))
        Assert.assertTrue(listWishlistV2FourItemsTypeLayoutData[6].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToList_whenTotalData_isMoreThanRecommPosition() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 1, totalData = 5, hasNextPage = false))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(wishlistFiveItemList, TYPE_LIST) } returns listWishlistV2FiveItemsOnly
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), any()) } returns listWishlistV2FiveItemsTypeLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[0].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[1].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[2].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[3].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[4].typeLayout.equals(TYPE_TOPADS))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[5].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[6].typeLayout.equals(TYPE_RECOMMENDATION_TITLE))
        Assert.assertTrue(listWishlistV2FiveItemsTypeLayoutData[7].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToRecommandation_When_Index_IsMoreThanZero() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 2, totalData = 5, hasNextPage = true, items = wishlistFiveItemList))

        val recomItem = RecommendationItem(productId = 1L)
        coEvery { getSingleRecommendationUseCase.getData(any()) }.answers { RecommendationWidget(recommendationItemList = listOf(recomItem)) }
        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        assert(wishlistV2ViewModel.wishlistV2Data.value is Success)
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[4].typeLayout.equals(TYPE_RECOMMENDATION_TITLE_WITH_MARGIN))
        assert((wishlistV2ViewModel.wishlistV2Data.value as Success).data[5].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToList_whenHasNextPage_onPageOne() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 1, totalData = 5, hasNextPage = true))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(wishlistFiveItemList, TYPE_LIST) } returns listWishlistV2FiveItemsOnly
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), any()) } returns listWishlistV2FiveItemsHasNextPageTypeLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageTypeLayoutData[0].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageTypeLayoutData[1].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageTypeLayoutData[2].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageTypeLayoutData[3].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageTypeLayoutData[4].typeLayout.equals(TYPE_TOPADS))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageTypeLayoutData[5].typeLayout.equals(TYPE_LIST))
    }

    @Test
    fun mapToList_whenHasNextPage_onPageTwo() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 2, totalData = 5, hasNextPage = true))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(wishlistFiveItemList, TYPE_LIST) } returns listWishlistV2FiveItemsOnly
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), any()) } returns listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData[0].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData[1].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData[2].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData[3].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData[4].typeLayout.equals(TYPE_RECOMMENDATION_TITLE))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageSecondPageTypeLayoutData[5].typeLayout.equals(TYPE_RECOMMENDATION_CAROUSEL))
    }

    @Test
    fun mapToList_whenHasNextPage_onPageThree() {
        val wishlistV2ResponseData = WishlistV2Response.Data(WishlistV2Response.Data.WishlistV2(page = 3, totalData = 5, hasNextPage = true))

        coEvery { wishlistV2UseCase.executeSuspend(any()) } returns wishlistV2ResponseData
        coEvery { wishlistV2ViewModel.mapToProductCardList(wishlistFiveItemList, TYPE_LIST) } returns listWishlistV2FiveItemsOnly
        coEvery { wishlistV2ViewModel.mapToRecommendation(any(), any()) } returns listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData

        wishlistV2ViewModel.loadWishlistV2(WishlistV2Params(), "")

        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData[0].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData[1].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData[2].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData[3].typeLayout.equals(TYPE_LIST))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData[4].typeLayout.equals(TYPE_TOPADS))
        Assert.assertTrue(listWishlistV2FiveItemsHasNextPageThirdPageTypeLayoutData[5].typeLayout.equals(TYPE_LIST))
    }
}