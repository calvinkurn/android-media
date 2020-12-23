package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_NAME
import com.tokopedia.topads.common.data.internal.ParamObject.INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DAILY_BUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_EDIT_OPTION
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_RECOM_EDIT_SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.raw.EDIT_GROUP_QUERY
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.TopadsManageGroupAdsInput
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

@GqlQuery("EditGroupQuery", EDIT_GROUP_QUERY)

class TopAdsEditUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<FinalAdResponse>(graphqlRepository) {

    fun setParam(dataProduct: MutableList<GroupEditInput.Group.AdOperationsItem>?, dataGroup: HashMap<String, Any?>?) {

        val variable: HashMap<String, Any> = HashMap()
        variable[INPUT] = convertToParam(dataProduct, dataGroup)
        setRequestParams(variable)

    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (FinalAdResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(FinalAdResponse::class.java)
        setGraphqlQuery(EditGroupQuery.GQL_QUERY)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)
        }, onError)
    }

    private fun convertToParam(dataProduct: MutableList<GroupEditInput.Group.AdOperationsItem>?, dataGroup: HashMap<String, Any?>?): TopadsManageGroupAdsInput {
        val priceBidGroup = dataGroup?.get(PARAM_PRICE_BID) as? Int
        val dailyBudgetGroup = dataGroup?.get(PARAM_DAILY_BUDGET) as? Int
        val groupId = dataGroup?.get(PARAM_GROUP_Id) as? Int
        return TopadsManageGroupAdsInput().apply {
            shopID = userSession.shopId
            groupID = groupId.toString()
            source = PARAM_RECOM_EDIT_SOURCE
            groupInput = GroupEditInput(
                    action = PARAM_EDIT_OPTION,
                    group = GroupEditInput.Group(
                            adOperations = dataProduct,
                            name = null,
                            type = PRODUCT,
                            dailyBudget = dailyBudgetGroup,
                            priceBid = priceBidGroup,
                    )
            )
        }

    }
}