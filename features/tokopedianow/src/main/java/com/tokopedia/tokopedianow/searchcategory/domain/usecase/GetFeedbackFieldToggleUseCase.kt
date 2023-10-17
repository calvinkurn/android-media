package com.tokopedia.tokopedianow.searchcategory.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetFeedbackFieldModel
import com.tokopedia.tokopedianow.searchcategory.domain.query.GetFeedbackFieldToggle
import javax.inject.Inject

class GetFeedbackFieldToggleUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GetFeedbackFieldModel>(gqlRepository) }

    suspend fun execute(): GetFeedbackFieldModel.Data {
        graphql.apply {
            setGraphqlQuery(GetFeedbackFieldToggle)
            setTypeClass(GetFeedbackFieldModel::class.java)

            val request = executeOnBackground()
            return request.tokonowFeedbackFieldToggle.data
        }
    }
}
