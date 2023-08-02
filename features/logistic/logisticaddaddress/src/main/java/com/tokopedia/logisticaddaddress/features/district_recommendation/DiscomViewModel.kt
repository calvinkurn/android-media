package com.tokopedia.logisticaddaddress.features.district_recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.param.ReverseGeocodeParam
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeCoroutineUseCase
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.GetDistrictRecomParam
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendationCoroutineUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendationCoroutineUseCase.Companion.FOREIGN_COUNTRY_MESSAGE
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendationCoroutineUseCase.Companion.LOCATION_NOT_FOUND_MESSAGE
import com.tokopedia.logisticaddaddress.features.district_recommendation.uimodel.AutofillUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscomViewModel @Inject constructor(
    private val executorDispatchers: CoroutineDispatchers,
    private val reverseGeocode: RevGeocodeCoroutineUseCase,
    private val getDistrictRecommendation: GetDistrictRecommendationCoroutineUseCase,
    private val mapper: DistrictRecommendationMapper
) : BaseViewModel(executorDispatchers.main) {
    var isMapsAvailable: Boolean = true
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _districtRecommendation =
        MutableLiveData<Result<AddressResponse>>()
    val districtRecommendation: LiveData<Result<AddressResponse>>
        get() = _districtRecommendation

    private val _autofill = MutableLiveData<Result<AutofillUiModel>>()
    val autoFill: LiveData<Result<AutofillUiModel>>
        get() = _autofill

    fun loadData(query: String, page: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response =
                    getDistrictRecommendation(GetDistrictRecomParam(query, page.toString()))
                _loading.value = false
                _districtRecommendation.value = Success(mapper.transform(response))
            } catch (e: Exception) {
                _loading.value = false
                _districtRecommendation.value = Fail(e)
            }
        }
    }

    fun reverseGeoCode(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val param = ReverseGeocodeParam(latlng = "$lat,$long")
                val response = reverseGeocode(param)
                if (response.messageError.isEmpty()) {
                    val model = AutofillUiModel(response.data, lat, long)
                    _loading.value = false
                    _autofill.value = Success(model)
                } else if (response.errorCode == AddressConstants.CIRCUIT_BREAKER_ON_CODE) {
                    throw Exception("Oops, alamat gagal dipilih. Silakan coba lagi.")
                } else {
                    val msg = response.messageError[0]
                    when {
                        msg.contains(FOREIGN_COUNTRY_MESSAGE) -> {
                            throw Exception(FOREIGN_COUNTRY_MESSAGE)
                        }

                        msg.contains(LOCATION_NOT_FOUND_MESSAGE) -> {
                            throw Exception(LOCATION_NOT_FOUND_MESSAGE)
                        }
                    }
                }
            } catch (e: Exception) {
                _loading.value = false
                _autofill.value = Fail(e)
            }
        }
    }
}
