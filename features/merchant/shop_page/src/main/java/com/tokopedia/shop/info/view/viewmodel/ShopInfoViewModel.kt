package com.tokopedia.shop.info.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesByShopIdUseCase
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class ShopInfoViewModel @Inject constructor(private val userSessionInterface: UserSessionInterface,
                                            private val getShopNoteUseCase: GetShopNotesByShopIdUseCase,
                                            private val getShopStatisticUseCase: GetShopStatisticUseCase,
                                            private val getShopReputationUseCase: GetShopReputationUseCase,
                                            dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val shopNotesResp = MutableLiveData<Result<List<ShopNoteViewModel>>>()
    val shopStatisticsResp = MutableLiveData<ShopStatisticsResp>()

    private suspend fun getShopNotes(shopId: String, isRefresh: Boolean): Result<List<ShopNoteViewModel>>{
        getShopNoteUseCase.params = GetShopNotesByShopIdUseCase.createParams(shopId)
        getShopNoteUseCase.isFromCacheFirst = !isRefresh
        try {
           return Success(getShopNoteUseCase.executeOnBackground().map {
                ShopNoteViewModel().apply {
                    shopNoteId = it.id?.toLongOrNull() ?: 0
                    title = it.title
                    position = it.position.toLong()
                    url = it.url
                    lastUpdate = it.updateTime
                }
            })
        } catch (t: Throwable){
            return Fail(t)
        }
    }

    private suspend fun getShopStatistics(shopId: String, isRefresh: Boolean) : ShopStatisticsResp {
        getShopStatisticUseCase.params = GetShopStatisticUseCase.createParams(shopId.toInt())
        getShopStatisticUseCase.isFromCacheFirst = !isRefresh
        return getShopStatisticUseCase.executeOnBackground()
    }

    fun getShopInfo(shopId: String, isRefresh: Boolean = false){
        launchCatchError(block = {
            coroutineScope{
                launch(Dispatchers.IO) {
                    shopNotesResp.postValue(getShopNotes(shopId, isRefresh))
                }
                val getShopStatisticRespAsync = async(Dispatchers.IO) {
                    getShopStatistics(shopId, isRefresh)
                }
                val getShopReputationRespAsync = async(Dispatchers.IO) {
                    getShopReputation(shopId)
                }
                shopStatisticsResp.postValue(concatResp(
                        getShopStatisticRespAsync.await(),
                        getShopReputationRespAsync.await())
                )
            }
        }){}
    }

    private fun concatResp(shopStatisticsResp: ShopStatisticsResp, shopBadge: ShopBadge?): ShopStatisticsResp {
        return shopStatisticsResp.copy(shopReputation = shopBadge)
    }

    private suspend fun getShopReputation(shopId: String) : ShopBadge?{
        getShopReputationUseCase.params = GetShopReputationUseCase.createParams(shopId.toInt())
        return try {
            getShopReputationUseCase.executeOnBackground()
        } catch (t: Throwable){
            null
        }
    }
}