package com.tokopedia.shop_settings.viewmodel.shopsettingsnote

import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ShopSettingsNoteAddEditViewModelTest: ShopSettingsNoteViewModelTestFixture() {

    private val id = "12212"
    private val title = "hello"
    private val content = "whats up"
    private val isTerms = false
    private val result = "Success"

    @Test
    fun `add shop note should be success and return data`() {
        runBlocking {
            onAddShopNote_thenReturnData(title, content, isTerms, result)

            val model = ShopNoteUiModel()
            model.title = title
            model.content = content
            model.terms = isTerms

            shopSettingsNoteAddEditViewModel.saveNote(
                shopNoteModel = model,
                isEdit = false
            )

            verifyAddShopNoteCalled(title, content, isTerms)

            Assert.assertEquals(Success(result), shopSettingsNoteAddEditViewModel.saveNote.value)
        }
    }

    @Test
    fun `add shop note should return exception`() {
        runBlocking {
            onAddShopNote_thenReturnException("", "", isTerms, result)

            val model = ShopNoteUiModel()
            model.title = null
            model.content = null
            model.terms = false

            shopSettingsNoteAddEditViewModel.saveNote(
                shopNoteModel = model,
                isEdit = false
            )

            verifyAddShopNoteCalled("", "", isTerms)

            Assert.assertTrue(shopSettingsNoteAddEditViewModel.saveNote.value is Fail)
        }
    }

    @Test
    fun `update shop note should be success and return data`() {
        runBlocking {
            onUpdateShopNote_thenReturnData(id, title, content, result)

            val model = ShopNoteUiModel(
                ShopNoteModel(
                    id = id,
                    title = title,
                    content = content)
            )

            shopSettingsNoteAddEditViewModel.saveNote(
                shopNoteModel = model,
                isEdit = true
            )

            verifyUpdateShopNoteCalled(id, title, content)

            Assert.assertEquals(Success(result), shopSettingsNoteAddEditViewModel.saveNote.value)
        }
    }

    @Test
    fun `update shop note should return exception`() {
        runBlocking {
            onUpdateShopNote_thenReturnException("", "", "", result)

            val model = ShopNoteUiModel(
                ShopNoteModel(
                    id = null,
                    title = null,
                    content = null)
            )

            shopSettingsNoteAddEditViewModel.saveNote(
                shopNoteModel = model,
                isEdit = true
            )

            verifyUpdateShopNoteCalled("", "", "")

            Assert.assertTrue(shopSettingsNoteAddEditViewModel.saveNote.value is Fail)
        }
    }

}