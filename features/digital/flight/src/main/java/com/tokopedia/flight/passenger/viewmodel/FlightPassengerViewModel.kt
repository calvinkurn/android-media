package com.tokopedia.flight.passenger.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.common.travel.domain.GetContactListUseCase
import com.tokopedia.common.travel.domain.UpsertContactListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 2019-09-05
 */

class FlightPassengerViewModel @Inject constructor(private val getContactListUseCase: GetContactListUseCase,
                                                 private val upsertContactListUseCase: UpsertContactListUseCase,
                                                 dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {
    val contactListResult = MutableLiveData<List<TravelContactListModel.Contact>>()

    fun getContactList(query: String) {
        launch {
            contactListResult.value = getContactListUseCase.execute(query = query,
                    product = GetContactListUseCase.PARAM_PRODUCT_HOTEL)
        }
    }

    fun updateContactList(query: String, contact: TravelContactListModel.Contact,
                          updatedContact: TravelUpsertContactModel.Contact) {
        launch {
            if (!updatedContact.equals(contact)) {
                upsertContactListUseCase.execute(query,
                        TravelUpsertContactModel(updateLastUsedProduct = UpsertContactListUseCase.PARAM_TRAVEL_HOTEL,
                                contacts = listOf(updatedContact)))
            }
        }
    }
}