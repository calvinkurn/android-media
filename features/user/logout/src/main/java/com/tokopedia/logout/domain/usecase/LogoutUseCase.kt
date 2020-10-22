package com.tokopedia.logout.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logout.R
import com.tokopedia.logout.di.LogoutContext
import com.tokopedia.logout.domain.model.LogoutDataModel
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
        @LogoutContext private val context: Context,
        private val useCase: GraphqlUseCase<LogoutDataModel>
) {
    fun execute(onSuccess: (LogoutDataModel) -> Unit, onError: (Throwable) -> Unit) {
        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_logout)
        useCase.apply {
            setTypeClass(LogoutDataModel::class.java)
            setGraphqlQuery(rawQuery)
            execute({ result ->
                onSuccess(result)
            }, { throwable ->
                onError(throwable)
            })
        }
    }
}