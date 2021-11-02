package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.GQL_POPUP_NAME
import com.tokopedia.product.manage.feature.list.data.model.PopupManagerResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class GetShopManagerPopupsUseCase @Inject constructor(@Named(GQL_POPUP_NAME)
                                                        private val query: String,
                                                      repository: GraphqlRepository)
    : GraphqlUseCase<PopupManagerResponse>(repository) {

    companion object {
        private const val SHOP_ID = "shopID"

        private fun createRequestParams(shopId: Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID, shopId)
            }
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(PopupManagerResponse::class.java)
    }

    suspend fun execute(shopId: Long): Boolean {
        val requestParams = createRequestParams(shopId)
        setRequestParams(requestParams.parameters)

        val data = executeOnBackground()
        return data.getShopManagerPopups.shopManagerPopupsData.isShowPopup
    }

}