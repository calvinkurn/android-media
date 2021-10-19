package com.tokopedia.logout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logout.domain.model.LogoutDataModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LogoutUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository):
    CoroutineUseCase<Unit, LogoutDataModel>(Dispatchers.IO) {

    override suspend fun execute(params: Unit): LogoutDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation logoutUser {
            logout_user {
                success
                errors {
                    name
                    message
                }
            }
        }
    """.trimIndent()
}

//class LogoutUseCase @Inject constructor(
//        @LogoutContext private val context: Context,
//        private val useCase: GraphqlUseCase<LogoutDataModel>
//) {
//    fun execute(onSuccess: (LogoutDataModel) -> Unit, onError: (Throwable) -> Unit) {
//        val rawQuery = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_logout)
//        useCase.apply {
//            setTypeClass(LogoutDataModel::class.java)
//            setGraphqlQuery(rawQuery)
//            execute({ result ->
//                onSuccess(result)
//            }, { throwable ->
//                onError(throwable)
//            })
//        }
//    }
//}