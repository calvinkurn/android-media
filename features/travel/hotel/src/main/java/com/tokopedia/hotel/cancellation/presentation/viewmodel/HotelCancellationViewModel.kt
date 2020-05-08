package com.tokopedia.hotel.cancellation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     val dispatcher: TravelDispatcherProvider) : BaseViewModel(dispatcher.io()) {

    private val mutableCancellationData = MutableLiveData<Result<HotelCancellationModel>>()
    val cancellationData: LiveData<Result<HotelCancellationModel>>
        get() = mutableCancellationData

    private val mutableCancellationSubmitData = MutableLiveData<Result<HotelCancellationSubmitModel>>()
    val cancellationSubmitData: LiveData<Result<HotelCancellationSubmitModel>>
        get() = mutableCancellationSubmitData

    fun getCancellationData(query: String) {
        /*
         Data is still dummy data
         */
        val gson = Gson()
        var dummyData = gson.fromJson(query, HotelCancellationModel.Response::class.java)
        mutableCancellationData.postValue(Success(dummyData.data))
    }

    fun submitCancellationData(query: String, cancelCartId: String, selectedId: String, freeText: String) {
        /*
         Data is still dummy data
         */
        val gson = Gson()
        var dummyData = gson.fromJson(query, HotelCancellationSubmitModel.Response::class.java)
        mutableCancellationSubmitData.postValue(Success(dummyData.data))
    }
}