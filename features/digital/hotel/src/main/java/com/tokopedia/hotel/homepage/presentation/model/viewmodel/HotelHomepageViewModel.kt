package com.tokopedia.hotel.homepage.presentation.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoData
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 04/04/19
 */
class HotelHomepageViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    val promoData = MutableLiveData<Result<MutableList<HotelPromoEntity>>>()

    fun getHotelPromo(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_PROMO, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelPromoData>()

            promoData.value = Success(data.travelBanner.toMutableList())
        }) {
            promoData.value = Fail(it)
        }
    }

    companion object {
        private val TYPE_HOTEL_PROMO = HotelPromoData::class.java
    }
}