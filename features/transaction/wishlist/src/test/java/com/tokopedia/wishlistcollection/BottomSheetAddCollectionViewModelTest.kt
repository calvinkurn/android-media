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
    private val listErrorMessage = arrayListOf<String>()

    @RelaxedMockK
    lateinit var getWishlistCollectionsBottomSheetUseCase: GetWishlistCollectionsBottomSheetUseCase

    @RelaxedMockK
    lateinit var addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase

    private var getWishlistCollectionBottomSheetResponseDataStatusOkAndErrorMessageIsEmpty = GetWishlistCollectionsBottomSheetResponse(
        getWishlistCollectionsBottomsheet = GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet(status = "OK", errorMessage = emptyList())
    )

    private var getWishlistCollectionBottomSheetResponseDataStatusOkAndErrorMessageIsNotEmpty = GetWishlistCollectionsBottomSheetResponse(
        getWishlistCollectionsBottomsheet = GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet(status = "OK", errorMessage = listErrorMessage)
    )

    private var getWishlistCollectionBottomSheetResponseDataStatusErrorAndErrorMessageIsEmpty = GetWishlistCollectionsBottomSheetResponse(
        getWishlistCollectionsBottomsheet = GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet(status = "ERROR", errorMessage = emptyList())
    )

    private var getWishlistCollectionBottomSheetResponseDataStatusErrorAndErrorMessageIsNotEmpty = GetWishlistCollectionsBottomSheetResponse(
        getWishlistCollectionsBottomsheet = GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet(status = "ERROR", errorMessage = listErrorMessage)
    )

    private var addWishlistCollectionItemsResponseDataStatusOkAndErrorMessageIsEmpty = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "OK", errorMessage = emptyList())
    )

    private var addWishlistCollectionItemsResponseDataStatusOkAndErrorMessageIsNotEmpty = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "OK", errorMessage = listErrorMessage)
    )

    private var addWishlistCollectionItemsResponseDataStatusErrorAndErrorMessageIsEmpty = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "ERROR", errorMessage = emptyList())
    )

    private var addWishlistCollectionItemsResponseDataStatusErrorAndErrorMessageIsNotEmpty = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "ERROR", errorMessage = listErrorMessage)
    )

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
        listErrorMessage.add("error")
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status OK And Error Message is Empty`() {
        // given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } returns getWishlistCollectionBottomSheetResponseDataStatusOkAndErrorMessageIsEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Success)
        assert((bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status OK And Error Message is Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } returns getWishlistCollectionBottomSheetResponseDataStatusOkAndErrorMessageIsNotEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status ERROR And Error Message is Empty`() {
        // given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } returns getWishlistCollectionBottomSheetResponseDataStatusErrorAndErrorMessageIsEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status ERROR And Error Message is Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } returns getWishlistCollectionBottomSheetResponseDataStatusErrorAndErrorMessageIsNotEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Failed`() {
        // given
        coEvery {
            getWishlistCollectionsBottomSheetUseCase(getWishlistCollectionBottomSheetParams)
        } throws throwable.throwable

        // when
        bottomSheetAddWishlistCollectionNameViewModel.getWishlistCollections(getWishlistCollectionBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.collectionsBottomSheet.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status OK and Error Message is Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusOkAndErrorMessageIsEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Success)
        assert((bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status OK and Error Message is not Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusOkAndErrorMessageIsNotEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status ERROR and Error Message is Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusErrorAndErrorMessageIsEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status ERROR and Error Message is not Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusErrorAndErrorMessageIsNotEmpty

        // when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Failed`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } throws throwable.throwable

        // when
        bottomSheetAddWishlistCollectionNameViewModel.saveToWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetAddWishlistCollectionNameViewModel.saveItemToCollections.value is Fail)
    }
}
