package com.tokopedia.shop.settings.notes.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.notes.view.domain.DeleteShopNoteUseCase
import com.tokopedia.shop.settings.notes.view.domain.GetShopNoteUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopSettingsNoteListViewModel @Inject constructor (
    private val getShopNotesUseCase: GetShopNoteUseCase,
    private val deleteShopNoteUseCase: DeleteShopNoteUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val getNote: LiveData<Result<List<ShopNoteModel>>>
        get() = _getNote
    val deleteNote: LiveData<Result<String>>
        get() = _deleteNote

    private val _getNote = MutableLiveData<Result<List<ShopNoteModel>>>()
    private val _deleteNote = MutableLiveData<Result<String>>()

    fun getShopNote() {
        launchCatchError(context = dispatchers.io, block = {
            val data = getShopNotesUseCase.execute()
            _getNote.postValue(Success(data))
        }) {
            _getNote.postValue(Fail(it))
        }
    }

    fun deleteShopNote(id: String) {
        launchCatchError(context = dispatchers.io, block = {
            val data = deleteShopNoteUseCase.execute(id)
            _deleteNote.postValue(Success(data))
        }) {
            _deleteNote.postValue(Fail(it))
        }
    }
}