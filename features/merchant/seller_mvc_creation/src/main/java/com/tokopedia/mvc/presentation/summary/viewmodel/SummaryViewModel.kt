package com.tokopedia.mvc.presentation.summary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _configuration = MutableLiveData<VoucherConfiguration>()
    val configuration: LiveData<VoucherConfiguration> get() = _configuration

    private val _information = MutableLiveData<VoucherInformation>()
    val information: LiveData<VoucherInformation> get() = _information

    fun setConfiguration(configuration: VoucherConfiguration) {
        _configuration.value = configuration
    }

    fun setInformation(information: VoucherInformation) {
        _information.value = information
    }

    fun setupEditMode(voucherId: String) {

    }
}
