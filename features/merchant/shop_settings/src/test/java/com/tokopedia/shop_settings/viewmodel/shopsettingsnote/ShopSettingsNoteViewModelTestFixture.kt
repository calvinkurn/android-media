package com.tokopedia.shop_settings.viewmodel.shopsettingsnote

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.notes.view.domain.*
import com.tokopedia.shop.settings.notes.view.viewmodel.ShopSettingsNoteAddEditViewModel
import com.tokopedia.shop.settings.notes.view.viewmodel.ShopSettingsNoteListReorderViewModel
import com.tokopedia.shop.settings.notes.view.viewmodel.ShopSettingsNoteListViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule

abstract class ShopSettingsNoteViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var addShopNoteUseCase: AddShopNoteUseCase

    @RelaxedMockK
    lateinit var updateShopNoteUseCase: UpdateShopNoteUseCase

    @RelaxedMockK
    lateinit var getShopNoteUseCase: GetShopNoteUseCase

    @RelaxedMockK
    lateinit var deleteShopNoteUseCase: DeleteShopNoteUseCase

    @RelaxedMockK
    lateinit var reorderShopNoteUseCase: ReorderShopNoteUseCase

    protected lateinit var shopSettingsNoteAddEditViewModel: ShopSettingsNoteAddEditViewModel
    protected lateinit var shopSettingsNoteListViewModel: ShopSettingsNoteListViewModel
    protected lateinit var shopSettingsNoteListReorderViewModel: ShopSettingsNoteListReorderViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(TextUtils::class)

        shopSettingsNoteAddEditViewModel = ShopSettingsNoteAddEditViewModel(
            addShopNoteUseCase,
            updateShopNoteUseCase,
            CoroutineTestDispatchersProvider
        )

        shopSettingsNoteListViewModel = ShopSettingsNoteListViewModel(
            getShopNoteUseCase,
            deleteShopNoteUseCase,
            CoroutineTestDispatchersProvider
        )

        shopSettingsNoteListReorderViewModel = ShopSettingsNoteListReorderViewModel(
            reorderShopNoteUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun onGetShopNote_thenReturnData(list: List<ShopNoteModel>) {
        coEvery {
            getShopNoteUseCase.execute()
        } returns list
    }

    protected fun onDeleteShopNote_thenReturnData(id: String, result: String) {
        coEvery {
            deleteShopNoteUseCase.execute(id)
        } returns result
    }

    protected fun onReorderShopNote_thenReturnData(idList: ArrayList<String>, result: String) {
        coEvery {
            reorderShopNoteUseCase.execute(idList)
        } returns result
    }

    protected fun onAddShopNote_thenReturnData(title: String, content: String, isTerms: Boolean, result: String) {
        coEvery {
            addShopNoteUseCase.execute(title, content, isTerms)
        } returns result
    }

    protected fun onUpdateShopNote_thenReturnData(id: String, title: String, content: String, result: String) {
        coEvery {
            updateShopNoteUseCase.execute(id, title, content)
        } returns result
    }

    protected fun onGetShopNote_thenReturnException() {
        coEvery {
            getShopNoteUseCase.execute()
        } throws Exception()
    }

    protected fun onDeleteShopNote_thenReturnException(id: String) {
        coEvery {
            deleteShopNoteUseCase.execute(id)
        } throws Exception()
    }

    protected fun onReorderShopNote_thenReturnException(idList: ArrayList<String>) {
        coEvery {
            reorderShopNoteUseCase.execute(idList)
        } throws Exception()
    }

    protected fun onAddShopNote_thenReturnException(title: String, content: String, isTerms: Boolean, result: String) {
        coEvery {
            addShopNoteUseCase.execute(title, content, isTerms)
        } throws Exception()
    }

    protected fun onUpdateShopNote_thenReturnException(id: String, title: String, content: String, result: String) {
        coEvery {
            updateShopNoteUseCase.execute(id, title, content)
        } throws Exception()
    }

    protected fun verifyGetShopNoteCalled() {
        coVerify { getShopNoteUseCase.execute() }
    }

    protected fun verifyDeleteShopNoteCalled() {
        coVerify { deleteShopNoteUseCase.execute(any()) }
    }

    protected fun verifyReorderShopNoteCalled() {
        coVerify { reorderShopNoteUseCase.execute(any()) }
    }

    protected fun verifyAddShopNoteCalled(title: String, content: String, isTerms: Boolean) {
        coVerify { addShopNoteUseCase.execute(title, content, isTerms) }
    }

    protected fun verifyUpdateShopNoteCalled(id: String, title: String, content: String) {
        coVerify { updateShopNoteUseCase.execute(id, title, content) }
    }
}