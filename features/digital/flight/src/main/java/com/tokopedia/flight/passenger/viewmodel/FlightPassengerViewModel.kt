package com.tokopedia.flight.passenger.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.common.travel.domain.GetContactListUseCase
import com.tokopedia.common.travel.domain.GetPhoneCodeByIdUseCase
import com.tokopedia.common.travel.domain.UpsertContactListUseCase
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 2019-09-05
 */

class FlightPassengerViewModel @Inject constructor(private val getContactListUseCase: GetContactListUseCase,
                                                 private val upsertContactListUseCase: UpsertContactListUseCase,
                                                   private val getPhoneCodeByIdUseCase: GetPhoneCodeByIdUseCase,
                                                 dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
    val contactListResult = MutableLiveData<List<TravelContactListModel.Contact>>()
    var nationalityData = MutableLiveData<CountryPhoneCode>()
    var passportIssuerCountryData = MutableLiveData<CountryPhoneCode>()

    fun getContactList(query: String, filterType: String = "") {
        launch {
            contactListResult.value = getContactListUseCase.execute(query = query,
                    filterType = filterType,
                    product = GetContactListUseCase.PARAM_PRODUCT_FLIGHT)
        }
    }

    fun updateContactList(query: String, contact: TravelContactListModel.Contact,
                          updatedContact: TravelUpsertContactModel.Contact) {
        launch {
            if (!updatedContact.equals(contact)) {
                upsertContactListUseCase.execute(query,
                        TravelUpsertContactModel(updateLastUsedProduct = UpsertContactListUseCase.PARAM_TRAVEL_FLIGHT,
                                contacts = listOf(updatedContact)))
            }
        }
    }

    fun getNationalityById(paramId: String) {
        launch(Dispatchers.IO) {
            nationalityData.postValue(getPhoneCodeByIdUseCase.execute(paramId))
        }
    }

    fun getPassportIssuerCountryById(paramId: String) {
        launch(Dispatchers.IO) {
            passportIssuerCountryData.postValue(getPhoneCodeByIdUseCase.execute(paramId))
        }
    }
}