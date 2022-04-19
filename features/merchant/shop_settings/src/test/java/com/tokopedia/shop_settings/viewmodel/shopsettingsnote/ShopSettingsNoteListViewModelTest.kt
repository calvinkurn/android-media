package com.tokopedia.shop_settings.viewmodel.shopsettingsnote

import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ShopSettingsNoteListViewModelTest: ShopSettingsNoteViewModelTestFixture() {

    private val id = "102"

    @Test
    fun `get shop note should be success and return data`() {
        runBlocking {
            onGetShopNote_thenReturnData(listOf(ShopNoteModel()))

            shopSettingsNoteListViewModel.getShopNote()

            verifyGetShopNoteCalled()

            Assert.assertEquals(Success(listOf(ShopNoteModel())), shopSettingsNoteListViewModel.getNote.value)
        }
    }

    @Test
    fun `get shop note should return exception`() {
        runBlocking {
            onGetShopNote_thenReturnException()

            shopSettingsNoteListViewModel.getShopNote()

            verifyGetShopNoteCalled()

            Assert.assertTrue(shopSettingsNoteListViewModel.getNote.value is Fail)
        }
    }

    @Test
    fun `delete shop note should be success and return data`() {
        runBlocking {
            val result = "Success"

            onDeleteShopNote_thenReturnData(id, result)

            shopSettingsNoteListViewModel.deleteShopNote(id)

            verifyDeleteShopNoteCalled()

            Assert.assertEquals(Success(result), shopSettingsNoteListViewModel.deleteNote.value)
        }
    }

    @Test
    fun `delete shop note should return exception`() {
        runBlocking {
            onDeleteShopNote_thenReturnException(id)

            shopSettingsNoteListViewModel.deleteShopNote(id)

            verifyDeleteShopNoteCalled()

            Assert.assertTrue(shopSettingsNoteListViewModel.deleteNote.value is Fail)
        }
    }
}