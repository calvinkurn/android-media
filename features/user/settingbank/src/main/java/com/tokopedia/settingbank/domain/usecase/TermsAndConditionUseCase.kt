package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_GET_TERMS_CONDITION
import com.tokopedia.settingbank.domain.model.SettingBankTNC
import com.tokopedia.settingbank.domain.model.TemplateData
import javax.inject.Inject


@GqlQuery("GQLTermsAndCondition", GQL_GET_TERMS_CONDITION)
class TermsAndConditionUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<SettingBankTNC>(graphqlRepository) {


    private val PARAM_TNC_TYPE: String = "type"
    private val NOTE: String = "notes"
    private val TNC: String = "tnc"

    var isRunning = false

    init {
        setGraphqlQuery(GQLTermsAndCondition.GQL_QUERY)
        setTypeClass(SettingBankTNC::class.java)
    }

    fun getNotes(onSuccess: (TemplateData) -> Unit, onError: (Throwable) -> Unit) {
        setRequestParams(mapOf(PARAM_TNC_TYPE to NOTE))
        execute({
            onSuccess(it.richieTNC.data)
        }, {
            onError(it)
        })
    }

    fun getTermsAndCondition(onSuccess: (TemplateData) -> Unit, onError: (Throwable) -> Unit) {
        if (isRunning)
            return
        isRunning = false
        setRequestParams(mapOf(PARAM_TNC_TYPE to TNC))
        execute({
            isRunning = false
            onSuccess(it.richieTNC.data)
        }, {
            isRunning = false
            onError(it)
        })
    }

}
