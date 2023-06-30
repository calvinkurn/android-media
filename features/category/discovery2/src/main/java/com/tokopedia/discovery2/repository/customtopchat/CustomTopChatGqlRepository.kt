package com.tokopedia.discovery2.repository.customtopchat

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.customtopchatdatamodel.CustomChatResponse
import javax.inject.Inject

open class CustomTopChatGqlRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), CustomTopChatRepository {

    override suspend fun getMessageId(queryParameterMap: MutableMap<String, Any>): CustomChatResponse? {
        return getGQLData(queryCustomTopchatGql,
            CustomChatResponse::class.java, queryParameterMap) as CustomChatResponse
    }

    private val queryCustomTopchatGql: String = """query getMessage(${'$'}fabShopId: Int!, ${'$'}source: String) {
    chatExistingChat(toShopId: ${'$'}fabShopId, source: ${'$'}source) {
      messageId
    }
}""".trimIndent()
}
