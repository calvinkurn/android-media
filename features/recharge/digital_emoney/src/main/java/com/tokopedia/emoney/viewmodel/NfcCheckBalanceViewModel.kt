package com.tokopedia.emoney.viewmodel

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class NfcCheckBalanceViewModel @Inject constructor(val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val intentFromNfc = MutableLiveData<Intent>()

    fun setIntentFromNfc(intent: Intent) {
        this.intentFromNfc.value = intent
    }
}