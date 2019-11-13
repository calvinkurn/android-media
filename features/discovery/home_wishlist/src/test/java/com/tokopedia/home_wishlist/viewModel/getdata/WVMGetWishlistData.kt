package com.tokopedia.home_wishlist.viewModel.getdata

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.*
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class WVMGetWishlistData : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Get wishlist data") {
        lateinit var wishlistViewmodel: WishlistViewModel
        createWishlistTestInstance()
        val mockUserId = "12345"
        val userSessionInterface by memoized<UserSessionInterface>()
        val wishlistRepository by memoized<WishlistRepository>()

        Scenario("Get wishlist data success with empty initial wishlist data will add new wishlist data") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data below recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                ))
            }
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData has only 3 wishlist data") {
                Assert.assertEquals(3, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
        }

        Scenario("Get wishlist data failed with empty initial wishlist data will set wishlist data with error model") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns failed wishlist entity data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData has only 1 error viewmodel") {
                Assert.assertEquals(1, wishlistViewmodel.wishlistLiveData.value!!.size)
                Assert.assertEquals(ErrorWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![0].javaClass)
            }
        }

        Scenario("Get wishlist data and received empty wishlist will add emptyviewmodel and recommendation") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns empty wishlist item data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf())
            }
            Given("Repository getSingleRecommendation returns 4 recommendation data") {
                wishlistRepository.givenRepositoryGetSingleRecommendationReturnsThis(listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem()
                ))
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData has 6 data, 1 empty wishlist, 1 recom title model and 4 recommendation") {
                Assert.assertEquals(6, wishlistViewmodel.wishlistLiveData.value!!.size)
                Assert.assertEquals(EmptyWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![0].javaClass)
                Assert.assertEquals(RecommendationTitleDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![1].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![2].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![3].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![5].javaClass)
            }
        }

        Scenario("Get wishlist data with non-empty initial wishlist data and received empty wishlist will reset wishlistLiveData with empty model") {

            val keywordFirst = "aduh"
            val keywordSecond = "yoi"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository for first keyword returns wishlist data above recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
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
            }
            Given("Repository for second keyword returns empty wishlist data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(), keywordSecond)
            }
            Given("Repository returns 1 recommendation data") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = 33),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
            }
            Given("Repository getSingleRecommendation returns 4 recommendation data") {
                coEvery { wishlistRepository.getSingleRecommendationData(0) } returns
                        RecommendationWidget(
                                recommendationItemList = listOf(
                                        RecommendationItem(),
                                        RecommendationItem(),
                                        RecommendationItem(),
                                        RecommendationItem()
                                )
                        )
            }
            Given("Live data is filled by data from getWishlist keyword first") {
                wishlistViewmodel.getWishlistData(keywordFirst)
            }
            Given("Live data is filled by data from getWishlist keyword second") {
                wishlistViewmodel.getWishlistData(keywordSecond)
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns empty wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
                        items = listOf(),
                        hasNextPage = false
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData has 6 data, 1 empty wishlist, 1 recom title model and 4 recommendation") {
                Assert.assertEquals(6, wishlistViewmodel.wishlistLiveData.value!!.size)
                Assert.assertEquals(EmptyWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![0].javaClass)
                Assert.assertEquals(RecommendationTitleDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![1].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![2].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![3].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
                Assert.assertEquals(RecommendationItemDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![5].javaClass)
            }
        }

        Scenario("Get wishlist data success with non-empty wishlistLiveData will overrided by new data") {

            val keywordFirst = "aduh"
            val keywordSecond = "yoi"

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data below recommendation treshold (4)") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2")
                ), keywordFirst)
            }
            Given("Repository returns new wishlist data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7")
                ), keywordSecond)
            }
            Given("Repository returns 1 recommendation data") {
                wishlistRepository.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = 33),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
            }
            Given("Live data is filled by data from getWishlist keyword first") {
                wishlistViewmodel.getWishlistData(keywordFirst)
            }
            Given("Live data is filled by data from getWishlist keyword second") {
                wishlistViewmodel.getWishlistData(keywordSecond)
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }

            Then("Expect wishlistLiveData has 3 new wishlist data") {
                Assert.assertEquals(3, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
        }

        Scenario("Get wishlist data failed with non-empty wishlistLiveData will reset wishlist data and add error model") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns wishlist data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2"),
                        WishlistItem(id="3")
                ))
            }
            Given("Live data is filled by 3 data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository throws error") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData is reset and has error model") {
                Assert.assertEquals(1, wishlistViewmodel.wishlistLiveData.value!!.size)
                Assert.assertEquals(ErrorWishlistDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![0].javaClass)
            }
        }

        Scenario("Recommendation widget is positioned in position 4 in every page request when fetch recom success") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns empty wishlist data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 9 wishlist item data in a request") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
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
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf(
                        RecommendationWidget()
                )
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData has 10 items (9 wishlist data + 1 recommendation widget)") {
                Assert.assertEquals(10, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect every 4 product recommendation widget is showed") {
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
            }
        }

        Scenario("Recommendation widget is not showed when fetch recom return empty") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns empty wishlist data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository returns 9 wishlist item data in a request") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
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
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect wishlistLiveData has 9 items of wishlist data") {
                Assert.assertEquals(9, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect no recommendation widget is showing") {
                wishlistViewmodel.wishlistLiveData.value!!.forEach {
                    if (it is RecommendationCarouselDataModel) {
                        Assert.assertFalse("Recommendation widget should not existed", true)
                    }
                }
            }
        }

        Scenario("Get wishlist data increase page number when success") {

            val defaultGetWishlistPageNumber = 0
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Repository returns empty wishlist data") {
                wishlistRepository.givenRepositoryGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository successfully returns 3 wishlist item data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = true
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect page number become 3") {
                Assert.assertEquals(defaultGetWishlistPageNumber + 1, wishlistViewmodel.currentPage)
            }
        }

        Scenario("Get wishlist data doesn't increase page number when failed") {

            val defaultGetWishlistPageNumber = 0
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Wishlist repository failed to fetch data") {
                coEvery { wishlistRepository.getData(any(), any()) } returns WishlistEntityData(
                        isSuccess = false,
                        errorMessage = ""
                )
                coEvery { wishlistRepository.getRecommendationData(any(), any()) } returns listOf()
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect page number still in default page number") {
                Assert.assertEquals(defaultGetWishlistPageNumber, wishlistViewmodel.currentPage)
            }
        }
    }
})