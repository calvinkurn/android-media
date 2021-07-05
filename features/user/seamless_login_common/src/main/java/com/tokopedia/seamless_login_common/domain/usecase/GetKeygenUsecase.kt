package com.tokopedia.seamless_login_common.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seamless_login_common.R
import com.tokopedia.seamless_login_common.data.KeyResponse
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class GetKeygenUsecase @Inject constructor(
        val resources: Resources,
        val repository: GraphqlRepository
): GraphqlUseCase<KeyResponse>(repository) {

    companion object {
        const val PARAM_MODULE = "module"
    }

    init {
        val query = GraphqlHelper.loadRawString(resources, R.raw.gql_get_pub_key)
        setTypeClass(KeyResponse::class.java)
        setParams()
        setGraphqlQuery(query)
    }

    private fun setParams() {
        val map = mutableMapOf<String, Any>(
                PARAM_MODULE to "seamless"
        )
        setRequestParams(map)
    }
}