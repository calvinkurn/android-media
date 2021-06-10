package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopInfoViewModelTest: ShopInfoViewModelTestFixture() {

    @Test
    fun when_get_shop_info_success__should_return_shop_info_data() {
        runBlocking {
            val shopInfo = ShopInfo()

            onGetShopInfo_thenReturn(shopInfo)

            viewModel.getShopInfo("1")

            val expectedShopInfo = shopInfo
                    .toShopInfoData()

            verifyGetShopInfoUseCaseCalled()
            verifyShopInfoEquals(expectedShopInfo)
        }
    }

    @Test
    fun when_get_shop_notes_success__should_return_shop_notes_view_model() {
        runBlocking {
            val shopNoteModel = ShopNoteModel()
            val shopNotes = listOf(shopNoteModel)

            onGetShopNotes_thenReturn(shopNotes)

            viewModel.getShopNotes("1")

            val shopNotesList = shopNotes.toViewModel()
            val expectedShopNotes = Success(shopNotesList)

            verifyGetShopNotesUseCaseCalled()
            verifyShopNotesEquals(expectedShopNotes)
        }
    }

    @Test
    fun when_get_shop_statistics_success__should_concat_shop_reputation_to_shop_statistics() {
        runBlocking {
            val shopBadge = ShopBadge()
            val shopStatistics = ShopStatisticsResp()

            onGetShopStatistics_thenReturn(shopStatistics)
            onGetShopReputation_thenReturn(shopBadge)

            viewModel.getShopStats("1")

            val expectedShopStatistics = shopStatistics
                    .concatWith(shopBadge)

            verifyGetShopStatisticsCalled()
            verifyGetShopReputationCalled()
            verifyShopStatisticsEquals(expectedShopStatistics)
        }
    }

    @Test
    fun when_get_shop_statistics_fail__should_return_throwable() {
        runBlocking {
            val shopStatistics = ShopStatisticsResp()

            onGetShopStatistics_thenReturn(shopStatistics)
            onGetShopReputationFailed_thenReturn(Throwable())

            viewModel.getShopStats("1")

            verifyGetShopStatisticsCalled()
            verifyGetShopReputationCalled()
        }
    }

    @Test
    fun when_call_is_my_shop_should_return_true() {
        val shopId = "2913"
        onGetShopId_thenReturn(shopId)
        verifyShopIdEquals(shopId)
    }

    @Test
    fun `check whether shopBadgeReputation value is success`() {
        val shopId = "2913"
        onGetShopReputation_thenReturn(ShopBadge())
        viewModel.getShopReputationBadge(shopId)
        assert(viewModel.shopBadgeReputation.value is Success)
    }

    //region stub
    private suspend fun onGetShopInfo_thenReturn(shopInfo: ShopInfo) {
        coEvery { getShopInfoUseCase.executeOnBackground() } returns shopInfo
    }

    private suspend fun onGetShopNotes_thenReturn(shopNotes: List<ShopNoteModel>) {
        coEvery { getShopNotesUseCase.executeOnBackground() } returns shopNotes
    }

    private fun onGetShopReputation_thenReturn(shopBadge: ShopBadge) {
        coEvery { getShopReputationUseCase.executeOnBackground() } returns shopBadge
    }

    private fun onGetShopReputationFailed_thenReturn(throwable: Throwable) {
        coEvery { getShopReputationUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetShopStatistics_thenReturn(shopStatistics: ShopStatisticsResp) {
        coEvery { getShopStatisticsUseCase.executeOnBackground() } returns shopStatistics
    }

    private fun onGetShopId_thenReturn(shopId: String) {
        every { userSessionInterface.shopId } returns shopId
    }
    //endregion

    //region verifications
    private fun verifyGetShopInfoUseCaseCalled() {
        coVerify { getShopInfoUseCase.executeOnBackground() }
    }

    private fun verifyGetShopNotesUseCaseCalled() {
        coVerify { getShopNotesUseCase.executeOnBackground() }
    }

    private fun verifyGetShopReputationCalled() {
        coVerify { getShopReputationUseCase.executeOnBackground() }
    }

    private fun verifyGetShopStatisticsCalled() {
        coVerify { getShopStatisticsUseCase.executeOnBackground() }
    }

    private fun verifyShopInfoEquals(expectedShopInfo: ShopInfoData) {
        val actualShopInfo = viewModel.shopInfo.value
        assertEquals(expectedShopInfo, actualShopInfo)
    }

    private fun verifyShopNotesEquals(expectedShopNotes: Success<List<ShopNoteUiModel>>) {
        val actualShopNotes = (viewModel.shopNotesResp.value as Success<List<ShopNoteUiModel>>)
        assertShopNoteEquals(expectedShopNotes, actualShopNotes)
    }

    private fun assertShopNoteEquals(
            expectedShopNotes: Success<List<ShopNoteUiModel>>,
            actualShopNotes: Success<List<ShopNoteUiModel>>
    ) {
        expectedShopNotes.data.forEachIndexed { index, expectedShopNote ->
            val actualShopNote = actualShopNotes.data[index]
            assertEquals(expectedShopNote.shopNoteId, actualShopNote.shopNoteId)
            assertEquals(expectedShopNote.title, actualShopNote.title)
            assertEquals(expectedShopNote.position, actualShopNote.position)
            assertEquals(expectedShopNote.url, actualShopNote.url)
            assertEquals(expectedShopNote.lastUpdate, actualShopNote.lastUpdate)
        }
    }

    private fun verifyShopStatisticsEquals(expectedShopStatistics: ShopStatisticsResp) {
        val actualShopStatistics = viewModel.shopStatisticsResp.value
        assertEquals(expectedShopStatistics, actualShopStatistics)
    }

    private fun verifyShopIdEquals(shopId: String) {
        assertEquals(viewModel.isMyShop(shopId), true)
    }
    //endregion

    // region private methods
    private fun ShopInfo.toShopInfoData(): ShopInfoData {
        return ShopInfoData(
                shopCore.shopID,
                shopCore.name,
                shopCore.description,
                shopCore.url,
                location,
                shopAssets.cover,
                shopCore.tagLine,
                goldOS.isOfficial,
                goldOS.isGold,
                createdInfo.openSince,
                emptyList(),
                shopSnippetUrl
        )
    }

    private fun List<ShopNoteModel>.toViewModel(): List<ShopNoteUiModel> {
        return map {
            ShopNoteUiModel().apply {
                shopNoteId = it.id?.toLongOrNull() ?: 0
                title = it.title
                position = it.position.toLong()
                url = it.url
                lastUpdate = it.updateTime
            }
        }
    }

    private fun ShopStatisticsResp.concatWith(shopBadge: ShopBadge): ShopStatisticsResp {
        return copy(shopReputation = shopBadge)
    }
    // endregion
}
