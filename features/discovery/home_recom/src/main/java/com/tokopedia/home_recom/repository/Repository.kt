package com.tokopedia.home_recom.repository

import com.tokopedia.graphql.data.model.GraphqlResponse

/**
 * Created by Lukas on 29/08/19
 */
abstract class Repository <T>{
    abstract suspend fun load(page: Int, ref: String, productIds: String): GraphqlResponse
    abstract fun getDefaultPageSize(): Int
}