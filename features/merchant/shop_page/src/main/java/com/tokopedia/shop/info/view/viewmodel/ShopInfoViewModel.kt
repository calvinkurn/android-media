package com.tokopedia.shop.info.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.shop.common.constant.ShopPartnerFsFullfillmentServiceTypeDef
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.domain.GetMessageIdChatUseCase
import com.tokopedia.shop.common.domain.GetShopReputationUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_INFO_SOURCE
import com.tokopedia.shop.common.graphql.data.shopinfo.ChatExistingChat
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.info.data.response.GetNearestEpharmacyWarehouseLocationResponse
import com.tokopedia.shop.info.domain.usecase.GetEpharmacyShopInfoUseCase
import com.tokopedia.shop.info.domain.usecase.GetNearestEpharmacyWarehouseLocationUseCase
import com.tokopedia.shop.info.view.model.ShopEpharmacyDetailData
import com.tokopedia.shop_widget.note.view.model.ShopNoteUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopInfoViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val getShopNoteUseCase: GetShopNoteUseCase,
    private val getShopInfoUseCase: GQLGetShopInfoUseCase,
    private val getShopReputationUseCase: GetShopReputationUseCase,
    private val getMessageIdChatUseCase: GetMessageIdChatUseCase,
    private val getEpharmacyShopInfoUseCase: GetEpharmacyShopInfoUseCase,
    private val getNearestEpharmacyWarehouseLocationUseCase: GetNearestEpharmacyWarehouseLocationUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatchers
) : BaseViewModel(coroutineDispatcherProvider.main) {

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId
    fun userId(): String = userSessionInterface.userId
    private fun isUserLogin(): Boolean = userSessionInterface.isLoggedIn

    val shopNotesResp = MutableLiveData<Result<List<ShopNoteUiModel>>>()
    val shopInfo = MutableLiveData<Result<ShopInfoData>>()
    val shopBadgeReputation = MutableLiveData<Result<ShopBadge>>()
    val messageIdOnChatExist = MutableLiveData<Result<String>>()

    private val _epharmDetailData = MutableLiveData<Result<ShopEpharmacyDetailData>>()
    val epharmDetailData: LiveData<Result<ShopEpharmacyDetailData>>
        get() = _epharmDetailData

    fun getShopInfo(shopId: String) {
        launchCatchError(block = {
            coroutineScope {
                val getShopInfo = withContext(coroutineDispatcherProvider.io) {
                    val shopIdParams = listOf(shopId.toIntOrZero())

                    getShopInfoUseCase.isFromCacheFirst = false
                    getShopInfoUseCase.params = GQLGetShopInfoUseCase
                        .createParams(shopIdParams, source = SHOP_INFO_SOURCE)

                    getShopInfoUseCase.executeOnBackground()
                }

                val shopInfoData = getShopInfo.mapToShopInfoData()
                shopInfo.postValue(Success(shopInfoData))
            }
        }, onError = { error ->
                shopInfo.postValue(Fail(error))
            })
    }

    fun getShopNotes(shopId: String) {
        launchCatchError(block = {
            coroutineScope {
                val shopNotes = withContext(coroutineDispatcherProvider.io) {
                    getShopNoteUseCase.params = GetShopNoteUseCase.createParams(shopId)
                    getShopNoteUseCase.isFromCacheFirst = false
                    Success(
                        getShopNoteUseCase.executeOnBackground().map {
                            ShopNoteUiModel().apply {
                                shopNoteId = it.id?.toLongOrNull() ?: 0
                                title = it.title
                                position = it.position.toLong()
                                url = it.url
                                lastUpdate = it.updateTime
                            }
                        }
                    )
                }
                shopNotesResp.postValue(shopNotes)
            }
        }) {
            shopNotesResp.postValue(Fail(it))
        }
    }

    fun getNearestEpharmWarehouseLocation(shopId: Long, districtId: Int) {
        launchCatchError(block = {
            val nearestEpharmWarehouseData = withContext(coroutineDispatcherProvider.io) {
                getNearestEpharmacyWarehouseLocationUseCase.params = GetNearestEpharmacyWarehouseLocationUseCase.createParams(shopId = shopId, districtId = districtId.toLong())
                getNearestEpharmacyWarehouseLocationUseCase.executeOnBackground()
            }
            val dataLocation = nearestEpharmWarehouseData.getNearestEpharmacyWarehouseLocation.data
            getShopGoApotikData(shopId = shopId, dataLocation = dataLocation)
        }, onError = { })
    }

    private fun getShopGoApotikData(shopId: Long, dataLocation: GetNearestEpharmacyWarehouseLocationResponse.NearestEpharmacyData.GetNearestEpharmacyWarehouseLocationDetailData) {
        launchCatchError(block = {
            val shopEpharmData = withContext(coroutineDispatcherProvider.io) {
                getEpharmacyShopInfoUseCase.params = GetEpharmacyShopInfoUseCase.createParams(shopId, dataLocation.warehouseID)
                getEpharmacyShopInfoUseCase.executeOnBackground()
            }
            val shopEpharmDetailData = shopEpharmData.getEpharmacyShopInfo
            _epharmDetailData.postValue(
                Success(
                    ShopEpharmacyDetailData(
                        gMapsUrl = dataLocation.gMapsURL,
                        address = dataLocation.address,
                        errorCode = shopEpharmDetailData.header.errorCode,
                        errMessages = shopEpharmDetailData.header.errorMessage,
                        apj = shopEpharmDetailData.dataEpharm.apj,
                        siaNumber = shopEpharmDetailData.dataEpharm.siaNumber,
                        sipaNumber = shopEpharmDetailData.dataEpharm.sipaNumber,
                        epharmacyWorkingHoursFmt = shopEpharmDetailData.dataEpharm.epharmacyWorkingHoursFmt
                    )
                )
            )
        }, onError = { error ->
                _epharmDetailData.postValue(Fail(error))
            })
    }

    fun getShopReputationBadge(shopId: String) {
        launchCatchError(coroutineDispatcherProvider.io, block = {
            shopBadgeReputation.postValue(Success(getShopReputation(shopId)))
        }) {
            shopBadgeReputation.postValue(Fail(it))
        }
    }

    private suspend fun getShopReputation(shopId: String): ShopBadge {
        getShopReputationUseCase.params = GetShopReputationUseCase.createParams(shopId.toIntOrZero())
        return getShopReputationUseCase.executeOnBackground()
    }

    fun getMessageIdOnChatExist(shopId: String) {
        if (!isUserLogin()) {
            messageIdOnChatExist.value = Fail(UserNotLoginException())
            return
        }

        launchCatchError(coroutineDispatcherProvider.io, block = {
            messageIdOnChatExist.postValue(Success(getMessageId(shopId).chatExistingChat.messageId))
        }) {
            messageIdOnChatExist.postValue(Fail(it))
        }
    }

    fun isShouldShowLicenseForDrugSeller(isGoApotik: Boolean, fsType: Int): Boolean = isGoApotik || fsType == ShopPartnerFsFullfillmentServiceTypeDef.EPHARMACY

    private suspend fun getMessageId(shopId: String): ChatExistingChat {
        getMessageIdChatUseCase.params = GetMessageIdChatUseCase.createParams(shopId)
        return getMessageIdChatUseCase.executeOnBackground()
    }
}
