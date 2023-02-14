package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.WishlistMockTimber
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcollection.data.response.*
import com.tokopedia.wishlistcollection.domain.*
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionEditViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import timber.log.Timber

@RunWith(JUnit4::class)
class WishlistCollectionEditViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistCollectionEditViewModel: WishlistCollectionEditViewModel
    private val timber = WishlistMockTimber()

    @RelaxedMockK
    lateinit var getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase

    @RelaxedMockK
    lateinit var updateWishlistCollectionUseCase: UpdateWishlistCollectionUseCase

    @RelaxedMockK
    lateinit var getWishlistCollectionByIdUseCase: GetWishlistCollectionByIdUseCase

    @RelaxedMockK
    lateinit var deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase

    private val throwable = Fail(Throwable(message = "Error"))

    private var getCollectionNames_StatusOk_ErrorEmpty = GetWishlistCollectionNamesResponse(
        GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = emptyList())
    )

    private var getCollectionNames_StatusOk_ErrorNotEmpty = GetWishlistCollectionNamesResponse(
        GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "OK", errorMessage = arrayListOf("error"))
    )

    private var getCollectionNames_StatusNotOk_ErrorEmpty = GetWishlistCollectionNamesResponse(
        GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = emptyList())
    )

    private var getCollectionNames_StatusNotOk_ErrorNotEmpty = GetWishlistCollectionNamesResponse(
        GetWishlistCollectionNamesResponse.GetWishlistCollectionNames(status = "ERROR", errorMessage = arrayListOf("error"))
    )

    private var updateWishlistAccessParam = UpdateWishlistCollectionParams()

    private var updateWishlistCollectionAccess_SuccessTrue_StatusOk_ErrorEmpty = UpdateWishlistCollectionResponse(
        UpdateWishlistCollectionResponse.UpdateWishlistCollection(
            status = "OK",
            errorMessage = emptyList(),
            data = UpdateWishlistCollectionResponse.UpdateWishlistCollection.Data(success = true)
        )
    )

    private var updateWishlistCollectionAccess_SuccessFalse_StatusOk_ErrorEmpty = UpdateWishlistCollectionResponse(
        UpdateWishlistCollectionResponse.UpdateWishlistCollection(
            status = "OK",
            errorMessage = emptyList(),
            data = UpdateWishlistCollectionResponse.UpdateWishlistCollection.Data(success = false)
        )
    )

    private var updateWishlistCollectionAccessStatusOkErrorNotEmpty = UpdateWishlistCollectionResponse(
        UpdateWishlistCollectionResponse.UpdateWishlistCollection(status = "OK", errorMessage = arrayListOf("error"))
    )

    private var updateWishlistCollectionAccessStatusNotOkErrorEmpty = UpdateWishlistCollectionResponse(
        UpdateWishlistCollectionResponse.UpdateWishlistCollection(status = "ERROR", errorMessage = emptyList())
    )

    private var updateWishlistCollectionAccessStatusNotOkErrorNotEmpty = UpdateWishlistCollectionResponse(
        UpdateWishlistCollectionResponse.UpdateWishlistCollection(status = "ERROR", errorMessage = arrayListOf("error"))
    )

    private val collectionId = "1"

    private var getWishlistCollectionById_StatusOk = GetWishlistCollectionByIdResponse(
        GetWishlistCollectionByIdResponse.GetWishlistCollectionById(status = "OK", errorMessage = emptyList())
    )

    private var getWishlistCollectionById_StatusNotOk = GetWishlistCollectionByIdResponse(
        GetWishlistCollectionByIdResponse.GetWishlistCollectionById(status = "ERROR", errorMessage = emptyList())
    )

    private val productId = "1"

    private var deleteWishlistCollectionResponseDataStatusOkErrorEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "OK", errorMessage = emptyList())
    )

    private var deleteWishlistCollectionResponseDataStatusOkErrorNotEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "OK", errorMessage = arrayListOf("error"))
    )

    private var deleteWishlistCollectionResponseDataStatusNotOkErrorEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "ERROR", errorMessage = emptyList())
    )

    private var deleteWishlistCollectionResponseDataStatusNotOkErrorNotEmpty = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "ERROR", errorMessage = arrayListOf("error"))
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Timber.plant(timber)
        wishlistCollectionEditViewModel = spyk(
            WishlistCollectionEditViewModel(
                dispatcher,
                getWishlistCollectionNamesUseCase,
                updateWishlistCollectionUseCase,
                getWishlistCollectionByIdUseCase,
                deleteWishlistCollectionUseCase
            )
        )
        val badgesUrl = arrayListOf<String>()
        badgesUrl.add("url")
    }

    // get wishlist collection names
    @Test
    fun `Execute GetWishlistCollectionNames Success Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getCollectionNames_StatusOk_ErrorEmpty

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionNames()

        // then
        assert(wishlistCollectionEditViewModel.collectionNames.value is Success)
        assert((wishlistCollectionEditViewModel.collectionNames.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status OK Error Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getCollectionNames_StatusOk_ErrorNotEmpty

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionNames()

        // then
        assert(wishlistCollectionEditViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status ERROR Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getCollectionNames_StatusNotOk_ErrorEmpty

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionNames()

        // then
        assert(wishlistCollectionEditViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionNames Success Status ERROR Error Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } returns getCollectionNames_StatusNotOk_ErrorNotEmpty

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionNames()

        // then
        assert(wishlistCollectionEditViewModel.collectionNames.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionNames Failed`() {
        // given
        coEvery {
            getWishlistCollectionNamesUseCase(Unit)
        } throws throwable.throwable

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionNames()

        // then
        assert(wishlistCollectionEditViewModel.collectionNames.value is Fail)
    }

    // update wishlist collection access
    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status OK And Error is Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccess_SuccessTrue_StatusOk_ErrorEmpty

        // when
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionEditViewModel.updateWishlistCollectionResult.value is Success)
        assert((wishlistCollectionEditViewModel.updateWishlistCollectionResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Failed Status OK And Error is Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccess_SuccessFalse_StatusOk_ErrorEmpty

        // when
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionEditViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status OK And Error is not Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccessStatusOkErrorNotEmpty

        // when
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionEditViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status Error And Error is Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccessStatusNotOkErrorEmpty

        // when
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionEditViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status Error And Error is not Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccessStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionEditViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Failed`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } throws throwable.throwable

        // when
        wishlistCollectionEditViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionEditViewModel.updateWishlistCollectionResult.value is Fail)
    }

    // get wishlist collection by ID
    @Test
    fun `Execute GetWishlistCollectionById Status OK And Error is Empty`() {
        // given
        coEvery {
            getWishlistCollectionByIdUseCase(collectionId)
        } returns getWishlistCollectionById_StatusOk

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionById(collectionId)

        // then
        assert(wishlistCollectionEditViewModel.getWishlistCollectionByIdResult.value is Success)
        assert((wishlistCollectionEditViewModel.getWishlistCollectionByIdResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetWishlistCollectionById Status ERROR`() {
        // given
        coEvery {
            getWishlistCollectionByIdUseCase(collectionId)
        } returns getWishlistCollectionById_StatusNotOk

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionById(collectionId)

        // then
        assert(wishlistCollectionEditViewModel.getWishlistCollectionByIdResult.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollectionById Failed`() {
        // given
        coEvery {
            getWishlistCollectionByIdUseCase(collectionId)
        } throws throwable.throwable

        // when
        wishlistCollectionEditViewModel.getWishlistCollectionById(collectionId)

        // then
        assert(wishlistCollectionEditViewModel.getWishlistCollectionByIdResult.value is Fail)
    }

    // delete wishlist collection
    @Test
    fun `Execute DeleteWishlistCollection Success Status OK Error is Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusOkErrorEmpty

        // when
        wishlistCollectionEditViewModel.deleteWishlistCollection(productId)

        // then
        assert(wishlistCollectionEditViewModel.deleteCollectionResult.value is Success)
        assert((wishlistCollectionEditViewModel.deleteCollectionResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status OK Error not Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusOkErrorNotEmpty

        // when
        wishlistCollectionEditViewModel.deleteWishlistCollection(productId)

        // then
        assert(wishlistCollectionEditViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status Not OK Error Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusNotOkErrorEmpty

        // when
        wishlistCollectionEditViewModel.deleteWishlistCollection(productId)

        // then
        assert(wishlistCollectionEditViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Success Status Not OK Error not Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } returns deleteWishlistCollectionResponseDataStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionEditViewModel.deleteWishlistCollection(productId)

        // then
        assert(wishlistCollectionEditViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollection Failed`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(productId)
        } throws throwable.throwable

        // when
        wishlistCollectionEditViewModel.deleteWishlistCollection(productId)

        // then
        assert(wishlistCollectionEditViewModel.deleteCollectionResult.value is Fail)
    }
}
