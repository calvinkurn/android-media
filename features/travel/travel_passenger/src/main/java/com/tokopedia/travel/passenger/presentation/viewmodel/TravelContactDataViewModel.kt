package com.tokopedia.travel.passenger.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.travel.passenger.domain.UpsertContactListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 2019-09-13
 */

class TravelContactDataViewModel @Inject constructor(
    private val getContactListUseCase: GetContactListUseCase,
    private val upsertContactListUseCase: UpsertContactListUseCase,
    dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val contactListResult = MutableLiveData<List<TravelContactListModel.Contact>>()

    fun getContactList(query: GqlQueryInterface, travelProduct: String) {

        launch {
            val contacts = getContactListUseCase.execute(query = query,
                    product = travelProduct)
            contactListResult.value = contacts.map {
                if (it.fullName.isBlank()) {
                    it.fullName = "${it.firstName} ${it.lastName}"
                }
                return@map it
            }.toMutableList()
        }
    }

    fun updateContactList(query: GqlQueryInterface,
                          updatedContact: TravelUpsertContactModel.Contact) {
        launch {
            upsertContactListUseCase.execute(query,
                    TravelUpsertContactModel(updateLastUsedProduct = UpsertContactListUseCase.PARAM_TRAVEL_HOTEL,
                            contacts = listOf(updatedContact)))
        }
    }
}