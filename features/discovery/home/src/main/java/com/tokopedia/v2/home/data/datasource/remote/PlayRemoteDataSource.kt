package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class PlayRemoteDataSource @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) {
    fun getPlayCard() {

    }
}