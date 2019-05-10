package com.tokopedia.shop.info.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class ShopInfoViewModel @Inject constructor(private val userSessionInterface: UserSessionInterface,
                                            private val getShopNoteUseCase: GetShopNotesByShopIdUseCase,
                                            private val getShopStatisticUseCase: GetShopStatisticUseCase,
                                            dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val shopNotesResp = MutableLiveData<Result<List<ShopNoteViewModel>>>()
    val shopStatisticsResp = MutableLiveData<ShopStatisticsResp>()

    private suspend fun getShopNotesAsync(shopId: String, isRefresh: Boolean) = async(Dispatchers.IO) {
        getShopNoteUseCase.params = GetShopNotesByShopIdUseCase.createParams(shopId)
        getShopNoteUseCase.isFromCacheFirst = !isRefresh
        try {
            Success(getShopNoteUseCase.executeOnBackground().map {
                ShopNoteViewModel().apply {
                    shopNoteId = it.id?.toLongOrNull() ?: 0
                    title = it.title
                    position = it.position.toLong()
                    url = it.url
                    lastUpdate = it.updateTime
                }
            })
        } catch (t: Throwable){
            Fail(t)
        }
    }

    private suspend fun getShopStatisticsAsync(shopId: String, isRefresh: Boolean) = async(Dispatchers.IO) {
        getShopStatisticUseCase.params = GetShopStatisticUseCase.createParams(shopId.toInt())
        getShopStatisticUseCase.isFromCacheFirst = !isRefresh
        getShopStatisticUseCase.executeOnBackground()
    }

    fun getShopInfo(shopId: String, isRefresh: Boolean = false){
        launchCatchError(block = {
            shopNotesResp.value = getShopNotesAsync(shopId, isRefresh).await()
            shopStatisticsResp.value = getShopStatisticsAsync(shopId, isRefresh).await()
        }){}
    }
}