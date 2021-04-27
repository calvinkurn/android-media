package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetMembershipUseCaseNew @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<MembershipStampProgress>
) : UseCase<MembershipStampProgress>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"

        @JvmStatic
        fun createRequestParams(shopId: Int): RequestParams = RequestParams.create().apply {
            putInt(PARAM_SHOP_ID, shopId)
        }

        private const val QUERY = """
            query membershipStampProgress(${'$'}shopId: Int!){
              membershipStampProgress(shopID:${'$'}shopId) {
                isUserRegistered
                isShown
                program {
                  id
                  cardID
                  sectionID
                  quests {
                    id
                    title
                    iconURL
                    questUserID
                    status
                    taskID
                    currentProgress
                    targetProgress
                    actionButton {
                      text
                      isShown
                    }
                  }
                }
                infoMessage {
                  title
                  cta {
                    text
                    url
                    appLink
                  }
                }
              }
              }
        """
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): MembershipStampProgress {
        gqlUseCase.setGraphqlQuery(QUERY)
        gqlUseCase.setTypeClass(MembershipStampProgress::class.java)
        gqlUseCase.setRequestParams(params.parameters)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        return gqlUseCase.executeOnBackground()
    }


}