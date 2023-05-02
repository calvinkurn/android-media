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
    private val listErrorMessage = arrayListOf<String>()

    @RelaxedMockK
    lateinit var getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase

    @RelaxedMockK
    lateinit var addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase

    @RelaxedMockK
    lateinit var createWishlistCollectionUseCase: CreateWishlistCollectionUseCase

    private var getWishlistCollectionNamesResponseDataStatusOkAndErrorMessageIsEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = emptyList())
    )

    private var getWishlistCollectionNamesResponseDataStatusOkAndErrorMessageIsNotEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = listErrorMessage)
    )

    private var getWishlistCollectionNamesResponseDataStatusErrorAndErrorMessageIsEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = emptyList())
    )

    private var getWishlistCollectionNamesResponseDataStatusErrorAndErrorMessageIsNotEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = listErrorMessage)
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

    private var createWishlistCollectionResponseDataStatusOkAndErrorMessageIsEmpty = CreateWishlistCollectionResponse(
        createWishlistCollection = CreateWishlistCollectionResponse.CreateWishlistCollection(status = "OK", errorMessage = emptyList())
    )

    private var createWishlistCollectionResponseDataStatusOkAndErrorMessageIsNotEmpty = CreateWishlistCollectionResponse(
        createWishlistCollection = CreateWishlistCollectionResponse.CreateWishlistCollection(status = "OK", errorMessage = listErrorMessage)
    )

    private var createWishlistCollectionResponseDataStatusErrorAndErrorMessageIsEmpty = CreateWishlistCollectionResponse(
        createWishlistCollection = CreateWishlistCollectionResponse.CreateWishlistCollection(status = "ERROR", errorMessage = emptyList())
    )

    private var createWishlistCollectionResponseDataStatusErrorAndErrorMessageIsNotEmpty = CreateWishlistCollectionResponse(
        createWishlistCollection = CreateWishlistCollectionResponse.CreateWishlistCollection(status = "ERROR", errorMessage = listErrorMessage)
    )

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
        listErrorMessage.add("error")
    }

    @Test
    fun `Execute GetWishlistCollectionsNames Success Status OK And Error Message is Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getWishlistCollectionNamesResponseDataStatusOkAndErrorMessageIsEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        // then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Success)
        assert((bottomSheetCreateNewCollectionViewModel.collectionNames.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute GetWishlistCollectionsNames Success Status OK And Error Message is Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getWishlistCollectionNamesResponseDataStatusOkAndErrorMessageIsNotEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        // then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status ERROR and Error Message is Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getWishlistCollectionNamesResponseDataStatusErrorAndErrorMessageIsEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        // then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Success Status ERROR and Error Message is not Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getWishlistCollectionNamesResponseDataStatusErrorAndErrorMessageIsNotEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        // then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionsBottomSheet Failed`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } throws throwable.throwable

        // when
        bottomSheetCreateNewCollectionViewModel.getWishlistCollectionNames()

        // then
        assert(bottomSheetCreateNewCollectionViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status OK And Error Message is Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusOkAndErrorMessageIsEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Success)
        assert((bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status OK And Error Message is Not Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusOkAndErrorMessageIsNotEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status ERROR And Error Message is Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusErrorAndErrorMessageIsEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Success Status ERROR And Error Message is not Empty`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } returns addWishlistCollectionItemsResponseDataStatusErrorAndErrorMessageIsNotEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute SaveToWishlistCollection Failed`() {
        // given
        coEvery {
            addWishlistCollectionItemsUseCase(addWishlistCollectionsHostBottomSheetParams)
        } throws throwable.throwable

        // when
        bottomSheetCreateNewCollectionViewModel.saveNewWishlistCollection(addWishlistCollectionsHostBottomSheetParams)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.addWishlistCollectionItem.value is Fail)
    }

    @Test
    fun `Execute CreateWishlistCollection Success Status OK And Error Message is Empty`() {
        // given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } returns createWishlistCollectionResponseDataStatusOkAndErrorMessageIsEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Success)
        assert((bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute CreateWishlistCollection Success Status OK And Error Message is not Empty`() {
        // given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } returns createWishlistCollectionResponseDataStatusOkAndErrorMessageIsNotEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute CreateWishlistCollection Success Status ERROR And Error Message is Empty`() {
        // given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } returns createWishlistCollectionResponseDataStatusErrorAndErrorMessageIsEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute CreateWishlistCollection Success Status ERROR And Error Message is Not Empty`() {
        // given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } returns createWishlistCollectionResponseDataStatusErrorAndErrorMessageIsNotEmpty

        // when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute CreateWishlistCollection Failed`() {
        // given
        coEvery {
            createWishlistCollectionUseCase(paramNewCollectionName)
        } throws throwable.throwable

        // when
        bottomSheetCreateNewCollectionViewModel.createNewWishlistCollection(paramNewCollectionName)

        // then
        assert(bottomSheetCreateNewCollectionViewModel.createWishlistCollectionResult.value is Fail)
    }
}
