package com.tokopedia.shop_settings.viewmodel.shopsettingsnote

import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ShopSettingsNoteListReorderViewModelTest: ShopSettingsNoteViewModelTestFixture() {

    @Test
    fun `reorder shop note should be success and return data`() {
        runBlocking {
            val result = "Success"

            onReorderShopNote_thenReturnData(arrayListOf(), result)

            shopSettingsNoteListReorderViewModel.reorderShopNote(arrayListOf())

            verifyReorderShopNoteCalled()

            Assert.assertEquals(Success(result), shopSettingsNoteListReorderViewModel.reorderNote.value)
        }
    }

    @Test
    fun `reorder shop note should return exception`() {
        runBlocking {
            onReorderShopNote_thenReturnException(arrayListOf())

            shopSettingsNoteListReorderViewModel.reorderShopNote(arrayListOf())

            verifyReorderShopNoteCalled()

            Assert.assertTrue(shopSettingsNoteListReorderViewModel.reorderNote.value is Fail)
        }
    }

}