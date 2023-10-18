package com.tokopedia.pdp.fintech.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdp.fintech.constants.GQL_GET_WIDGET_DETAIL_V2
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.view.FintechPriceURLDataModel
import javax.inject.Inject

@GqlQuery("PayLaterGetPdpWidget", GQL_GET_WIDGET_DETAIL_V2)
class FintechWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<WidgetDetail>(graphqlRepository) {

    fun getWidgetData(
        onSuccess: (WidgetDetail) -> Unit,
        onError: (Throwable) -> Unit,
        productCategory: String,
        listofAmountandUrls: HashMap<String, FintechPriceURLDataModel>,
        shopId: String,
        parentId: String,
        ) {
        try {
            this.setTypeClass(WidgetDetail::class.java)
            this.setRequestParams(
                getRequestParams(productCategory, listofAmountandUrls, shopId, parentId)
            )
            this.setGraphqlQuery(PayLaterGetPdpWidget.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(
        productCategory: String,
        listofAmountandUrls: HashMap<String, FintechPriceURLDataModel>,
        shopId: String,
        parentId: String
    ): MutableMap<String, Any?> {

        var listOfVariantDetail: MutableList<WidgetRequestModel> =
            setAmountList(listofAmountandUrls)

        return mutableMapOf(
            REQUEST to setProductDetailMap(productCategory, listOfVariantDetail, shopId, parentId)
        )
    }

    private fun setAmountList(
        listofAmountandUrls: HashMap<String, FintechPriceURLDataModel>
    ): MutableList<WidgetRequestModel> {
        val listOfVariantDetail: MutableList<WidgetRequestModel> = ArrayList()
        listofAmountandUrls.forEach { (key, value) ->
            listOfVariantDetail.add(
                WidgetRequestModel(
                    amount = value.price?.toDouble() ?: 0.0
                )
            )
        }
        return listOfVariantDetail
    }

    private fun setProductDetailMap(
        productCategory: String,
        listOfVariantDetail: List<WidgetRequestModel>,
        shopId: String,
        parentId: String
    ): MutableMap<String, Any> {
        val detailMap = mutableMapOf<String, Any>()
        detailMap[PARAM_PRODUCT_CATEGORY] = productCategory
        detailMap[PARAM_LIST_PRODUCT_DETAIL] = listOfVariantDetail
        detailMap[PARAM_SHOP_ID_V2] = shopId
        detailMap[PARAM_PRODUCT_ID] = parentId
        return detailMap
    }

    companion object {
        const val PARAM_PRODUCT_CATEGORY = "product_category"
        const val PARAM_LIST_PRODUCT_DETAIL = "list"
        const val PARAM_SHOP_ID_V2 = "shop_id_v2"
        const val PARAM_PRODUCT_ID = "product_id"
        const val REQUEST = "request"
    }
}

data class WidgetRequestModel(
    @SerializedName("amount")
    val amount: Double,
)
