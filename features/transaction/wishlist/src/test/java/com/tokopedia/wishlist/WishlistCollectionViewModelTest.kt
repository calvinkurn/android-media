package com.tokopedia.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.response.WishlistCollectionResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistCollectionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistCollectionViewModel: WishlistCollectionViewModel

    @RelaxedMockK
    lateinit var getWishlistCollectionUseCase: GetWishlistCollectionUseCase

    @RelaxedMockK
    lateinit var deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase

    private var collectionWishlistResponseData = WishlistCollectionResponse(
        getWishlistCollections = WishlistCollectionResponse.GetWishlistCollections(status = "OK"))
    private val throwable = Fail(Throwable(message = "Error"))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        wishlistCollectionViewModel = spyk(
            WishlistCollectionViewModel(
                dispatcher,
                getWishlistCollectionUseCase,
                deleteWishlistCollectionUseCase
            )
        )
    }

    // wishlist_collection_success
    @Test
    fun `Execute GetWishlistCollections Success`() {
        //given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseData

        //when
        wishlistCollectionViewModel.getWishlistCollections()

        //then
        assert(wishlistCollectionViewModel.collections.value is Success)
    }

    // wishlist_collection_failed
    @Test
    fun getWishlistCollection_shouldReturnFail() {
        //given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } throws throwable.throwable

        //when
        wishlistCollectionViewModel.getWishlistCollections()

        //then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }
}