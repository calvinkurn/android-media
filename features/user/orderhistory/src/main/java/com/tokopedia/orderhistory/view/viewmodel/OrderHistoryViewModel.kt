package com.tokopedia.orderhistory.view.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class OrderHistoryViewModel @Inject constructor() : ViewModel() {

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
    }

}