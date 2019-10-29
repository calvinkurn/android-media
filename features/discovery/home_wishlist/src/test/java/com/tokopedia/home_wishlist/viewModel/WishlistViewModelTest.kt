package com.tokopedia.home_wishlist.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.datamodel.LoadingDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.home_wishlist.TestDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistViewModelTest {
    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var wishlistRepository: WishlistRepository

    @MockK
    lateinit var removeWishlistUseCase: RemoveWishListUseCase

    @MockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WishlistViewModel

    private val dispatcherProvider = TestDispatcherProvider()

    @Before
    fun setup(){
        MockKAnnotations.init(this)
//        viewModel = WishlistViewModel(
//                userSessionInterface,
//                wishlistRepository,
//                dispatcherProvider,
//                removeWishlistUseCase,
//                addToCartUseCase,
//                bulkRemoveWishlistUseCase)
    }

    @Test
    fun givenProductToBeUnWishlisted_whenRemoveWishlistSuccess_expectProductRemovedFromWishlistData() {
        val mockProductId = "3"
        val mockUserId = "54321"

        // given list of wishlistData
        viewModel.wishlistData.value = listOf(
                WishlistItemDataModel(WishlistItem(id="1")),
                WishlistItemDataModel(WishlistItem(id="2")),
                WishlistItemDataModel(WishlistItem(id="3")),
                WishlistItemDataModel(WishlistItem(id="4"))
        )

        // given userid and callback
        every { userSessionInterface.userId } returns mockUserId

        // given remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(mockProductId, mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onSuccessRemoveWishlist(firstArg())
                }

        //when viewmodel remove wishlist product
        viewModel.removeWishlistedProduct(mockProductId)

        //verify that unwishlisted product is removed from wishlistData
        assertEquals(viewModel.wishlistData.value!!.size, 3)
        viewModel.wishlistData.value!!.forEach {
            if (it is WishlistItemDataModel) {
                if (it.productItem.id == mockProductId) {
                    assertFalse("Unwishlisted product still exist!" ,true)
                }
            }
        }
    }

    @Test
    fun givenProductToBeUnWishlisted_whenRemoveWishlistFailed_expectProductStillExistInWishlistData() {
        val mockProductId = "3"
        val mockUserId = "54321"

        // given list of wishlistData
        viewModel.wishlistData.value = listOf(
                WishlistItemDataModel(WishlistItem(id="1")),
                WishlistItemDataModel(WishlistItem(id="2")),
                WishlistItemDataModel(WishlistItem(id="3")),
                WishlistItemDataModel(WishlistItem(id="4"))
        )

        // given userid and callback
        every { userSessionInterface.userId } returns mockUserId

        // given remove wishlist usecase successfully remove wishlist
        every { removeWishlistUseCase.createObservable(mockProductId, mockUserId, any()) }
                .answers {
                    (thirdArg() as WishListActionListener).onErrorRemoveWishlist("Error", firstArg())
                }

        //when viewmodel remove wishlist product
        viewModel.removeWishlistedProduct(mockProductId)

        //verify that unwishlisted product is removed from wishlistData
        assertEquals(viewModel.wishlistData.value!!.size, 4)
        viewModel.wishlistData.value!!.forEach {
            if (it is WishlistItemDataModel) {
                if (it.productItem.id == mockProductId) {
                    assertFalse("Unwishlisted product still in exist" , false)
                }
            }
        }
    }

    @Test
    fun givenEmptyWishlistData_whenOLoadInitialData_expectWishlistDataMustContainsLoadingModel() {
        //when viewmodel remove wishlist product
        viewModel.loadInitialPage()

        //verify that unwishlisted product is removed from wishlistData
        assertEquals(viewModel.wishlistData.value!!.size, 1)
        assertEquals(LoadingDataModel::class.java, viewModel.wishlistData.value!![0].javaClass)
    }

    @Test
    fun givenEmptyWishlistData_whenOnGetWishlistDataSuccess_expectWishlistDataUpdated() {
        //given wishlist repository return some data
        coEvery() { wishlistRepository.getData(any(), any()) }.returns(
                listOf(
                        WishlistItem(id = "1"),
                        WishlistItem(id = "2"),
                        WishlistItem(id = "2")
                )
        )

        //given recommendation repository return no data
        coEvery { wishlistRepository.getRecommendationData(any(), any(), any()) }.returns(listOf())

        //when viewmodel remove wishlist product
        viewModel.getWishlistData()

        //verify wishlistData is updated with new wishlist item
        assertEquals(3, viewModel.wishlistData.value!!.size)
    }


}