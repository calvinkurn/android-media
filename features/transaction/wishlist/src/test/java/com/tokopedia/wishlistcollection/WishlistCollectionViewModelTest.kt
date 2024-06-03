package com.tokopedia.wishlistcollection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.WishlistMockTimber
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.collection.data.response.AffiliateUserDetailOnBoardingBottomSheetResponse
import com.tokopedia.wishlist.collection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionSharingDataResponse
import com.tokopedia.wishlist.collection.domain.AffiliateUserDetailOnBoardingBottomSheetUseCase
import com.tokopedia.wishlist.collection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlist.collection.domain.GetWishlistCollectionSharingDataUseCase
import com.tokopedia.wishlist.collection.domain.GetWishlistCollectionUseCase
import com.tokopedia.wishlist.collection.util.WishlistCollectionPrefs
import com.tokopedia.wishlist.collection.view.viewmodel.WishlistCollectionViewModel
import com.tokopedia.wishlist.detail.data.model.WishlistCollectionState
import com.tokopedia.wishlist.detail.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.detail.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlistcommon.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcommon.data.response.UpdateWishlistCollectionResponse
import com.tokopedia.wishlistcommon.domain.UpdateWishlistCollectionUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
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

    @RelaxedMockK
    lateinit var affiliateUserDetailOnBoardingBottomSheetUseCase: AffiliateUserDetailOnBoardingBottomSheetUseCase

    @RelaxedMockK
    lateinit var wishlistCollectionPrefs: WishlistCollectionPrefs

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

    private var getAffiliateUserDetail_registered = AffiliateUserDetailOnBoardingBottomSheetResponse(
        AffiliateUserDetailOnBoardingBottomSheetResponse.AffiliateUserDetail(isRegistered = true)
    )

    private var getAffiliateUserDetail_unregistered = AffiliateUserDetailOnBoardingBottomSheetResponse(
        AffiliateUserDetailOnBoardingBottomSheetResponse.AffiliateUserDetail(isRegistered = false)
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
                affiliateUserDetailOnBoardingBottomSheetUseCase,
                updateWishlistCollectionUseCase,
                wishlistCollectionPrefs
            )
        )
        val badges = arrayListOf<RecommendationItem.Badge>()
        badges.add(RecommendationItem.Badge("loren", "url"))
        badges.add(RecommendationItem.Badge("ipsum", "url"))

        val listRecommLabel = arrayListOf<RecommendationLabel>()
        listRecommLabel.add(RecommendationLabel(title = "test", imageUrl = "testUrl"))

        val recommItem1 = RecommendationItem(name = "recomm1", badges = badges, labelGroupList = listRecommLabel)
        val recommItem2 = RecommendationItem(name = "recomm2", badges = badges, labelGroupList = listRecommLabel)
        val recommItem3 = RecommendationItem(name = "recomm3", badges = badges, labelGroupList = listRecommLabel)
        listRecommendationItem = listOf(recommItem1, recommItem2, recommItem3)

        recommendationWidget = RecommendationWidget(
            tid = "123",
            recommendationItemList = listRecommendationItem,
            recommendationFilterChips = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip()),
            title = "TestRecomm"
        )
    }

    @Test
    fun `When Call loadPage Will Execute GetWishlistCollections With Success Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkErrorEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        mockkStatic(RemoteConfigInstance::class)

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_AFFILIATE_TICKER, "")
        } returns RollenceKey.WISHLIST_AFFILIATE_TICKER

        // when
        wishlistCollectionViewModel.loadPage()

        // the
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `When Call loadPage Will Execute GetWishlistCollections With Success EmptyState is True Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkEmptyStateErrorEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        mockkStatic(RemoteConfigInstance::class)

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_AFFILIATE_TICKER, "")
        } returns RollenceKey.WISHLIST_AFFILIATE_TICKER

        // when
        wishlistCollectionViewModel.loadPage()

        // then
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `When Call loadPage Will Execute GetWishlistCollections With Success Status OK Error Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkErrorNotEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget()

        // when
        wishlistCollectionViewModel.loadPage()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `When Call loadPage Will Execute GetWishlistCollections With Success Status ERROR Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusNotOkErrorEmpty

        // when
        wishlistCollectionViewModel.loadPage()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `When Call loadPage Will Execute GetWishlistCollections With Success Status ERROR Error Not Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusNotOkErrorNotEmpty

        // when
        wishlistCollectionViewModel.loadPage()

        // then
        assert(wishlistCollectionViewModel.collections.value is Fail)
    }

    @Test
    fun `When Call loadPage Will Execute GetWishlistCollections With Failed Result`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.loadPage()

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
        assert(wishlistCollectionViewModel.collectionData.value is WishlistCollectionState.Update)
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
    fun loadRecommendation_getRecommendationFailed() {
        // given
        coEvery {
            wishlistCollectionViewModel.getRecommendationWishlistV2(any(), any(), any())
        } throws RuntimeException("error")

        // when
        wishlistCollectionViewModel.loadRecommendation(0)

        // then
        assert(wishlistCollectionViewModel.collectionData.value is WishlistCollectionState.Error)
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

    @Test
    fun `Execute getAffiliateUserDetail Success Registered`() {
        // given
        coEvery {
            affiliateUserDetailOnBoardingBottomSheetUseCase(Unit)
        } returns getAffiliateUserDetail_registered

        // when
        wishlistCollectionViewModel.getAffiliateUserDetail()

        // then
        assert(wishlistCollectionViewModel.isUserAffiliate.value is Success)
        assert((wishlistCollectionViewModel.isUserAffiliate.value as Success).data.isRegistered)
    }

    @Test
    fun `Execute getAffiliateUserDetail Success Not Registered`() {
        // given
        coEvery {
            affiliateUserDetailOnBoardingBottomSheetUseCase(Unit)
        } returns getAffiliateUserDetail_unregistered

        // when
        wishlistCollectionViewModel.getAffiliateUserDetail()

        // then
        assert(wishlistCollectionViewModel.isUserAffiliate.value is Success)
        assert(!(wishlistCollectionViewModel.isUserAffiliate.value as Success).data.isRegistered)
    }

    @Test
    fun `Execute getAffiliateUserDetail Failed`() {
        // given
        coEvery {
            affiliateUserDetailOnBoardingBottomSheetUseCase(Unit)
        } throws throwable.throwable

        // when
        wishlistCollectionViewModel.getAffiliateUserDetail()

        // then
        assert(wishlistCollectionViewModel.isUserAffiliate.value is Fail)
        assert((wishlistCollectionViewModel.isUserAffiliate.value as Fail).throwable == throwable.throwable)
    }

    @Test
    fun `When Call loadPage and getWishlistCollections Execute GetWishlistCollections Success Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkErrorEmpty

        coEvery {
            singleRecommendationUseCase.getData(any())
        } returns RecommendationWidget(
            recommendationItemList = listOf(
                RecommendationItem(),
                RecommendationItem(),
                RecommendationItem()
            )
        )

        mockkStatic(RemoteConfigInstance::class)

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_AFFILIATE_TICKER, "")
        } returns RollenceKey.WISHLIST_AFFILIATE_TICKER

        // when
        wishlistCollectionViewModel.loadPage()

        wishlistCollectionViewModel.getWishlistCollections()

        // the
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `When Call getWishlistCollections Function Will Execute GetWishlistCollections With Success Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkErrorEmpty

        mockkStatic(RemoteConfigInstance::class)

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_AFFILIATE_TICKER, "")
        } returns RollenceKey.WISHLIST_AFFILIATE_TICKER

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // the
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `When Call getWishlistCollections Function Will Execute GetWishlistCollections With Success EmptyState is True Status OK Error Empty`() {
        // given
        coEvery {
            getWishlistCollectionUseCase(Unit)
        } returns collectionWishlistResponseDataStatusOkEmptyStateErrorEmpty

        mockkStatic(RemoteConfigInstance::class)

        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_AFFILIATE_TICKER, "")
        } returns RollenceKey.WISHLIST_AFFILIATE_TICKER

        // when
        wishlistCollectionViewModel.getWishlistCollections()

        // then
        assert(wishlistCollectionViewModel.collections.value is Success)
        assert((wishlistCollectionViewModel.collections.value as Success).data.errorMessage.isEmpty())
    }

    @Test
    fun `When Call getWishlistCollections Will Execute GetWishlistCollections With Success Status OK Error Not Empty`() {
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
    fun `When Call getWishlistCollections Execute GetWishlistCollections With Success Status ERROR Error Empty`() {
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
    fun `When Call Execute GetWishlistCollections With Success Status ERROR Error Not Empty`() {
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
    fun `When Call Execute GetWishlistCollections With Failed Result`() {
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
    fun `When Close Ticker, setHasClosed() Should Be Called`() {
        // given
        every {
            wishlistCollectionPrefs.setHasClosed(true)
        } returns Unit

        // when
        wishlistCollectionViewModel.closeTicker(true)

        // then
        verify {
            wishlistCollectionPrefs.setHasClosed(true)
        }
    }
}
