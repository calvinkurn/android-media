package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.DataAddAddress
import com.tokopedia.logisticCommon.data.response.KeroDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.add_address.AddAddressResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddressFormViewModel @Inject constructor(private val repo: KeroRepository,
                            private val getDistrictMapper: GetDistrictMapper) : ViewModel() {

    private val _districtDetail = MutableLiveData<Result<KeroDistrictRecommendation>>()
    val districtDetail: LiveData<Result<KeroDistrictRecommendation>>
        get() = _districtDetail

    private val _saveAddress = MutableLiveData<Result<DataAddAddress>>()
    val saveAddress: LiveData<Result<DataAddAddress>>
        get() = _saveAddress

    fun getDistrictDetail(districtId: String) {
        viewModelScope.launch {
            try {
                val districtDetail = repo.getZipCode(districtId)
                _districtDetail.value = Success(districtDetail.keroDistrictDetails)
            } catch (e: Throwable) {
                _districtDetail.value = Fail(e)
            }
        }
    }

    fun saveAddress(model: SaveAddressDataModel) {
        viewModelScope.launch {
            try {
                val saveAddressData = repo.saveAddress(model)
                _saveAddress.value = Success(saveAddressData.keroAddAddress.data)
            } catch (e: Throwable) {
                _saveAddress.value = Fail(e)
            }
        }
    }
}