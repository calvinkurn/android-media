package com.tokopedia.user_identification_common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo
import javax.inject.Inject

class GetStatusKtpUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): UseCase<CheckKtpStatusPojo>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): CheckKtpStatusPojo {
        val rawQuery = rawQueries[KYCConstant.QUERY_IS_KTP]
        val gqlRequest = GraphqlRequest(rawQuery,
                CheckKtpStatusPojo::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(CheckKtpStatusPojo::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(CheckKtpStatusPojo::class.java)
        }
    }

    companion object {
        private const val IMAGE = "image"
        private const val IDENTIFIER = "id"
        private const val SOURCE = "src"

        fun createParam(img: String): HashMap<String, Any> {
            return hashMapOf(
                    IMAGE to img,
                    IDENTIFIER to "",
                    SOURCE to "kyc"
            )
        }
    }

}