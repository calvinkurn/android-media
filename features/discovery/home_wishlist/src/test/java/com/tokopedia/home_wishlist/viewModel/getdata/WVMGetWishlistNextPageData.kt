package com.tokopedia.home_wishlist.viewModel.getdata

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMGetWishlistNextPageData : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Get wishlist next page data") {
        lateinit var wishlistViewmodel: WishlistViewModel
        createWishlistTestInstance()
        val mockUserId = "12345"
        val userSessionInterface by memoized<UserSessionInterface>()
        val wishlistRepository by memoized<WishlistRepository>()

        Scenario("Get next page data and received empty wishlist would not change wishlist data value") {

            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns 3 wishlist data with values for initial data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                ), hasNextPage = true, page = currentPage)
            }
            Given("Live data is filled by data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns empty wishlist item data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(),
                        hasNextPage = false, page = nextPage)
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlist data is still same as initial value") {
                Assert.assertEquals(3, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
        }

        Scenario("Get next page data success will add existing data with new data") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns 3 wishlist data with values") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                ))
            }
            Given("Live data is filled by data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
                        items = listOf(
                                WishlistItem(id="5"),
                                WishlistItem(id="6"),
                                WishlistItem(id="7")
                        ),
                        hasNextPage = false,
                        isSuccess = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlistLiveData has 6 (3 old + 3 new) wishlist data") {
                Assert.assertEquals(6, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
        }

        Scenario("Get next page data failed and would not change wishlist data value and trigger load more action data") {

            val mockErrorMessage = "NOT OKAY"
            val keyword = "contoh"
            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns 5 wishlist data on current page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5")
                ), keyword, page = currentPage)
            }
            Given("Repository returns 9 wishlist data with values on next page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
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
            }
            Given("Repository returns 1 recommendation widget") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(listOf())
            }
            Given("Viewmodel get initial wishlist page") {
                wishlistViewmodel.getWishlistData(keyword)
            }
            Given("Wishlist repository throws error") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(isSuccess = false, errorMessage = mockErrorMessage)
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlist data is not changed") {
                Assert.assertEquals(6, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect load more action data is triggered with error message") {
                Assert.assertEquals(false, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().isSuccess)
                Assert.assertEquals(mockErrorMessage, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().message)
            }
            Then("Expect load more wishlist action data can only retrieved once") {
                val wishlistEventData = wishlistViewmodel.loadMoreWishlistAction.value!!
                val eventLoadMoreFirst = wishlistEventData.getContentIfNotHandled()
                val eventLoadMoreSecond = wishlistEventData.getContentIfNotHandled()
                Assert.assertEquals(null, eventLoadMoreSecond)
            }
        }

        Scenario("Get next page data throws error and would not change wishlist data value and trigger load more action data") {

            val keyword = "contoh"
            val currentPage = 1
            val nextPage = 2
            val mockErrorMessage = "NOT OKAY"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns 5 wishlist data on current page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3"),
                        WishlistItem(id="4"),
                        WishlistItem(id="5")
                ), keyword, page = currentPage)
            }
            Given("Repository returns 9 wishlist data with values on next page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
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
            }
            Given("Repository returns 1 recommendation widget") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(listOf())
            }
            Given("Viewmodel get initial wishlist page") {
                wishlistViewmodel.getWishlistData(keyword)
            }
            Given("Wishlist repository throws error") {
                coEvery { wishlistRepository.getData(any(), nextPage) } answers {
                    throw Throwable(mockErrorMessage)
                }
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlist data is not changed") {
                Assert.assertEquals(6, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect load more action data is triggered with error message") {
                Assert.assertEquals(false, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().isSuccess)
                Assert.assertEquals(mockErrorMessage, wishlistViewmodel.loadMoreWishlistAction.value!!.peekContent().message)
            }
            Then("Expect load more wishlist action data can only retrieved once") {
                val wishlistEventData = wishlistViewmodel.loadMoreWishlistAction.value!!
                val eventLoadMoreFirst = wishlistEventData.getContentIfNotHandled()
                val eventLoadMoreSecond = wishlistEventData.getContentIfNotHandled()
                Assert.assertEquals(null, eventLoadMoreSecond)
            }
        }

        Scenario("Recommendation widget is positioned in position 4 in every load more page request when fetch recom success") {

            val keyword = "contoh"
            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Repository returns 20 wishlist data on current page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(
                        useDefaultWishlistItem = true,
                        keyword = keyword,
                        page = currentPage)
            }
            Given("Repository returns 20 wishlist data with values on next page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(
                        useDefaultWishlistItem = true,
                        keyword = keyword,
                        page = nextPage)
            }
            Given("Repository returns 1 recommendation widget") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(listOf())
            }
            Given("Viewmodel get initial wishlist page") {
                wishlistViewmodel.getWishlistData(keyword)
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlistLiveData has 42 items (40 wishlist data + 2 recommendation widget)") {
                Assert.assertEquals(42, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect every 4 product recommendation widget is showed, in 4 for 1st page and 25 for 2nd page") {
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![25].javaClass)
            }
        }

        Scenario("Recommendation widget is not showed when fetch recom return empty") {

            val keyword = "contoh"
            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Repository returns 20 wishlist data on current page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(
                        useDefaultWishlistItem = true,
                        keyword = keyword,
                        page = currentPage)
            }
            Given("Repository returns 20 wishlist data with values on next page") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(
                        useDefaultWishlistItem = true,
                        keyword = keyword,
                        page = nextPage)
            }
            Given("Repository returns 1 recommendation widget") {
                coEvery {
                    wishlistRepository.getRecommendationData(any(), any()) } returns
                        listOf()
            }
            Given("Viewmodel get initial wishlist page") {
                wishlistViewmodel.getWishlistData(keyword)
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect wishlistLiveData has 40 items of wishlist data") {
                Assert.assertEquals(40, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect no recommendation widget is showing") {
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel) {
                        Assert.assertFalse("Recommendation widget should not existed", true)
                    }
                }
            }
        }

        Scenario("Get next page data increase page number when fetch data success") {

            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository successfully returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), nextPage) } returns WishlistEntityData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = true,
                        isSuccess = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }
            Given("Wishlist repository for page 1 returns empty data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(),
                        page = currentPage, hasNextPage = true)
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect page number not increased") {
                Assert.assertEquals(currentPage + 1, wishlistViewmodel.currentPage)
            }
        }

        Scenario("Get next page data doesn't increase page number when fetch data failed") {
            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository for page 2 throws error") {
                coEvery { wishlistRepository.getData(any(), nextPage) } returns WishlistEntityData(
                        items = listOf(),
                        hasNextPage = true,
                        isSuccess = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }
            Given("Wishlist repository for page 1 returns empty data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(),
                        page = currentPage, hasNextPage = true)
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect page number not increased") {
                Assert.assertEquals(currentPage, wishlistViewmodel.currentPage)
            }
        }

        Scenario("Get next page data doesn't increase page number when fetch data throws error") {

            val currentPage = 1
            val nextPage = 2

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository for page 2 throws error") {
                coEvery { wishlistRepository.getData(any(), nextPage) } answers {
                    throw Throwable("Error")
                }
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }
            Given("Wishlist repository for page 1 returns empty data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(),
                        page = currentPage, hasNextPage = true)
            }

            When("Viewmodel get next page wishlist data") {
                wishlistViewmodel.getNextPageWishlistData()
            }

            Then("Expect page number not increased") {
                Assert.assertEquals(currentPage, wishlistViewmodel.currentPage)
            }
        }
    }
})