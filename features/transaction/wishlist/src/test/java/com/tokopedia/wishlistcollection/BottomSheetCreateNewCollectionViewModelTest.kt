package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionsBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.domain.AddWishlistCollectionItemsUseCase
import com.tokopedia.wishlistcollection.domain.CreateWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetCreateNewCollectionViewModel
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
class BottomSheetCreateNewCollectionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var bottomSheetCreateNewCollectionViewModel: BottomSheetCreateNewCollectionViewModel

    @RelaxedMockK
    lateinit var getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase

    @RelaxedMockK
    lateinit var addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase

    @RelaxedMockK
    lateinit var createWishlistCollectionUseCase: CreateWishlistCollectionUseCase

    private var getWishlistCollectionNamesResponseDataStatusOk = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK"))

    private var getWishlistCollectionNamesResponseDataStatusError = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR"))

    private var addWishlistCollectionItemsResponseDataStatusOk = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "OK"))

    private var addWishlistCollectionItemsResponseDataStatusError = AddWishlistCollectionItemsResponse(
        addWishlistCollectionItems = AddWishlistCollectionItemsResponse.AddWishlistCollectionItems(status = "ERROR"))

    private var createWishlistCollectionResponseDataStatusOk = CreateWishlistCollectionResponse(
        createWishlistCollection = CreateWishlistCollectionResponse.CreateWishlistCollection(status = "OK"))

    private var createWishlistCollectionResponseDataStatusError = CreateWishlistCollectionResponse(
        createWishlistCollection = CreateWishlistCollectionResponse.CreateWishlistCollection(status = "ERROR"))

    private val throwable = Fail(Throwable(message = "Error"))

    private val paramNewCollectionName = "test"

    private var getWishlistCollectionBottomSheetParams = GetWishlistCollectionsBottomSheetParams()
    private var addWishlistCollectionsHostBottomSheetParams = AddWishlistCollectionsHostBottomSheetParams()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bottomSheetCreateNewCollectionViewModel = spyk(
            BottomSheetCreateNewCollectionViewModel(
                dispatcher,
                getWishlistCollectionNamesUseCase,
                addWishlistCollectionItemsUseCase,
                createWishlistCollectionUseCase
            )
        )
    }

    @Test
    fun `Execute GetWishlistCollectionsNames Success Status OK`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getWishlistCollectionNamesResponseDataStatusOk

        //when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Success)
        assert((bottomSheetCreateNewCollectionViewModel.collectionNames.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status ERROR`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getWishlistCollectionNamesResponseDataStatusError

        //when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Failed`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } throws throwable.throwable

        //when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status OK`() {
        //given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusOk

        //when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        //then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Success)
        assert((bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status ERROR`() {
        //given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusError

        //when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        //then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Failed`() {
        //given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } throws throwable.throwable

        //when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        //then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute CreateWishlistCollection Success Status OK`() {
        //given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } returns createWishlistCollectionResponseDataStatusOk

        //when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        //then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Success)
        assert((bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute CreateWishlistCollection Success Status ERROR`() {
        //given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } returns createWishlistCollectionResponseDataStatusError

        //when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        //then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute CreateWishlistCollection Failed`() {
        //given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } throws throwable.throwable

        //when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        //then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Fail)
    }
}