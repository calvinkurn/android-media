package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.content.Context
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class AddAddressMockConfig : MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(AUTOCOMPLETE_KEY, getRawString(context, R.raw.autocomplete_jak), FIND_BY_CONTAINS)
        addMockResponse(GET_DISTRICT_KEY, getRawString(context, R.raw.get_district_jakarta), FIND_BY_CONTAINS)
        addMockResponse(SAVE_ADDRESS_KEY, getRawString(context, R.raw.save_address_success), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        const val AUTOCOMPLETE_KEY = "KeroMapsAutoComplete"
        const val GET_DISTRICT_KEY = "KeroPlacesGetDistrict"
        const val SAVE_ADDRESS_KEY = "kero_add_address"
    }
}