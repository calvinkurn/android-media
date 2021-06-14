package com.tokopedia.shop.note.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.domain.GetShopNoteUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopNoteBottomSheetViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val getShopNoteUseCase: GetShopNoteUseCase,
): BaseViewModel(dispatcherProvider.main) {

    private val _shopNotes = MutableLiveData<Result<List<ShopNoteModel>>>()
    val shopNotes: LiveData<Result<List<ShopNoteModel>>> = _shopNotes

    fun getShopNotes(shopId: String) {
        launchCatchError(block = {
            val shopNotes = withContext(dispatcherProvider.io) {
                getShopNoteUseCase.params = GetShopNoteUseCase.createParams(shopId)
                getShopNoteUseCase.isFromCacheFirst = false
                getShopNoteUseCase.executeOnBackground()
            }
            _shopNotes.value = Success(shopNotes)
        }){
            _shopNotes.value = Fail(it)
        }
    }

}