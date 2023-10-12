package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.mps.domain.model.AceSearchShopMPS
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS

internal fun GraphqlResponse.getSearchShopMPS(): SearchShopMPS =
    getData<AceSearchShopMPS>(AceSearchShopMPS::class.java)
        ?.searchShopMPS
        ?: SearchShopMPS()
