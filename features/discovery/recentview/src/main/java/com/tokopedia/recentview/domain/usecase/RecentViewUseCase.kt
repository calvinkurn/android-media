package com.tokopedia.recentview.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.recentview.data.entity.RecentViewResult
import com.tokopedia.recentview.data.mapper.RecentViewMapper
import com.tokopedia.recentview.data.query.RecentViewQuery
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.util.*

/**
 * @author by yoasfs on 13/08/20
 */
class RecentViewUseCase (
        private val graphqlRepository: GraphqlRepository,
        private val recentViewMapper: RecentViewMapper
): UseCase<ArrayList<RecentViewDetailProductDataModel>>() {

    private val params = RequestParams.create()

    private val PARAM_USER_ID = "userID";

    override suspend fun executeOnBackground(): ArrayList<RecentViewDetailProductDataModel> {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val request = GraphqlRequest(
                RecentViewQuery.getQuery(),
                RecentViewResult::class.java,
                params.parameters
        )
        val response = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        response.getError(RecentViewResult::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    throw Throwable(it[0].message)
                }
            }
        }

        val data = response.getData<RecentViewResult>(RecentViewResult::class.java)
        return recentViewMapper.convertToViewModel(data.item)
    }

    fun getParam(loginID: String) {
        params.parameters.clear()
        params.putInt(PARAM_USER_ID, loginID.toIntOrNull() ?: 0)
    }

}