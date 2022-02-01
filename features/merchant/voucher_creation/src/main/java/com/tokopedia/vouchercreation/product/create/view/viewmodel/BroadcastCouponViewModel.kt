package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BroadcastCouponViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase
) : BaseViewModel(dispatchers.main) {


    private val _broadcastMetadata = MutableLiveData<Result<ChatBlastSellerMetadata>>()
    val broadcastMetadata: LiveData<Result<ChatBlastSellerMetadata>> = _broadcastMetadata

    private val _shop = MutableLiveData<Result<ShopBasicDataResult>>()
    val shop: LiveData<Result<ShopBasicDataResult>> = _shop

    fun getBroadcastMetaData() {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getBroadCastMetaDataUseCase.executeOnBackground()
                }
                _broadcastMetadata.value = Success(result)
            },
            onError = {
                _broadcastMetadata.setValue(Fail(it))
            }
        )
    }

    fun getShopDetail() {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getShopBasicDataUseCase.executeOnBackground()
                }
                _shop.value = Success(result)
            },
            onError = {
                _shop.setValue(Fail(it))
            }
        )
    }

}