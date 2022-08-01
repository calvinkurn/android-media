package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionsBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionsBottomSheetUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetAddCollectionViewModel
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
class BottomSheetAddCollectionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var bottomSheetAddWishlistCollectionNameViewModel: BottomSheetAddCollectionViewModel

    @RelaxedMockK
    lateinit var getWishlistCollectionsBottomSheetUseCase: GetWishlistCollectionsBottomSheetUseCase

    @RelaxedMockK
    lateinit var addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase

    private var getWishlistCollectionBottomSheetResponseDataStatusOk = GetWishlistCollectionsBottomSheetResponse(
        getWishlistCollectionsBottomsheet = GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet(status = "OK"))

    private var getWishlistCollectionBottomSheetResponseDataStatusError = GetWishlistCollectionsBottomSheetResponse(
        getWishlistCollectionsBottomsheet = GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet(status = "ERROR"))

    private var addWishlistCollectionItemsResponseDataStatusOk = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "OK"))

    private var addWishlistCollectionItemsResponseDataStatusError = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "ERROR"))

    private val throwable = Fail(Throwable(message = "Error"))

    private var getWishlistCollectionBottomSheetParams = GetWishlistCollectionsBottomSheetParams()
    private var addWishlistCollectionsHostBottomSheetParams = AddWishlistCollectionsHostBottomSheetParams()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bottomSheetAddWishlistCollectionNameViewModel = spyk(
            BottomSheetAddCollectionViewModel(
                dispatcher,
                getWishlistCollectionsBottomSheetUseCase,
                addWishlistCollectionItemsUseCase
            )
        )
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status OK`() {
        //given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } returns getWishlistCollectionBottomSheetResponseDataStatusOk

        //when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        //then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Success)
        assert((bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status ERROR`() {
        //given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } returns getWishlistCollectionBottomSheetResponseDataStatusError

        //when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        //then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Failed`() {
        //given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } throws throwable.throwable

        //when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        //then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status OK`() {
        //given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusOk

        //when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        //then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Success)
        assert((bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status ERROR`() {
        //given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusError

        //when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        //then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Failed`() {
        //given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } throws throwable.throwable

        //when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        //then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Fail)
    }
}