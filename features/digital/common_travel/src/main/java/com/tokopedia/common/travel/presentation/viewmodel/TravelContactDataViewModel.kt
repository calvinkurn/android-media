package com.tokopedia.common.travel.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.common.travel.domain.GetContactListUseCase
import com.tokopedia.common.travel.domain.UpsertContactListUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 2019-09-13
 */

class TravelContactDataViewModel @Inject constructor(private val getContactListUseCase: GetContactListUseCase,
                                                     private val upsertContactListUseCase: UpsertContactListUseCase,
                                                     val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val contactListResult = MutableLiveData<List<TravelContactListModel.Contact>>()

    fun getContactList(query: String, travelProduct: String) {

        launch {
            var contacts = getContactListUseCase.execute(query = query,
                    product = travelProduct)
            contactListResult.value = contacts.map {
                if (it.fullName.isBlank()) {
                    it.fullName = "${it.firstName} ${it.lastName}"
                }
                return@map it
            }.toMutableList()
        }
    }

    fun updateContactList(query: String,
                          updatedContact: TravelUpsertContactModel.Contact) {
        launch {
            upsertContactListUseCase.execute(query,
                    TravelUpsertContactModel(updateLastUsedProduct = UpsertContactListUseCase.PARAM_TRAVEL_HOTEL,
                            contacts = listOf(updatedContact)))
        }
    }
}