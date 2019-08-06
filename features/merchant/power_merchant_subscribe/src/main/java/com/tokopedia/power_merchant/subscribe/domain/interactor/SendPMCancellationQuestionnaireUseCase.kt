package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import javax.inject.Inject

class SendPMCancellationQuestionnaireUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PMCancellationQuestionnaireModel>(graphqlRepository) {
    init {


    }

    fun setParam(){

//        setRequestParams()
    }
}