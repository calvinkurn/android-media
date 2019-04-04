package com.tokopedia.topads.debit.autotopup.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class TopAdsAutoTopUpViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                   private val userSessionInterface: UserSessionInterface,
                                                   @Named("Main")
                                                   val dispatcher: CoroutineDispatcher):
        BaseViewModel(dispatcher){

    val getAutoTopUpStatus = MutableLiveData<Result<AutoTopUpStatus>>()
    val statusSaveSelection = MutableLiveData<SavingAutoTopUpState>()

    fun getAutoTopUpStatusFull(rawQuery: String) {
        val params = mapOf(PARAM_SHOP_ID to userSessionInterface.shopId)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, AutoTopUpData.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<AutoTopUpData.Response>()

            if (data.response == null){
                getAutoTopUpStatus.value = Fail(Exception("Tidak ada data"))
            } else if (data.response.errors.isEmpty()){
                getAutoTopUpStatus.value = Success(data.response.data)
            } else {
                getAutoTopUpStatus.value = Fail(ResponseErrorException(data.response.errors))
            }
        }){
            getAutoTopUpStatus.value = Fail(it)
        }
    }

    fun saveSelection(rawQuery: String, isActive: Boolean, selectedItem: AutoTopUpItem) {
        statusSaveSelection.value = Loading
        val params = mapOf(PARAM_SHOP_ID to userSessionInterface.shopId.toInt(),
                PARAM_ACTION to (if (isActive) TOGGLE_ON else TOGGLE_OFF),
                PARAM_SELECTION_ID to selectedItem.id)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, AutoTopUpData.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<AutoTopUpData.Response>()

            if (data.response == null){
                throw Exception("Tidak ada data")
            } else if (data.response.errors.isEmpty()){
                statusSaveSelection.value = ResponseSaving(true, null)
            } else {
                throw ResponseErrorException(data.response.errors)
            }
        }){
            statusSaveSelection.value = ResponseSaving(false, it)
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_ACTION = "action"
        private const val PARAM_SELECTION_ID = "selectionId"

        private const val TOGGLE_ON = "toggle_on"
        private const val TOGGLE_OFF = "toggle_off"
    }
}