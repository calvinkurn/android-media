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

    private var collectionNamesResponseDataStatusOkAndEmptyMessage = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = emptyList()))

    private var collectionNamesResponseDataStatusOkAndMessageNotEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = listErrorMessage))

    private var collectionNamesResponseDataStatusErrorAndMessageNotEmpty = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = listErrorMessage))

    private var collectionNamesResponseDataStatusErrorAndEmptyMessage = GetWishlistCollectionNamesResponse(
        getWishlistCollectionNames = GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = emptyList()))

    private var updateCollectionNameResponseDataStatusOk = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "OK", errorMessage = emptyList()))

    private var updateCollectionNameResponseDataStatusError = UpdateWishlistCollectionNameResponse(
        updateWishlistCollectionName = UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName(status = "ERROR", errorMessage = listErrorMessage))

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
        } returns collectionNamesResponseDataStatusOkAndEmptyMessage

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
        } returns collectionNamesResponseDataStatusOkAndMessageNotEmpty

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
        } returns collectionNamesResponseDataStatusErrorAndEmptyMessage

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
        } returns collectionNamesResponseDataStatusErrorAndEmptyMessage

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
        } returns updateCollectionNameResponseDataStatusOk

        //when
        bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionName(newCollectionNameParam)

        //then
        assert(bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value is Success)
        assert((bottomSheetUpdateWishlistCollectionNameViewModel.updateWishlistCollectionNameResult.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute UpdateWishlistCollectionName Success Status ERROR`() {
        //given
        coEvery {
            updateWishlistCollectionNameUseCase(newCollectionNameParam)
        } returns updateCollectionNameResponseDataStatusError

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