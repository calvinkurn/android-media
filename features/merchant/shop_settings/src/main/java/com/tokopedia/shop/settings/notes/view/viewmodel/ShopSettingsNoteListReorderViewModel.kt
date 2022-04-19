package com.tokopedia.shop.settings.notes.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.notes.view.domain.DeleteShopNoteUseCase
import com.tokopedia.shop.settings.notes.view.domain.GetShopNoteUseCase
import com.tokopedia.shop.settings.notes.view.domain.ReorderShopNoteUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopSettingsNoteListReorderViewModel @Inject constructor (
    private val reorderShopNoteUseCase: ReorderShopNoteUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val reorderNote: LiveData<Result<String>>
        get() = _reorderNote

    private val _reorderNote = MutableLiveData<Result<String>>()

    fun reorderShopNote(noteIdList: ArrayList<String>) {
        launchCatchError(context = dispatchers.io, block = {
            val data = reorderShopNoteUseCase.execute(noteIdList)
            _reorderNote.postValue(Success(data))
        }) {
            _reorderNote.postValue(Fail(it))
        }
    }
}