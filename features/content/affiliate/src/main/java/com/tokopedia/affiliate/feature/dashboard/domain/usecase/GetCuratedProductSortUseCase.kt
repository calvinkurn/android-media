package com.tokopedia.affiliate.feature.dashboard.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.data.pojo.AffiliateProductSortQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-09.
 */
class GetCuratedProductSortUseCase @Inject constructor(
        @ApplicationContext private val context: Context
) : GraphqlUseCase() {

    fun getRequest(): GraphqlRequest {
        return GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_af_curated_product_sort),
                AffiliateProductSortQuery::class.java,
                false
        )
    }
}