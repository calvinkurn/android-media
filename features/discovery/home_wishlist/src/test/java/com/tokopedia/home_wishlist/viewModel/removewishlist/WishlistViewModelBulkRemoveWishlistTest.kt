package com.tokopedia.home_wishlist.viewModel.removewishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.datamodel.WishlistActionData
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

/**
 * Created by Lukas on 25/07/20.
 */

class WishlistViewModelBulkRemoveWishlistTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var wishlistViewModel: WishlistViewModel
    private val mockUserId = "12345"
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val bulkRemoveWishlistUseCase = mockk<BulkRemoveWishlistUseCase>(relaxed = true)
    private val getWishlistDataUseCase = mockk<GetWishlistDataUseCase>(relaxed = true)
    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>(relaxed = true)

    @Test
    fun `Remove empty list with default parameter`(){
        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                getRecommendationUseCase = getRecommendationUseCase,
                getWishlistDataUseCase = getWishlistDataUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                userSessionInterface = userSessionInterface,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)
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

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // Bulk remove usecase returns that all data successfully removed from wishlist
        every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                .answers {
                    (secondArg() as Subscriber<WishlistActionData>).onNext(
                            WishlistActionData(true, "0,1,2,3,4")
                    )
                }

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Mark all product position to be removed
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 0, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 1, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 2, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 3, isChecked = true)

        wishlistViewModel.enterBulkMode()

        // Bulk remove is called
        wishlistViewModel.bulkRemoveWishlist()


        // Expect that 0 wishlist data is removed, so the rest is 10 data
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect all item is not in bulk mode
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            }
        }

        // Expect all item is not marked
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnChecked) {
                Assert.assertFalse("Wishlist item data model still on marked state!", true)
            }
        }
        // Expect that wishlist action for bulk remove is success and not partially failed
        val bulkRemoveWishlistActionData = wishlistViewModel.bulkRemoveWishlistActionData.value
        Assert.assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
        Assert.assertEquals(false, bulkRemoveWishlistActionData.peekContent().isPartiallyFailed)

        // Expect that recommendation section position is not changed in position 4
        Assert.assertEquals(BannerTopAdsDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)
        wishlistViewModel.bulkRemoveWishlist(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 22, 23, 44, 74))
    }


    @Test
    fun `Remove wishlist with recom carousel inside`(){
        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                getRecommendationUseCase = getRecommendationUseCase,
                getWishlistDataUseCase = getWishlistDataUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                userSessionInterface = userSessionInterface,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)
        coEvery { getWishlistDataUseCase.getData(any()) } returns WishlistEntityData(items = listOf(
                WishlistItem(id="1"), WishlistItem(id="2"), WishlistItem(id="3"), WishlistItem(id="4"), WishlistItem(id="5"),
                WishlistItem(id="6"), WishlistItem(id="7"), WishlistItem(id="8"), WishlistItem(id="9"), WishlistItem(id="10"),
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20")
        )) andThen WishlistEntityData(items = listOf(
                WishlistItem(id="11"), WishlistItem(id="12"), WishlistItem(id="13"), WishlistItem(id="14"), WishlistItem(id="15"),
                WishlistItem(id="16"), WishlistItem(id="17"), WishlistItem(id="18"), WishlistItem(id="19"), WishlistItem(id="20"),
                WishlistItem(id="21"), WishlistItem(id="22"), WishlistItem(id="23"), WishlistItem(id="24"), WishlistItem(id="25"),
                WishlistItem(id="26"), WishlistItem(id="27"), WishlistItem(id="28"), WishlistItem(id="29"), WishlistItem(id="30")
        ))

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()
        wishlistViewModel.getNextPageWishlistData()

        // Bulk remove usecase returns that all data successfully removed from wishlist
        every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                .answers {
                    (secondArg() as Subscriber<WishlistActionData>).onNext(
                            WishlistActionData(true, "0,1,2,3,4")
                    )
                }

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Mark all product position to be removed
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 0, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 1, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 2, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 3, isChecked = true)

        wishlistViewModel.enterBulkMode()

        // Bulk remove is called
        wishlistViewModel.bulkRemoveWishlist()


        // Expect that 0 wishlist data is removed, so the rest is 10 data
        // Expect all item is not in bulk mode
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            }
        }

        // Expect all item is not marked
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnChecked) {
                Assert.assertFalse("Wishlist item data model still on marked state!", true)
            }
        }
    }

    @Test
    fun `Successfully bulk remove all selected wishlist`() {
        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                getRecommendationUseCase = getRecommendationUseCase,
                getWishlistDataUseCase = getWishlistDataUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                userSessionInterface = userSessionInterface,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns 9 wishlist data above recommendation treshold (4)
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

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // Bulk remove usecase returns that all data successfully removed from wishlist
        every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                .answers {
                    (secondArg() as Subscriber<WishlistActionData>).onNext(
                            WishlistActionData(true, "0,1,2,3,4")
                    )
                }

        // User id
        every { userSessionInterface.userId } returns mockUserId
        // Mark all product position to be removed
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 0, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 1, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 2, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 3, isChecked = true)
        wishlistViewModel.setWishlistOnMarkDelete(productPosition = 10, isChecked = true)

        wishlistViewModel.enterBulkMode()

        // Bulk remove is called
        wishlistViewModel.bulkRemoveWishlist(
                listOf(0,1,2,3)
        )


        // Expect that 4 wishlist data is removed, so the rest is 6 data
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect all item is not in bulk mode
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            }
        }

        // Expect all item is not marked
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnChecked) {
                Assert.assertFalse("Wishlist item data model still on marked state!", true)
            }
        }
        // Expect that wishlist action for bulk remove is success and not partially failed
        val bulkRemoveWishlistActionData = wishlistViewModel.bulkRemoveWishlistActionData.value
        Assert.assertEquals(true, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
        Assert.assertEquals(false, bulkRemoveWishlistActionData.peekContent().isPartiallyFailed)

        // Expect that recommendation section position is not changed in position 4
        Assert.assertEquals(BannerTopAdsDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)

    }

    @Test
    fun `Failed to remove some item of selected wishlist`() {
        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                getWishlistDataUseCase = getWishlistDataUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                userSessionInterface = userSessionInterface,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns 9 wishlist data above recommendation threshold (4)
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

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()

        // Bulk remove usecase returns that have some failed data
        every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                .answers {
                    (secondArg() as Subscriber<WishlistActionData>).onNext(
                            WishlistActionData(true, "0,1,2,3")
                    )
                }

        // User id
        every { userSessionInterface.userId } returns mockUserId

        wishlistViewModel.enterBulkMode()

        // Bulk remove is called to remove wishlist position 1,2,3 and 4
        wishlistViewModel.bulkRemoveWishlist(
                listOf(0,1,2,3)
        )


        // Expect that 3 wishlist data is removed (from 10 wishlist item data), so the rest is 6 data
        Assert.assertEquals(6, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect all item is not in bulk mode
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            }
        }

        // Expect all item is not marked
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnChecked) {
                Assert.assertFalse("Wishlist item data model still on marked state!", true)
            }
        }

        // Expect that wishlist action for bulk remove is success but partially failed
        val bulkRemoveWishlistActionData = wishlistViewModel.bulkRemoveWishlistActionData.value
        Assert.assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
        Assert.assertEquals(true, bulkRemoveWishlistActionData.peekContent().isPartiallyFailed)

        Assert.assertEquals(WishlistItemDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)

    }

    @Test
    fun `Bulk remove wishlist throws error`() {
        val mockErrorMessage = "NOT OKAY"
        // Wishlist viewmodel
        wishlistViewModel = createWishlistViewModel(
                getRecommendationUseCase = getRecommendationUseCase,
                getWishlistDataUseCase = getWishlistDataUseCase,
                bulkRemoveWishlistUseCase = bulkRemoveWishlistUseCase,
                userSessionInterface = userSessionInterface,
                topAdsImageViewUseCase = topAdsImageViewUseCase
        )

        topAdsImageViewUseCase.givenGetImageData(arrayListOf(TopAdsImageViewModel(imageUrl = "")))

        // Get wishlist usecase returns 9 wishlist data above recommendation threshold (4)
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

        // Get recommendation usecase returns recommendation data
        getRecommendationUseCase.givenRepositoryGetRecommendationDataReturnsThis(
                listOf(
                        RecommendationItem(productId = 11),
                        RecommendationItem(productId = 22),
                        RecommendationItem(productId = 33),
                        RecommendationItem(productId = 44),
                        RecommendationItem(productId = 55)
                )
        )

        wishlistViewModel.enterBulkMode()

        // Live data is filled by data from getWishlist
        wishlistViewModel.getWishlistData()
        // Bulk remove usecase returns that have some failed data
        every { bulkRemoveWishlistUseCase.execute(any(), any()) }
                .answers {
                    (secondArg() as Subscriber<List<WishlistActionData>>).onError(
                            Throwable(mockErrorMessage)
                    )
                }

        // User id
        every { userSessionInterface.userId } returns mockUserId

        wishlistViewModel.enterBulkMode()

        // Bulk remove is called to remove wishlist position 1,2,3 and 4
        wishlistViewModel.bulkRemoveWishlist(
                listOf(0,1,2,3)
        )


        // Expect that wishlist data is still same as initial value
        Assert.assertEquals(11, wishlistViewModel.wishlistLiveData.value!!.size)
        // Expect all item is not in bulk mode
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", true)
            } else if (it is RecommendationCarouselDataModel && it.isOnBulkRemoveProgress) {
                Assert.assertFalse("Wishlist item data model still on bulk remove progress state!", false)
            }
        }

        // Expect all item is not marked
        wishlistViewModel.wishlistLiveData.value?.forEach {
            if (it is WishlistItemDataModel && it.isOnChecked) {
                Assert.assertFalse("Wishlist item data model still on marked state!", true)
            }
        }

        // Expect that wishlist action for bulk remove is failed and not partially failed
        val bulkRemoveWishlistActionData = wishlistViewModel.bulkRemoveWishlistActionData.value
        Assert.assertEquals(false, bulkRemoveWishlistActionData!!.peekContent().isSuccess)
        Assert.assertEquals(false, bulkRemoveWishlistActionData.peekContent().isPartiallyFailed)
        Assert.assertEquals(mockErrorMessage, bulkRemoveWishlistActionData.peekContent().message)

        // Expect that recommendation section position is not changed in position 4
        Assert.assertEquals(BannerTopAdsDataModel::class.java,
                wishlistViewModel.wishlistLiveData.value!![4].javaClass)

    }
}