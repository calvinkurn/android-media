package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.WishlistMockTimber
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionSharingDataResponse
import com.tokopedia.wishlistcommon.data.response.UpdateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionSharingDataUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionUseCase
import com.tokopedia.wishlistcommon.domain.UpdateWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import timber.log.Timber

@RunWith(JUnit4::class)
class WishlistCollectionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var wishlistCollectionViewModel: WishlistCollectionViewModel
    private var recommendationWidget = RecommendationWidget()
    private var listRecommendationItem = listOf<RecommendationItem>()
    private val timber = WishlistMockTimber()

    @RelaxedMockK
    lateinit var getWishlistCollectionUseCase: GetWishlistCollectionUseCase

    @RelaxedMockK
    lateinit var deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase

    @RelaxedMockK
    lateinit var singleRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase

    @RelaxedMockK
    lateinit var getWishlistCollectionSharingDataUseCase: GetWishlistCollectionSharingDataUseCase

    @RelaxedMockK
    lateinit var updateWishlistCollectionUseCase: UpdateWishlistCollectionUseCase

    private var collectionWishlistResponseDataStatusOkErrorEmpty = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(status = "OK", errorMessage = emptyList())
    )

    private var collectionWishlistResponseDataStatusOkEmptyStateErrorEmpty = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(
            status = "OK",
            errorMessage = emptyList(),
            data = GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData(isEmptyState = true)
        )
    )

    private var collectionWishlistResponseDataStatusOkErrorNotEmpty = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(status = "OK", errorMessage = arrayListOf("error"))
    )

    private var collectionWishlistResponseDataStatusNotOkErrorEmpty = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(status = "ERROR", errorMessage = emptyList())
    )

    private var collectionWishlistResponseDataStatusNotOkErrorNotEmpty = GetWishlistCollectionResponse(
        getWishlistCollections = GetWishlistCollectionResponse.GetWishlistCollections(status = "ERROR", errorMessage = arrayListOf("error"))
    )

    private val throwable = Fail(Throwable(message = "Error"))

    private val collectionIdToBeDeleted = "1"

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

    private var deleteWishlistProgressStatusOkErrorEmpty = DeleteWishlistProgressResponse(
        DeleteWishlistProgressResponse.DeleteWishlistProgress(
            status = "OK",
            errorMessage = emptyList()
        )
    )

    private var deleteWishlistProgressStatusOkErrorNotEmpty = DeleteWishlistProgressResponse(
        DeleteWishlistProgressResponse.DeleteWishlistProgress(
            status = "OK",
            errorMessage = arrayListOf("error")
        )
    )

    private var deleteWishlistProgressStatusNotOkErrorEmpty = DeleteWishlistProgressResponse(
        DeleteWishlistProgressResponse.DeleteWishlistProgress(
            status = "ERROR",
            errorMessage = emptyList()
        )
    )

    private var deleteWishlistProgressStatusNotOkErrorNotEmpty = DeleteWishlistProgressResponse(
        DeleteWishlistProgressResponse.DeleteWishlistProgress(
            status = "ERROR",
            errorMessage = arrayListOf("error")
        )
    )

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

    private var updateWishlistAccessParam = UpdateWishlistCollectionParams()

    private var getCollectionSharingData_StatusOk = GetWishlistCollectionSharingDataResponse(
        GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData(status = "OK")
    )

    private var getCollectionSharingData_StatusNotOk_ErrorEmpty = GetWishlistCollectionSharingDataResponse(
        GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData(status = "ERROR", errorMessage = emptyList())
    )

    private var getCollectionSharingData_StatusNotOk_ErrorNotEmpty = GetWishlistCollectionSharingDataResponse(
        GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData(status = "ERROR", errorMessage = arrayListOf("error"))
    )

    private var collectionId = 1L

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Timber.plant(timber)
        wishlistCollectionViewModel = spyk(
            WishlistCollectionViewModel(
                dispatcher,
                getWishlistCollectionUseCase,
                deleteWishlistCollectionUseCase,
                singleRecommendationUseCase,
                deleteWishlistProgressUseCase,
                getWishlistCollectionSharingDataUseCase,
                updateWishlistCollectionUseCase
            )
        )
        val badgesUrl = arrayListOf<String>()
        badgesUrl.add("url")

        val listRecommLabel = arrayListOf<RecommendationLabel>()
        listRecommLabel.add(RecommendationLabel(title = "test", imageUrl = "testUrl"))

        val recommItem1 = RecommendationItem(name = "recomm1", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
        val recommItem2 = RecommendationItem(name = "recomm2", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
        val recommItem3 = RecommendationItem(name = "recomm3", badgesUrl = badgesUrl, labelGroupList = listRecommLabel)
        listRecommendationItem = listOf(recommItem1, recommItem2, recommItem3)

        recommendationWidget = RecommendationWidget(
            tid = "123",
            recommendationItemList = listRecommendationItem,
            recommendationFilterChips = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip()),
            title = "TestRecomm"
        )
    }

    @Test
    fun `Execute GetWishlistCollections Success Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkErrorEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetWishlistCollections Success EmptyState is True Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkEmptyStateErrorEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetWishlistCollections Success Status OK Error Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkErrorNotEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollections Success Status ERROR Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusNotOkErrorEmpty

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollections Success Status ERROR Error Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `Execute GetWishlistCollections Failed`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Success Status OK Error Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } returns deleteWishlistCollectionResponseDataStatusOkErrorEmpty

        // when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        // then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Success)
        assert((wishlistCollectionViewModel.deleteCollectionResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute DeleteWishlistCollections Success Status OK Error Not Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } returns deleteWishlistCollectionResponseDataStatusOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        // then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Success Status Not Ok Error Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } returns deleteWishlistCollectionResponseDataStatusNotOkErrorEmpty

        // when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        // then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Success Status Not Ok Error Not Empty`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } returns deleteWishlistCollectionResponseDataStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        // then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun `Execute DeleteWishlistCollections Failed`() {
        // given
        coEvery {
            deleteWishlistCollectionUseCase(collectionIdToBeDeleted)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.deleteWishlistCollection(collectionIdToBeDeleted)

        // then
        assert(wishlistCollectionViewModel.deleteCollectionResult.value is Fail)
    }

    @Test
    fun loadRecommendation_shouldReturnSuccess() {
        // given
        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns recommendationWidget

        // when
        wishlistCollectionViewModel.loadRecommendation(0)

        // then
        assert(wishlistCollectionViewModel.collectionData.value is Success<List<WishlistCollectionTypeLayoutData>>)
    }

    // recommendation_failed
    @Test
    fun loadRecommendation_shouldReturnFail() {
        val throwable = spyk(Throwable())

        // given
        coEvery {
            singleRecommendationUseCase.getData(any())
        } throws Exception()

        // when
        wishlistCollectionViewModel.loadRecommendation(0)

        // then
        SoftAssertions.assertSoftly {
            timber.lastLogMessage() contentEquals throwable.localizedMessage
        }
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status OK And Error is Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusOkErrorEmpty

        // when
        wishlistCollectionViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistCollectionViewModel.deleteWishlistProgressResult.value is Success)
        assert((wishlistCollectionViewModel.deleteWishlistProgressResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status OK And Error is not Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistCollectionViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status Error And Error is Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusNotOkErrorEmpty

        // when
        wishlistCollectionViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistCollectionViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Success Status Error And Error is not Empty`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } returns deleteWishlistProgressStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistCollectionViewModel.deleteWishlistProgressResult.value is Fail)
    }

    @Test
    fun `Execute GetDeleteWishlistProgress Failed`() {
        // given
        coEvery {
            deleteWishlistProgressUseCase(Unit)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.getDeleteWishlistProgress()

        // then
        assert(wishlistCollectionViewModel.deleteWishlistProgressResult.value is Fail)
    }

    // update access wishlist collection
    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status OK And Error is Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccess_SuccessTrue_StatusOk_ErrorEmpty

        // when
        wishlistCollectionViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionViewModel.updateWishlistCollectionResult.value is Success)
        assert((wishlistCollectionViewModel.updateWishlistCollectionResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Failed Status OK And Error is Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccess_SuccessFalse_StatusOk_ErrorEmpty

        // when
        wishlistCollectionViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status OK And Error is not Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccessStatusOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status Error And Error is Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccessStatusNotOkErrorEmpty

        // when
        wishlistCollectionViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Success Status Error And Error is not Empty`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } returns updateWishlistCollectionAccessStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionViewModel.updateWishlistCollectionResult.value is Fail)
    }

    @Test
    fun `Execute UpdateAccessWishlistCollection Failed`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            updateWishlistCollectionUseCase(updateWishlistAccessParam)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.updateAccessWishlistCollection(updateWishlistAccessParam)

        // then
        assert(wishlistCollectionViewModel.updateWishlistCollectionResult.value is Fail)
    }

    // get collection sharing data
    @Test
    fun `Execute GetCollectionSharingData Success Status OK`() {
        // given
        coEvery {
            getWishlistCollectionSharingDataUseCase(collectionId)
        } returns getCollectionSharingData_StatusOk

        // when
        wishlistCollectionViewModel.getWishlistCollectionSharingData(collectionId)

        // then
        assert(wishlistCollectionViewModel.getWishlistCollectionSharingDataResult.value is Success)
        assert((wishlistCollectionViewModel.getWishlistCollectionSharingDataResult.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `Execute GetCollectionSharingData Success Not OK And Error is Empty`() {
        // given
        coEvery {
            getWishlistCollectionSharingDataUseCase(collectionId)
        } returns getCollectionSharingData_StatusNotOk_ErrorEmpty

        // when
        wishlistCollectionViewModel.getWishlistCollectionSharingData(collectionId)

        // then
        assert(wishlistCollectionViewModel.getWishlistCollectionSharingDataResult.value is Fail)
    }

    @Test
    fun `Execute GetCollectionSharingData Status Not OK And Error is not Empty`() {
        // given
        coEvery {
            getWishlistCollectionSharingDataUseCase(collectionId)
        } returns getCollectionSharingData_StatusNotOk_ErrorNotEmpty

        // when
        wishlistCollectionViewModel.getWishlistCollectionSharingData(collectionId)

        // then
        assert(wishlistCollectionViewModel.getWishlistCollectionSharingDataResult.value is Fail)
    }

    @Test
    fun `Execute GetCollectionSharingData Failed`() {
        // given
        updateWishlistAccessParam = UpdateWishlistCollectionParams(id = 1L)
        coEvery {
            getWishlistCollectionSharingDataUseCase(collectionId)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.getWishlistCollectionSharingData(collectionId)

        // then
        assert(wishlistCollectionViewModel.getWishlistCollectionSharingDataResult.value is Fail)
    }
}
