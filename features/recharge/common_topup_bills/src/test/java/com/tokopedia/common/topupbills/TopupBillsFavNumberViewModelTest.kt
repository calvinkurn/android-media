package com.tokopedia.common.topupbills

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.favoritepage.domain.usecase.ModifyRechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.favoritepage.domain.usecase.RechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberActionType
import com.tokopedia.common.topupbills.favoritepage.view.viewmodel.TopupBillsFavNumberViewModel
import com.tokopedia.common.topupbills.favoritepage.view.viewmodel.TopupBillsFavNumberViewModel.Companion.ERROR_FETCH_AFTER_DELETE
import com.tokopedia.common.topupbills.favoritepage.view.viewmodel.TopupBillsFavNumberViewModel.Companion.ERROR_FETCH_AFTER_UNDO_DELETE
import com.tokopedia.common.topupbills.favoritepage.view.viewmodel.TopupBillsFavNumberViewModel.Companion.ERROR_FETCH_AFTER_UPDATE
import com.tokopedia.common.topupbills.response.CommonTopupbillsDummyData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TopupBillsFavNumberViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var rechargeFavoriteNumberUseCase: RechargeFavoriteNumberUseCase

    @RelaxedMockK
    lateinit var modifyRechargeFavoriteNumberUseCase: ModifyRechargeFavoriteNumberUseCase

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var topupBillsFavNumberViewModel: TopupBillsFavNumberViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        topupBillsFavNumberViewModel = TopupBillsFavNumberViewModel(
            rechargeFavoriteNumberUseCase,
            modifyRechargeFavoriteNumberUseCase,
            testCoroutineRule.dispatchers,
        )
    }


    @Test
    fun getPersoFavoriteNumber_returnSuccessData() {
        // Given
        val favoriteNumber = CommonTopupbillsDummyData.getFavoriteNumberSuccess()
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } returns favoriteNumber

        // When
        topupBillsFavNumberViewModel.getPersoFavoriteNumbers(listOf(), listOf())

        // Then
        val actualData = topupBillsFavNumberViewModel.persoFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)

        assertThat(resultData.first.items[0].title == "081288888888")
    }

    @Test
    fun getFavoriteNumber_nullActionType_returnFailData() {
        // Given
        val throwable = MessageErrorException("error")
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } throws throwable

        // When
        topupBillsFavNumberViewModel.getPersoFavoriteNumbers(listOf(), listOf())

        // Then
        val actualData = topupBillsFavNumberViewModel.persoFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        assertEquals(throwable.message, error.message)
    }

    @Test
    fun getFavoriteNumber_actionTypeUpdate_returnUpdateErrorMessage() {
        // Given
        val throwable = MessageErrorException("error")
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } throws throwable

        // When
        topupBillsFavNumberViewModel.getPersoFavoriteNumbers(
            listOf(), listOf(), true, FavoriteNumberActionType.UPDATE)

        // Then
        val actualData = topupBillsFavNumberViewModel.persoFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        assertEquals(ERROR_FETCH_AFTER_UPDATE, error.message)
    }

    @Test
    fun getFavoriteNumber_actionTypeDelete_returnDeleteErrorMessage() {
        // Given
        val throwable = MessageErrorException("error")
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } throws throwable

        // When
        topupBillsFavNumberViewModel.getPersoFavoriteNumbers(
            listOf(), listOf(), true, FavoriteNumberActionType.DELETE)

        // Then
        val actualData = topupBillsFavNumberViewModel.persoFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        assertEquals(ERROR_FETCH_AFTER_DELETE, error.message)
    }

    @Test
    fun getFavoriteNumber_actionTypeUndoDelete_returnUndoDeleteErrorMessage() {
        // Given
        val throwable = MessageErrorException("error")
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } throws throwable

        // When
        topupBillsFavNumberViewModel.getPersoFavoriteNumbers(
            listOf(), listOf(), true, FavoriteNumberActionType.UNDO_DELETE)

        // Then
        val actualData = topupBillsFavNumberViewModel.persoFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        assertEquals(ERROR_FETCH_AFTER_UNDO_DELETE, error.message)
    }


    @Test
    fun updateSeamlessFavoriteNumber_returnSuccessResponse() {
        // Given
        val favoriteNumber = CommonTopupbillsDummyData.modifySeamlessFavoriteNumberSuccess()

        coEvery { modifyRechargeFavoriteNumberUseCase.executeOnBackground() } returns favoriteNumber

        // When
        topupBillsFavNumberViewModel.modifySeamlessFavoriteNumber(
            0,
            0,
            "",
            "",
            0,
            "",
            true,
            "",
            FavoriteNumberActionType.UPDATE,
            ""
        )

        // Then
        val actualData = topupBillsFavNumberViewModel.seamlessFavNumberUpdateData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)

        assertThat(resultData == favoriteNumber.updateFavoriteDetail)
    }

    @Test
    fun updateSeamlessFavoriteNumber_returnFailResponse() {
        // Given
        val exception = MessageErrorException("error")
        coEvery { modifyRechargeFavoriteNumberUseCase.executeOnBackground() } throws exception

        // When
        topupBillsFavNumberViewModel.modifySeamlessFavoriteNumber(
            0,
            0,
            "",
            "",
            0,
            "",
            true,
            "",
            FavoriteNumberActionType.UPDATE,
            ""
        )

        // Then
        val actualData = topupBillsFavNumberViewModel.seamlessFavNumberUpdateData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        org.junit.Assert.assertEquals(exception.message, error.message)
    }

    @Test
    fun deleteSeamlessFavoriteNumber_returnSuccessResponse() {
        // Given
        val favoriteNumber = CommonTopupbillsDummyData.modifySeamlessFavoriteNumberSuccess()

        coEvery { modifyRechargeFavoriteNumberUseCase.executeOnBackground() } returns favoriteNumber

        // When
        topupBillsFavNumberViewModel.modifySeamlessFavoriteNumber(
            0,
            0,
            "",
            "",
            0,
            "",
            true,
            "",
            FavoriteNumberActionType.DELETE,
            ""
        )

        // Then
        val actualData = topupBillsFavNumberViewModel.seamlessFavNumberDeleteData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)

        assertThat(resultData == favoriteNumber.updateFavoriteDetail)
    }

    @Test
    fun deleteSeamlessFavoriteNumber_returnFailResponse() {
        // Given
        val exception = MessageErrorException("error")
        coEvery { modifyRechargeFavoriteNumberUseCase.executeOnBackground() } throws exception

        // When
        topupBillsFavNumberViewModel.modifySeamlessFavoriteNumber(
            0,
            0,
            "",
            "",
            0,
            "",
            true,
            "",
            FavoriteNumberActionType.DELETE,
            ""
        )

        // Then
        val actualData = topupBillsFavNumberViewModel.seamlessFavNumberDeleteData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        org.junit.Assert.assertEquals(exception.message, error.message)
    }

    @Test
    fun undoDeleteSeamlessFavoriteNumber_returnSuccessResponse() {
        // Given
        val favoriteNumber = CommonTopupbillsDummyData.modifySeamlessFavoriteNumberSuccess()

        coEvery { modifyRechargeFavoriteNumberUseCase.executeOnBackground() } returns favoriteNumber

        // When
        topupBillsFavNumberViewModel.modifySeamlessFavoriteNumber(
            0,
            0,
            "",
            "",
            0,
            "",
            true,
            "",
            FavoriteNumberActionType.UNDO_DELETE,
            ""
        )

        // Then
        val actualData = topupBillsFavNumberViewModel.seamlessFavNumberUndoDeleteData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)

        assertThat(resultData == favoriteNumber.updateFavoriteDetail)
    }

    @Test
    fun undoDeleteSeamlessFavoriteNumber_returnFailResponse() {
        // Given
        val exception = MessageErrorException("error")
        coEvery { modifyRechargeFavoriteNumberUseCase.executeOnBackground() } throws exception

        // When
        topupBillsFavNumberViewModel.modifySeamlessFavoriteNumber(
            0,
            0,
            "",
            "",
            0,
            "",
            true,
            "",
            FavoriteNumberActionType.UNDO_DELETE,
            ""
        )

        // Then
        val actualData = topupBillsFavNumberViewModel.seamlessFavNumberUndoDeleteData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        org.junit.Assert.assertEquals(exception.message, error.message)
    }

    @Test
    fun createParamSource_multipleCategory_returnCorrectOutput() {
        val topupBillsFavNumberViewModel = TopupBillsFavNumberViewModel(
            rechargeFavoriteNumberUseCase,
            ModifyRechargeFavoriteNumberUseCase(graphqlRepository),
            testCoroutineRule.dispatchers,
        )
        val categoryIds = listOf(1,2,20)
        val output = topupBillsFavNumberViewModel.createSourceParam(categoryIds)

        assertTrue(output == "digital-personalization1,2,20")
    }

    @Test
    fun createParamSource_singleCategory_returnCorrectOutput() {
        val topupBillsFavNumberViewModel = TopupBillsFavNumberViewModel(
                rechargeFavoriteNumberUseCase,
                ModifyRechargeFavoriteNumberUseCase(graphqlRepository),
                testCoroutineRule.dispatchers,
            )
        val categoryIds = listOf(26)
        val output = topupBillsFavNumberViewModel.createSourceParam(categoryIds)

        assertTrue(output == "digital-personalization26")
    }
}
