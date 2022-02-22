package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.presentation.data.DataPlanDataFactory
import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DigitalPDPDataPlanViewModelTest: DigitalPDPDataPlanViewModelTestFixture() {

    private val dataFactory = DataPlanDataFactory()
    private val mapperFactory = DigitalDenomMapper()


}