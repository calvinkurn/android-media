package com.tokopedia.feedplus.profilerecommendation.domain.usecase

import com.tokopedia.feedplus.profilerecommendation.domain.model.SetOnboardingStatusResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-09-17.
 */
class SetOnboardingStatusUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        @Named(MUTATION_SET_ONBOARDING_STATUS) val query: String
) : GraphqlUseCase<SetOnboardingStatusResponse>(graphqlRepository) {

    companion object {
        const val MUTATION_SET_ONBOARDING_STATUS = "mutation_set_onboarding_status"
    }

    init {
        setTypeClass(SetOnboardingStatusResponse::class.java)
        setGraphqlQuery(query)
    }
}