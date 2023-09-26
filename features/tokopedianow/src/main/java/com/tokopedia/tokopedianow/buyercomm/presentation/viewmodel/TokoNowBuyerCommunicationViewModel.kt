package com.tokopedia.tokopedianow.buyercomm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.buyercomm.domain.mapper.BuyerCommunicationMapper
import com.tokopedia.tokopedianow.buyercomm.domain.usecase.GetBuyerCommunicationUseCase
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import javax.inject.Inject

class TokoNowBuyerCommunicationViewModel @Inject constructor(
    private val getBuyerCommunicationUseCase: GetBuyerCommunicationUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val buyerCommunicationData: LiveData<BuyerCommunicationData>
        get() = _buyerCommunicationData

    private val _buyerCommunicationData = MutableLiveData<BuyerCommunicationData>()

    fun onViewCreated(data: BuyerCommunicationData?) {
        if (data == null) {
            getBuyerCommunicationData()
        } else {
            updateBuyerCommunicationData(data)
        }
    }

    fun getWarehousesData(): List<WarehouseData> {
        return addressData.getWarehousesData()
    }

    private fun getBuyerCommunicationData() {
        launchCatchError(block = {
            val response = getBuyerCommunicationUseCase.execute(addressData)
            val data = BuyerCommunicationMapper.mapToBuyerCommunicationData(response)
            updateBuyerCommunicationData(data)
        }) {
        }
    }

    private fun updateBuyerCommunicationData(data: BuyerCommunicationData) {
        _buyerCommunicationData.postValue(data)
    }
}
