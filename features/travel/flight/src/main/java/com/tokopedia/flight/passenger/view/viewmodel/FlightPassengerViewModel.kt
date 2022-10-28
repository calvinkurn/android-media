package com.tokopedia.flight.passenger.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.travel.country_code.domain.TravelCountryCodeByIdUseCase
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 2019-09-05
 */

class FlightPassengerViewModel @Inject constructor(private val getContactListUseCase: GetContactListUseCase,
                                                   private val getPhoneCodeByIdUseCase: TravelCountryCodeByIdUseCase,
                                                   private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {
    val contactListResult = MutableLiveData<List<TravelContactListModel.Contact>>()
    var nationalityData = MutableLiveData<TravelCountryPhoneCode>()
    var passportIssuerCountryData = MutableLiveData<TravelCountryPhoneCode>()

    fun getContactList(query: GqlQueryInterface, filterType: String = "") {
        launch {
            val contacts = getContactListUseCase.execute(query = query,
                    filterType = filterType,
                    product = GetContactListUseCase.PARAM_PRODUCT_FLIGHT)

            contactListResult.postValue(
                    contacts.map {
                        if (it.fullName.isBlank()) {
                            it.fullName = "${it.firstName} ${it.lastName}"
                        }
                        return@map it
                    }.toMutableList()
            )
        }
    }

    fun getNationalityById(rawQuery: GqlQueryInterface, paramId: String) {
        launch(dispatcherProvider.main) {
            when (val result = getPhoneCodeByIdUseCase.execute(rawQuery, paramId)) {
                is Success -> {
                    nationalityData.postValue(result.data)
                }
            }
        }
    }

    fun getPassportIssuerCountryById(rawQuery: GqlQueryInterface, paramId: String) {
        launch(dispatcherProvider.main) {
            when (val result = getPhoneCodeByIdUseCase.execute(rawQuery, paramId)) {
                is Success -> {
                    passportIssuerCountryData.postValue(result.data)
                }
            }
        }
    }
}