package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
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

    @RelaxedMockK
    lateinit var singleRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase

    private var collectionWishlistResponseDataStatusOk = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(status = "OK"))

    private var collectionWishlistResponseDataStatusError = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(status = "ERROR"))

    private val throwable = Fail(Throwable(message = "Error"))

    private val collectionIdToBeDeleted = "1"

    private var deleteWishlistCollectionResponseDataStatusOk = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "OK"))

    private var deleteWishlistCollectionResponseDataStatusError = DeleteWishlistCollectionResponse(
        deleteWishlistCollection = DeleteWishlistCollectionResponse.DeleteWishlistCollection(status = "ERROR"))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        wishlistCollectionViewModel = spyk(
            WishlistCollectionViewModel(
                dispatcher,
                getWishlistCollectionUseCase,
                deleteWishlistCollectionUseCase,
                singleRecommendationUseCase,
                deleteWishlistProgressUseCase
            )
        )
    }

    @Test
    fun `Execute GetWishlistCollections Success Status OK`() {
        //given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOk

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        //when
        wishlistCollectionViewModel.getWishlistCollections()

        //then
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute GetWishlistCollections Success Status ERROR`() {
        //given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusError

        //when
        wishlistCollectionViewModel.getWishlistCollections()

        //then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollections Failed`() {
        //given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } throws throwable.throwable

        //when
        wishlistCollectionViewModel.getWishlistCollections()

        //then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Success Status OK`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } returns deleteWishlistCollectionResponseDataStatusOk

        //when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        //then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Success)
        assert((wishlistCollectionViewModel.deleteCollectionResult.value as Success).data.status == "OK")
    }

    @Test
    fun `Execute DeleteWishlistCollections Success Status ERROR`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } returns deleteWishlistCollectionResponseDataStatusError

        //when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        //then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Failed`() {
        //given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } throws throwable.throwable

        //when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        //then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Fail)
    }
}