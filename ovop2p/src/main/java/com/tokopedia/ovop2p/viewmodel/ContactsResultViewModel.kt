package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

import com.tokopedia.ovop2p.model.ContactsResult

class ContactsResultViewModel(application: Application) : AndroidViewModel(application) {

    var contactsResultMutableLiveData: MutableLiveData<ContactsResult>? = null

    override fun onCleared() {
        super.onCleared()
    }
}
