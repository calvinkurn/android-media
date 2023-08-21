package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.TopadsManageGroupAdsInput
import com.tokopedia.topads.common.data.response.TopadsManageGroupAdsResponse
import com.tokopedia.topads.common.domain.query.GetTopadsManageGroupAds
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TopAdsManageGroupAdsUseCase @Inject constructor() : GraphqlUseCase<TopadsManageGroupAdsResponse>() {

    companion object{
        private const val SOURCE = "android.topads_widget"
        private const val GROUP_ACTION = "edit"
        private const val GROUP_TYPE = "product"
        private const val AD_ACTION = "add"
        private const val PARAM_KEY = "input"
    }


    @Suppress("DeprecatedMethod")
    fun executeUseCase(
        requestParams: RequestParams,
        success:(TopadsManageGroupAdsResponse) -> Unit,
        failure:(Throwable) -> Unit
    ){
       setTypeClass(TopadsManageGroupAdsResponse::class.java)
        setRequestParams(requestParams.parameters)
        setGraphqlQuery(GetTopadsManageGroupAds)
        execute(
            {
                success.invoke(it)
            },{
                failure.invoke(it)
            }
        )
    }

    fun createRequestParams(groupID:String,shopID:String,productID:String) : RequestParams {
        val params = TopadsManageGroupAdsInput().apply {
            source = SOURCE
            shopId = shopID
            groupId = groupID
            groupOperation = TopadsManageGroupAdsInput.GroupOperation().apply {
                action = GROUP_ACTION
                group = TopadsManageGroupAdsInput.GroupOperation.Group().apply {
                    type = GROUP_TYPE
                    adOperations = listOf(
                        GroupEditInput.Group.AdOperationsItem().apply {
                            action = AD_ACTION
                            ad = GroupEditInput.Group.AdOperationsItem.Ad().apply {
                                productId = productID
                            }
                        }
                    )
                }
            }
        }
        return RequestParams().apply {
            putObject(PARAM_KEY,params)
        }
    }
}
