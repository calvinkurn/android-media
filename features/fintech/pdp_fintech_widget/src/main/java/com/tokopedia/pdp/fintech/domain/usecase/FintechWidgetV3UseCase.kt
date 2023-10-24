package com.tokopedia.pdp.fintech.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdp.fintech.constants.GQL_GET_WIDGET_DETAIL_V3
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetailV3
import com.tokopedia.pdp.fintech.view.FintechPriceURLDataModel
import javax.inject.Inject

@GqlQuery("PayLaterGetPdpWidgetV3", GQL_GET_WIDGET_DETAIL_V3)
class FintechWidgetV3UseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<WidgetDetailV3>(graphqlRepository) {

        fun getWidgetV3Data(
            shopId: String,
            parentProductId: String,
            productCategory: String,
            listOfAmountAndUrls: HashMap<String, FintechPriceURLDataModel>,
            onSuccess: (WidgetDetailV3) -> Unit,
            onError: (Throwable) -> Unit,
        ) {
            this.setTypeClass(WidgetDetailV3::class.java)
            this.setRequestParams(generateRequestParam(
                shopId, parentProductId, productCategory, listOfAmountAndUrls
            ))
            this.setGraphqlQuery(PayLaterGetPdpWidgetV3.GQL_QUERY)
            this.execute({
                onSuccess.invoke(it)
            }, {
                onError.invoke(it)
            })
        }

    private fun generateRequestParam(
        shopId: String,
        parentProductId: String,
        productCategory: String,
        listOfAmountAndUrls: HashMap<String, FintechPriceURLDataModel>,
    ): MutableMap<String, Any?> {
        return mutableMapOf(
            REQUEST to mutableMapOf(
                PARAM_SHOP_ID to shopId,
                PARAM_PARENT_PRODUCT_ID to parentProductId,
                PARAM_PRODUCT_CATEGORY to productCategory,
                PARAM_LIST_PRODUCT_DETAIL to setAmountAndUrlList(listOfAmountAndUrls)
            )
        )
    }

    private fun setAmountAndUrlList(
        listOfAmountAndUrls: HashMap<String, FintechPriceURLDataModel>
    ): MutableList<WidgetPriceURLData> {
        val listOfVariantDetail: MutableList<WidgetPriceURLData> = ArrayList()
        listOfAmountAndUrls.forEach { (key, value) ->
            listOfVariantDetail.add(
                WidgetPriceURLData(
                    amount = value.price?.toDouble() ?: 0.0,
                    url = value.url
                )
            )
        }
        return listOfVariantDetail
    }

    companion object {
        private const val PARAM_PRODUCT_CATEGORY = "product_category"
        private const val PARAM_LIST_PRODUCT_DETAIL = "list"
        private const val PARAM_SHOP_ID= "shop_id"
        private const val PARAM_PARENT_PRODUCT_ID = "parent_product_id"
        private const val REQUEST = "request"
    }
}

data class WidgetPriceURLData(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("redirect_url")
    val url: String
)
