package com.tokopedia.shareexperience.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExImageGeneratorQuery : GqlQueryInterface {

    private val operationName = "imagenerator_generate_image"

    override fun getOperationNameList(): List<String> {
        return listOf(operationName)
    }

    override fun getQuery(): String = """
        mutation imagenerator_generate_image(${'$'}sourceID:String!, ${'$'}args:[ImageneratorGenerateImageArg]){
              imagenerator_generate_image(sourceID:${'$'}sourceID, args:${'$'}args){
                image_url
                source_id
              }
            }
    """.trimIndent()

    override fun getTopOperationName(): String = operationName
}
