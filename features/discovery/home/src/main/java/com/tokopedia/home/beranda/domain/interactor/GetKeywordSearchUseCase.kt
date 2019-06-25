package com.tokopedia.home.beranda.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import javax.inject.Inject

class GetKeywordSearchUseCase @Inject constructor(
        private val context: Context
) : GraphqlUseCase() {

    fun getRequest(): GraphqlRequest {
        return GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.home_gql_keyword_search),
                KeywordSearchData::class.java
        )
    }
}