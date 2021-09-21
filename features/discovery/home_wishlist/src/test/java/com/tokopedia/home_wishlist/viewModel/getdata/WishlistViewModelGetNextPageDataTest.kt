package com.tokopedia.home_wishlist.viewModel.getdata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.GetWishlistParameter
import com.tokopedia.home_wishlist.model.datamodel.BannerTopAdsDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetImageData
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 25/07/20.
 */

class WishlistViewModelGetNextPageDataTest {
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
    fun `Get next page data and received empty wishlist would not change wishlist data value`() {

        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        // Get wishlist use case returns 3 wishlist data with values for initial data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ), hasNextPage = true, page = currentPage)

        // Live data is filled by data from getWishlistData
        wishlistViewModel.getWishlistData()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase returns empty wishlist item data for next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(),
                hasNextPage = false, page = nextPage)

        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlist data is still same as initial value
        Assert.assertEquals(3, wishlistViewModel.wishlistLiveData.value!!.size)
    }

    @Test
    fun `Get next page data success will add existing data with new data`() {

        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        // Get wishlist usecase returns 3 wishlist data with values") {
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ), page = currentPage)

        // User id
        every { userSessionInterface.userId } returns mockUserId

        // Live data is filled by data from getWishlistData
        wishlistViewModel.getWishlistData()
        // Get wishlist usecase returns 3 wishlist item data for next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                wishlistItems = listOf(
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7")
                ),
                hasNextPage = false, page = nextPage
        )


        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlistLiveData has 6 (3 old + 3 new) wishlist data
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
    }

    @Test
    fun `Get next page data success in bulk mode should not contains recommendation carousel`() {
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase returns 20 wishlist data with values
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true,
                page = currentPage,
                hasNextPage = true)

        // Get wishlist usecase returns 20 wishlist item data for next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true,
                hasNextPage = false,
                page = nextPage
        )

        // Get recommendation usecase always returns data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
        // Live data is filled by data from getWishlistData
        wishlistViewModel.getWishlistData()
        // Wishlist enter bulk mode
        wishlistViewModel.enterBulkMode()

        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlistLiveData has 40 items (20 page1 + 20 page2) wishlist data
        Assert.assertEquals(40, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect wishlist data not contains recommendation data model
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is RecommendationCarouselDataModel) {
                Assert.assertFalse("Wishlist contains recommendation data model in bulk load more", true)
            }
        }

    }

    @Test
    fun `Get next page data success in bulk mode will add existing data with new data in bulk mode`() {
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        // Get wishlist usecase returns 3 wishlist data with values
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ), page = currentPage)

        // Live data is filled by data from getWishlistData
        wishlistViewModel.getWishlistData()
        // Wishlist view model entering bulk mode
        wishlistViewModel.enterBulkMode()
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase returns 3 wishlist item data for next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                wishlistItems = listOf(
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7")
                ),
                hasNextPage = false, page = nextPage
        )

        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlistLiveData has 6 (3 old + 3 new) wishlist data
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect all visitable is set to bulk mode
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is RecommendationCarouselDataModel && !it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Item recommendation carousel not in bulk mode", true)
            }
            if (it is WishlistItemDataModel && !it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Item wishlist not in bulk mode", true)
            }
        }
    }

    @Test
    fun `Get next page data failed and would not change wishlist data value and trigger load more action data`() {

        val mockErrorMessage = "NOT OKAY"
        val keyword = "contoh"
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))
        // Get wishlist usecase returns 5 wishlist data on current page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3"),
                WishlistItem(id="4"),
                WishlistItem(id="5")
        ), keyword, page = currentPage)
        // Get wishlist usecase returns 9 wishlist data with values on next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="6"),
                WishlistItem(id="7"),
                WishlistItem(id="8"),
                WishlistItem(id="9"),
                WishlistItem(id="10"),
                WishlistItem(id="11"),
                WishlistItem(id="12"),
                WishlistItem(id="13"),
                WishlistItem(id="14")
        ), keyword, page = nextPage, hasNextPage = false)

        // Get recommendation usecase always returns recommendation widget
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                recommendationItems = listOf()
        )
        // View model get initial wishlist page
        wishlistViewModel.getWishlistData(keyword)
        // Get wishlist usecase is failed")
        coEvery { getWishlistDataUseCase.getData(
                GetWishlistParameter(keyword, nextPage)
        ) } returns WishlistEntityData(isSuccess = false, errorMessage = mockErrorMessage)

        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlist data is not changed
        Assert.assertEquals(7, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect load more action data is triggered with error message
        Assert.assertEquals(false, wishlistViewModel.loadMoreWishlistAction.value!!.peekContent().isSuccess)
        Assert.assertEquals(mockErrorMessage, wishlistViewModel.loadMoreWishlistAction.value!!.peekContent().message)

        // Expect load more wishlist action data can only retrieved once
        val wishlistEventData = wishlistViewModel.loadMoreWishlistAction.value!!

        wishlistEventData.getContentIfNotHandled()
        val eventLoadMoreSecond = wishlistEventData.getContentIfNotHandled()
        Assert.assertEquals(null, eventLoadMoreSecond)
    }

    @Test
    fun `Get next page data throws error would not change wishlist data value and trigger load more action data`() {

        val keyword = "contoh"
        val currentPage = 1
        val nextPage = 2
        val mockErrorMessage = "NOT OKAY"

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns 5 wishlist data on current page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3"),
                WishlistItem(id="4"),
                WishlistItem(id="5")
        ), keyword, page = currentPage)

        // Get wishlist usecase returns 9 wishlist data with values on next page"
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="6"),
                WishlistItem(id="7"),
                WishlistItem(id="8"),
                WishlistItem(id="9"),
                WishlistItem(id="10"),
                WishlistItem(id="11"),
                WishlistItem(id="12"),
                WishlistItem(id="13"),
                WishlistItem(id="14")
        ), keyword, page = nextPage, hasNextPage = false)

        // Get recommendation usecase returns recommendation widget
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
        // View model get initial wishlist page
        wishlistViewModel.getWishlistData(keyword)
        // Get wishlist usecase throws error
        coEvery { getWishlistDataUseCase.getData(
                GetWishlistParameter(keyword, nextPage)
        ) } answers {
            throw Throwable(mockErrorMessage)
        }


        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlist data is not changed
        Assert.assertEquals(7, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect load more action data is triggered with error message
        Assert.assertEquals(false, wishlistViewModel.loadMoreWishlistAction.value!!.peekContent().isSuccess)
        Assert.assertEquals(mockErrorMessage, wishlistViewModel.loadMoreWishlistAction.value!!.peekContent().message)

        // Expect load more wishlist action data can only retrieved once
        val wishlistEventData = wishlistViewModel.loadMoreWishlistAction.value!!
        wishlistEventData.getContentIfNotHandled()
        val eventLoadMoreSecond = wishlistEventData.getContentIfNotHandled()
        Assert.assertEquals(null, eventLoadMoreSecond)

    }

    @Test
    fun `Recommendation widget is positioned in position 4 in every load more page request when fetch recom success`() {

        val keyword = "contoh"
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )
        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase returns 20 wishlist data on current page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true,
                keyword = keyword,
                page = currentPage)

        // Get wishlist usecase returns 20 wishlist data with values on next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true,
                keyword = keyword,
                page = nextPage)

        // Get recommendation usecase returns recommendation widget
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
        // View model get initial wishlist page
        wishlistViewModel.getWishlistData(keyword)

        // View model get next page wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlistLiveData has 43 items (40 wishlist data + 2 recommendation widget + 1 topads)
        Assert.assertEquals(43, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect every 4 product recommendation widget is showed, in 4 for 1st page and 25 for 2nd page
        Assert.assertEquals(BannerTopAdsDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)
        Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![21].javaClass)
    }

    @Test
    fun `Recommendation widget is not showed when fetch recom return empty`() {

        val keyword = "contoh"
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase returns 20 wishlist data on current page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true,
                keyword = keyword,
                page = currentPage)
        // Get wishlist usecase returns 20 wishlist data with values on next page
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                useDefaultWishlistItem = true,
                keyword = keyword,
                page = nextPage)
        // Get recommendation usecase returns 1 recommendation widget

        coEvery {
            getRecommendationUseCase.getData(any()) } returns
                listOf()
        // View model get initial wishlist page
        wishlistViewModel.getWishlistData(keyword)

        // View model get wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect wishlistLiveData has 40 items of wishlist data
        Assert.assertEquals(40, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect no recommendation widget is showing
        wishlistViewModel.wishlistLiveData.value!!.forEach {
            if (it is RecommendationCarouselDataModel) {
                Assert.assertFalse("Recommendation widget should not existed", true)
            }
        }
    }

    @Test
    fun `Get next page data increase page number when fetch data success`() {

        val keyword = "keyword"
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(
                userSessionInterface = userSessionInterface,
                getWishlistDataUseCase = getWishlistDataUseCase,
                getSingleRecommendationUseCase = getSingleRecommendationUseCase,
                getRecommendationUseCase = getRecommendationUseCase,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase successfully returns 3 wishlist item data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ), keyword = keyword, hasNextPage = true, page = currentPage)
        // Get wishlist usecase 1 returns empty data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                WishlistItem(id="1"),
                WishlistItem(id="2"),
                WishlistItem(id="3")
        ), keyword = keyword, page = nextPage, hasNextPage = true)

        // View model get wishlist data
        wishlistViewModel.getWishlistData(keyword = keyword)
        // View model get next page wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect page number increased
        Assert.assertEquals(nextPage, wishlistViewModel.currentPage)
    }

    @Test
    fun `Get next page data doesn't increase page number when fetch data failed`() {
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface, getWishlistDataUseCase, getSingleRecommendationUseCase, getRecommendationUseCase)
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase for page 2 is failed
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                wishlistItems = listOf(),
                hasNextPage = true,
                page = nextPage
        )
        coEvery { getWishlistDataUseCase.getData(GetWishlistParameter(page = nextPage)) } returns WishlistEntityData(
                items = listOf(),
                hasNextPage = true,
                isSuccess = false)
        // Get wishlist usecase for page 1 returns empty data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                listOf(),
                page = currentPage,
                hasNextPage = true
        )

        // View model get next page wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect page number not increased
        Assert.assertEquals(currentPage, wishlistViewModel.currentPage)
    }

    @Test
    fun `Get next page data doesn't increase page number when fetch data throws error`() {

        val keyword = "keyword"
        val currentPage = 1
        val nextPage = 2

        // Wishlist view model
        wishlistViewModel = createWishlistViewModel(userSessionInterface, getWishlistDataUseCase, getSingleRecommendationUseCase, getRecommendationUseCase)
        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Get wishlist usecase for page 2 throws error
        coEvery { getWishlistDataUseCase.getData(
                GetWishlistParameter(keyword, nextPage)
        ) } answers {
            throw Throwable("Error")
        }
        // Wishlist repository for page 1 returns empty data
        getWishlistDataUseCase.givenGetWishlistDataReturnsThis(
                keyword = keyword,
                wishlistItems = listOf(),
                page = currentPage,
                hasNextPage = true
        )

        // View model get next page wishlist data
        wishlistViewModel.getNextPageWishlistData()

        // Expect page number not increased
        Assert.assertEquals(currentPage, wishlistViewModel.currentPage)
    }
}