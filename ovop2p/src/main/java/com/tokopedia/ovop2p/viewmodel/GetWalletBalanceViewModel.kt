package com.tokopedia.ovop2p.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

import com.tokopedia.ovop2p.model.WalletDataBase

class GetWalletBalanceViewModel(application: Application) : AndroidViewModel(application) {
    var walletLiveData: MutableLiveData<WalletDataBase>? = null
}
