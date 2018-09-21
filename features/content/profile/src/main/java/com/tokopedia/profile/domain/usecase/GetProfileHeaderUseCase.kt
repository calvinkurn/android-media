package com.tokopedia.profile.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfileHeaderUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase) {
    companion object {

    }

    fun execute(variables: HashMap<String, Any>, subscriber: Subscriber<GraphqlResponse>) {

    }

    fun unsubcribe() {
        graphqlUseCase.unsubscribe()
    }
}