package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPPaketDataRepository
import javax.inject.Inject

class DigitalPDPDataPlanViewModel @Inject constructor(
    val repo: DigitalPDPPaketDataRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {



}