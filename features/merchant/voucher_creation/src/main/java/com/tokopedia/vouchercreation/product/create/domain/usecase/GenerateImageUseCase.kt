package com.tokopedia.vouchercreation.product.create.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.product.create.data.response.GenerateImageResponse
import javax.inject.Inject

class GenerateImageUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): BaseGqlUseCase<String>() {

    companion object {

        private const val SOURCE_ID = "sourceID"
        private const val ARGS = "args"

        private const val QUERY = """mutation imagenerator_generate_image(${'$'}sourceID:String!, ${'$'}args:[ImageneratorGenerateImageArg]){
              imagenerator_generate_image(sourceID:${'$'}sourceID, args:${'$'}args){
                image_url
              }
            }
        """


    }

    override suspend fun executeOnBackground(): String {
        val request = GraphqlRequest(QUERY, GenerateImageResponse::class.java, params.parameters)
        val response = graphqlRepository.response(listOf(request))

        val data = response.getData<GenerateImageResponse>(GenerateImageResponse::class.java)

        if (!TextUtils.isEmpty(data.imageGeneratorModel.imageUrl)) {
            return data.imageGeneratorModel.imageUrl
        } else {
            throw MessageErrorException("Error in image generation")
        }
    }

    fun createParam(sourceId: String, args: ArrayList<ImageGeneratorRequestData>): HashMap<String, Any> {
        return hashMapOf(
            SOURCE_ID to sourceId,
            ARGS to args
        )
    }

}
