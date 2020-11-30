package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopScheduleViewModel
@Inject constructor(
        private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val message get() = _message
    val shopBasicData get() = _shopBasicData
    private val _message = MutableLiveData<Result<String>>()
    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataModel>>()

    fun updateShopSchedule(@ShopScheduleActionDef action: Int,
                           closeNow: Boolean,
                           closeStart: String?,
                           closeEnd: String?,
                           closeNote: String) {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                updateShopScheduleUseCase.getData(
                        UpdateShopScheduleUseCase.createRequestParams(action, closeNow, closeStart, closeEnd, closeNote)
                )
            }
            _message.value = Success(data)
        }) {
            _message.value = Fail(it)
        }
    }

    fun getShopBasicData() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getShopBasicDataUseCase.getData(RequestParams.EMPTY)
            }
            _shopBasicData.value = Success(data)
        }) {
            _shopBasicData.value = Fail(it)
        }
    }
}