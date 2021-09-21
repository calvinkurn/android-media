package com.tokopedia.shop.info.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_INFO_SOURCE
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.domain.GetShopReputationUseCase
import com.tokopedia.shop.common.domain.GetShopNoteUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class ShopInfoViewModel @Inject constructor(private val userSessionInterface: UserSessionInterface,
                                            private val getShopNoteUseCase: GetShopNoteUseCase,
                                            private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                            private val getShopStatisticUseCase: GetShopStatisticUseCase,
                                            private val getShopReputationUseCase: GetShopReputationUseCase,
                                            private val coroutineDispatcherProvider: CoroutineDispatchers
): BaseViewModel(coroutineDispatcherProvider.main){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val shopNotesResp = MutableLiveData<Result<List<ShopNoteUiModel>>>()
    val shopStatisticsResp = MutableLiveData<ShopStatisticsResp>()
    val shopInfo = MutableLiveData<ShopInfoData>()
    val shopBadgeReputation = MutableLiveData<Result<ShopBadge>>()

    fun getShopInfo(shopId: String) {
        launchCatchError(block = {
            coroutineScope{
                val getShopInfo = withContext(coroutineDispatcherProvider.io) {
                    val shopIdParams = listOf(shopId.toIntOrZero())

                    getShopInfoUseCase.isFromCacheFirst = false
                    getShopInfoUseCase.params = GQLGetShopInfoUseCase
                            .createParams(shopIdParams, source = SHOP_INFO_SOURCE)

                    getShopInfoUseCase.executeOnBackground()
                }

                val shopInfoData = getShopInfo.mapToShopInfoData()
                shopInfo.postValue(shopInfoData)
            }
        }){}
    }

    fun getShopNotes(shopId: String) {
        launchCatchError(block = {
            coroutineScope{
                val shopNotes = withContext(coroutineDispatcherProvider.io) {
                    getShopNoteUseCase.params = GetShopNoteUseCase.createParams(shopId)
                    getShopNoteUseCase.isFromCacheFirst = false

                    try {
                        Success(getShopNoteUseCase.executeOnBackground().map {
                            ShopNoteUiModel().apply {
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

                shopNotesResp.postValue(shopNotes)
            }
        }){}
    }

    fun getShopReputationBadge(shopId: String) {
        launchCatchError(coroutineDispatcherProvider.io, block = {
            getShopReputation(shopId)?.let {
                shopBadgeReputation.postValue(Success(it))
            }
        }) {}
    }

    fun getShopStats(shopId: String) {
        launchCatchError(block = {
            coroutineScope{
                val getShopStatisticRespAsync = async(coroutineDispatcherProvider.io) {
                    getShopStatistics(shopId)
                }
                val getShopReputationRespAsync = async(coroutineDispatcherProvider.io) {
                    getShopReputation(shopId)
                }

                shopStatisticsResp.postValue(concatResp(
                        getShopStatisticRespAsync.await(),
                        getShopReputationRespAsync.await())
                )
            }
        }) {}
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

    private suspend fun getShopStatistics(shopId: String) : ShopStatisticsResp {
        getShopStatisticUseCase.params = GetShopStatisticUseCase.createParams(shopId.toInt())
        getShopStatisticUseCase.isFromCacheFirst = false
        return getShopStatisticUseCase.executeOnBackground()
    }
}