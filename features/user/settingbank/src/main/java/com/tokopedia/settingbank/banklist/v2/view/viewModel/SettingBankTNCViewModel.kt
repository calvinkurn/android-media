package com.tokopedia.settingbank.banklist.v2.view.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.settingbank.banklist.v2.di.QUERY_GET_ADD_BANK_TNC
import com.tokopedia.settingbank.banklist.v2.domain.SettingBankTNC
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnTNCError
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnTNCSuccess
import com.tokopedia.settingbank.banklist.v2.view.viewState.TNCViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


const val PARAM_TNC_TYPE = "type"
const val PARAM_TNC_NOTE = "notes"
const val PARAM_TNC_POPUP = "tnc"

class SettingBankTNCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  private val rawQueries: Map<String, String>,
                                                  dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val tncNoteTemplate = MutableLiveData<TemplateData>()
    val tncPopUpTemplate = MutableLiveData<TNCViewState>()

    internal fun loadTNCNoteTemplate() {
        launchCatchError(block = {
            val data = loadTNCByType(PARAM_TNC_NOTE)
            tncNoteTemplate.value = (data.getSuccessData() as SettingBankTNC).richieTNC.data
        }) {
            it.printStackTrace()
        }
    }

    internal fun loadTNCPopUpTemplate() {
        launchCatchError(block = {
            val data = loadTNCByType(PARAM_TNC_POPUP)
            tncPopUpTemplate.value = OnTNCSuccess((data.getSuccessData() as SettingBankTNC).richieTNC.data)
        }) {
            tncPopUpTemplate.value = OnTNCError(it)
            it.printStackTrace()
        }
    }

    private suspend fun loadTNCByType(type: String): GraphqlResponse = withContext(Dispatchers.IO) {
        val paramsInfo = mapOf(PARAM_TNC_TYPE to type)
        val graphRequest = GraphqlRequest(rawQueries[QUERY_GET_ADD_BANK_TNC],
                SettingBankTNC::class.java, paramsInfo)
        graphqlRepository.getReseponse(listOf(graphRequest))
    }

}