package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class ShopInfoViewModel @Inject constructor(private val userSessionInterface: UserSessionInterface,
                                            private val getShopNoteUseCase: GetShopNotesByShopIdUseCase,
                                            dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    private suspend fun getShopNotes(shopId: String, isRefresh: Boolean) = async(Dispatchers.IO) {
        getShopNoteUseCase.params = GetShopNotesByShopIdUseCase.createParams(shopId)
        getShopNoteUseCase.isFromCacheFirst = !isRefresh
        try {
            Success(getShopNoteUseCase.executeOnBackground())
        } catch (t: Throwable){
            Fail(t)
        }
    }
}