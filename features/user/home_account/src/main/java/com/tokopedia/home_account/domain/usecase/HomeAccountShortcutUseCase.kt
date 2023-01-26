package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.ShortcutResponse
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

open class HomeAccountShortcutUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Unit, ShortcutResponse>(dispatcher.io) {

    override fun graphqlQuery(): String =
        """
            query {
              tokopoints {
                status {
                  tier {
                    nameDesc
                    eggImageHomepageURL
                    backgroundImgURLMobile
                  }
                }
              }
              tokopointsShortcutList(groupCodes: ["account_page_widget"]) {
                shortcutGroupList {
                  groupCode
                  shortcutList {
                    id
                    cta {
                      text
                      url
                      appLink
                    }
                    iconImageURL
                    description
                  }
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): ShortcutResponse {
        return graphqlRepository.request(graphqlQuery(), params)
//        val gqlRequest = GraphqlRequest(graphqlQuery(),
//                ShortcutResponse::class.java, mapOf<String, Any>())
//        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
//                .Builder(CacheType.ALWAYS_CLOUD).build())
//        val errors = gqlResponse.getError(ShortcutResponse::class.java)
//        if (!errors.isNullOrEmpty()) {
//            throw MessageErrorException(errors[0].message)
//        } else {
//            var data: ShortcutResponse? = gqlResponse.getData(ShortcutResponse::class.java)
//            if(data == null) {
//                val mapResponse = Utils.convertResponseToJson(gqlResponse)
//                data = ShortcutResponse()
//                AccountErrorHandler.logDataNull("Account_GetShortcutDataUseCase",
//                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
//            }
//            return data
//        }
    }
}
