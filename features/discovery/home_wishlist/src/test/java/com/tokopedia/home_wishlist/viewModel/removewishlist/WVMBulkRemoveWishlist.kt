package com.tokopedia.home_wishlist.viewModel.removewishlist

import com.tokopedia.home_wishlist.InstantTaskExecutorRuleSpek
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewModel.createWishlistTestInstance
import com.tokopedia.home_wishlist.viewModel.createWishlistViewModel
import com.tokopedia.home_wishlist.viewModel.givenGetWishlistDataReturnsThis
import com.tokopedia.home_wishlist.viewModel.givenRepositoryGetRecommendationDataReturnsThis
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistActionData
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import io.mockk.every
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber

class WVMBulkRemoveWishlist : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Bulk remove wishlist") {
        lateinit var wishlistViewmodel: WishlistViewModel
        val mockUserId = "12345"
        createWishlistTestInstance()
        val userSessionInterface by memoized<UserSessionInterface>()
        val bulkRemoveWishlistUseCase by memoized<BulkRemoveWishlistUseCase>()
        val getWishlistDataUseCase by memoized<GetWishlistDataUseCase>()
        val getRecommendationUseCase by memoized<GetRecommendationUseCase>()

        Scenario("Successfully bulk remove all selected wishlist") {
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)") {
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
                ))
            }
            Given("Get recommendation usecase returns recommendation data") {
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
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("Bulk remove usecase returns that all data successfully removed from wishlist") {
                every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                        .answers {
                            (secondArg() as Subscriber<WishlistActionData>).onNext(
                                    WishlistActionData(true, "0,1,2,3,4")
                            )
                        }
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }
            Given("Mark all product position to be removed") {
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 0, isChecked = true)
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 1, isChecked = true)
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 2, isChecked = true)
                wishlistViewmodel.setWishlistOnMarkDelete(productPosition = 3, isChecked = true)
            }

            When("Bulk remove is called") {
                wishlistViewmodel.bulkRemoveWishlist(
                        listOf(0,1,2,3)
                )
            }

            Then("Expect that 4 wishlist data is removed, so the rest is 6 data") {
                Assert.assertEquals(6, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect all item is not in bulk mode") {
                wishlistViewmodel.wishlistLiveData.value?.forEach {
                    if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
                    } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
                    }
                }
            }
            Then("Expect all item is not marked") {
                wishlistViewmodel.wishlistLiveData.value?.forEach {
                    if (it is WishlistItemDataModel && it.isOnChecked) {
                        Assert.assertFalse("Wishlist item data model still on marked state!", true)
                    }
                }
            }
            Then("Expect that wishlist action for bulk remove is success and not partially failed") {
                val bulkRemoveWishlistActionData = wishlistViewmodel.bulkRemoveWishlistActionData.value
                Assert.assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
                Assert.assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isPartiallyFailed)
            }
            Then("Expect that recommendation section position is not changed in position 4") {
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
            }
        }

        Scenario("Failed to remove some item of selected wishlist") {
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)") {
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
                ))
            }
            Given("Get recommendation usecase returns recommendation data") {
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
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("Bulk remove usecase returns that have some failed data") {
                every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                        .answers {
                            (secondArg() as Subscriber<WishlistActionData>).onNext(
                                    WishlistActionData(true, "0,1,2,3")
                            )
                        }
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }

            When("Bulk remove is called to remove wishlist position 1,2,3 and 4") {
                wishlistViewmodel.bulkRemoveWishlist(
                        listOf(0,1,2,3)
                )
            }

            Then("Expect that 3 wishlist data is removed (from 10 wishlist item data), so the rest is 6 data") {
                Assert.assertEquals(7, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect all item is not in bulk mode") {
                wishlistViewmodel.wishlistLiveData.value?.forEach {
                    if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
                    } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
                    }
                }
            }
            Then("Expect all item is not marked") {
                wishlistViewmodel.wishlistLiveData.value?.forEach {
                    if (it is WishlistItemDataModel && it.isOnChecked) {
                        Assert.assertFalse("Wishlist item data model still on marked state!", true)
                    }
                }
            }
            Then("Expect that wishlist action for bulk remove is success but partially failed") {
                val bulkRemoveWishlistActionData = wishlistViewmodel.bulkRemoveWishlistActionData.value
                Assert.assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
                Assert.assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isPartiallyFailed)
            }
            Then("Expect that recommendation section position is not changed in position 4") {
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
            }
        }

        Scenario("Bulk remove wishlist throws error") {
            val mockErrorMessage = "NOT OKAY"
            Given("Wishlist viewmodel") {
                wishlistViewmodel = createWishlistViewModel()
            }
            Given("Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)") {
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
                ))
            }
            Given("Get recommendation usecase returns recommendation data") {
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
            Given("Live data is filled by data from getWishlist") {
                wishlistViewmodel.getWishlistData()
            }
            Given("Bulk remove usecase returns that have some failed data") {
                every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                        .answers {
                            (secondArg() as Subscriber<List<WishlistActionData>>).onError(
                                    Throwable(mockErrorMessage)
                            )
                        }
            }
            Given("User id") {
                every { userSessionInterface.userId } returns mockUserId
            }

            When("Bulk remove is called to remove wishlist position 1,2,3 and 4") {
                wishlistViewmodel.bulkRemoveWishlist(
                        listOf(0,1,2,3)
                )
            }

            Then("Expect that wishlist data is still same as initial value") {
                Assert.assertEquals(10, wishlistViewmodel.wishlistLiveData.value!!.size)
            }
            Then("Expect all item is not in bulk mode") {
                wishlistViewmodel.wishlistLiveData.value?.forEach {
                    if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
                    } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                        Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
                    }
                }
            }
            Then("Expect all item is not marked") {
                wishlistViewmodel.wishlistLiveData.value?.forEach {
                    if (it is WishlistItemDataModel && it.isOnChecked) {
                        Assert.assertFalse("Wishlist item data model still on marked state!", true)
                    }
                }
            }
            Then("Expect that wishlist action for bulk remove is failed and not partially failed") {
                val bulkRemoveWishlistActionData = wishlistViewmodel.bulkRemoveWishlistActionData.value
                Assert.assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
                Assert.assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isPartiallyFailed)
                Assert.assertEquals(mockErrorMessage, bulkRemoveWishlistActionData!!.peekContent().message)
            }
            Then("Expect that recommendation section position is not changed in position 4") {
                Assert.assertEquals(RecommendationCarouselDataModel::class.java,
                        wishlistViewmodel.wishlistLiveData.value!![4].javaClass)
            }
        }
    }
})