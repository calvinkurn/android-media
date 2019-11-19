package com.tokopedia.home_wishlist.viewModel.getdata

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.*
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
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
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getSingleRecommendationUseCase by memoized<GetSingleRecommendationUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()

        Scenario("Get wishlist data success with empty initial wishlist data will add new wishlist data") {

            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist data returns wishlist data below recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
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
            Given("Get wishlist data returns 3 wishlist item data") {
                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = false
                )
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
                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect isWishlistErrorInFirstPageState is true") {
                Assert.assertEquals(true, wishlistViewmodel.isWishlistErrorInFirstPageState.value)
            }
            Then("Expect isWishlistEmptyState is false") {
                Assert.assertEquals(false, wishlistViewmodel.isWishlistEmptyState.value)
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
            Given("Get wishlist data returns empty wishlist item data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Get recommendation getSingleRecommendation returns 4 recommendation data") {
                getSingleRecommendationUseCase.givenGetSingleRecommendationReturnsThis(listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem()
                ))
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect isWishlistErrorInFirstPageState is true") {
                Assert.assertEquals(false, wishlistViewmodel.isWishlistErrorInFirstPageState.value)
            }
            Then("Expect isWishlistEmptyState is false") {
                Assert.assertEquals(true, wishlistViewmodel.isWishlistEmptyState.value)
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
            Given("Get wishlist data for first keyword returns wishlist data above recommendation treshold (4)") {
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
            }
            Given("Get wishlist use case for second keyword returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(), keywordSecond)
            }
            Given("Get recommendation use case returns 1 recommendation data") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                        listOf(
                                RecommendationItem(productId = 11),
                                RecommendationItem(productId = 22),
                                RecommendationItem(productId = 33),
                                RecommendationItem(productId = 44),
                                RecommendationItem(productId = 55)
                        )
                )
            }
            Given("Get single recommendation usecase getSingleRecommendation returns 4 recommendation data") {
                coEvery { getSingleRecommendationUseCase.getData(any()) } returns
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
            Given("Get wishlist usecase returns empty wishlist item data") {
                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                        items = listOf(),
                        hasNextPage = false
                )
            }

            When("Viewmodel get wishlist data") {
                wishlistViewmodel.getWishlistData()
            }

            Then("Expect isWishlistEmptyState is false") {
                Assert.assertEquals(true, wishlistViewmodel.isWishlistEmptyState.value)
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
            Given("Get wishlist usecase returns wishlist data below recommendation treshold (4)") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="1"),
                        WishlistItem(id="2")
                ), keywordFirst)
            }
            Given("Get wishlist usecase returns new wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
                        WishlistItem(id="5"),
                        WishlistItem(id="6"),
                        WishlistItem(id="7")
                ), keywordSecond)
            }
            Given("get recommendation usecase returns 1 recommendation data") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
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
            Given("Get wishlist usecase returns wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
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
            Given("Get wishlist usecase throws error") {
                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(isSuccess = false, errorMessage = "")
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
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Get wishlist usecase returns 9 wishlist item data in a request") {
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
            }
            Given(" Get recommendation usecase return data") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf(
                        RecommendationItem()
                ))
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
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Get wishlist usecase returns 9 wishlist item data in a request") {
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
            }
            Given("Get recommendation usecase returns empty data") {
                coEvery { getRecommendationUseCase.getData(any()) } returns listOf()
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
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Get wishlist usecase successfully returns 3 wishlist item data") {
                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                        items = listOf(
                                WishlistItem(id="1"),
                                WishlistItem(id="2"),
                                WishlistItem(id="3")
                        ),
                        hasNextPage = true
                )
            }
            Given("Get recommendation usecase successfully returns data") {
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
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
            Given("Get wishlist usecase returns empty wishlist data") {
                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf())
            }
            Given("Live data is filled by empty data from getWishlistData") {
                wishlistViewmodel.getWishlistData()
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Get wishlist usecase failed to fetch data") {
                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
                        isSuccess = false,
                        errorMessage = ""
                )
            }
            Given("Get recommendation usecase successfuly get data"){
                getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(listOf())
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