package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.shop.common.constant.ShopPartnerFsFullfillmentServiceTypeDef
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.data.model.ShopShipmentData
import com.tokopedia.shop.common.graphql.data.shopinfo.ChatExistingChat
import com.tokopedia.shop.common.graphql.data.shopinfo.ChatMessageId
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop_widget.note.view.model.ShopNoteUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopInfoViewModelTest : ShopInfoViewModelTestFixture() {

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
    fun when_get_shop_info_error_shop_info_value_should_be_null() {
        runBlocking {
            onGetShopInfo_thenReturn_Error()

            viewModel.getShopInfo("1")

            verifyGetShopInfoUseCaseCalled()
            verifyShopInfoNull()
        }
    }

    @Test
    fun when_get_shop_notes_success__should_return_shop_notes_view_model() {
        runBlocking {
            val shopNoteModel = ShopNoteModel(id = "123")
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
    fun `check whether shopNoteId is 0 if id from response is null`() {
        runBlocking {
            val shopNoteModel = ShopNoteModel(
                id = null
            )
            val shopNotes = listOf(shopNoteModel)

            onGetShopNotes_thenReturn(shopNotes)

            viewModel.getShopNotes("1")

            val shopNoteId = (viewModel.shopNotesResp.value as Success).data.getOrNull(0)?.shopNoteId
            assert(shopNoteId == 0L)
        }
    }

    @Test
    fun `check whether shopNoteId is 0 if id from response is string`() {
        runBlocking {
            val shopNoteModel = ShopNoteModel(
                id = "asd"
            )
            val shopNotes = listOf(shopNoteModel)

            onGetShopNotes_thenReturn(shopNotes)

            viewModel.getShopNotes("1")

            val shopNoteId = (viewModel.shopNotesResp.value as Success).data.getOrNull(0)?.shopNoteId
            assert(shopNoteId == 0L)
        }
    }

    @Test
    fun when_get_shop_notes_error__should_post_fail() {
        runBlocking {
            onGetShopNotes_thenReturn_Error()

            viewModel.getShopNotes("1")

            verifyGetShopNotesUseCaseCalled()
            verifyShopNotesResponseIsFail()
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

    @Test
    fun `check whether shopBadgeReputation value is error`() {
        val shopId = "2913"
        onGetShopReputation_thenThrowError()
        viewModel.getShopReputationBadge(shopId)
        assert(viewModel.shopBadgeReputation.value is Fail)
    }

    @Test
    fun `check whether userId should return mocked value`() {
        val mockUserId = "753348464"
        onGetUserId_thenReturn(mockUserId)
        val userId = viewModel.userId()
        assert(userId == mockUserId)
    }

    @Test
    fun `when user is login and success to get chat existing message id`() {
        runBlocking {
            // define return expected
            val shopMessageChat = ChatMessageId("123")
            onGetMessageIdChat_thenReturn(ChatExistingChat(shopMessageChat))
            isLoginSession_returnTrue()

            // on exceute
            viewModel.getMessageIdOnChatExist("12")
            val resultInReal = viewModel.messageIdOnChatExist.value as Success<String>

            // on assertion result
            assert(viewModel.messageIdOnChatExist.value is Success)
            assertEquals("123", resultInReal.data)
        }
    }

    @Test
    fun `when user not login to get chat existing message id`() {
        runBlocking {
            // define return expected
            isLoginSession_returnFalse()

            // on exceute
            viewModel.getMessageIdOnChatExist("0")

            // on assertion result
            val actualResultOfMessageId = (viewModel.messageIdOnChatExist.value)
            assert(actualResultOfMessageId is Fail)
            val failData = actualResultOfMessageId as Fail
            assert(failData.throwable is UserNotLoginException)
        }
    }

    @Test
    fun `when shop is GoApotik should return true`() {
        // given
        val isGoApotik = true

        // when
        val result = viewModel.isShouldShowLicenseForDrugSeller(isGoApotik = isGoApotik, fsType = ShopPartnerFsFullfillmentServiceTypeDef.DEFAULT)

        // then
        assert(result)
    }

    @Test
    fun `when shop is EPharmacy should return true`() {
        // given
        val isGoApotik = false

        // when
        val result = viewModel.isShouldShowLicenseForDrugSeller(isGoApotik = isGoApotik, fsType = ShopPartnerFsFullfillmentServiceTypeDef.EPHARMACY)

        // then
        assert(result)
    }

    @Test
    fun `when shop is not EPharmacy or not GoApotik should return false`() {
        // given
        val isGoApotik = false

        // when
        val result = viewModel.isShouldShowLicenseForDrugSeller(isGoApotik = isGoApotik, fsType = ShopPartnerFsFullfillmentServiceTypeDef.DEFAULT)

        // then
        assert(!result)
    }

    @Test
    fun `when user login but error to get chat existing message id`() {
        runBlocking {
            // define return expected
            isLoginSession_returnTrue()
            onGetMessageIdChat_thenReturn_Error()

            // on exceute
            viewModel.getMessageIdOnChatExist("0")

            // on assertion result
            val actualResultOfMessageId = (viewModel.messageIdOnChatExist.value)
            assert(actualResultOfMessageId is Fail)
        }
    }

    //region stub
    private suspend fun onGetShopInfo_thenReturn(shopInfo: ShopInfo) {
        coEvery { getShopInfoUseCase.executeOnBackground() } returns shopInfo
    }

    private suspend fun onGetShopInfo_thenReturn_Error() {
        coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()
    }

    private suspend fun onGetShopNotes_thenReturn(shopNotes: List<ShopNoteModel>) {
        coEvery { getShopNotesUseCase.executeOnBackground() } returns shopNotes
    }
    private suspend fun onGetShopNotes_thenReturn_Error() {
        coEvery { getShopNotesUseCase.executeOnBackground() } throws Exception()
    }

    private fun onGetShopReputation_thenReturn(shopBadge: ShopBadge) {
        coEvery { getShopReputationUseCase.executeOnBackground() } returns shopBadge
    }

    private fun onGetShopReputation_thenThrowError() {
        coEvery { getShopReputationUseCase.executeOnBackground() } throws Exception()
    }

    private fun onGetShopId_thenReturn(shopId: String) {
        every { userSessionInterface.shopId } returns shopId
    }

    private fun onGetUserId_thenReturn(userId: String) {
        every { userSessionInterface.userId } returns userId
    }
    //endregion

    //region verifications
    private fun verifyGetShopInfoUseCaseCalled() {
        coVerify { getShopInfoUseCase.executeOnBackground() }
    }

    private fun verifyGetShopNotesUseCaseCalled() {
        coVerify { getShopNotesUseCase.executeOnBackground() }
    }

    private fun verifyShopInfoEquals(expectedShopInfo: ShopInfoData) {
        val actualShopInfo = viewModel.shopInfo.value
        assertEquals(expectedShopInfo, actualShopInfo)
    }

    private fun verifyShopInfoNull() {
        val actualShopInfo = viewModel.shopInfo.value
        assertEquals(actualShopInfo, null)
    }

    private fun verifyShopNotesEquals(expectedShopNotes: Success<List<ShopNoteUiModel>>) {
        val actualShopNotes = (viewModel.shopNotesResp.value as Success<List<ShopNoteUiModel>>)
        assertShopNoteEquals(expectedShopNotes, actualShopNotes)
    }

    private fun verifyShopNotesResponseIsFail() {
        val shopNotesResponse = viewModel.shopNotesResp.value
        assert(shopNotesResponse is Fail)
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

    private fun verifyShopIdEquals(shopId: String) {
        assertEquals(viewModel.isMyShop(shopId), true)
    }
    //endregion

    // region private methods
    private fun ShopInfo.toShopInfoData(): ShopInfoData {
        return ShopInfoData(
            shopId = shopCore.shopID,
            name = shopCore.name,
            description = shopCore.description,
            url = shopCore.url,
            location = location,
            imageCover = shopAssets.cover,
            tagLine = shopCore.tagLine,
            isOfficial = goldOS.isOfficial,
            isGold = goldOS.isGold,
            openSince = createdInfo.openSince,
            shipments = emptyList(),
            shopSnippetUrl = shopSnippetUrl,
            isGoApotik = isGoApotik,
            siaNumber = epharmacyInfo.siaNumber,
            sipaNumber = epharmacyInfo.sipaNumber,
            apj = epharmacyInfo.apj,
            partnerLabel = partnerLabel,
            fsType = partnerInfo.fsType,
            partnerName = partnerInfo.partnerName
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
    // endregion

    // start GetMessageId
    private suspend fun onGetMessageIdChat_thenReturn(shopMessageChat: ChatExistingChat) {
        coEvery { getMessageIdChatUseCase.executeOnBackground() } returns shopMessageChat
    }

    private suspend fun onGetMessageIdChat_thenReturn_Error() {
        coEvery { getMessageIdChatUseCase.executeOnBackground() } throws Exception()
    }

    private fun isLoginSession_returnTrue() {
        every { userSessionInterface.isLoggedIn } returns true
    }

    private fun isLoginSession_returnFalse() {
        every { userSessionInterface.isLoggedIn } returns false
    }
    // end GetMessageId
}
