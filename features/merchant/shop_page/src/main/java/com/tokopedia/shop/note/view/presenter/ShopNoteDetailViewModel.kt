package com.tokopedia.shop.note.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/8/18.
 */
class ShopNoteDetailViewModel @Inject constructor(
        private val getShopNoteUseCase: GetShopNoteUseCase,
        private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    val shopNoteDetailData: LiveData<Result<ShopNoteModel>>
        get() = _shopNoteDetailData
    private val _shopNoteDetailData = MutableLiveData<Result<ShopNoteModel>>()

    fun getShopNoteList(shopId: String, noteId: String) {
        launchCatchError(dispatcherProvider.io,block = {
            getShopNoteUseCase.params = GetShopNoteUseCase.createParams(shopId, noteId)
            val shopNoteDetailData = getShopNoteUseCase.executeOnBackground()
            _shopNoteDetailData.postValue(Success(shopNoteDetailData.first()))
        }){
            _shopNoteDetailData.postValue(Fail(it))
        }
    }

}