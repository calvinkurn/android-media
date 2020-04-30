package com.tokopedia.hotel.cancellation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationViewModel constructor(private val graphqlRepository: GraphqlRepository,
                                             val dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    private val mutableCancellationData = MutableLiveData<Result<HotelCancellationModel>>()
    val cancellationData: LiveData<Result<HotelCancellationModel>>
        get() = mutableCancellationData

    fun getCancellationData(query: String) {
        val gson  = Gson()
        var dummyData = gson.fromJson(query, HotelCancellationModel.Response::class.java)
        mutableCancellationData.postValue(Success(dummyData.data))
    }
}