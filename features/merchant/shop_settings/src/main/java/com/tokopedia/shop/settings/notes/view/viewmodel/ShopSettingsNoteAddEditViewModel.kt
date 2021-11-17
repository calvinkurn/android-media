package com.tokopedia.shop.settings.notes.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.domain.AddShopNoteUseCase
import com.tokopedia.shop.settings.notes.view.domain.UpdateShopNoteUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopSettingsNoteAddEditViewModel @Inject constructor (
    private val addShopNoteUseCase: AddShopNoteUseCase,
    private val editShopNoteUseCase: UpdateShopNoteUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val saveNote: LiveData<Result<String>>
        get() = _saveNote

    private val _saveNote = MutableLiveData<Result<String>>()

    fun saveNote(shopNoteModel: ShopNoteUiModel, isEdit: Boolean) {
        if (isEdit) {
            editShopNote(
                id = shopNoteModel.id.orEmpty(),
                title = shopNoteModel.title.orEmpty(),
                content = shopNoteModel.content.orEmpty()
            )
        } else {
            addShopNote(
                title = shopNoteModel.title.orEmpty(),
                content = shopNoteModel.content.orEmpty(),
                isTerms = shopNoteModel.terms
            )
        }
    }


    private fun addShopNote(title: String, content: String, isTerms: Boolean) {
        launchCatchError(context = dispatchers.io, block = {
            val data = addShopNoteUseCase.execute(title, content, isTerms)
            _saveNote.postValue(Success(data))
        }) {
            _saveNote.postValue(Fail(it))
        }
    }

    private fun editShopNote(id: String, title: String, content: String) {
        launchCatchError(context = dispatchers.io, block = {
            val data = editShopNoteUseCase.execute(id, title, content)
            _saveNote.postValue(Success(data))
        }) {
            _saveNote.postValue(Fail(it))
        }
    }
}
