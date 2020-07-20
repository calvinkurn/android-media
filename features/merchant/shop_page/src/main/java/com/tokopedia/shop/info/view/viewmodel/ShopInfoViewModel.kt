package com.tokopedia.shop.info.view.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_INFO_SOURCE
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
                                            private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                            private val getShopStatisticUseCase: GetShopStatisticUseCase,
                                            private val getShopReputationUseCase: GetShopReputationUseCase,
                                            dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val shopNotesResp = MutableLiveData<Result<List<ShopNoteViewModel>>>()
    val shopStatisticsResp = MutableLiveData<ShopStatisticsResp>()
    val shopInfo = MutableLiveData<ShopInfoData>()
    val shopImageSave: LiveData<String> get() = _shopImageSave

    private val _shopImageSave = MutableLiveData<String>()

    fun saveShopImageToPhoneStorage(context: Context?, shopInfoData: ShopInfoData) {
        launchCatchError(Dispatchers.IO, {
            ImageHandler.loadImageWithTarget(context, shopInfoData.shopSnippetUrl, object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedFile = ImageUtils.writeImageToTkpdPath(
                            ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE,
                            resource,
                            true
                    )
                    if(savedFile != null) {
                        _shopImageSave.value = savedFile.absolutePath
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // no op
                }
            })
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getShopInfo(shopId: String) {
        launchCatchError(block = {
            coroutineScope{
                val getShopInfo = withContext(Dispatchers.IO) {
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
                val shopNotes = withContext(Dispatchers.IO) {
                    getShopNoteUseCase.params = GetShopNotesByShopIdUseCase.createParams(shopId)
                    getShopNoteUseCase.isFromCacheFirst = false

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

                shopNotesResp.postValue(shopNotes)
            }
        }){}
    }

    fun getShopStats(shopId: String) {
        launchCatchError(block = {
            coroutineScope{
                val getShopStatisticRespAsync = async(Dispatchers.IO) {
                    getShopStatistics(shopId)
                }
                val getShopReputationRespAsync = async(Dispatchers.IO) {
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