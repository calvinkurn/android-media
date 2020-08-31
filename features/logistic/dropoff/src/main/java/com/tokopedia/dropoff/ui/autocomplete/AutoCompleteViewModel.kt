package com.tokopedia.dropoff.ui.autocomplete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.dropoff.data.query.AutoCompleteQuery
import com.tokopedia.dropoff.data.query.GetAddressQuery
import com.tokopedia.dropoff.data.query.GetDistrictQuery
import com.tokopedia.dropoff.data.response.autoComplete.AutocompleteResponse
import com.tokopedia.dropoff.data.response.getAddress.AddressResponse
import com.tokopedia.dropoff.data.response.getDistrict.GetDistrictResponse
import com.tokopedia.dropoff.domain.mapper.AutoCompleteMapper
import com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticdata.data.autocomplete.SavedAddress
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace
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
        private val mapper: AutoCompleteMapper) : BaseViewModel(dispatcher) {

    private val mAutoCompleteList = MutableLiveData<Result<List<SuggestedPlace>>>()
    val autoCompleteList: LiveData<Result<List<SuggestedPlace>>>
        get() = mAutoCompleteList
    private val mValidatedDistrict = MutableLiveData<Result<ValidatedDistrict>>()
    val validatedDistrict: LiveData<Result<ValidatedDistrict>>
        get() = mValidatedDistrict
    private val mSavedAddress = MutableLiveData<Result<List<SavedAddress>>>()
    val savedAddress: LiveData<Result<List<SavedAddress>>>
        get() = mSavedAddress

    fun getAutoCompleteList(keyword: String) {
        autoCompleteUseCase.setTypeClass(AutocompleteResponse::class.java)
        autoCompleteUseCase.setGraphqlQuery(AutoCompleteQuery.keroAutoCompleteGeocode)
        autoCompleteUseCase.setRequestParams(mapOf(
                "param" to keyword
        ))

        autoCompleteUseCase.execute(
                {
                    mAutoCompleteList.value = Success(mapper.mapAutoComplete(it))
                },
                { mAutoCompleteList.value = Fail(it) })
    }

    fun getLatLng(placeId: String) {
        getDistrictUseCase.setTypeClass(GetDistrictResponse::class.java)
        getDistrictUseCase.setGraphqlQuery(GetDistrictQuery.keroPlacesGetDistrict)
        getDistrictUseCase.setRequestParams(mapOf(
                "param" to placeId
        ))

        getDistrictUseCase.execute(
                {
                    mValidatedDistrict.value = Success(mapper.mapValidate(it))
                },
                { mValidatedDistrict.value = Fail(it) }
        )
    }

    fun getSavedAddress() {
        getSavedAddressUseCase.setTypeClass(AddressResponse::class.java)
        getSavedAddressUseCase.setGraphqlQuery(GetAddressQuery.keroAddressCorner)
        getSavedAddressUseCase.setRequestParams(mapOf(
                "input" to mapOf(
                        "show_address" to true,
                        "page" to 1,
                        "limit" to 5
                )
        ))

        getSavedAddressUseCase.execute(
                {
                    mSavedAddress.value = Success(mapper.mapAddress(it))
                },
                {
                    mSavedAddress.value = Fail(it)
                }
        )

    }

}