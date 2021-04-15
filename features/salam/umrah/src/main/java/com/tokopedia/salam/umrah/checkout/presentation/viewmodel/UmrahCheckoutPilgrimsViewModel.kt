package com.tokopedia.salam.umrah.checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by firman on 20/12/2019
 */

class UmrahCheckoutPilgrimsViewModel @Inject constructor(val getContactListUseCase: GetContactListUseCase, coroutineDispatcher: CoroutineDispatchers)
    : BaseViewModel(coroutineDispatcher.main) {
    private val contactListResultMutable = MutableLiveData<List<TravelContactListModel.Contact>>()
    val contactListResult: LiveData<List<TravelContactListModel.Contact>>
        get() = contactListResultMutable

    fun getContactList(query: String, type: String = "ADULT") {
        launch {
            var contacts = getContactListUseCase.execute(query = query,
                    filterType = type,
                    product = GetContactListUseCase.PARAM_PRODUCT_HOTEL)
            contactListResultMutable.value = contacts.map {
                if (it.fullName.isBlank()) {
                    it.fullName = "${it.firstName} ${it.lastName}"
                }
                return@map it
            }.toMutableList()
        }
    }
}
