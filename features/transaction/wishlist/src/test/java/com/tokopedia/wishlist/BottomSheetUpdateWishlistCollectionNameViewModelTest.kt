package com.tokopedia.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionNameParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionNameResponse
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import com.tokopedia.wishlistcollection.domain.UpdateWishlistCollectionNameUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetUpdateWishlistCollectionNameViewModel
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
class BottomSheetUpdateWishlistCollectionNameViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var bottomSheetUpdateWishlistCollectionNameViewModel: BottomSheetUpdateWishlistCollectionNameViewModel

    @RelaxedMockK
    lateinit var getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase

    @RelaxedMockK
    lateinit var updateWishlistCollectionNameUseCase: UpdateWishlistCollectionNameUseCase

    private var collectionNamesResponseData = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK"))

    private var updateCollectionNameResponseData = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "OK"))

    private val throwable = Fail(Throwable(message = "Error"))

    private val newCollectionNameParam = UpdateWishlistCollectionNameParams(collectionName = "newCollectionName")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bottomSheetUpdateWishlistCollectionNameViewModel = spyk(
            BottomSheetUpdateWishlistCollectionNameViewModel(
                dispatcher,
                getWishlistCollectionNamesUseCase,
                updateWishlistCollectionNameUseCase
            )
        )
    }

    // get wishlist collection names success
    @Test
    fun `Execute GetWishlistCollectionNames Success`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns collectionNamesResponseData

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value is Success)
        assert((bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value as Success).data.status.equals("OK"))
    }

    // get wishlist collection names failed
    @Test
    fun `Execute GetWishlistCollectionNames Failed`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } throws throwable.throwable

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value is Fail)
    }

    // update wishlist collection name success
    @Test
    fun `Execute UpdateWishlistCollectionName Success`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } returns updateCollectionNameResponseData

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Success)
        assert((bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value as Success).data.status.equals("OK"))
    }

    // get wishlist collection names failed
    @Test
    fun `Execute UpdateWishlistCollectionName Failed`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } throws throwable.throwable

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Fail)
    }
}