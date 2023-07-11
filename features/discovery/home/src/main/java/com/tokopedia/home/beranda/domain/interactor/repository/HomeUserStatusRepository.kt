package com.tokopedia.home.beranda.domain.interactor.repository

import android.util.Log
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeUserStatusRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<Any>
) : UseCase<Any>() {

    override suspend fun executeOnBackground(): Any {
        return graphqlUseCase.executeOnBackground()
    }

    suspend fun hitHomeStatusThenIgnoreResponse() {
        try {
            executeOnBackground()
        } catch (e: Exception) {
            Log.e("HomeStatus", "hitHomeStatusThenIgnoreResponse: ", e)
        }
    }
}
