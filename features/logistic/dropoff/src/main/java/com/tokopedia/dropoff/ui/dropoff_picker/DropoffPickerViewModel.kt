package com.tokopedia.dropoff.ui.dropoff_picker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.dropoff.data.query.LocationQuery
import com.tokopedia.dropoff.data.response.getStore.GetStoreResponse
import com.tokopedia.dropoff.domain.mapper.GetStoreMapper
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DropoffPickerViewModel
@Inject constructor(private val gql: GraphqlRepository,
                    private val mapper: GetStoreMapper) : ViewModel(), CoroutineScope {

    private val mStoreResponse = MutableLiveData<Result<DropoffUiModel>>()
    val storeData: LiveData<Result<DropoffUiModel>>
        get() = mStoreResponse

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + onError

    fun getStores(latLng: String) {
        val gqlRequest = GraphqlRequest(LocationQuery.keroAddressStoreLocation,
                GetStoreResponse::class.java, generateQuery(latLng))
        launch {
            val response = gql.getReseponse(listOf(gqlRequest))
                    .getData<GetStoreResponse>(GetStoreResponse::class.java)
                    ?: throw Exception("Can't extract object from Graphql Response")

            val stores = mapper.map(response)
            mStoreResponse.value = Success(stores)
        }
    }

    private val onError = CoroutineExceptionHandler { _, e ->
        mStoreResponse.value = Fail(e)
    }

    private fun generateQuery(latLng: String): Map<String, Any> = mapOf(
            "query" to mapOf(
                    "latlng" to latLng,
                    "type" to 3
            ))

}