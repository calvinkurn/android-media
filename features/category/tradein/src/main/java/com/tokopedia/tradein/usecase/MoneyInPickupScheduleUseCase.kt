package com.tokopedia.tradein.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class MoneyInPickupScheduleUseCase @Inject constructor(
        private val repository: TradeInRepository) {

    private fun getQuery(resources: Resources?): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_get_pickup_schedule_option)
    }

    suspend fun getPickupScheduleOption(resources: Resources?): ResponseData {
        val request = HashMap<String, String>()
        return repository.getGQLData(getQuery(resources), ResponseData::class.java, request)
    }
}