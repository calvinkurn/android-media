package com.example.home.play


import com.example.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomePresenterTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get play data") {
        lateinit var homePresenter: HomePresenter
        val mockUserId = "12345"

        Scenario("Get wishlist data success with empty initial wishlist data will add new wishlist data") {

            Given("Wishlist viewmodel") {
                val component = DaggerHomeTestComponent.builder()
                homePresenter = createPresenter()
            }
//            Given("Get wishlist data returns wishlist data below recommendation treshold (4)") {
//                getWishlistDataUseCase.givenGetWishlistDataReturnsThis(listOf(
//                        WishlistItem(id = "1"),
//                        WishlistItem(id = "2"),
//                        WishlistItem(id = "3")
//                ))
//            }
//            Given("Live data is filled by data from getWishlist") {
//                homePresenter.getWishlistData()
//            }
//            Given("User id") {
//                every { userSessionInterface.userId } returns mockUserId
//            }
//            Given("Get wishlist data returns 3 wishlist item data") {
//                coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(
//                        items = listOf(
//                                WishlistItem(id = "1"),
//                                WishlistItem(id = "2"),
//                                WishlistItem(id = "3")
//                        ),
//                        hasNextPage = false
//                )
//            }
//
//            When("Viewmodel get wishlist data") {
//                homePresenter.getWishlistData()
//            }
//
//            Then("Expect wishlistLiveData has only 3 wishlist data") {
//                Assert.assertEquals(3, homePresenter.wishlistLiveData.value!!.size)
//            }
        }
    }
})