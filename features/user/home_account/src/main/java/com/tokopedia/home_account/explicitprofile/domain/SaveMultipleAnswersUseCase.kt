package com.tokopedia.home_account.explicitprofile.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.explicitprofile.data.ExplicitProfileSaveMultiAnswers
import com.tokopedia.home_account.explicitprofile.data.SaveMultipleAnswersParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SaveMultipleAnswersUseCase @Inject constructor(
    private val repository: MultiRequestGraphqlUseCase
) : CoroutineUseCase<MutableList<SaveMultipleAnswersParam>, ExplicitProfileSaveMultiAnswers>(Dispatchers.IO) {

    override suspend fun execute(
        params: MutableList<SaveMultipleAnswersParam>
    ): ExplicitProfileSaveMultiAnswers {
        repository.clearRequest()
        repository.clearCache()
        repository.setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )

        params.forEach {
            repository.addRequest(
                GraphqlRequest(
                    SaveMultipleAnswerGqlQuery(),
                    ExplicitProfileSaveMultiAnswers::class.java,
                    it.toMapParam()
                )
            )
        }

        return repository.executeOnBackground().getSuccessData()
    }

    override fun graphqlQuery(): String = ""

    internal class SaveMultipleAnswerGqlQuery: GqlQueryInterface {
        override fun getOperationNameList(): List<String> {
            return listOf(
                "explicitprofileSaveMultiAnswers"
            )
        }

        override fun getQuery(): String {
            return """
                mutation explicitprofileSaveMultiAnswers(${'$'}input: SaveMultiAnswersRequest!) {
                  explicitprofileSaveMultiAnswers(input: ${'$'}input) {
                    message
                  }
                }
            """.trimIndent()
        }

        override fun getTopOperationName(): String {
            return "explicitprofileSaveMultiAnswers"
        }
    }
}