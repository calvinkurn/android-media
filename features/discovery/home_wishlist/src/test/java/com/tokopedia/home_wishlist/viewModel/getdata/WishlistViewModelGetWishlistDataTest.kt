package com.tokopedia.home_wishlist.viewModel.getdata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.util.Status
import com.tokopedia.home_wishlist.viewModel.*
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 25/07/20.
 */

class WishlistViewModelGetWishlistDataTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wishlistViewModel: WishlistViewModel
    private val mockUserId = "12345"
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getSingleRecommendationUseCase = mockk<GetSingleRecommendationUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)

    @Test
    fun `Test Load Initial Page and error`(){

        val observer = mockk<Observer<Status>>(relaxed = true)
        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface= userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase,
                getRecommendationUseCase = getRecommendationUseCase)

        wishlistViewModel.isWishlistState.observeForever(observer)


        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist usecase throws error
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")

        // Live data is filled by 3 data from getWishlistData
        wishlistViewModel.getWishlistData(shouldShowInitialPage = true)

        // Expect wishlistLiveData is reset and has error model
        Assert.assertEquals(1, wishlistViewModel.wishlistLiveData.value!!.size)
        Assert.assertEquals(ErrorWishlistDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![0].javaClass)

        verify {
            observer.onChanged(Status.LOADING)
        }
    }

    @Test
    fun `Get wishlist data doesn't increase page number when failed`(){
        val defaultGetWishlistPageNumber = 0
        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface= userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase,
                getRecommendationUseCase = getRecommendationUseCase)

        // Get wishlist use case returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        // Live data is filled by empty data from getWishlistData
        wishlistViewModel.getWishlistData()

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist usecase failed to fetch data
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                isSuccess = false,
                errorMessage = ""
        )

        // Get recommendation usecase successfuly get data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())


        // View model get wishlist data
        wishlistViewModel.getWishlistData()

        // Expect page number still in default page number
        Assert.assertEquals(defaultGetWishlistPageNumber, wishlistViewModel.currentPage)

    }

    @Test
    fun `Get wishlist data increase page number when success`(){
        val defaultGetWishlistPageNumber = 0

        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase)

        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        wishlistViewModel.getWishlistData()

        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist use case successfully returns 3 wishlist item data
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                ),
                hasNextPage = true
        )

        // Get recommendation usecase successfully returns data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())


        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect page number become 3
        Assert.assertEquals(defaultGetWishlistPageNumber + 1, wishlistViewModel.currentPage)


    }

    @Test
    fun `Recommendation widget is not showed when fetch recom return empty`(){
        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        // Live data is filled by empty data from getWishlistData
        wishlistViewModel.getWishlistData()

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist usecase returns 9 wishlist item data in a request
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7"),
                        WishlistItem(id="8"),
                        WishlistItem(id="9")
                ),
                hasNextPage = false
        )

        // Get recommendation usecase returns empty data
        coEvery { getRecommendationUseCase.getData(any()) } returns listOf()


        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()


        // Expect wishlistLiveData has 9 items of wishlist data
        Assert.assertEquals(10, wishlistViewModel.wishlistLiveData.value!!.size)

        // Expect no recommendation widget is showing
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is RecommendationCarouselDataModel) {
                Assert.assertFalse("Recommendation widget should not existed", true)
            }
        }
    }

    @Test
    fun `Recommendation widget is positioned in position 4 in every page request when fetch recom success`(){

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        // Get wishlist usecase returns 9 wishlist item data in a request
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                        WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                        WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                        WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
                ),
                hasNextPage = false
        )

        // User id
        every { userSessionInterface.userId } returns mockUserId andThen null


        //  Get recommendation usecase return data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf(
                RecommendationItem()
        ))

        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect wishlistLiveData has 21 items (20 wishlist data + 1 recommendation widget)
        Assert.assertEquals(21, wishlistViewModel.wishlistLiveData.value!!.size)

        wishlistViewModel.getNextPageWishlistData()

        // Expect every 4 product recommendation widget is showed
        Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![25].javaClass)


        wishlistViewModel.onProductClick(0, 25, 0)
        Assert.assertTrue(wishlistViewModel.productClickActionData.value != null)

        wishlistViewModel.onPDPActivityResultForWishlist(0, true)
        Assert.assertTrue((wishlistViewModel.wishlistLiveData.value!![25] as RecommendationCarouselDataModel).list.get(0).recommendationItem.isWishlist)


        Assert.assertEquals(wishlistViewModel.getUserId(), mockUserId)
        Assert.assertEquals(wishlistViewModel.getUserId(), "")
    }

    @Test
    fun `Recommendation widget is positioned in position 4 in every page request when fetch recom success and try click with null position click`(){

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        // Get wishlist usecase returns 9 wishlist item data in a request
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                        WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                        WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                        WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
                ),
                hasNextPage = false
        )

        // User id
        every { userSessionInterface.userId } returns mockUserId andThen null


        //  Get recommendation usecase return data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf(
                RecommendationItem()
        ))

        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect wishlistLiveData has 21 items (20 wishlist data + 1 recommendation widget)
        Assert.assertEquals(21, wishlistViewModel.wishlistLiveData.value!!.size)

        wishlistViewModel.getNextPageWishlistData()

        // Expect every 4 product recommendation widget is showed
        Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![25].javaClass)


        Assert.assertTrue(wishlistViewModel.productClickActionData.value == null)

        wishlistViewModel.onPDPActivityResultForWishlist(0, true)


        Assert.assertEquals(wishlistViewModel.getUserId(), mockUserId)
        Assert.assertEquals(wishlistViewModel.getUserId(), "")
    }

    @Test
    fun `Recommendation widget is positioned in position 4 in every page request when fetch recom success and try click`(){

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase, topAdsImageViewUseCase = topAdsImageViewUseCase)

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        // Get wishlist usecase returns 9 wishlist item data in a request
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                        WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                        WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                        WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
                ),
                hasNextPage = false
        )

        // User id
        every { userSessionInterface.userId } returns mockUserId andThen null


        //  Get recommendation usecase return data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf(
                RecommendationItem()
        ))

        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect wishlistLiveData has 21 items (20 wishlist data + 1 recommendation widget)
        Assert.assertEquals(21, wishlistViewModel.wishlistLiveData.value!!.size)

        wishlistViewModel.getNextPageWishlistData()

        // Expect every 4 product recommendation widget is showed
        Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![25].javaClass)


        Assert.assertTrue(wishlistViewModel.productClickActionData.value == null)

        wishlistViewModel.onPDPActivityResultForWishlist(0, true)
        Assert.assertTrue(!(wishlistViewModel.wishlistLiveData.value!![25] as RecommendationCarouselDataModel).list.get(0).recommendationItem.isWishlist)


        Assert.assertEquals(wishlistViewModel.getUserId(), mockUserId)
        Assert.assertEquals(wishlistViewModel.getUserId(), "")
    }

    @Test
    fun `Get wishlist data failed with non-empty wishlistLiveData will reset wishlist data and add error model`(){

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase)

        // Get wishlist usecase returns wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ))

        // Live data is filled by 3 data from getWishlistData
        wishlistViewModel.getWishlistData()

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist usecase throws error
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")


        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect wishlistLiveData is reset and has error model
        Assert.assertEquals(1, wishlistViewModel.wishlistLiveData.value!!.size)
        Assert.assertEquals(ErrorWishlistDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![0].javaClass)
    }


    @Test
    fun `Get wishlist data success with non-empty wishlistLiveData will override by new data`(){
        val keywordFirst = "aduh"
        val keywordSecond = "yoi"

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase)

        // Get wishlist usecase returns wishlist data below recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2")
        ), keywordFirst)

        // Get wishlist usecase returns new wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="5"),
                WishlistItem(id="6"),
                WishlistItem(id="7")
        ), keywordSecond)

        // get recommendation usecase returns 1 recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Live data is filled by data from getWishlist keyword first
        wishlistViewModel.getWishlistData(keywordFirst)

        // Live data is filled by data from getWishlist keyword second
        wishlistViewModel.getWishlistData(keywordSecond)

        // User id
        every { userSessionInterface.userId } returns mockUserId


        // Expect wishlistLiveData has 3 new wishlist data
        Assert.assertEquals(3, wishlistViewModel.wishlistLiveData.value!!.size)
    }


    @Test
    fun `Get wishlist data with non-empty initial wishlist data and received empty wishlist will reset wishlistLiveData with empty model`(){
        val keywordFirst = "aduh"
        val keywordSecond = "yoi"

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface= userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist data for first keyword returns wishlist data above recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3"),
                WishlistItem(id="4"),
                WishlistItem(id="5"),
                WishlistItem(id="6"),
                WishlistItem(id="7"),
                WishlistItem(id="8"),
                WishlistItem(id="9")
        ), keywordFirst)

        // Get wishlist use case for second keyword returns empty wishlist data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(), keywordSecond)

        // Get recommendation use case returns 1 recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Get single recommendation usecase getSingleRecommendation returns 4 recommendation data
        coEvery { getSingleRecommendationUseCase.getData(any()) } returns
                RecommendationWidget(
                        recommendationItemList = listOf(
                                RecommendationItem(),
                                RecommendationItem(),
                                RecommendationItem(),
                                RecommendationItem()
                        )
                )

        // Live data is filled by data from getWishlist keyword first
        wishlistViewModel.getWishlistData(keywordFirst)

        // Live data is filled by data from getWishlist keyword second
        wishlistViewModel.getWishlistData(keywordSecond)

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist usecase returns empty wishlist item data
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(),
                hasNextPage = false
        )

        // View model get wishlist data
        wishlistViewModel.getWishlistData()

        // Expect wishlistLiveData has 6 data, 1 empty wishlist, 1 recom title model and 4 recommendation
        Assert.assertEquals(wishlistViewModel.getUserId(), mockUserId)
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
        Assert.assertEquals(EmptyWishlistDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![0].javaClass)
        Assert.assertEquals(RecommendationTitleDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![1].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![2].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![3].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![5].javaClass)

    }

    @Test
    fun `Get wishlist data and received empty wishlist will add empty view model and recommendation`(){
        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface= userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase)

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist data returns empty wishlist item data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())

        // Get recommendation getSingleRecommendation returns 4 recommendation data
        getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem()
        ))


        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect isWishlistErrorInFirstPageState is true
        Assert.assertEquals(false, wishlistViewModel.isWishlistErrorInFirstPageState.value)

        // Expect wishlistLiveData has 6 data, 1 empty wishlist, 1 recom title model and 4 recommendation
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
        Assert.assertEquals(EmptyWishlistDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![0].javaClass)
        Assert.assertEquals(RecommendationTitleDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![1].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![2].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![3].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)
        Assert.assertEquals(RecommendationItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![5].javaClass)
    }

    @Test
    fun `Get wishlist data failed with empty initial wishlist data will set wishlist data with error model`(){


        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface= userSessionInterface, getWishlistDataUseCase = getWishlistDataUseCase, getRecommendationUseCase = getRecommendationUseCase)

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Wishlist repository returns failed wishlist entity data
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")


        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect isWishlistErrorInFirstPageState is true
        Assert.assertEquals(true, wishlistViewModel.isWishlistErrorInFirstPageState.value)

        // Expect wishlistLiveData has only 1 error view model
        Assert.assertEquals(1, wishlistViewModel.wishlistLiveData.value!!.size)
        Assert.assertEquals(ErrorWishlistDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![0].javaClass)


    }

    @Test
    fun `Get wishlist data success with empty initial wishlist data will add new wishlist data`(){

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface= userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase)

        // Get wishlist data returns wishlist data below recommendation threshold (4)
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ))

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Get wishlist data returns 3 wishlist item data
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                items = listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                ),
                hasNextPage = false
        )


        // View model get wishlist data
        wishlistViewModel.getWishlistData()


        // Expect wishlistLiveData has only 3 wishlist data")
        Assert.assertEquals(3, wishlistViewModel.wishlistLiveData.value!!.size)

    }
}