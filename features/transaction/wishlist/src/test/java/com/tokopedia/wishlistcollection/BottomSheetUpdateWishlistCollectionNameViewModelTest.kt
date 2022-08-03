package com.tokopedia.wishlistcollection

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
    private val listErrorMessage = arrayListOf<String>()

    @RelaxedMockK
    lateinit var getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase

    @RelaxedMockK
    lateinit var updateWishlistCollectionNameUseCase: UpdateWishlistCollectionNameUseCase

    private var collectionNamesResponseDataStatusOkAndMessageIsEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = emptyList()))

    private var collectionNamesResponseDataStatusOkAndMessageIsNotEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = listErrorMessage))

    private var collectionNamesResponseDataStatusErrorAndMessageIsNotEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = listErrorMessage))

    private var collectionNamesResponseDataStatusErrorAndMessageIsEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = emptyList()))

    private var updateCollectionNameResponseDataStatusOkAndMessageIsEmpty = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "OK", errorMessage = emptyList()))

    private var updateCollectionNameResponseDataStatusOkAndMessageIsNotEmpty = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "OK", errorMessage = listErrorMessage))

    private var updateCollectionNameResponseDataStatusErrorAndMessageIsNotEmpty = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "ERROR", errorMessage = listErrorMessage))

    private var updateCollectionNameResponseDataStatusErrorAndMessageIsEmpty = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "ERROR", errorMessage = emptyList()))

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
        listErrorMessage.add("error")
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status OK And Message is Empty`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns collectionNamesResponseDataStatusOkAndMessageIsEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value is Success)
        assert((bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status OK And Message is not Empty`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns collectionNamesResponseDataStatusOkAndMessageIsNotEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status ERROR and Message is Empty`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns collectionNamesResponseDataStatusErrorAndMessageIsEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status ERROR and Message is not Empty`() {
        //given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns collectionNamesResponseDataStatusErrorAndMessageIsNotEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.getWishlistCollectionNames()

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.collectionNames.value is Fail)
    }

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

    @Test
    fun `Execute UpdateWishlistCollectionName Success Status OK`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } returns updateCollectionNameResponseDataStatusOkAndMessageIsEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Success)
        assert((bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute UpdateWishlistCollectionName Success Status OK And Message is not Empty`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } returns updateCollectionNameResponseDataStatusOkAndMessageIsNotEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Fail)
    }

    @Test
    fun `Execute UpdateWishlistCollectionName Status Error And Message is Not Empty`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } returns updateCollectionNameResponseDataStatusErrorAndMessageIsNotEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Fail)
    }

    @Test
    fun `Execute UpdateWishlistCollectionName Status Error And Message is Empty`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } returns updateCollectionNameResponseDataStatusErrorAndMessageIsEmpty

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Fail)
    }

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