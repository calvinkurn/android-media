package com.tokopedia.play.broadcaster.util.handler

import com.tokopedia.graphql.data.model.GraphqlResponse


/**
 * Created by mzennis on 06/11/20.
 */
interface UseCaseHandler {

    suspend fun executeWithRetry(): GraphqlResponse

}