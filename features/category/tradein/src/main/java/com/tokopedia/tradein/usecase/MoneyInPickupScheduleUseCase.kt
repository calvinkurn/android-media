package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class MoneyInPickupScheduleUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val repository: TradeInRepository) {

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_pickup_schedule_option)
    }

    suspend fun getPickupScheduleOption(): ResponseData {
        val request = HashMap<String, String>()
        return repository.getGQLData(getQuery(), ResponseData::class.java, request) as ResponseData
    }
}