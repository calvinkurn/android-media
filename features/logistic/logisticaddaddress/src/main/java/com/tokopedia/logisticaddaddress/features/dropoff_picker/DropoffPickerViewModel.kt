package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.GetStoreMapper
import com.tokopedia.logisticaddaddress.domain.model.dropoff.DropoffUiModel
import com.tokopedia.logisticaddaddress.domain.model.dropoff.GetStoreResponse
import com.tokopedia.logisticaddaddress.domain.query.LocationQuery
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DropoffPickerViewModel
@Inject constructor(dispatcher: CoroutineDispatcher,
                    private val getStoreUseCase: GraphqlUseCase<GetStoreResponse>,
                    private val mapper: GetStoreMapper) : BaseViewModel(dispatcher) {

    private val mStoreResponse = MutableLiveData<Result<DropoffUiModel>>()
    val stores: LiveData<Result<DropoffUiModel>>
        get() = mStoreResponse

    fun getStores(latlng: String) {

        getStoreUseCase.setTypeClass(GetStoreResponse::class.java)
        getStoreUseCase.setRequestParams(mapOf("latlng" to latlng))
        getStoreUseCase.setGraphqlQuery(LocationQuery.keroAddressStoreLocation)

        getStoreUseCase.execute(
                { response ->
                    if (response.keroAddressStoreLocation.data.isNotEmpty()) {
                        mStoreResponse.value = Success(mapper.map(response))
                    }
                },
                { _ ->
                    // todo: This is dummy implementation prior to live of production
                    mStoreResponse.value = Success(
                            mapper.map(LocationQuery.getStoreDummyObject))
                }
        )
    }

}