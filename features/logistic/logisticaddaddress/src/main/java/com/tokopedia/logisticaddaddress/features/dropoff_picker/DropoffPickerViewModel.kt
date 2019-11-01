package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.domain.model.dropoff.Data
import com.tokopedia.logisticaddaddress.domain.model.dropoff.GetStoreResponse
import com.tokopedia.logisticaddaddress.domain.query.LocationQuery
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DropoffPickerViewModel
@Inject constructor(dispatcher: CoroutineDispatcher,
                    private val getStoreUseCase: GraphqlUseCase<GetStoreResponse>) : BaseViewModel(dispatcher) {

    private val mutableStoreResponse = MutableLiveData<Result<List<Data>>>()
    val stores: LiveData<Result<List<Data>>>
        get() = mutableStoreResponse

    private val mutableRadius = MutableLiveData<Int>()
    val radius: LiveData<Int>
        get() = mutableRadius

    fun getStores(latlng: String) {

        getStoreUseCase.setTypeClass(GetStoreResponse::class.java)
        getStoreUseCase.setRequestParams(mapOf("latlng" to latlng))
        getStoreUseCase.setGraphqlQuery(LocationQuery.keroAddressStoreLocation)

        getStoreUseCase.execute(
                { response ->
                    if (response.keroAddressStoreLocation.data.isNotEmpty()) {
                        mutableStoreResponse.value = Success(response.keroAddressStoreLocation.data)
                        mutableRadius.value = response.keroAddressStoreLocation.globalRadius
                    }
                },
                { _ ->
                    // todo: This is dummy implementation prior to live of production
                    mutableStoreResponse.value = Success(
                            LocationQuery.getStoreDummyObject.keroAddressStoreLocation.data)
                    mutableRadius.value = LocationQuery.getStoreDummyObject.keroAddressStoreLocation.globalRadius
                }
        )
    }

}