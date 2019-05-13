package com.tokopedia.hotel.orderdetail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 13/05/19
 */

class HotelOrderDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                    dispatcher: CoroutineDispatcher,
                                                    @Named("dummy_order_detail")
                                                    private val dummyOrderDetail: String): BaseViewModel(dispatcher){

    val orderDetailData = MutableLiveData<Result<HotelOrderDetail>>()

    fun getOrderDetail(rawQuery: String) {
        launchCatchError(block = {
            val gson = Gson()
            orderDetailData.value = Success(gson.fromJson(dummyOrderDetail,
                    HotelOrderDetail.Response::class.java).response)
        }) {

        }
    }

}