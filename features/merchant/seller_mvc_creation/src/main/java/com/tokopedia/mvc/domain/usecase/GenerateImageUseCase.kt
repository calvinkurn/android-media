package com.tokopedia.mvc.domain.usecase

import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GenerateImageUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): UseCase<String>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(QUERY, GenerateImageResponse::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.CACHE_FIRST).build())

        val response = gqlResponse.getData<GenerateImageResponse>(GenerateImageResponse::class.java)
        if (!TextUtils.isEmpty(response.imageGeneratorModel.imageUrl)) {
            return response.imageGeneratorModel.imageUrl
        } else {
            throw MessageErrorException("Error in image generation")
        }
    }

    data class GenerateImageParams(
        @SerializedName("key")
        @Expose
        val key: String,
        @SerializedName("value")
        @Expose
        val value: String
    )

    data class GenerateImageResponse(
        @SerializedName("imagenerator_generate_image")
        @Expose
        val imageGeneratorModel: ImageGeneratorModel = ImageGeneratorModel()
    ) {
        data class ImageGeneratorModel(
            @SerializedName("image_url")
            @Expose
            val imageUrl: String = ""
        )
    }

    companion object {
        private const val SOURCE_ID = "sourceID"
        private const val ARGS = "args"

        const val QUERY = """mutation imagenerator_generate_image(${'$'}sourceID:String!, ${'$'}args:[ImageneratorGenerateImageArg]){
              imagenerator_generate_image(sourceID:${'$'}sourceID, args:${'$'}args){
                image_url
              }
            }
        """

        fun createParam(sourceId: String, args: ArrayList<GenerateImageParams>): HashMap<String, Any> {
            return hashMapOf(
                SOURCE_ID to sourceId,
                ARGS to args
            )
        }
    }
}

