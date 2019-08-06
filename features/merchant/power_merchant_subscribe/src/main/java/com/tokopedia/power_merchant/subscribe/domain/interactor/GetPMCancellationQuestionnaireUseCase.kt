package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPMCancellationQuestionnaireUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PMCancellationQuestionnaireModel>(graphqlRepository) {
    init {


    }

    fun setParam(){

//        setRequestParams()
    }
}