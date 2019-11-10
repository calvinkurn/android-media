package com.tokopedia.logisticaddaddress.features.autocomplete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.data.entity.response.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.data.query.AutoCompleteQuery
import com.tokopedia.logisticaddaddress.data.query.GetAddressQuery
import com.tokopedia.logisticaddaddress.data.query.GetDistrictQuery
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.autocomplete.model.AddressResultUi
import com.tokopedia.logisticaddaddress.features.autocomplete.model.AutoCompleteResultUi
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AutoCompleteViewModel
@Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val autoCompleteUseCase: GraphqlUseCase<AutocompleteResponse>,
        private val getDistrictUseCase: GraphqlUseCase<GetDistrictResponse>,
        private val getSavedAddressUseCase: GraphqlUseCase<AddressResponse>,
        private val autoCompleteMapper: AutocompleteMapper,
        private val getDistrictMapper: GetDistrictMapper) : BaseViewModel(dispatcher) {

    private val mAutoCompleteList = MutableLiveData<Result<List<AutoCompleteResultUi>>>()
    val autoCompleteList: LiveData<Result<List<AutoCompleteResultUi>>>
        get() = mAutoCompleteList
    private val mValidatedDistrict = MutableLiveData<Result<GetDistrictDataUiModel>>()
    val validatedDistrict: LiveData<Result<GetDistrictDataUiModel>>
        get() = mValidatedDistrict
    private val mSavedAddress = MutableLiveData<Result<List<AddressResultUi>>>()
    val savedAddress: LiveData<Result<List<AddressResultUi>>>
        get() = mSavedAddress

    fun getAutoCompleteList(keyword: String) {
        autoCompleteUseCase.setTypeClass(AutocompleteResponse::class.java)
        autoCompleteUseCase.setGraphqlQuery(AutoCompleteQuery.keroAutoCompleteGeocode)
        autoCompleteUseCase.setRequestParams(mapOf(
                "param" to keyword
        ))

        autoCompleteUseCase.execute(
                {
                    mAutoCompleteList.value = Success(autoCompleteMapper.mapLean(it))
                },
                { mAutoCompleteList.value = Fail(it) })
    }

    fun getLatlng(placeId: String) {
        getDistrictUseCase.setTypeClass(GetDistrictResponse::class.java)
        getDistrictUseCase.setGraphqlQuery(GetDistrictQuery.keroPlacesGetDistrict)
        getDistrictUseCase.setRequestParams(mapOf(
                "param" to placeId
        ))

        getDistrictUseCase.execute(
                {
                    mValidatedDistrict.value = Success(getDistrictMapper.mapLean(it))
                },
                { mValidatedDistrict.value = Fail(it) }
        )
    }

    fun getSavedAddress() {
        getSavedAddressUseCase.setTypeClass(AddressResponse::class.java)
        getSavedAddressUseCase.setGraphqlQuery(GetAddressQuery.keroAddressCorner)
        getSavedAddressUseCase.setRequestParams(mapOf(
                "input" to mapOf(
                        "show_address" to true
                )
        ))

        getSavedAddressUseCase.execute(
                {
                    mSavedAddress.value = Success(autoCompleteMapper.mapAddress(it))
                },
                {
                    mSavedAddress.value = Fail(it)
                }
        )

    }

}